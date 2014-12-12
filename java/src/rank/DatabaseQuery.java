package rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

public class DatabaseQuery {

	private
		Statement stat;
		ResultSet results;
		ResultSet numberOfAll;
	public DatabaseQuery(Connection conn)
	{
		
		results = null;
		try {
			stat = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ResultSet getResultSet()
	{
		return getResults();
	}
	public int getTotal()
	{
		return getTotalCount();
	}
	
	private ResultSet getResults()
	{
		
			
			try {
				results = stat.executeQuery("select pl_title, count(*) pocet from pagelinks group by pl_title order by pocet;");
				return results;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
		
		return null;
	}
	private int getTotalCount()
	{
		try {
			numberOfAll = stat.executeQuery("select max(pocet) from (select pl_title, count(*) pocet from pagelinks group by pl_title) tabulka;");
			numberOfAll.next();
			return numberOfAll.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
