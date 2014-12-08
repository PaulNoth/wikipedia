package testAndTools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;

import mergingInflectedFormNE.MergerOfInflectedForm;
import mergingInflectedFormNE.LinkAnchor;
import mergingInflectedFormNE.NamedEntity;

/**
 * @author Michal Blanarik
 * The Class TestFileCreater.
 */
public class TestFileCreater {

/**
 * Instantiates a new test file creater.
 */
public TestFileCreater() {
	
}	
	
/**
 * Instantiates a new test file creater.
 *
 * @param linkAnchorList the list which contains all pairs of links and anchors
 * @param filePath the file path
 */
public TestFileCreater(LinkedList<LinkAnchor> linkAnchorList, String filePath) {
	createTestFile(linkAnchorList, filePath);	
}

/**
 * Creates the test file.
 *
 * @param linkAnchorList the link anchor list
 * @param filePath the file path
 */
public void createTestFile(LinkedList<LinkAnchor> linkAnchorList, String filePath){
	LinkedList<NamedEntity> llne = prepareTestData(linkAnchorList);
	
	
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
public LinkedList<NamedEntity> prepareTestData(LinkedList<LinkAnchor> linkAnchorList) {
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
				ne.addInflectedForm(la.getAnchor());
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
	String str = "";
	for(NamedEntity ne : llne){
		//str=str+ "============================================================\n" + ne.getNE();
		str=str+ i + "  ============================================================";
		for(String infe : ne.getInflectedForms()){
			str=str+ "\n" + infe;
		}
		str=str + "\n";
		i++;
	}
	str=str.substring(0, str.length()-1);
	//System.out.println(str);
	return str;
}

}
