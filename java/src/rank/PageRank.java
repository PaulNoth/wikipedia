package rank;

public class PageRank {
	public String getPageRank(int total, int actuall)
	{
		return countPageRank(total,actuall);
	}
	private String countPageRank(int total, int actuall)
	{
		if(total != 0)
		{
			return "" + ((actuall*10)/total);
		}
		else
		{
			return "error";
		}
	}
}
