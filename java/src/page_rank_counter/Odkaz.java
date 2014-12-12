/**
 * TRieda re zaznamenanie jednotlivych odkazov ukazujucich na stranky ktorym sa pocita pagerank
 * Stranky, ktorym sa pocita pagerank su ulozene v classe Zaznam.
 */
public class Odkaz {

	private String id;
	private int count;
	private double pageRank;
	
	Odkaz(String _id)
	{
		id = _id;
		count = 1;
		setPageRank(1);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String _id) {
		id = _id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int _count) {
		count += _count;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double d) {
		this.pageRank = d;
	}
	
}
