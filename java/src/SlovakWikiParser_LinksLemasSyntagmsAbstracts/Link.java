package wiki;

public class Link {

	private int infoboxStartLine;	
	private int infoboxEndLine;
	
	private String label;
	private String lemma;
	private String wLnk;
	private String dbLnk;
	private String articleAbstract;
	private String type;
	private String syntax;
	private String text;
	
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
	public String getText()
	{
		return this.text;
	}
	
	
	public int getInfoboxStartLine()
	{
		return this.infoboxStartLine;
	}
	public int getInfoboxEndLine()
	{
		return this.infoboxEndLine;
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
	public void setText(String s)
	{
		this.text=s;
	}
	
	public void setInfoboxStartLine(int ln)
	{
		this.infoboxStartLine=ln;
	}
	public void setInfoboxEndLine(int ln)
	{
		this.infoboxEndLine=ln;
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
		
		this.lemma = firstLetterToupper(this.lemma);
		this.articleAbstract="";
		this.wLnk="http://sk.wikipedia.org/wiki/" + this.lemma.replace(" ", "_");;
		this.dbLnk="http://sk.dbpedia.org/resource/" + this.lemma.replace(" ", "_");
		//this.dbLnk = "http://sk.dbpedia.org/resource/Marcel_Hossa";
		this.syntax="";
		this.type="";
		this.text="";
		this.infoboxStartLine=0;
		this.infoboxEndLine=0;
	}
	
	private static String firstLetterToupper(String s)
	{
		String strResult="";
		String first = s.substring(0, 1);
		strResult=first.toUpperCase() + s.substring(1);
		
		return strResult;
	}
}
