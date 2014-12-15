package rank;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XMLWriter {

	private 
		PageRank pr;
		Document doc;
	public XMLWriter()
	{
		pr = new PageRank();
	}
	
	public void makeFile(int totalCount, ResultSet results)
	{
		createFile(totalCount, results);
	}
	private void createFile( int totalCount, ResultSet results)
	{
		
		try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				
				// root elements
				doc = docBuilder.newDocument();
				Element rootElement = doc.createElement("page_rank_schema");
				doc.appendChild(rootElement);
				
				while(results.next())
				{
					Element pages = doc.createElement("pages");
					rootElement.appendChild(pages);
					
					Element page_id = doc.createElement("page_id");
					page_id.appendChild(doc.createTextNode(results.getString("pl_title")));
					pages.appendChild(page_id);
					
					Element link_count = doc.createElement("link_count");
					link_count.appendChild(doc.createTextNode(results.getString("pocet")));
					pages.appendChild(link_count);
					
					Element page_rank = doc.createElement("page_rank");
					page_rank.appendChild(doc.createTextNode(pr.getPageRank(totalCount, results.getInt("pocet"))));
					pages.appendChild(page_rank);
				}
				
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("C:\\martinka\\sample-output-pagerank.xml"));
		 
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
		 
				transformer.transform(source, result);
		 
				System.out.println("File saved!");
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private void createElements(Element root, ResultSet results) throws DOMException, SQLException
	{
		// staff elements
		Element pages = doc.createElement("pages");
		root.appendChild(pages);
		
		Element page_id = doc.createElement("page_id");
		page_id.appendChild(doc.createTextNode(results.getString("pl_title")));
		pages.appendChild(page_id);
		
		Element link_count = doc.createElement("link_count");
		link_count.appendChild(doc.createTextNode(results.getString("pocet")));
		pages.appendChild(link_count);
	}
	
}
