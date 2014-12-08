package vi_dictionary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Testingclass {
    
    HashMap<Integer,DictionaryEntry> Dictionary;
    
    public HashMap<Integer,DictionaryEntry> GetDictionary()
    {
        return Dictionary;
    }
    
    public Testingclass() throws UnsupportedEncodingException, IOException, ParserConfigurationException, SAXException
    {
       Dictionary = new HashMap<Integer,DictionaryEntry>();
       FileReader fr = new FileReader();
       Dictionary = fr.ReadFile(Dictionary,true);
    }
}
