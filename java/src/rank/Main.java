package rank;

 
import java.sql.Connection;

 
public class Main {
		
 
  public static void main(String[] argv) {
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	DatabaseConnector dc = new DatabaseConnector();
	Connection connection = dc.getConnection();	
	DatabaseQuery dq = new DatabaseQuery(connection);
	//poslat do xml writera
	XMLWriter xw = new XMLWriter();
	xw.makeFile(dq.getTotal(), dq.getResultSet());
	
	dc.stopConnection();
  }
}