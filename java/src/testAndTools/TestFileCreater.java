package testAndTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import other.TitleExtraction;
import mergingInflectedFormNE.InflectedForm;
import mergingInflectedFormNE.MergerOfInflectedForm;
import mergingInflectedFormNE.LinkAnchor;
import mergingInflectedFormNE.NamedEntity;
import mergingInflectedFormNE.Page;

/**
 * @author Michal Blanarik
 * The Class TestFileCreater.
 */
public class TestFileCreater {

/**
 * The main method.
 *
 * @param args the arguments
 */
public static void main(String[] args){
	TestFileCreater tfc = new TestFileCreater();
	try {
		String inFile = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
		String outFile = "../data/skwiki_test_link_anchor_cleaned.txt";
		tfc.createTestFileForLinkAnchor(inFile, outFile);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/**
 * Creates the test file for link anchor.
 *
 * @param inFile the in file
 * @param outFile the out file
 * @throws IOException Signals that an I/O exception has occurred.
 */
public void createTestFileForLinkAnchor(String inFile, String outFile) throws IOException{
	FileInputStream fstream = new FileInputStream(inFile);
	DataInputStream in = new DataInputStream(fstream);
	BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
	
	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
	
	String strLine;
				
	Pattern pLink = Pattern.compile("([^\\]\\[]{0,25})(\\[\\[([^]]+?)\\]\\]\\p{L}*)([^\\]\\[]{0,25})");	//regex for page title detection
	Matcher mLink;
	
	Pattern pOtherLink = Pattern.compile("(Special:|User_talk:|Súbor:|Kategória:)");
	Matcher mOtherLink;
	
	//int i = 0;
	while ((strLine = br.readLine()) != null){
		
		//System.out.println(strLine);
		mLink = pLink.matcher(strLine);
		while(mLink.find()){
			//System.out.println(mLink.group(1) + "\n"+ mLink.group(2) + "\n" + mLink.group(3) + "\n" + mLink.group(4) + "\n====================\n");
			mOtherLink = pOtherLink.matcher(strLine); 
			if(!mOtherLink.find()){
				System.out.println(mLink.group(1).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">") 
						  + "\n" + mLink.group(2).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">")
						  + "\n" + mLink.group(3).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">")
						  + "\n" + mLink.group(4).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">")
						  + "\n====================\n");

				out.write(mLink.group(2).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ").replaceAll("''", ""));
				out.newLine();
			//	//i++;
			}
			//System.out.println(strLine);

		}
		

	}
	//System.out.println("Pocet spracovanych riadkov suboru: " + i);
	
	out.close();
			
	br.close();
	in.close();
	fstream.close();
	
}

public void createTestFileForPageTitle(String filePath){
	BufferedWriter out;
	try {
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
		
		
		
		out.close();
	} catch (IOException e) {
		System.out.println("Error: " + "could not write to file" + "  " + filePath);
		e.printStackTrace();
	}	
}

/**
 * Creates the test file.
 *
 * @param  linkAnchorList the list which contains all pairs of links and anchors
 * @param filePath the file path
 */
public void createTestFileForNamedEntityMerging(LinkedList<LinkAnchor> linkAnchorList, String filePath){
	LinkedList<NamedEntity> llne = prepareTestDataForNamedEntityMerging(linkAnchorList);
	
	
	BufferedWriter out;
	try {
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
		
	
		out.write(namedEntityListToString(llne));
				
		out.close();
	} catch (IOException e) {
		System.out.println("Error: " + "could not write to file" + "  " + filePath);
		e.printStackTrace();
	}
}

/**
 * Prepare test data.
 *
 * @param linkAnchorList the link anchor list
 * @return the linked list
 */
public LinkedList<NamedEntity> prepareTestDataForNamedEntityMergingWithCount(LinkedList<LinkAnchor> linkAnchorList) {
	LinkedList<NamedEntity> llne = new LinkedList<NamedEntity>();
	
	int index;
	for(LinkAnchor la : linkAnchorList){
		if((index=findCorrespondingNE( la, llne))!=-1){
			llne.get(index).addInflectedForm(la.getAnchor(), la.getLink());
		}
		else{
			if(la.getAnchor().length()==0)
			{
				llne.add(new NamedEntity(la));
			}
			else{
				llne.add(new NamedEntity(la));
			}
		}
	}
	return llne;
}

private int findCorrespondingNE(LinkAnchor la, LinkedList<NamedEntity> llne){
int i=0;
for(NamedEntity ne : llne){
	if(ne.getNE().equals(la.getLink())){
		//System.out.println(ne.getNE() + "			" + la.getLink());
		return i;
	}
	i++;
}	
return -1;
}

/**
 * Prepare test data.
 *
 * @param linkAnchorList the link anchor list
 * @return the linked list
 */
public LinkedList<NamedEntity> prepareTestDataForNamedEntityMerging(LinkedList<LinkAnchor> linkAnchorList) {
	LinkedList<NamedEntity> llne = new LinkedList<NamedEntity>();
	LinkedList<String> lls = new LinkedList<String>();
	for(LinkAnchor la : linkAnchorList){		//create list of named entity with multiple entity occurance
		lls.add(la.getCleanLink());
	}
	
	
	Collections.sort(lls, new StringComparatorBaseOnAlphabeticalOrder());
	StringTools st = new StringTools();
	st.uniq(lls);
	
	for(String s : lls){		//create list of named entity
		llne.add(new NamedEntity(s));
	}
		
	for(LinkAnchor la : linkAnchorList){		//add inflected form to the list
		for(NamedEntity ne: llne){
			if(ne.getNE().equals(la.getCleanLink())){
				ne.addInflectedForm(la.getAnchor(),la.getLink());
				break;
			}
		}		
	}	
	return llne;
}

/**
 * Named entity list to string.
 *
 * @param llne the llne
 * @return the string
 */
public String namedEntityListToString(LinkedList<NamedEntity> llne){
	int i = 1;
	StringBuilder s = new StringBuilder();
	//String str = "";
	for(NamedEntity ne : llne){
		//str=str+ "============================================================\n" + ne.getNE();
		s.append(i + "  ============================================================");
		//str=str+ i + "  ============================================================";
		for(InflectedForm infe : ne.getListInflectedForms()){
			s.append("\n" + infe.getInflectedForm());
			//str=str+ "\n" + infe;
		}
		s.append("\n");
		//str=str + "\n";
		i++;
	}
	s.substring(0,s.length()-1);
	//str=str.substring(0, str.length()-1);
	//System.out.println(str);
	//return str;
	return s.toString();
}

/**
 * Named entity list to string with number of occurences.
 *
 * @param llne the llne
 * @return the string
 */
public String namedEntityListToStringWithNumberOfOccurences(LinkedList<NamedEntity> llne){
	int i = 1;
	StringBuilder s = new StringBuilder();
	//String str = "";
	for(NamedEntity ne : llne){
		//str=str+ "============================================================\n" + ne.getNE();
		s.append("============================================================\n" + i + "	" + ne.getNE());
		//str=str+ i + "  ============================================================";
		for(InflectedForm infe : ne.getListInflectedForms()){
			s.append("\n" + infe.getInflectedForm() + "		" + infe.getOccurances());
			//str=str+ "\n" + infe;
		}
		s.append("\n");
		//str=str + "\n";
		i++;
	}
	s.substring(0,s.length()-1);
	//str=str.substring(0, str.length()-1);
	//System.out.println(str);
	//return str;
	return s.toString();
}

}
