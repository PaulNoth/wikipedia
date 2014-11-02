package mergingInflectedFormNE;

import java.util.LinkedList;

import testAndTools.TestFileCreater;


// TODO: Auto-generated Javadoc
/**
 * The Class InflectedFormMerge.
 */
public class InflectedFormMerge {

	/** The page list. */
	static LinkedList<Page> pageList; 
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		InflectedFormMerge ifm = new InflectedFormMerge();	
		
		//ifm.suffixExtraction(ifm.loadMergedNamedEntity());
		
		//LinkedList<LinkAnchor> listLinkAnchor = ifm.loadLinkAnchorFromTXT("../data/skwiki_link_anchor_cleaned.txt");
		LinkedList<LinkAnchor> listLinkAnchor = ifm.loadLinkAnchorFromXML("../data/sample_input_skwiki-latest-pages-articles_3-pages.xml");
		//LinkedList<LinkAnchor> listLinkAnchor = ifm.loadLinkAnchorFromXML("../data/skwiki-latest-pages-articles.xml");
		//ifm.printLinkAnchor(listLinkAnchor);
		//ifm.namedEntityMergeTestFileCreate();
		//ifm.createLinkAnchorTestFile();
		//LinkedList<LinkAnchor> listLinkAnchor = ifm.loadLinkAnchor();
	}
	
	public void suffixExtraction(LinkedList<NamedEntity> llNE){
		SuffixExtractor se = new SuffixExtractor();
		LinkedList<Suffix> llSuf = new LinkedList<Suffix>();
		se.SuffixExtraction(llNE, llSuf);		
	}
	
	public LinkedList<NamedEntity> loadMergedNamedEntity(){
		FileReader fr = new FileReader();
		return fr.readMergedNE("../data/sample_test_data_merged_inflected_forms_with_count.txt");
	}
	
	public void namedEntityMerge(){
		String file = "../data/skwiki_link_anchor_cleaned.txt";
		FileReader fr = new FileReader();
		LinkedList<LinkAnchor> listLinkAnchor = fr.readLinkAnchorFromFile(file);
		
		MergerOfInflectedForm moif = new MergerOfInflectedForm(listLinkAnchor);
		FileWriter fw = new FileWriter();
		fw.writeFile("../data/sample_output_merged_inflected_forms.txt", fw.namedEntityListToString(moif.getNamedEntityList()));
		
		System.out.println(this.namedEntityListToStringWithNumberOfOccurences(moif.getNamedEntityList()));
		//TestFileCreater tfc = new TestFileCreater();
		//System.out.println(tfc.namedEntityListToString(moif.getNamedEntityList()));
	}
	
	public void namedEntityMergeTestFileCreate(){
		String file = "../data/skwiki_link_anchor_cleaned.txt";
		FileReader fr = new FileReader();
		LinkedList<LinkAnchor> listLinkAnchor = fr.readLinkAnchorFromFile(file);
		
		TestFileCreater tfc = new TestFileCreater();
		LinkedList<NamedEntity> llne = tfc.prepareTestDataForNamedEntityMergingWithCount(listLinkAnchor);
		
		FileWriter fw = new FileWriter();
		fw.writeFile("../data/sample_test_data_merged_inflected_forms_with_count.txt", tfc.namedEntityListToStringWithNumberOfOccurences(llne));
	}

	public LinkedList<LinkAnchor> loadLinkAnchorFromXML(String file){
     	pageList = new LinkedList<Page>();
		FileReaderXML fr = new FileReaderXML(file, pageList);		
		LinkedList<LinkAnchor> listLinkAnchor = fr.getLinkAnchor();
		return listLinkAnchor;
	}
	
	public LinkedList<LinkAnchor> loadLinkAnchorFromTXT(String file){
     	FileReader fr = new FileReader();	
     	LinkedList<LinkAnchor> listLinkAnchor = fr.readLinkAnchorFromFile(file);
		return listLinkAnchor;
	}
	
	public void createLinkAnchorTestFile(){
		String file = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
     	//String file = "../data/skwiki-latest-pages-articles.xml";
     	pageList = new LinkedList<Page>();
		FileReaderXML fr = new FileReaderXML(file, pageList);		
		LinkedList<LinkAnchor> listLinkAnchor = fr.getLinkAnchor();
		FileWriter fw = new FileWriter();
		fw.writeLinksToFile("../data/skwiki_link_anchor_cleaned.txt", listLinkAnchor);
	}
	
	/**
	 * Write pages.
	 *
	 * @param pageList the page list
	 */
	public static void writePages(LinkedList<Page> pageList){
		for(Page p : pageList){
			System.out.println("=========================================================================");
			System.out.println(p.getPageTitle());
			System.out.println(p.getPageText());
			System.out.println();
		}
	}
	
	public void printLinkAnchor(LinkedList<LinkAnchor> llla){
		for(LinkAnchor la : llla){
			System.out.println(la.getLink() + "		" + la.getAnchor() + "		" + la.getLink());
		}
	}
	
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
