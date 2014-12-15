
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author patrik
 */
public class SQLParser {

    final int bufferSize = 1024 * 2;
    String inputFile = null;
    String table = null;

    // find indexes
    int insertIndex = -1;
    int rowIndex = -1;

    // String buffer
    StringBuilder buff = null;

    // FILE Stream
    BufferedReader stream = null;

    // Stream opened
    boolean opened = false;

    // Patern
    Pattern pattern = null;

    SQLParser(String File, String table, String regex) {
        inputFile = File;
        this.table = table;
        buff = new StringBuilder();

        pattern = Pattern.compile(regex);
    }

    /*
     load buffer data from file
     */
    protected String getData() throws Exception {
        char[] buffer = new char[bufferSize];
        if (!opened) {
            stream = new BufferedReader(new FileReader(inputFile));
            opened = true;
        }

        // READ NEXT BLOCK
        int len = stream.read(buffer);

        if (len < 1) {
            stream.close();
            opened = false;
            return null;
        }

        String tmp = new String(buffer);
        if (len == bufferSize) {
            return tmp;
        } else if (tmp.length() < len) {
            return tmp;
        } else {
            return tmp.substring(0, len);
        }
    }

    /*
     prepare for loading data, find insert statments
     */
    protected boolean prepare() throws Exception {
        do {
            String data = getData();

            if (data == null) {
                return false;
            }

            buff.append(data);
        } while (match(buff.toString()) == false);

        buff = new StringBuilder(buff.substring(insertIndex));

        return true;
    }

    /*
     helper method to findnext record
     */
    protected Matcher findNext() throws Exception {
        int end;

        if (insertIndex == -1) {
            if (!prepare()) {
                return null;
            }
        }

        do {
            Matcher m = pattern.matcher(buff);

            if (m.find() && (end = m.end()) < buff.length()) {
                buff = new StringBuilder(buff.substring(end));
                return m;
            } else {
                String data = getData();

                if (data == null) {
                    return null;
                }

                buff.append(data);
            }
        } while (true);
    }

    /*
     get all columns next records
     */
    public String getRow() throws Exception {
        Matcher m = findNext();

        return m.group();
    }

    /*
     get select columns next records
     */
    public String[] getRow(String... names) throws Exception {
        Matcher m = findNext();

        if (m == null) {
            throw new Exception("End");
        }

        String[] s = new String[names.length];

        int i = 0;
        for (String name : names) {
            s[i] = m.group(name);
            i++;
        }

        return s;
    }

    /*
     helper function match insert statment
     */
    protected boolean match(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `").append(table).append("` VALUES");
        Pattern p = Pattern.compile(sb.toString());
        Matcher m = p.matcher(s);

        boolean find = m.find();

        if (find) {
            insertIndex = m.end();
            return true;
        } else {
            return false;
        }
    }
}
