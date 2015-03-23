package vi_dictionary;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.String;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import static java.lang.System.in;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import jdk.internal.org.xml.sax.SAXException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileReader 
{
    private String FILENAME = "../../data/enwiki-latest-langlinks.sql"; 
    private String FILENAME_XML = "../../data/dictionary_fi_no_sk_output.xml";
    
    
    /*
    Metoda otvori predspracovany xml subor a nacita slovnik do pamate vo forme
    sindexovaneho zoznamu. 
    
    */
    
    public Directory ConstructDictionaryFromXML() throws ParserConfigurationException, org.xml.sax.SAXException, IOException
    {
        Directory index = new RAMDirectory();     
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
        try 
        {
        SAXParser saxParser = saxParserFactory.newSAXParser();
        
        MyHandler handler = new MyHandler();
        saxParser.parse(new File(FILENAME_XML), handler);
        
        
        int counter = handler.GetCount();
        index = handler.GetDictionary();
        
        System.out.println("Zindexovanych " + counter + " záznamov");
        
        
        }
        
        catch (ParserConfigurationException e) 
        {
            e.printStackTrace();
        } 
        return index;
    }
    
    
    /* 
    Metoda vracia Hashmapu slovnika.
    */
    public HashMap<Integer,DictionaryEntry> ConstructDictionary(HashMap<Integer,DictionaryEntry> Dictionary) throws FileNotFoundException, UnsupportedEncodingException, IOException
    {
        Dictionary = ReadFile(Dictionary,false);
        return Dictionary;
    }
    
    
    /*
    Metoda spracovava subor z wikipedie a vytvara slovnik v pamati vo forme hashmapy
    */
    public HashMap<Integer,DictionaryEntry> ReadFile(HashMap<Integer,DictionaryEntry> Dictionary, boolean firstonly) throws FileNotFoundException, UnsupportedEncodingException, IOException 
    {
	FileInputStream fs = new FileInputStream(FILENAME);
	DataInputStream ds = new DataInputStream(fs);
	BufferedReader reader = new BufferedReader(new InputStreamReader(fs, "UTF8"));
	String line;
        int counter=0;
					      
	while ((line = reader.readLine()) != null)
        {          
            counter++;
            if(line.contains("INSERT INTO"))
            {
                
            try
            {
                String[] substrings = line.split("\\)\\,");   
                if(substrings.length==0)
                {
                    substrings[0] = line;
                }
                
                substrings[0] = substrings[0].substring(31);
                System.out.println("Spracovavam riadok " + counter + "/797");
                int counterString=0;
                int counterp = 0;
                for (String item: substrings)
                {     
                    counterString++;
                    if(item.contains("'fi'") || item.contains("'sk'") || item.contains("'no'"))
                    {
                        float percentage = (float)counterString /(float)substrings.length * 100;
                        if(percentage >= counterp)
                        {                     
                            System.out.println((int)percentage + "%");
                            counterp +=20;
                        }
                        
                           boolean addItem = false;
                     
                            
                        item = item.replaceAll("\\(","");
                        item = item.replaceAll("\\)","");
                        item = item.replaceAll("\\'","");
                        item = item.replaceAll(";","");//prerobit
                        
                        String[] itemParts = item.split(",");
                        
                        if(itemParts.length <3 || itemParts[0] == null || itemParts[1] == null || itemParts[2] == null) 
                            continue;
                        
                        int ID = Integer.parseInt(itemParts[0]);
                        
                        DictionaryEntry DE = Dictionary.get(ID);//null;//listContainsItem(Dictionary, ID);
                        
                        
                        if(DE == null)
                        {
                            DE = new DictionaryEntry();
                            addItem = true;
                            DE.SetID((ID));
                        }
                        
                        LanguageLink l = new LanguageLink();
                        l.SetName(itemParts[2]);
                        l.SetLink("http://" + itemParts[1] + ".wikipedia.org/wiki/" + itemParts[2]);
                        
                        switch(itemParts[1])
                        {
                            case "fi":
                                DE.SetFILang(l);
                                break;
                            case "sk":
                                DE.SetSKLang(l);
                                break;
                            case "no":
                                DE.SetNOLang(l);
                                break;
                        }
                        
                        if(addItem)
                            Dictionary.put(ID,DE);                       
                    }
                }
            }
            catch(Exception e)
            {
                System.out.println(counter);
            }
            
            }
            
            if(firstonly && Dictionary.size() > 0) break;
        }
				
        fs.close();
	ds.close();
	reader.close();
		return Dictionary;
	}
}
