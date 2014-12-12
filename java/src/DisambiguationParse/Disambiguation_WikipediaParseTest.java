package DisambiguationParse;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;


public class Disambiguation_WikipediaParseTest {
     
	/* in test we use first file from en wiki dumps
	 * file: enwiki-lates-pages-articles1.xml
	 * we test onto first four values which parser finds 
	 * you need: file into wiki directory
	 * if you change file, you must change four values in the bottom of code
	 * 
	 */
	
	
	static ArrayList<String> listOfTitles = new ArrayList<String>();
	static String userdir = System.getProperty("user.dir");
	/* file which is for test, this file you have in dumps wikipedia*/
	 String workfile = userdir+"/wiki/enwiki-latest-pages-articles1.xmlddd";
	
	static boolean IndexFunction = true; //if index was turn on before, false
	
	@Test
	public void parsertest(){
	/* parsertest work with test handler*/
	 PageCallbackHandler handler = new Disambiguation_WikipediaHandlerTest();
	 String cesta= userdir+"/wiki/enwiki-latest-pages-articles1.xmlddd";
	
     WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(cesta);
     try {
     	
 	    
         wxsp.setPageCallback(handler);
         wxsp.parse();
         
        
         
         
 }catch(Exception e) {
         e.printStackTrace();
 }
    
     
    /* this is test for disambiguation page, if test works first four page are there:*/ 
    String test1 = listOfTitles.get(0);
    //Austin (disambiguation)
    
    String test2 = listOfTitles.get(1);
    //Aberdeen (disambiguation)
    
    String test3 = listOfTitles.get(2);
    //Argument (disambiguation)
    
    String test4 = listOfTitles.get(3);
    //Animal (disambiguation)
    
    assertEquals(test1,"Austin (disambiguation)");
    assertEquals(test2,"Aberdeen (disambiguation)");
    assertEquals(test3,"Argument (disambiguation)");
    assertEquals(test4,"Animal (disambiguation)");
     
   /* if all of four is true, file structure is OK*/
     
     
     
	
	}
}
