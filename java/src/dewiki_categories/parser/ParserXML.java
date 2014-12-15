package dewiki_categories.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import dewiki_categories.com.scireum.open.xml.NodeHandler;
import dewiki_categories.com.scireum.open.xml.StructuredNode;
import dewiki_categories.com.scireum.open.xml.XMLReader;

//SINGLETON class
public class ParserXML 
{
	//singleton instance
	private static ParserXML instance = null;
	private static int articlesCounter = 0;
	private static int categoriesCounter = 0;
	
	private long[] stats = new long[2];
	public long[] getStats() {
		return stats;
	}
	//default relative path to XML file
	//+ setter
	private String pathToXML = ".\\data\\input-dewiki-categories.xml";
	public void setPathToXML(String pathToXML) {
		this.pathToXML = pathToXML;
	}

	//XML reader
	private XMLReader reader = null;
	
	//Regex strings
	private String regexString = "(\\[\\[Kategorie:)(.*)(\\]\\])";
	private Pattern p = Pattern.compile(regexString);
	
	private HashMap<String, Integer> categHashmap = new HashMap<String, Integer>();
	public HashMap<String, Integer> getCategHashmap() {
		return categHashmap;
	}

	//
	//CONSTRUCTOR
	//
	private ParserXML() {
		reader = new XMLReader();
	}
	
	//getter for Instance
	public static ParserXML getInstance()
	{
		if(instance == null)
			return new ParserXML();
		else
			return instance;
	}
	
	public boolean parseFile() throws IOException
	{		
		File file = new File(".\\data\\output-dewiki-articles.txt");
		 
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		final BufferedWriter bf = new BufferedWriter(new FileWriter(file));
		
		reader.addHandler("page", new NodeHandler() {			
			@Override
			public void process(StructuredNode node) {
				
				try {
					//get the name and ID of article
					try
					{
						String nodeName = node.queryString("title");
						int nodeId = Integer.parseInt( node.queryString("id") );
						System.out.println("Processing node #" + articlesCounter++);
						
						if(!"".equals(nodeName) && nodeId > 0)
						{
							bf.write(nodeName + " | " + nodeId);
							bf.newLine();
							bf.flush();
						}
					}
					catch(NumberFormatException nfe) { System.out.println(nfe.getMessage()); }
					catch(Exception e) { System.out.println(e.getMessage()); }
					
					// We can now conveniently query the sub-dom of each node
					// using XPATH:
					String nodeText = node.queryString("revision/text/text()");
					Matcher matcher = p.matcher(nodeText);
					
					String category = "";
					try
					{
						while(matcher.find())
						{
							category = matcher.group(2);
							String[] arr = category.split("\\|");

							insertIntoMap(arr[0]);
							categoriesCounter++;
						}
					}
					catch(IllegalStateException e) { 
						System.out.println(e.getMessage()); 
					}
					
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			reader.parse(new FileInputStream(pathToXML));
			
			stats[0] = articlesCounter;
			stats[1] = categoriesCounter;
			articlesCounter = 0;
			categoriesCounter = 0;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		bf.close();
		return true;
	}
	
	private void insertIntoMap(String categ)
	{
		//if we already encountered the category
		if(categHashmap.containsKey(categ))
		{
			//increment value of occurrences
			categHashmap.put(categ, categHashmap.get(categ)+1);
		}
		else //if first time seen category
			categHashmap.put(categ, 1);
	}
}
