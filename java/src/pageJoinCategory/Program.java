

import java.sql.Connection;
import java.sql.DriverManager;


public class Program {

    static String cacheFile = "pageLinkCategories.csv";

    public static final String INPUT_LINKS = "enwiki-latest-categorylinks.sql";
    public static final String INPUT_PAGE = "enwiki-latest-page.sql";
    
    public static void main(String args[]) throws Exception {
        int argc = 0;
        if (args.length == 0) {
            System.out.println("program [parse, build, index, search] [search phrase]");
            return;
        }

        if (args[0].equals("search")) {
            Search s = new Search();
            for(int i=1; i < args.length; i++) {
                s.search(args[i].toLowerCase());
            }
            s.close();
        } else if (args[argc].equals("parse")) {
            Connection conn = database();

            Parser parser;
            if(args.length == 3) {
                parser = new Parser(args[1], args[2]);
            } else {
                parser = new Parser(INPUT_LINKS, INPUT_PAGE);
            }
            parser.run(conn);

            conn.close();
        } else if (args[argc].equals("build")) {
            Connection conn = database();

            Builder builder = new Builder(conn, cacheFile);
            builder.run();

            conn.close();
        } else if (args[argc].equals("index")) {
            Indexing i = new Indexing(cacheFile);
            i.run();
        }
    }

    
    /*
        Create database Connection
    */
    private static Connection database() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1/wiki?rewriteBatchedStatements=true", "root", "");
    }
}
