package InflectedFormMergeTest;

import static org.junit.Assert.*;

import java.util.HashMap;

import mergingInflectedFormNE.Main;
import mergingInflectedFormNE.NamedEntity;
import mergingInflectedFormNE.NamedEntityCreator;

import org.junit.Test;

import testAndTools.BasicFileIO;


// TODO: Auto-generated Javadoc
/**
 * The Class NamedEntityFromLinkAnchorTest.
 */
public class NamedEntityFromLinkAnchorTest {

	/**
	 * Test.
	 */
	@Test
	public void test() {
		String testFile = "../data/skwiki_test_merged_inflected_forms_sample.txt";
		Main m = new Main();

		HashMap<String, NamedEntity> hmNamedEntity = m.createNamedEntityFromLinkAnchorSample(false);
		NamedEntityCreator nec = new NamedEntityCreator();
		String outputData = nec.namedEntityHashMapToString(hmNamedEntity);
		
		BasicFileIO bfIO = new BasicFileIO();
		
		String testData = bfIO.basicFileRead(testFile);

		//System.out.println(outputData);
		assertEquals(outputData,testData);
	}
}
