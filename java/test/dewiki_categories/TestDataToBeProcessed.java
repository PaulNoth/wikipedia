package dewiki_categories.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import dewiki_categories.com.scireum.open.xml.NodeHandler;
import dewiki_categories.com.scireum.open.xml.StructuredNode;
import dewiki_categories.com.scireum.open.xml.XMLReader;

//opis vystupov statistik v dokumente

/*
 * Test cases for input files for GUI application
 * these test method test presence and correct format of 
 * four needed input files
 */
public class TestDataToBeProcessed {

	//alter directory in which are the four input files if needed
	private static final String FILE_DIR = ".\\data";
	private static final String FILE_TEXT_EXT1 = ".xml";
	private static final String FILE_TEXT_EXT2 = ".txt";
	
	private static boolean foundOne = false;
	private static int statsFound;

	//alter paths to files as needed. These are the recommended default values
	private final String pathToCategoriesXml = ".\\data\\output-dewiki-categories.xml";
	private final String pathToArticlesTxt = ".\\data\\output-dewiki-articles.txt";
	private final String pathToSQLCategoriesXml = ".\\data\\output-dewiki-SQLcategories.xml";
	private final String pathToCategoryLinksTxt = ".\\data\\output-dewiki-SQLlinks.txt";
	
	@Before
	public void setUp() throws Exception {

	}
	
	/*
	 * This method tests presence of all four input files
	 * uses FILE_DIR defined above and lists files inside
	 * Method looks for exactly two TXT and two XML files 
	 * with the prefix "output"
	 * PASS if the files are present
	 */
	@Test
	public void testPresenceOfInputFiles()
	{
		FilenameFilter fnf1 = new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.startsWith("output") && name.endsWith(FILE_TEXT_EXT1);
			}
		};
		
		FilenameFilter fnf2 = new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.startsWith("output") && name.endsWith(FILE_TEXT_EXT2);
			}
		};
		
		File dir = new File(FILE_DIR);
		if(!dir.isDirectory())
		{
			Assert.fail("Directory does not exist!");
		}
		
		String[] list = dir.list(fnf1);
		
		if(list.length != 2)
		{
			Assert.fail("There need to be more than 1 file");
		}
		
		String[] list2 = dir.list(fnf2);
		
		if(list2.length != 2)
		{
			Assert.fail("There need to be exactly two big TXT files.");
		}
	}
	
	/*
	 * This method tests correct format of Categories XML file
	 * searches whether there is this structure:
	 * <data>
	 *   <category>
	 *     <name></name>
	 *     <count></count>
	 *   </category>
	 * </data>
	 * It also makes sure Count is a number
	 * PASS if the XML has correct format
	 */
	@Test
	public void testStructureOfCategoriesXML()
	{
		foundOne = false;
		statsFound = 0;
		XMLReader reader = new XMLReader();
		reader.addHandler("data", new NodeHandler() {			
			@Override
			public void process(StructuredNode node) {
				try {
					// query subtree
					node.queryString("category/name/text()");
					Integer.parseInt(node.queryString("category/count/text()"));
					foundOne = true;
					
				} catch (XPathExpressionException e) {
					Assert.fail(e.getMessage());
				} catch (NumberFormatException nfe) {
					Assert.fail(nfe.getMessage());
				}
			}
		});
		reader.addHandler("stats", new NodeHandler() {			
			@Override
			public void process(StructuredNode node) {
				try {
					// query subtree
					String name = node.queryString("name");
					Long.parseLong(node.queryString("count"));
					if(!"articles number".equals(name) && !"categories number".equals(name))
						Assert.fail("No allowed stats name found");
					else
						statsFound++;
					
				} catch (XPathExpressionException e) {
					Assert.fail(e.getMessage());
				} catch (NumberFormatException nfe) {
					Assert.fail(nfe.getMessage());
				}
			}
		});
		
		try {
			reader.parse(new FileInputStream(pathToCategoriesXml));
			if(!foundOne)
				Assert.fail("No such node found");
			if(statsFound != 2)
				Assert.fail("Two stat numbers not found");
		} catch (ParserConfigurationException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (SAXException e) {
			Assert.fail(e.getMessage());
		}
	}

	/*
	 * This method tests correct format of Categories SQL file
	 * searches whether there is this structure:
	 * <sqldata>
	 *   <category>
	 *     <name></name>
	 *     <pages></pages>
	 *   </category>
	 * </sqldata>
	 * It also makes sure Pages is a number
	 * PASS if the XML has correct format
	 */
	@Test
	public void testStructureOfSQLCategoriesXML()
	{
		foundOne = false;
		XMLReader reader = new XMLReader();
		reader.addHandler("sqldata", new NodeHandler() {			
			@Override
			public void process(StructuredNode node) {
				try {
					// query subtree
					node.queryString("category/name/text()");
					Integer.parseInt(node.queryString("category/pages/text()"));
					foundOne = true;
					
				} catch (XPathExpressionException e) {
					Assert.fail(e.getMessage());
				} catch (NumberFormatException nfe) {
					Assert.fail(nfe.getMessage());
				}
			}
		});
		
		try {
			reader.parse(new FileInputStream(pathToSQLCategoriesXml));	
			if(!foundOne)
				Assert.fail("No such node found");
		} catch (ParserConfigurationException e) {
			Assert.fail(e.getMessage());
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		} catch (SAXException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	/*
	 * This method tests correct format of Articles TXT file
	 * it gets the line from file and tries to split it according to " | "
	 * it also check whether the argument after | character is a number
	 * PASS if the TXT file has correct format
	 */
	@Test
	public void testStructureOfArticlesTXT()
	{
		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new FileReader(pathToArticlesTxt));

			String line = bfr.readLine();
			String[] arr = line.split(" \\| ");
			if(arr.length != 2)
				Assert.fail("Line was not in a correct format.");
			
			Integer.parseInt(arr[1]);

		} catch (NumberFormatException nfe) {
			Assert.fail("Number not found");
		} catch (FileNotFoundException fnfe) {
			   Assert.fail("File not found");
		} catch (IOException e) {
			Assert.fail("IO exception " + e.getMessage());
		}
		finally
		{
			try {
				bfr.close();
			} catch (IOException e) {
				Assert.fail("Close() IO exception " + e.getMessage());
			}
		}
	}
	
	/*
	 * This method tests correct format of SQL links TXT file
	 * it gets the line from file and tries to split it according to " | "
	 * it also check whether the argument after | character is a number
	 * PASS if the TXT file has correct format
	 */
	@Test
	public void testStructureOfSQLLinksTXT()
	{
		BufferedReader bfr = null;
		try {
			bfr = new BufferedReader(new FileReader(pathToCategoryLinksTxt));

			String line = bfr.readLine();
			String[] arr = line.split(" \\| ");
			if(arr.length != 2)
				Assert.fail("Line was not in a correct format.");
				
			Integer.parseInt(arr[1]);

		} catch (NumberFormatException nfe) {
			Assert.fail("Number not found");
		} catch (FileNotFoundException fnfe) {
			   Assert.fail("File not found");
		} catch (IOException e) {
			Assert.fail("IO exception " + e.getMessage());
		}
		finally
		{
			try {
				bfr.close();
			} catch (IOException e) {
				Assert.fail("Close() IO exception " + e.getMessage());
			}
		}
	}
}
