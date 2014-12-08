package abstracts;

/**
 * 
 * @author Matej Toma
 *
 */
public class ParsedWikiAbstractEntity {
	private String titleTag;
	private String abstractTextTag;
	
	//Create empty entity
	public ParsedWikiAbstractEntity(){
		this.titleTag = "";
		this.abstractTextTag = "";
	}
	
	//Getters and setters
	public void setTitleTag(String title){
		this.titleTag = title;
	}
	
	public void setAbstractTextTag(String text){
		this.abstractTextTag = text;
	}
	
	public String getTitleTag(){
		return titleTag;
	}
	
	public String getAbstractTextTag(){
		return abstractTextTag;
	}
}
