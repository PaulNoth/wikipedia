package anchors;

public class Record 
{
	private String page_title;
	private String page_link;
	private String page_anchor;
	
	public String return_page_title()
	{	return page_title;	}
	
	public String return_page_link()
	{	return page_link;	}
	
	public String return_page_anchor()
	{	return page_anchor;	}
	
	//---------------------
	
	public void set_page_title(String page_title)
	{	this.page_title = page_title;	}
	
	public void set_page_link(String page_link)
	{	this.page_link = page_link;	}
	
	public void set_page_anchor(String page_anchor)
	{	this.page_anchor = page_anchor;	}
	
}
	

