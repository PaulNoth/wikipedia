package anchors;

import static org.junit.Assert.*;
import mergingInflectedFormNE.InflectedFormMerge;
import org.junit.Test;


public class LinkExtractionTest {

	@Test
	public void test() {
		String Testfile = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
		String file = "../data/skwiki_link_anchor_cleaned.txt";
		InflectedFormMerge imf = new InflectedFormMerge();
		
		String sampleData = imf.linkAnchorListToString(imf.loadLinkAnchorFromXML(Testfile)); 
		String outputData = imf.linkAnchorListToString(imf.loadLinkAnchorFromTXT(file));
		assertEquals(outputData,sampleData);
	}

}
