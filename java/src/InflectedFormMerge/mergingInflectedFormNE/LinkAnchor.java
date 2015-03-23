package mergingInflectedFormNE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
* The Class LinkAnchor.
*/
public class LinkAnchor {

	/** The whole link with brackets and suffixes. */
	private String link="";				//whole link
	
	/** The link add info. */
	private String linkAddInfo="";		//information in brackets
	
	/** The clean link. */
	private String cleanLink="";		//link cleaned from brackets
	
	/** The anchor. */
	private String anchor="";
	//    \\[\\[(\\p{Lu}[^#\\|\\]\\[]+?\\|\\p{Lu}\\p{L}[^\\|\\]\\[]+?)\\]\\]
	
	/** The number of occurrences. */
	private int numberOfOccurrences = 1;
	
	/**
	 * Instantiates a new link anchor.
	 *
	 * @param s the s
	 */
	public LinkAnchor(String s){
		s = s.replaceAll("\\{\\{--\\}\\}", "--");
		// the "{{ }}" is not completly correct remove from dataset [[Juraj Palkoviè (1769{{--}}1850)|profesor J. Palkoviè]]
		Pattern pWrongLink = Pattern.compile(".*[#${}]+.*");		//regex for wrong link detection
		Matcher mWrongLink;
		mWrongLink = pWrongLink.matcher(s);
		if(mWrongLink.find()){
			//System.out.println("Error: wrong Link	" + s + "\n");
			this.link="";
		}
		else{
			Pattern pAnchor = Pattern.compile(".+\\]\\][^\\[]+$");	//regex for anchor after brackets detection
			Matcher mAnchor;
			mAnchor = pAnchor.matcher(s);
			if(mAnchor.find()){				//[[link]]suffix
				anchor = s.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
				manageLink(s.replaceAll("\\[\\[", "").replaceAll("\\]\\].+", ""));
			}
			else{
				s = s.replaceAll("\\[\\[", "").replaceAll("\\]\\]", "");
				if(s.contains("|")){		//[[link|anchor]]
					String[] pom = s.split("\\|");
					if(pom.length==0){				//this cope with links that contains only "&amp;", "&quot;", "''"
						System.out.println("Error: empty Link\n");
						this.link="";
					}
					else if(pom.length < 2){
						System.out.println(s);
						anchor = "";		
						/*!!!!!!!!!!!!!*/	manageLink( pom[0]);		
						System.out.println(this.link);
					}
					else{
						anchor = pom[1];
						manageLink( pom[0]);
					}
				}
				else{						//[[link]]
					anchor = "";
					manageLink(s);
				}
			}
		}
		
	}
	
	/**
	 * Manage link.
	 *
	 * @param s the s
	 */
	private void manageLink(String s){
		this.link=s.trim();
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
	 * Sets the occurrence.
	 *
	 * @param occurrence the new occurrence
	 */
	public void setOccurrence(int occurrence){
		this.numberOfOccurrences = occurrence;
	}
	
	/**
	 * Adds the occurrence.
	 */
	public void addOccurrence(){
		this.numberOfOccurrences++;
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
	
	/**
	 * Gets the occurrences.
	 *
	 * @return the occurrences
	 */
	public int getOccurrences(){
		return this.numberOfOccurrences;
	}
}
