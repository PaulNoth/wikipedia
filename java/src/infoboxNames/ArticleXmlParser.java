package infoboxNames;

import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ArticleXmlParser {

	//creating and starting parser for wikipedia xml file
	public StringBuilder parseXml(InputStream in)
	{
	
		StringBuilder infoboxes = new StringBuilder();
		
		try
		{
			ArticleParserHandler handler = new ArticleParserHandler();
			
			XMLReader parser = XMLReaderFactory.createXMLReader();
			
			parser.setContentHandler(handler);
			
			InputSource source = new InputSource(in);
			
			parser.parse(source);
			
			//get parsed information
			infoboxes = handler.getInfoboxes();
		
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		return infoboxes;
	}
}
