package vi_dictionary;
import java.util.LinkedList;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter 
{
    private String FILENAMEOUTPUT = "../../data/dictionary_fi_no_sk_output.xml"; 
    
    /*
    Metoda zapise do xml suboru slovnik nachadzajuci sa v pamati.
    */
    
    public void CreateXML(HashMap<Integer,DictionaryEntry> Dictionary)
    {
         try {
 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		Document doc = docBuilder.newDocument();
                
		Element rootElement = doc.createElement("Dictionary");
		doc.appendChild(rootElement);
                for(Map.Entry<Integer, DictionaryEntry> entryz : Dictionary.entrySet())
                {
                    DictionaryEntry item = entryz.getValue();
                    
                    Element entry = doc.createElement("Entry");
                    rootElement.appendChild(entry);
                    
                    Element id = doc.createElement("ID");
                    id.appendChild(doc.createTextNode((Integer.toString(item.GetID()))));
                    
                    entry.appendChild(id);
                    
                    if(item.GetSKLang() != null)
                    {
                    Element SK = doc.createElement("SK");
                    Element Name1 = doc.createElement("Name");
                    Element Link1 = doc.createElement("Link");
                    Name1.appendChild(doc.createTextNode(item.GetSKLang().GetName()));
                    Link1.appendChild(doc.createTextNode(item.GetSKLang().GetLink()));
                    
                    SK.appendChild(Name1);
                    SK.appendChild(Link1);
                    entry.appendChild(SK);
                    }
                    
                    if(item.GetFILang()!=null)
                    {
                    Element FI = doc.createElement("FI");
                    Element Name2 = doc.createElement("Name");
                    Element Link2 = doc.createElement("Link");
                    Name2.appendChild(doc.createTextNode(item.GetFILang().GetName()));
                    Link2.appendChild(doc.createTextNode(item.GetFILang().GetLink()));
                    
                    FI.appendChild(Name2);
                    FI.appendChild(Link2);
                    entry.appendChild(FI);
                    }
                    
                    if(item.GetNOLang()!=null)
                    {
                        Element NO = doc.createElement("NO");
                        Element Name3 = doc.createElement("Name");
                        Element Link3 = doc.createElement("Link");
                        Name3.appendChild(doc.createTextNode(item.GetNOLang().GetName()));
                        Link3.appendChild(doc.createTextNode(item.GetNOLang().GetLink()));
                    
                        NO.appendChild(Name3);
                        NO.appendChild(Link3);
                        entry.appendChild(NO);
                    }
                }
                
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                
                
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(FILENAMEOUTPUT));
		//StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
}
