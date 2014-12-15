package mergingInflectedFormNE;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		Main m = new Main();
		//m.loadLinkAnchorFromXML(true);
		//m.loadLinkAnchorFromXMLsample(true);
		
		//m.createNamedEntityFromLinkAnchorSample(true);
		//FileReader fr = new FileReader();
		//HashMap<String, NamedEntity> hmNE = fr.readNamedEntity("../data/sample_test_data_merged_inflected_forms.txt");
		//NamedEntityCreator nec = new NamedEntityCreator();
		//String outputData = nec.namedEntityHashMapToString(hmNE);
		//System.out.println(outputData);
		
		HashMap<String,Suffix> hmSuf = new HashMap<String,Suffix>();
		HashMap<String,RootChange> hmRootChange = new HashMap<String,RootChange>();
		HashMap<String, NamedEntity> hmNamedEntity = m.createNamedEntityFromLinkAnchorSample(false);
		m.extractSuffixesAndRootChanges(hmNamedEntity, hmSuf, hmRootChange);
		
		FileWriter fr = new FileWriter();
		fr.writeSuffixesToFile("../data/skwiki_suffixes_sample.txt", hmSuf, true);
		
		fr.writeRootChangeToFile("../data/skwiki_rootChanges_sample.txt", hmRootChange, true);
	}

	/**
	 * Extract suffixes and root changes.
	 *
	 * @param hmNE the hm ne
	 * @param hmSuf the hm suf
	 * @param hmRootChange the hm root change
	 */
	public void extractSuffixesAndRootChanges(HashMap<String, NamedEntity> hmNE, HashMap<String,Suffix> hmSuf, HashMap<String,RootChange> hmRootChange){
		SuffixExtractor sufE = new SuffixExtractor();
		sufE.SuffixAndRootChangeExtraction(hmNE, hmSuf, hmRootChange);		
	}
	
	/**
	 * Creates the named entity from link anchor.
	 *
	 * @param fileOutput the file output
	 * @return the hash map
	 */
	public HashMap<String, NamedEntity> createNamedEntityFromLinkAnchor(boolean fileOutput){
		HashMap<String, LinkAnchor> hmLinkAnchor = loadLinkAnchorFromXML(false);
		
	
		NamedEntityCreator nec = new NamedEntityCreator();
		HashMap<String, NamedEntity> hmNamedEntity = nec.prepareNamedEntityWithCount(hmLinkAnchor);
		
		if(fileOutput){
			FileWriter fw = new FileWriter();
			fw.writeNamedEntityToFile("../data/skwiki_merged_inflected_forms.txt", hmNamedEntity);	
		}
		//System.out.println(nec.namedEntityHashMapToStringWithNumberOfOccurences(hmNamedEntity));
		//FileWriter fw = new FileWriter();
		//fw.writeNamedEntityToFile("../data/sample_test_data_merged_inflected_forms.txt", hmNamedEntity);
		return hmNamedEntity;
	}
	
	/**
	 * Creates the named entity from loaded link anchor sample.
	 *
	 * @param hmLinkAnchor the hm link anchor
	 * @param fileOutput the file output
	 * @return the hash map
	 */
	public HashMap<String, NamedEntity> createNamedEntityFromLoadedLinkAnchorSample(HashMap<String, LinkAnchor> hmLinkAnchor, boolean fileOutput){
		NamedEntityCreator nec = new NamedEntityCreator();
		HashMap<String, NamedEntity> hmNamedEntity = nec.prepareNamedEntityWithCount(hmLinkAnchor);
		//System.out.println(nec.namedEntityHashMapToStringWithNumberOfOccurences(hmNamedEntity));
		
		if(fileOutput){
			FileWriter fw = new FileWriter();
			fw.writeNamedEntityToFile("../data/skwiki_merged_inflected_forms_sample.txt", hmNamedEntity);	
		}		
		return hmNamedEntity;
	}
	
	/**
	 * Creates the named entity from loaded link anchor.
	 *
	 * @param hmLinkAnchor the hm link anchor
	 * @param fileOutput the file output
	 * @return the hash map
	 */
	public HashMap<String, NamedEntity> createNamedEntityFromLoadedLinkAnchor(HashMap<String, LinkAnchor> hmLinkAnchor, boolean fileOutput){
		NamedEntityCreator nec = new NamedEntityCreator();
		HashMap<String, NamedEntity> hmNamedEntity = nec.prepareNamedEntityWithCount(hmLinkAnchor);
		//System.out.println(nec.namedEntityHashMapToStringWithNumberOfOccurences(hmNamedEntity));
		
		if(fileOutput){
			FileWriter fw = new FileWriter();
			fw.writeNamedEntityToFile("../data/skwiki_merged_inflected_forms.txt", hmNamedEntity);	
		}		
		return hmNamedEntity;
	}
	
	/**
	 * Creates the named entity from link anchor sample.
	 *
	 * @param fileOutput the file output
	 * @return the hash map
	 */
	public HashMap<String, NamedEntity> createNamedEntityFromLinkAnchorSample(boolean fileOutput){
		HashMap<String, LinkAnchor> hmLinkAnchor = loadLinkAnchorFromXMLsample(false);
		
	
		NamedEntityCreator nec = new NamedEntityCreator();
		HashMap<String, NamedEntity> hmNamedEntity = nec.prepareNamedEntityWithCount(hmLinkAnchor);
		//System.out.println(nec.namedEntityHashMapToStringWithNumberOfOccurences(hmNamedEntity));
		
		if(fileOutput){
			FileWriter fw = new FileWriter();
			fw.writeNamedEntityToFile("../data/skwiki_merged_inflected_forms_sample.txt", hmNamedEntity);	
		}
		
		return hmNamedEntity;
	}
	
	/**
	 * Load link anchor from txt.
	 *
	 * @param inputFile the input file
	 * @return the hash map
	 */
	public HashMap<String, LinkAnchor>/*LinkedList<LinkAnchor>*/ loadLinkAnchorFromTXT(String inputFile){
		FileReader fr = new FileReader();
		return fr.readLinkAnchorFromFile(inputFile);
		//return fr.readLinkAnchorFromFile(inputFile, true);
	}
	
	/**
	 * Load link anchor from xml.
	 *
	 * @param fileoutput the fileoutput
	 * @return the hash map
	 */
	public HashMap<String, LinkAnchor> loadLinkAnchorFromXML(boolean fileoutput){
		HashMap<String, LinkAnchor> hmLA = new HashMap<String, LinkAnchor>();
		String inputFile = "../data/skwiki-latest-pages-articles.xml";
		readWikiXML(hmLA, inputFile, fileoutput);
		return hmLA;
	}
	
	/**
	 * Load link anchor from xm lsample.
	 *
	 * @param fileoutput the fileoutput
	 * @return the hash map
	 */
	public HashMap<String, LinkAnchor> loadLinkAnchorFromXMLsample(boolean fileoutput){
		HashMap<String, LinkAnchor> hmLA = new HashMap<String, LinkAnchor>();
		String inputFile = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
		readWikiXMLsample(hmLA, inputFile, fileoutput);
		return hmLA;
	}
//=================================================================================================================================	
	/**
 * Save list of named entity to file.
 *
 * @param hmNamedEntity the hm named entity
 * @param outputFile the output file
 */
public void saveListOfNamedEntityToFile(HashMap<String, NamedEntity> hmNamedEntity, String outputFile){
		FileWriter fw = new FileWriter();
		if(outputFile.equals("")){
			fw.writeNamedEntityToFile("../data/skwiki_merged_inflected_forms.txt", hmNamedEntity);
		}
		else{
			fw.writeNamedEntityToFile("outputFile", hmNamedEntity);
		}		
	}
	
	
	/**
	 * Read wiki xm lsample.
	 *
	 * @param hmLA the hm la
	 * @param inputFileName the input file name
	 * @param fileOutput the file output
	 */
	public void readWikiXMLsample(HashMap<String, LinkAnchor> hmLA, String inputFileName, boolean fileOutput){
		FileReaderXML frXML = new FileReaderXML(inputFileName, hmLA);
		//frXML.printHashMapLinkAnchor();
		if(fileOutput){
			FileWriter fw = new FileWriter();
			fw.writeLinksToFile("../data/skwiki_link_anchor_sample.txt", frXML.getLinkAnchor(), true);
		}
	}
	
	/**
	 * Read wiki xml.
	 *
	 * @param hmLA the hm la
	 * @param inputFileName the input file name
	 * @param fileOutput the file output
	 */
	public void readWikiXML(HashMap<String, LinkAnchor> hmLA, String inputFileName, boolean fileOutput){
		FileReaderXML frXML = new FileReaderXML(inputFileName, hmLA);
		//frXML.printHashMapLinkAnchor();
		if(fileOutput){
			FileWriter fw = new FileWriter();
			fw.writeLinksToFile("../data/skwiki_link_anchor.txt", frXML.getLinkAnchor(), true);
		}
	}
	
}
