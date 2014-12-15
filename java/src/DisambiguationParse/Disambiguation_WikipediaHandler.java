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




public class Disambiguation_WikipediaHandler extends Disambiguation_WikipediaParse_Main implements PageCallbackHandler {
	
        public void process(WikiPage page) {
        	
       
        	 String userdir = System.getProperty("user.dir"); 	
        
    		
    
    		
    		/*
    		 * If indexFunction is true, so we create index file - we store title of every page and file location
    		 */
    		if(IndexFunction==true){
    			
 
    			
    			String titleforme = page.getTitle().trim();
        		String titleid = page.getID();
    			
    		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(userdir+"/WikiParse/index.txt", true)))) {
    		    out.println("T:"+titleforme+"|"+titleid+"|"+WorkingFile+"|>");
    		}catch (IOException e) {
    		    //exception handling left as an exercise for the reader
    		}
    		
    		}
    	
        	
        	if(page.getTitle().indexOf("disambiguation")>=0 || page.getCategories().indexOf("disambiguation")>=0 || page.getWikiText().indexOf("disambiguation")>=0){
        	
        	/*
        	 * We search disabiguation pages, this page are marked different so cases:
        	 * In title
        	 * In categories
        	 * In wikitext
        	 * 	
        	 */
        		
        		
        		listOfTitles.add(page.getTitle());
        		listOfCategories.add(page.getCategories().toString());
        		listOfLinks.add(page.getLinks().toString());
        		
             /*
              * Add page title, links and categories to lists
              *    
              *    
              */
                    
        	}
        }

}