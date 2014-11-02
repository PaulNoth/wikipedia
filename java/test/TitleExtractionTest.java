package jUnitTest;

import static org.junit.Assert.*;

import org.junit.Test;

import other.TitleExtraction;
import testAndTools.BasicFileIO;

public class TitleExtractionTest {

	/**
	 * Test.
	 */
	@Test
	public void test() {
		String testFile = "../data/skwiki_page_titles_cleaned.txt";
		String inputFile = "../data/skwiki-latest-pages-articles.xml";
		String outputData;
		String testData;
		
		//assertEquals("asad\nahoj ja som Michal\nale ja nie som","asad\nahoj ja som Michal\nale ja nie som");
		
		BasicFileIO bfIO = new BasicFileIO();
		testData = bfIO.basicFileRead(testFile); 
		
		TitleExtraction te = new TitleExtraction();
		outputData = te.extractPageTitles(inputFile);
		//assertEquals(testData,testData+"\n");
		assertEquals(testData.substring(101),outputData.substring(100)); 

	}

}
