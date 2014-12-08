package mergingInflectedFormNE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class LinkAnchor.
 */
public class LinkAnchor {

	/** The link. */
	private String link;				//whole link
	
	/** The link add info. */
	private String linkAddInfo="";		//information in brackets
	
	/** The clean link. */
	private String cleanLink;			//link cleaned from brackets
	
	/** The anchor. */
	private String anchor;
	//    \\[\\[(\\p{Lu}[^#\\|\\]\\[]+?\\|\\p{Lu}\\p{L}[^\\|\\]\\[]+?)\\]\\]
	
	/**
	 * Instantiates a new link anchor.
	 *
	 * @param s the s
	 */
	public LinkAnchor(String s){
		s = s.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
		String[] pom = s.split("\\|");
		anchor = pom[1];
		manageLink( pom[0]);
		
	}
	
	/**
	 * Manage link.
	 *
	 * @param s the s
	 */
	private void manageLink(String s){
		this.link=s;
		// (\\(.+\\))
		Pattern pAddInfo = Pattern.compile("(^[\\p{L}\\s]+?)\\s?\\((.+)\\)");	//regex for addition information in brackets detection
		Matcher mAddInfo;
		mAddInfo = pAddInfo.matcher(s);
		if(mAddInfo.find()){
			
			//System.out.println(mAddInfo.groupCount());
			cleanLink = mAddInfo.group(1);
			linkAddInfo = mAddInfo.group(2);
		}
		else{
			cleanLink = s;
		}
		//System.out.println("----------------\n" + link + "|" + anchor + "   --" + clearLink + "--   --" + linkAddInfo + "--");
		
	}
	
	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink(){
		return link;
	}
	
	/**
	 * Gets the link add info.
	 *
	 * @return the link add info
	 */
	public String getLinkAddInfo(){
		return linkAddInfo;
	}
	
	/**
	 * Gets the clean link.
	 *
	 * @return the clean link
	 */
	public String getCleanLink(){
		return cleanLink;
	}
	
	/**
	 * Gets the anchor.
	 *
	 * @return the anchor
	 */
	public String getAnchor(){
		return anchor;
	}
}
