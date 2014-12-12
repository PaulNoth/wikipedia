package mergingInflectedFormNE;

import java.io.File;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

// TODO: Auto-generated Javadoc
/**
 * The Class DomXMLfileReader.
 */
public class DomXMLfileReader {

/** The file. */
private String file;	

/** The page list. */
private LinkedList<Page> pageList;

/**
 * Instantiates a new dom xm lfile reader.
 *
 * @param file the file
 * @param pageList the page list
 */
public DomXMLfileReader(String file, LinkedList<Page> pageList){
	this.file=file;
	this.pageList = pageList;
	readXML(file, pageList);
}

/**
 * Read xml.
 *
 * @param file the file
 * @param pageList the page list
 */
private void readXML(String file, LinkedList<Page> pageList){
	File xmlFile = new File(file);
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	try {
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xmlFile);
		
		doc.getDocumentElement().normalize();
		
		NodeList nl = doc.getElementsByTagName("page");
		//page, title, text 
		
		int i;
		for(i=0;i<nl.getLength();i++){
			Node n = nl.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE){
				Element eElement = (Element) n;
				System.out.println("===================================================================");
				System.out.println("Title: " + eElement.getElementsByTagName("title").item(0).getTextContent());
				//System.out.println("Page text:\n" + eElement.getElementsByTagName("text").item(0).getTextContent()+"\n");
			}
		}
	} catch (Exception e) {
		System.out.println("Error: XML parse failed");
		e.printStackTrace();
	}
}

}
