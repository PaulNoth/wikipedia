package anchors;

import static org.junit.Assert.*;

import java.util.LinkedList;

import mergingInflectedFormNE.FileReaderXML;
import mergingInflectedFormNE.FileWriter;
import mergingInflectedFormNE.LinkAnchor;
import mergingInflectedFormNE.MergerOfInflectedForm;
import mergingInflectedFormNE.Page;

import org.junit.Test;

import testAndTools.TestFileCreater;

// TODO: Auto-generated Javadoc
/**
 * The Class InflectedFormMergingTest.
 */
public class InflectedFormMergingTest {

	/**
	 * Test.
	 */
	@Test
	public void test() {
		String file = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
		LinkedList<Page> pageList = new LinkedList<Page>();
		FileReaderXML fr = new FileReaderXML(file, pageList);
		
		LinkedList<LinkAnchor> la = fr.getLinkAnchor();
		
		TestFileCreater tfc = new TestFileCreater();
		tfc.prepareTestDataForNamedEntityMerging(la);
		String sampleData = tfc.namedEntityListToString(tfc.prepareTestDataForNamedEntityMerging(la));
		
		MergerOfInflectedForm moif = new MergerOfInflectedForm(la);
		FileWriter fw = new FileWriter();
		String outputData = fw.namedEntityListToString(moif.getNamedEntityList());
	
		assertEquals(outputData,sampleData); 
		//assertTrue("Sample output is equal to program output",outputData.equals(sampleData));
	}

}
