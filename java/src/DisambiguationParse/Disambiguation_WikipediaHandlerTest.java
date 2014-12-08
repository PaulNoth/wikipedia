package DisambiguationParse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;


/* this class is part of wikipediaparseTest 
 * we test parse one file, which is included in ParseTest 
 */

public class Disambiguation_WikipediaHandlerTest extends Disambiguation_WikipediaParseTest implements PageCallbackHandler {
	
        public void process(WikiPage page) {
    
    		
    		
    		/*if(IndexFunction==true){
    		
    		}*/
        	
        	/* this is part of the test of structure file from wiki xml*/
        	if(page.getTitle().indexOf("disambiguation")>=0 || page.getCategories().indexOf("disambiguation")>=0){
        	
        		
        		//System.out.println(page.getTitle().trim());
               
        		
        		listOfTitles.add(page.getTitle().trim());
        	
                    
        	}
        }

}