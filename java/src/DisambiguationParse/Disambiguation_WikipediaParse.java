package DisambiguationParse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;


public class Disambiguation_WikipediaParse {
        /**
         * @author Michal Petras
         * 2014 FIIT - Vyhladavanie informacii
         */
	
	/* dorobit vysledky do wikipage a navod na junit testy*/
	
	/* Main class
	 * Use - parse abstract xml OR call wikipedia files parser
	 * If variable IndexFunction is true - indexing of page will be working, if false, skip this step
	 * If variable ParseAbstractFunction is true - class ParseAbstract will parse abstract.xml and create abstract index
	 *
	 * We could parse in small steps - for example 2gb files, but we need full abstract index (create from 4gb xml file - 10-15 minutes),
	 * but if we have every file, this parse every pages (small files = could missing pages)
	 *
	 *
	 * USE STEPS FOR NEW USER:
	 * 1. parseAbstract_function = true (you need wikipedia abstract file in abstract directory)
	 * 2. parse Abstract and create index (in directory abstracParse)
	 * 3. parseAbtract_function  = false (this turn of abstract parse)
	 * 4. IndexFunction = true
	 * 5. Load wikipedia xml files into directory Wiki
	 * 6. Run program
	 * 7. Program create index.txt in wikiParse
	 * 8. Program create file in root - this file is final parse of disambiguation page
	 * 9. with new wikipedia xml file in directory wiki, we need create new index (IndexFunction)
	 * 10. StartService = true -> this turn off parsers and turn on Search Service 
	 *
	 */
	
	/* RUN JAR
	 * 
	 * java -Xmx1024M -jar wikiparse.jar Index Abstract Service Help
	 * words in arg will turn on function 
	 * Index - indexfunction of wikifile
	 * Abstract - abstractparse
	 * Service - final search service of parse file
	 * 
	 * 
	 * */
	
	
	
	
	/* list where we store informations we find */
	static ArrayList<String> listOfTitles = new ArrayList<String>();
	static ArrayList<String> listOfCategories = new ArrayList<String>();
	static ArrayList<String> listOfLinks = new ArrayList<String>();
	static ArrayList<String> InWhichFile = new ArrayList<String>();
	static String WorkingFile = "";
	static boolean IndexFunction = false; // if true - indexing , if false - skip indexing - we must have index
	static boolean ParseAbstract_function = false; // if true - parse and create abstract index, if false - skip - we must have index
	static boolean StartService = true; //if true, parser wont work, serach service will turn on
	static boolean Help = false; //if true, parser wont work, serach service will turn on
	
	static String userdir = System.getProperty("user.dir");
	File file = new File (userdir+"/WikiParse/index.txt");
        public static void main(String[] args) {
        	
        	for (String s: args) {
               if(s.equals("Index"))IndexFunction = true;
               if(s.equals("Abstract"))ParseAbstract_function = true;
               if(s.equals("Service"))StartService = true;
               if(s.equals("Help"))Help = true;
            }
        	
     if(Help==true){
    	 
    	 System.out.println(" * java wikiparse.jar Index Abstract Service"+
" * words in arg will turn on function "+
	" * Index - indexfunction of wikifile"+
	" * Abstract - abstractparse"+
	" * Service - final search service of parse file"+
	" * -Xmx1024M");
    	 
    	 
    	System.exit(0); 
     }
        	
        	if(StartService==true)
        	{
        		
        		/* call final service, write page in console */ 
        		Disambiguation_FinalService fs = new Disambiguation_FinalService();
        		fs.openservice();
        		
        		
        		
        		
        		/*if search service is turn off, parsers are turn on*/
        	}else{
              
                
                if(ParseAbstract_function==true)
                {
                	Disambiguation_ParseAbstract pa = new Disambiguation_ParseAbstract();
                	try {
						pa.parse();
					} catch (IOException e) {
					
						e.printStackTrace();
					}
                	
                }
          
                Date date = new Date();
                
                // display time and date using toString()
                System.out.println(date.toString());
                
                /*iterate in wiki folder for wiki files */
                File path = new File(userdir+"/wiki");
                PageCallbackHandler handler = new Disambiguation_WikipediaHandler();
                File [] files = path.listFiles();
                for (int i = 0; i < files.length; i++){
                    if (files[i].isFile()){ //this line weeds out other directories/folders
                        System.out.println("Load File: "+files[i]);
                        WorkingFile = files[i].toString();
                        
                        
                        String cesta= files[i].toString();
                        WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(cesta);
                        
                    
                      
                        try {
                        	
                        	    
                                wxsp.setPageCallback(handler);
                                wxsp.parse();
                                
                               
                                
                                
                        }catch(Exception e) {
                                e.printStackTrace();
                        }
                        
                        
                        
                    }
                }
                
          /* after indexing and iterate we call search */  
          Disambiguation_Search a = new Disambiguation_Search();
          try {
        	  
        	  
			a.generate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          
        }
        }
}
