package ttltoxml;

import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


/**
 * @author Skrisa Július
 * 
 * This class is handling the creation of xml document. It is handling the records one by one.
 * During handling of one records there is several operations to be done:
 * 	1. method will check if the record contains predicate that is supported
 *  2. then the program will search in existing xml document (created during handling previous records) and its looking for an article where the record belongs
 *     if it is not found the article element will be created.
 *  3 record is saved in xml document on its place and element with data content is created.
 */
public class XmlHandlerOfParsedTtlRecords {
	
	private static DocumentBuilderFactory documentFactory;
	private static DocumentBuilder documentBuilder;
	private static Document document;
	private static Element rootElement;
	
	public XmlHandlerOfParsedTtlRecords(){
		try {
			documentFactory = DocumentBuilderFactory.newInstance();  
			documentBuilder = documentFactory.newDocumentBuilder();
			
			document = documentBuilder.newDocument();
			rootElement = document.createElement("Artcls");  
			document.appendChild(rootElement);
			
			Element numElement = document.createElement("NumOfArtcl");
			numElement.appendChild(document.createTextNode("0"));
			rootElement.appendChild(numElement);
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void handle(List<RecordModel>list){

			for(int i = 0; i< list.size(); i++){
				RecordModel r =list.get(i);
				System.out.println("Adding element " + r.getProperty() + " of " + r.getResource());
				addAttrs(r);
			}
			list.clear();

	}
	
	public static void handle(RecordModel r){ // adding record to xml document

			if(!r.getResource().equals("") &&  !r.getValue().equals(""))
				addAttrs(r);
	}
	
	private static void addAttrs(RecordModel recordModel) {
		Element node = document.getElementById(recordModel.getResource());

		switch(recordModel.getProperty()){
			case "label": 
				if(node == null){ // if there is not node that representing resource of this element - Lbl -> create node and save its label
					node = createNode(recordModel);
				}

				node.appendChild(createElement("Lbl", recordModel.getValue(),false));
				break;
			case "isPrimaryTopicOf": 
				if(node == null){
					node = createNode(recordModel);
				}
				node.appendChild(createElement("Wklnk", recordModel.getValue(),false));
				break;
			case "wikiPageExternalLink": 
				if(node == null){
					node = createNode(recordModel);
				}
				NodeList ExtrnlsList = node.getElementsByTagName("Extrnls"); //if there is not node Extrnls which encapsulate elements Extrnl , create it
				org.w3c.dom.Node Extrnls = ExtrnlsList.item(0);
				if(Extrnls == null)
					Extrnls = node.appendChild(createElement("Extrnls","",true));
				Extrnls.appendChild(createElement("Extrnl",recordModel.getValue(),false));
				incrementCounter(Extrnls);
				break;
			case "subject": 
				if(node == null){
					node = createNode(recordModel);
				}
				NodeList CtgrsList = node.getElementsByTagName("Ctgrs");
				org.w3c.dom.Node Ctgrs = CtgrsList.item(0);
				if(Ctgrs == null)
					Ctgrs = node.appendChild(createElement("Ctgrs","",true));
				Ctgrs.appendChild(createElement("Ctgr",recordModel.getValue(),false));
				incrementCounter(Ctgrs);
				break;
			case "comment": 
				if(node == null){
					node = createNode(recordModel);
				}
				node.appendChild(createElement("Abstrct", recordModel.getValue(),false));
				break;
			case "wikiPageWikiLink":
				if(node == null)
					return;
				NodeList RfrncsList = node.getElementsByTagName("Rfrncs");
				org.w3c.dom.Node Rfrncs = RfrncsList.item(0);
				if(Rfrncs == null)
					Rfrncs = node.appendChild(createElement("Rfrncs","",true));
				Rfrncs.appendChild(createElement("Rfrnc",recordModel.getValue(),false));
				incrementCounter(Rfrncs);
				break;
			case "wikiPageRedirects": 
				if(node == null){
					node = document.getElementById(recordModel.getValue());
					if(node == null)
						node = createNode(recordModel,true);
				}
				NodeList RdrctsList = node.getElementsByTagName("Rdrcts");
				org.w3c.dom.Node Rdrcts = RdrctsList.item(0);
				if(Rdrcts == null)
					Rdrcts = node.appendChild(createElement("Rdrcts","",true));
				Rdrcts.appendChild(createElement("Rdrct",recordModel.getResource(),false));
				incrementCounter(Rdrcts);
				break;
			default:
		}
		
	}
	private static void incrementCounter(org.w3c.dom.Node extrnls) { // handling count attributes in elements
		NamedNodeMap attrs = extrnls.getAttributes();
		org.w3c.dom.Node count = attrs.getNamedItem("count");
		String cStr = count.getNodeValue();
		int cInt = Integer.parseInt(cStr);
		cInt++;
		count.setNodeValue(Integer.toString(cInt));
	}

	public static Element createElement(String name, String value, boolean count){ // create element and fill it with value
		Element e = document.createElement(name);
		
		if(count)
			e.setAttribute("count", "0");
		
		e.appendChild(document.createTextNode(value));
		return e;
	}
	private static Element createNode(RecordModel recordModel) { // creat new article element
		NodeList NumOfArtcl = document.getElementsByTagName("NumOfArtcl");
		
		if(NumOfArtcl.item(0) != null)	{			
			int num = Integer.parseInt(NumOfArtcl.item(0).getTextContent());
			num++;
			NumOfArtcl.item(0).setTextContent(Integer.toString(num));
		}
		
		Element Artcl = document.createElement("Artcl");
		Artcl.setAttribute("Id", recordModel.getResource());
		Artcl.setIdAttribute("Id", true);
		rootElement.appendChild(Artcl);
		return Artcl;
	}
	
	private static Element createNode(RecordModel recordModel, boolean res) { // speacial case with creating article - if it is created during the adding the record which have switched the resource and value 
		Element Artcl = document.createElement("Artcl");					  // for example wiki redirects. Because we want to show where the site redirects not what site redirects to it.
		if(!res)
			Artcl.setAttribute("Id", recordModel.getValue());
		else
			Artcl.setAttribute("Id", recordModel.getResource());
		Artcl.setIdAttribute("Id", true);
		rootElement.appendChild(Artcl);
		return Artcl;
	}
	@SuppressWarnings("deprecation")
	void saveXmlFile(){
		try {
		
			OutputFormat format = new OutputFormat(document); 
			format.setLineWidth(65);
			format.setIndenting(true);
			format.setIndent(2);
			Writer out = new StringWriter();
			XMLSerializer serializer = new XMLSerializer(out, format);
			serializer.serialize(document);
			
			String formattedXML = out.toString();
			File outFile = new File(SettingsOfTtlFiles.OutputFile);
			outFile.getParentFile().mkdirs();
			PrintWriter writer = new PrintWriter(SettingsOfTtlFiles.OutputFile, "UTF-8");
			writer.print(formattedXML);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	
}
