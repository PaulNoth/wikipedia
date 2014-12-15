

import au.com.bytecode.opencsv.CSVWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

/**
 *
 * @author patrik
 */
public class Builder {

    BufferedWriter file = null;

    Connection conn = null;

    String fileName = null;

    Builder(Connection conn, String fileName) {
        this.conn = conn;
        this.fileName = fileName;
    }
    
    /*
        initialy builder method, write data from database
    */
    public void run() throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(fileName), ',');
        writeCsv(writer);
        writer.close();
    }

    /*
        Get linked data from database
    */
    private ResultSet prepareResult(Connection conn) throws Exception {
        // For faster selection data
        Statement selectStatments = conn.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
        selectStatments.setFetchSize(Integer.MIN_VALUE);

        return selectStatments.executeQuery("SELECT page.name as page, category.name as category FROM page LEFT JOIN category ON page.id = category.id ORDER BY page.id LIMIT 100000");
    }

    /*
        Write data from database to csv file
    */
    private void writeCsv(CSVWriter writer) throws Exception {
        ResultSet rs = prepareResult(conn);

        String pageCache = null;
        LinkedList list = new LinkedList();

        while (rs.next()) {
            String categoryName = rs.getString("category");
            String pageName = rs.getString("page");

            if (pageCache == null) {
                pageCache = pageName;
                list.add(categoryName);
            } else if (pageCache.equals(pageName)) {
                list.add(categoryName);
            } else {
                String[] tmp = new String[list.size() + 1];
                tmp[0] = pageCache;
                
                int i = 1;
                for(Object category : list) {
                    tmp[i++] = (String) category;
                }
                list.clear();
                writer.writeNext(tmp);
                
                // NEXT ROUND
                pageCache = pageName;
                list.add(categoryName);
            }
        }
    }
}
