package indexing;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import ttltoxml.SettingsOfTtlFiles;


/**
 * @author Skrisa Július
 * Parsing and saving xml file data to memory
 */
public class LoadGeneretedArticleXmlFile {
	Document doc;
	String file = SettingsOfTtlFiles.OutputFile;
	
	public LoadGeneretedArticleXmlFile(){ //loading xml file to memory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
