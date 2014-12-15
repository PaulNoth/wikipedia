package DisambiguationParse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;


public class Disambiguation_Search extends Disambiguation_WikipediaParse_Main {
	
	
	/*
	 * Search class and generate method 
	 * 1. Load index(index create with first wikiparse) of every wikipage to the hashmap 
	 * 2. Work with links, which we find by first parse 
	 * 	  - every page has many links, so links are in list and we iterate  
	 * 3. In for cycle, we look into the hashmap and compare, if link is in the hashmap, we store file location and link
	 *    - hashmap is the fastest way in this case
	 * 4. Start final parse with list of file location and link
	 * 
	 */
	
	
	static Map<String, String> hashMap2 = new HashMap<String,  String>(); 
	
	public void generate() throws IOException{
	
		List<String[]> myList = new ArrayList<String[]>();
		 String userdir = System.getProperty("user.dir"); 	
		 File path = new File(userdir+"/WikiParse");
         
         File [] files = path.listFiles();
         System.out.println("Save index to the HashMap ... Please wait ... ");
         for (int i = 0; i < files.length; i++){
             if (files[i].isFile()){ 
                 /* load index file from wiki pages*/
                 WorkingFile = files[i].toString();
                 System.out.println("FILE INDEX OF WIKIPAGES: "+WorkingFile);
                 String directory_w= files[i].toString();
                 /* create buffer for reader */
                 final BufferedReader reader = new BufferedReader(new FileReader(directory_w));
                 
                 while(reader.ready()) {
                   /*read index file*/
                	 String read_string = reader.readLine().toString();
                	
                     if(read_string.contains("T:")){
                     String[] all_string = read_string.split("\\|");
                     /* save name of page and location file into hashmap*/
                     hashMap2.put(all_string[0], all_string[2]);}
                    
                     
                 }
                 reader.close();
          
                 
                 System.out.println("WORK WITH LINKS OF EVERY PAGES "+" | PAGES Count:"+listOfLinks.size());
                 for(int z=0;z<listOfLinks.size();z++){
                /* every pages has many links , listOflinks - one page has links*/	 
                	
                	 
             	String getLinks = listOfLinks.get(z);
             	/* getlinks - list of links, format [link+,link1,link2]*/
             	getLinks.replaceAll("\\[", "");
             	getLinks.replaceAll("\\]", "");
             	
             	String[] arrayOfLinks = getLinks.split(",");
             	/* this is split links */
           
             	for(int k = 0;k<arrayOfLinks.length;k++){
             		String oneLink = arrayOfLinks[k].trim();
             		
             		if(hashMap2.containsKey("T:"+oneLink)){
             		/* check if link is in the our index - if we create index of one or two files -link may be not exist*/	
             		
                 	/* if link exists - add to list of our links*/	
                 		String searchFile = hashMap2.get("T:"+oneLink);
               		  String[] array = new String[3];
               		  array[0]=listOfTitles.get(z);
               		 array[1]=searchFile;
               		 array[2]=oneLink;
               		 	myList.add(array);
             			
             		}
             	
             	
             		
            		
             		 
             		
             		
             		
             		
             		
             	}
             	
             	
             			
 				} 
                 /* final parse - we work with our list from above + abstract parse*/
                 System.out.println("FINAL PARSE START ... ");
                 Disambiguation_FinalParse fp = new Disambiguation_FinalParse();
         		 fp.finalize_parse(myList);
                 
             }
         }
		
	}

}
