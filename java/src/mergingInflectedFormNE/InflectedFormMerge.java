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
		//String file = "sample_skwiki-latest-pages-articles.xml";
		//String file = "skwiki-latest-pages-articles.xml";
		String file = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
		pageList = new LinkedList<Page>();
		FileReader fr = new FileReader(file, pageList);
		
		LinkedList<LinkAnchor> la = fr.getLinkAnchor();
		
		TestFileCreater tfc = new TestFileCreater(la, "../data/sample_test_data_merged_inflected_forms.txt");
		//System.out.println(fr.ToStringLinkAnchorList());
		
		//for(LinkAnchor lal : la){
		//	System.out.println(lal.getAnchor() + "  " + lal.getCleanLink());
		//}
		
		
		MergerOfInflectedForm moif = new MergerOfInflectedForm(la);
		FileWriter fw = new FileWriter();
		fw.writeFile("../data/sample_output_merged_inflected_forms.txt", fw.namedEntityListToString(moif.getNamedEntityList()));
		
		//System.out.println(tfc.namedEntityListToString(moif.getNamedEntityList()));
		
		
		//writePages(pageList);
		
		//DomXMLfileReader xmlfr= new DomXMLfileReader(file, pageList);
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

}
