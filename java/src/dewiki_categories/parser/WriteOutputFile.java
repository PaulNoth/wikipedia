package dewiki_categories.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WriteOutputFile {
	
	public static void WriteSQLCategoriesMap( HashMap<String, Integer> hashMap )
	{
		Document doc = getNewDocument();
		if(doc != null)
		{
			Element rootElement = doc.createElement("sqldata");
			doc.appendChild(rootElement);
			
			Iterator<Entry<String, Integer>> it = hashMap.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();

				Element categ = doc.createElement("category");
				rootElement.appendChild(categ);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(pairs.getKey()));
				categ.appendChild(name);
				
				Element pages = doc.createElement("pages");
				pages.appendChild(doc.createTextNode(pairs.getValue().toString()));
				categ.appendChild(pages);
			}
			
			writeFile(doc, "SQLcategories");
		}
	}
	
	public static void WriteXMLCategoriesMap( HashMap<String, Integer> hashMap, long[] statistics )
	{
		Document doc = getNewDocument();
		if(doc != null)
		{
			Element rootElement = doc.createElement("data");
			doc.appendChild(rootElement);
			
			{
				Element stats = doc.createElement("stats");
				rootElement.appendChild(stats);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode("articles number"));
				stats.appendChild(name);
				
				Element pages = doc.createElement("count");
				pages.appendChild(doc.createTextNode(String.valueOf(statistics[0])));
				stats.appendChild(pages);
			}
			
			{
				Element stats = doc.createElement("stats");
				rootElement.appendChild(stats);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode("categories number"));
				stats.appendChild(name);
				
				Element pages = doc.createElement("count");
				pages.appendChild(doc.createTextNode(String.valueOf(statistics[1])));
				stats.appendChild(pages);
			}
			
			Iterator<Entry<String, Integer>> it = hashMap.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();

				Element categ = doc.createElement("category");
				rootElement.appendChild(categ);
				
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(pairs.getKey()));
				categ.appendChild(name);
				
				Element pages = doc.createElement("count");
				pages.appendChild(doc.createTextNode(pairs.getValue().toString()));
				categ.appendChild(pages);
			}
			
			writeFile(doc, "categories");
		}
	}
	
	//returns new XML document
	private static Document getNewDocument()
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			return docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println( e.getMessage() );
		}
		
		return null;
	}
	
	//write output file from give document
	private static void writeFile(Document d, String fileName)
	{
		try {	 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(d);
			StreamResult result = new StreamResult(new File(".\\data\\output-dewiki-" + fileName + ".xml"));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
			System.out.println("File saved to disk.");
	 
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }		
	}
	
}
