package vi_dictionary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

public class TestingclassTest {
    
    public TestingclassTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of GetDictionary method, of class Testingclass.
     */
    @Test
    public void testGetDictionary() throws IOException, UnsupportedEncodingException, ParserConfigurationException, SAXException {
        System.out.println("GetDictionary");
        Testingclass instance = new Testingclass();
      
        HashMap<Integer,DictionaryEntry> Dictionary = instance.GetDictionary();
       
        assertEquals(9142415, Dictionary.get(9142415).GetID());
        if(Dictionary.get(9142415).GetSKLang() != null)
        {
        assertEquals(Dictionary.get(35652129).GetSKLang().GetName(),"Aki Heikkinen");
        assertEquals(Dictionary.get(35652129).GetSKLang().GetLink(),"http://sk.wikipedia.org/wiki/Aki Heikkinen");
        }
        
        if(Dictionary.get(9142415).GetFILang() != null)
        {
        assertEquals(Dictionary.get(9142415).GetFILang().GetName(),"Aki Heikkinen");
        assertEquals(Dictionary.get(9142415).GetFILang().GetLink(),"http://fi.wikipedia.org/wiki/Aki Heikkinen"); 
        }
        
        if(Dictionary.get(9142415).GetNOLang() != null)
        {
        assertEquals(Dictionary.get(9142415).GetNOLang().GetName(),"Aki Heikkinen");
        assertEquals(Dictionary.get(9142415).GetNOLang().GetLink(),"http://no.wikipedia.org/wiki/Aki Heikkinen"); 
        }
    }
    
}
