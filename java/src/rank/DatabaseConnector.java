package rank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConnector {

	private
		Connection connection;
	public DatabaseConnector()
		{
			connection = null;
		}
	public Connection getConnection()
	{
		return startConnection();
	}
	public void stopConnection()
	{
		endConnection();
	}
	private Connection startConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver not found?");
			e.printStackTrace();
			return null;
		}
	 
		System.out.println("MySQL JDBC Driver Registered!");
		
	 
		try {
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/test","root", "mnau");
	 
		} catch (SQLException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
			return null;
		}
	 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			return connection;
		} else {
			System.out.println("Failed to make connection!");
		}
		return null;
	}
	
	private void endConnection()
	{
		try {
			connection.close();
			System.out.println("Connection closed.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Connection not closed!");
		}
	}
}
