package wiki;

import java.util.List;

public class Sentence {
	private String fullSent;
	private List<Link> links;
	
	public String getFullSent()
	{
		return this.fullSent;
	}

	public List<Link> getLinks()
	{
		return this.links;
	}
	
	public void setFullSent(String s)
	{
		this.fullSent = s;
	}
	
	public void setLinks(List<Link> l)
	{
		this.links = l;
	}
	
}
