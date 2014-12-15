package InflectedFormMergeTest;

import static org.junit.Assert.*;

import java.util.HashMap;

import mergingInflectedFormNE.FileReader;
import mergingInflectedFormNE.Main;
import mergingInflectedFormNE.NamedEntity;
import mergingInflectedFormNE.NamedEntityCreator;
import mergingInflectedFormNE.RootChange;
import mergingInflectedFormNE.Suffix;

import org.junit.Test;

import testAndTools.BasicFileIO;
import testAndTools.StringTools;

// TODO: Auto-generated Javadoc
/**
 * The Class SuffixesTest.
 */
public class SuffixesTest {

	/**
	 * Test.
	 */
	@Test
	public void test() {
		String testFile = "../data/skwiki_test_suffixes_sample.txt";
		Main m = new Main();
		FileReader fr = new FileReader();
		StringTools st = new StringTools();
		
		HashMap<String,Suffix> hmSuf = new HashMap<String,Suffix>();
		HashMap<String,RootChange> hmRootChange = new HashMap<String,RootChange>();
		HashMap<String, NamedEntity> hmNamedEntity = m.createNamedEntityFromLinkAnchorSample(false);
		m.extractSuffixesAndRootChanges(hmNamedEntity, hmSuf, hmRootChange);
		
		String outputData = st.suffixHashMapToString(hmSuf);
		
		BasicFileIO bfIO = new BasicFileIO();
		
		String testData = bfIO.basicFileRead(testFile);

		//System.out.println(outputData);
		assertEquals(outputData,testData.substring(1));
	}

}
