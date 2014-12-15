package abstracts;

/**
 * 
 * @author Matej Toma
 *
 */
public class ParsedWikiArticleEntity {
	private String titleTag;
	private String textTag;
	private Integer namespace;
	
	//Create empty entity
	public ParsedWikiArticleEntity(){
		this.titleTag = "";
		this.textTag = "";
		this.namespace = 0;
	}
	
	//Getters and setters
	public void setTitleTag(String title){
		this.titleTag = title;
	}
	
	public String getTitleTag(){
		return titleTag;
	}
	
	public void setTextTag(String text){
		this.textTag = text;
	}
	
	public String getTextTag(){
		return textTag;
	}
	
	public void setNamespace(Integer ns){
		this.namespace = ns;
	}
	
	public Integer getNamespace(){
		return namespace;
	}
}
