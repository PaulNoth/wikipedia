package testAndTools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mergingInflectedFormNE.Forms;
import mergingInflectedFormNE.LinkAnchor;
import mergingInflectedFormNE.RootChange;
import mergingInflectedFormNE.Suffix;

// TODO: Auto-generated Javadoc
/**
 * The Class StringTools.
 */
public class StringTools {

	/**
	 * Removes the html characters from string.
	 *
	 * @param str the str
	 * @return the string
	 */
	public String removeHTMLCharactersFromString(String str){
		str = str.trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ").replaceAll("''", "").replaceAll("&lt", "<").replaceAll("&gt", "<").replaceAll("\"", "").replaceAll("\\{\\{--\\}\\}", "--");
		return 	str.trim().replaceAll("\t", " ");
	}
	
	/**
	 * Suffix hash map to string.
	 *
	 * @param hmSuf the hm suf
	 * @return the string
	 */
	public String suffixHashMapToString(HashMap<String, Suffix> hmSuf){
		SortHashMap sHM = new SortHashMap();
		hmSuf = sHM.sortSuffixesByNumberOfOccurrences(hmSuf);
		
		StringBuilder str = new StringBuilder();
				
		for(Map.Entry<String, Suffix> entry : hmSuf.entrySet()){
			Suffix suf = entry.getValue();
			
			str.append(suf.getSuffix() + "	" + suf.getNumberOfOccurrences() + "\n");
			
			HashMap<String, Forms> pomForm = suf.getStringsWithSuffix();
			pomForm = sHM.sortFormsNumberOfOccurrences(pomForm);
				
			for(Map.Entry<String, Forms> e : pomForm.entrySet()){
				Forms f = e.getValue();
				str.append(f.getForm1() + "	" + f.getForm2() + "	" + f.getOccurrences()+"\n");
			}				
			
			str.append("\n");
		}
		
		return str.toString();		
	}
	
	/**
	 * Root change hash map to string.
	 *
	 * @param hmRootChange the hm root change
	 * @return the string
	 */
	public String rootChangeHashMapToString(HashMap<String, RootChange> hmRootChange){
		SortHashMap sHM = new SortHashMap();
		hmRootChange = sHM.sortRootChangeByNumberOfOccurrences(hmRootChange);
		
		StringBuilder str = new StringBuilder();
		
		for(Map.Entry<String, RootChange> entry : hmRootChange.entrySet()){
			RootChange rc = entry.getValue();
			str.append(rc.getRootBeforeChange() + "	" + rc.getRootChange() + "	" + rc.getNumberOfOccurrences()+"\n");
					
			HashMap<String, Forms> pomForm = rc.getHashMapForms();
			pomForm = sHM.sortFormsNumberOfOccurrences(pomForm);				
			for(Map.Entry<String, Forms> e : pomForm.entrySet()){
				Forms f = e.getValue();
				str.append(f.getForm1() + "	" + f.getForm2() + "	" + f.getOccurrences()+"\n");
			}
			str.append("\n");
		}
		return str.toString();		
	}	
	
	/**
	 * Link anchor hash mapto string.
	 *
	 * @param hmLinkAnchor the hm link anchor
	 * @return the string
	 */
	public String linkAnchorHashMaptoString(HashMap<String, LinkAnchor> hmLinkAnchor){
		StringBuilder str = new StringBuilder();
		for(Map.Entry<String, LinkAnchor> entry : hmLinkAnchor.entrySet()){
			String key = entry.getKey();
			LinkAnchor la = entry.getValue();
			if(la.getAnchor().length()>0){
				str.append("[["+la.getLink()+"|"+la.getAnchor()+"]]\n");
			}
			else{
				str.append("[["+la.getLink()+"]]\n");
			}			
		}
		return str.toString();
	}
	
	/**
	 * Link anchor list to string.
	 *
	 * @param listLinkAnchor the list link anchor
	 * @return the string
	 */
	public String linkAnchorListToString(LinkedList<LinkAnchor> listLinkAnchor){
		StringBuilder str = new StringBuilder();
		for(LinkAnchor la : listLinkAnchor){
			if(la.getAnchor().length()>0){
				str.append("[["+la.getLink()+"|"+la.getAnchor()+"]]\n");
			}
			else{
				str.append("[["+la.getLink()+"]]\n");
			}			
		}
		return str.toString();
	}

	
	/**
	 * Uniq.
	 *
	 * @param ls the ls
	 */
	public void uniq(List<String> ls){		//remove redundant multiple occurrence strings from String list
		int i;								// list has to be in alphabeticla order
		for(i=0;i<(ls.size()-1);i++){
			if(ls.get(i).equals(ls.get(i+1))){
				ls.remove(i+1);
				i--;
			}
		}
	}

	/**
	 * Uniq ne.
	 *
	 * @param ls the ls
	 */
	public void uniqNE(List<String> ls){	//remove redundant multiple occurrence strings from Named Entity list
		int i;								// list has to be in alphabeticla order
		for(i=0;i<(ls.size()-1);i++){
			if(ls.get(i).equals(ls.get(i+1))){
				ls.remove(i+1);
				i--;
			}
		}
	}

	/**
	 * Uniq la.
	 *
	 * @param la the la
	 */
	public void uniqLA(List<LinkAnchor> la){	//remove redundant multiple occurrence strings from LinkAnchor list
		int i;									// list has to be in alphabeticla order
		for(i=0;i<(la.size()-1);i++){
			if(la.get(i).getLink().equals(la.get(i+1).getLink())){
				la.remove(i+1);
				i--;
			}
		}
	}
	}
