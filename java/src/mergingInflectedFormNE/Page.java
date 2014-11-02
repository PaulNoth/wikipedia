package mergingInflectedFormNE;

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class Page.
 */
public class Page {

/** The title. */
private String title="";

/** The page text. */
private String pageText="";

/**
 * Instantiates a new page.
 *
 * @param title the title
 * @param text the text
 * @param link_anchor the link_anchor
 */
public Page(String title, String text, LinkedList<LinkAnchor> link_anchor){
	handleTitle(title);
	handlePageText(text, link_anchor);
}

/**
 * Handle title.
 *
 * @param title the title
 */
private void handleTitle(String title){
	this.title=title;
}

/**
 * Handle page text.
 *
 * @param text the text
 * @param link_anchor the link_anchor
 */
private void handlePageText(String text, LinkedList<LinkAnchor> link_anchor){
	text= text.replaceAll("</?text.*?>", "");
	//System.out.println(text);
	//  \[\[(\p{Lu}[^#\|\]\[]+?\|\p{Lu}\p{L}[^\|\]\[]+?)\]\]
	
	//Pattern pLink = Pattern.compile("\\[\\[(\\p{Lu}[^#\\|\\]\\[]+?\\|\\p{Lu}.? ?\\p{L}[^\\|\\]\\[]+?)\\]\\]");
	//Matcher mLink;
	
	Pattern pLink = Pattern.compile("([^\\]\\[]{0,25})(\\[\\[([^]]+?)\\]\\]\\p{L}*)([^\\]\\[]{0,25})");	//regex for page title detection
	Matcher mLink;
	
	Pattern pOtherLink = Pattern.compile("\\[\\[[^\\|\\]]+:[^\\]]+\\]\\]");
	Matcher mOtherLink;
	//Pattern pOtherLink = Pattern.compile("(Special:|User_talk:|Súbor:|súbor:|Kategória:|Wikipedia:|Wikipédia:|wikipédia:|Pomoc:|User:|Image:|Redaktor:|en:|Meta:)");
	//Matcher mOtherLink;
	
	String[] sSplit = text.split("\n");
	
	String pom;
	for(String s : sSplit){
		mLink = pLink.matcher(s);
		while(mLink.find()){
			mOtherLink = pOtherLink.matcher(s); 
			if(!mOtherLink.find()){
				pom=mLink.group(2).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ").replaceAll("''", "");
				//pom=mLink.group(1).trim();
				//System.out.println(pom);
				link_anchor.add(new LinkAnchor(pom));
			}
		}
			
	}
	
	this.pageText=text;
}

/**
 * Gets the page title.
 *
 * @return the page title
 */
public String getPageTitle(){
	return this.title;
}

/**
 * Gets the page text.
 *
 * @return the page text
 */
public String getPageText(){
	return this.pageText;
}
}
