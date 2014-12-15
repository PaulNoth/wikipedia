
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author patrik
 */
public class Parser {

    public static String INPUT_LINKS;
    public static String INPUT_PAGE;

    final static HashMap pages = new HashMap();

    protected static String regexEnum = "\\s*(?<type>null|'page'|'subcat'|'file'){1}\\s*";

    Parser(String links, String page) {
        INPUT_LINKS = links;
        INPUT_PAGE = page;
    }

    /*
     definition regex string
     */
    protected static String regexString(String name) {
        return "\\s*(?<" + name + ">null|NULL|'[^']*')\\s*";
    }

    /*
     definition regex integer
     */
    protected static String regexInteger(String name) {
        return "\\s*(?<" + name + ">\\d+)\\s*";
    }

    /*
     definition regex double
     */
    protected static String regexDouble(String name) {
        return "\\s*(?<" + name + ">[0-9.]+)\\s*";
    }

    /*
     definition regex link file
     */
    public static String getRegexLinks() {
        return "(\\(" + regexInteger("from") + "," + regexString("to") + "," + regexString("sortkey") + "," + regexString("timestamp") + "," + regexString("sortkeyPrefix") + "," + regexString("collation") + "," + regexEnum + ")+\\)";
    }

    /*
     definition regex page file
     */
    public static String getRegexPage() {
        return "(\\(" + regexInteger("id") + "," + regexInteger("namespace") + "," + regexString("title") + "," + regexString("restrictions") + "," + regexInteger("counter") + "," + regexInteger("isRedirect") + "," + regexInteger("isNew") + "," + regexDouble("random") + "," + regexString("touched") + "," + regexString("linksUpdated") + "," + regexInteger("latest") + "," + regexInteger("len") + "," + regexString("contentModel") + ")+\\)";
    }

    /*
     initialy parser method
     */
    public void run(final Connection conn) throws Exception {
        final String regexLinks = getRegexLinks();
        final String regexPage = getRegexPage();

        Statement stmt = conn.createStatement();
        try {
            // set commit manualy
            stmt.executeUpdate("SET autocommit=0;");
        } finally {
            stmt.close();
        }

        conn.setAutoCommit(false);

        // create thread for import data from link file
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                // create instance parser for link file
                SQLParser s = new SQLParser(INPUT_LINKS, "categorylinks", regexLinks);

                try {
                    Statement stmt = conn.createStatement();
                    try {
                        int round = 1;
                        // create statments
                        PreparedStatement statments = conn.prepareStatement("INSERT INTO category VALUES(?,?)");

                        while (true) {
                            try {
                                String[] data = s.getRow("from", "to");

                                String category = data[1].substring(1, data[1].length() - 1);

                                statments.setInt(1, Integer.parseInt(data[0]));
                                statments.setString(2, category);
                                statments.addBatch();

                                if (round % 50000 == 0) {
                                    // manualy commit after 50 000 records
                                    statments.executeBatch();
                                }
                                round++;
                            } catch (Exception e) {
                                statments.executeBatch();
                                if ("End".equals(e.getMessage()) == false) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }

                    } finally {
                        stmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t1.setName("category");
        t1.start();

        
        // create thread for import data from page file
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // create instance parser for page file
                SQLParser s2 = new SQLParser(INPUT_PAGE, "page", regexPage);

                try {
                    Statement stmt = conn.createStatement();

                    try {
                        int round = 1;

                        // create statments
                        PreparedStatement psParms = conn.prepareStatement("INSERT INTO page VALUES(?,?)");

                        while (true) {
                            try {
                                String[] data = s2.getRow("id", "title");

                                psParms.setInt(1, Integer.parseInt(data[0]));
                                psParms.setString(2, data[1].substring(1, data[1].length() - 1));
                                psParms.addBatch();

                                if (round % 50000 == 0) {
                                    // manualy commit after 50 000 records
                                    psParms.executeBatch();
                                }
                                round++;
                            } catch (Exception e) {
                                psParms.executeBatch();
                                if ("End".equals(e.getMessage()) == false) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                        }

                    } finally {
                        stmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t2.setName("page");
        t2.start();

        // WAIT UNTIL THREAD FINISH
        t1.join();
        t2.join();
    }
}
