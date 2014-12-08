package wiki;

public class Link {

	private String label;
	private String lemma;
	private String wLnk;
	private String dbLnk;
	private String articleAbstract;
	private String type;
	private String syntax;
	
	public String getLabel()
	{
		return this.label;
	}
	public String getLemma()
	{
		return this.lemma;
	}
	public String getWLnk()
	{
		return this.wLnk;
	}
	public String getDbLnk()
	{
		return this.dbLnk;
	}
	public String getArticleAbstract()
	{
		return this.articleAbstract;
	}
	public String getType()
	{
		return this.type;
	}
	public String getSyntax()
	{
		return this.syntax;
	}
	
	
	public void setLabel(String s)
	{
		this.label=s;
	}
	public void setLemma(String s)
	{
		this.lemma=s;
	}
	public void setWLnk(String s)
	{
		this.wLnk=s;
	}
	public void setDbLnk(String s)
	{
		this.dbLnk=s;
	}
	public void setArticleAbstract(String s)
	{
		this.articleAbstract=s;
	}
	public void setType(String s)
	{
		this.type=s;
	}
	public void setSyntax(String s)
	{
		this.syntax=s;
	}
	
	/**
	 * creates Link object from parsed link in form [[...]]
	 * */
	public Link(String parsed)
	{
		//in case the word contains its lemma
		
		if(parsed.endsWith("]]"))
		{
			String[] splitted = parsed.replace("[[", "").replace("]]", "").split("\\|");
			
			this.lemma = splitted[0];
			if(splitted.length>1)
			{
				this.label = splitted[1];
			}
			else
				this.label = splitted[0];
		}
		else
		{
			String[] splitted = parsed.replace("[[", "").split("\\]\\]");
			this.lemma = splitted[0];
			if(splitted.length>1)
			{
				this.label = splitted[0] + splitted[1];
			}
			else
			{
				this.label = splitted[0];
			}
		}
		
		this.articleAbstract="";
		this.wLnk="http://sk.wikipedia.org/wiki/" + this.lemma.replace(" ", "_");;
		this.dbLnk="http://sk.dbpedia.org/resource/" + this.lemma.replace(" ", "_");
		//this.dbLnk = "http://sk.dbpedia.org/resource/Marcel_Hossa";
		this.syntax="";
		this.type="";
	}
}
