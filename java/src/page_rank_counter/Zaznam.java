/**
 * Trieda popisujuca jednotlive zaznamy, pre ktore sa pocita pagerank.
 */
public class Zaznam {
	private String meno;
	private String id;
	private double pageRank;
	private double pageRankCount;
	private double pageRankChange;
	
	Zaznam(String _meno)
	{
		meno = _meno;
		id = "0";
		setPageRank(1);
		setPageRankCount(0);
		setPageRankChange(1);
		
	}
	
	public void setMeno(String _meno)
	{
		meno = _meno;
	}
	public String getMeno()
	{
		return meno;
	}
	public void setId(String _id)
	{
		id = _id;
	}
	public String getId()
	{
		return id;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public double getPageRankCount() {
		return pageRankCount;
	}

	public void setPageRankCount(double pageRankCount) {
		this.pageRankCount = pageRankCount;
	}

	public double getPageRankChange() {
		return pageRankChange;
	}

	public void setPageRankChange(double pageRankChange) {
		this.pageRankChange = pageRankChange;
	}

}
