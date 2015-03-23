package InflectedFormMergeTest;

import static org.junit.Assert.*;

import java.util.HashMap;

import mergingInflectedFormNE.LinkAnchor;
import mergingInflectedFormNE.Main;

import org.junit.Test;

import testAndTools.StringTools;

// TODO: Auto-generated Javadoc
/**
 * The Class LinkExtractionTest.
 */
public class LinkExtractionTest {

	/**
	 * Test.
	 */
	@Test
	public void test() {
		String file = "../data/skwiki_test_link_anchor_sample.txt";
		StringTools st = new StringTools();
		Main m = new Main();

		String sampleData = st.linkAnchorHashMaptoString(m.loadLinkAnchorFromXMLsample(false)); 
		//String outputData = st.linkAnchorListToString(m.loadLinkAnchorFromTXT(file));
		String outputData = st.linkAnchorHashMaptoString(m.loadLinkAnchorFromTXT(file));
		//System.out.println(outputData);
		assertEquals(outputData,sampleData);
	}

}
