package DisambiguationParse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Disambiguation_FinalParse {
	 static Map<String, ArrayList<String>> hashMap = new HashMap<String,  ArrayList<String>>(); 
	 static Map<String, String> hashMap_abstract = new HashMap<String,  String>();
	 static ArrayList<String> findme = new ArrayList<String>();
	 static ArrayList<String> findme2 = new ArrayList<String>();
	 String WHERE = "";
	
	/*
	 * In this class, we took final parse
	 * We make unique hashmap for the files:
	 * first.xml - link1,link2,link3
	 * second.xml - link4,link5,link6
	 * So we have links with the right files in hashMap .. 
	 * Next we iterate in hashmap ... 
	 * We get first.xml and iterate in links 
	 * With these links we have reade compare list for first.xml 
	 * Next we run wikipediaHandler_parse and compare with list 
	 * We get second.xml ... and the same steps .. 
	 * 
	 * 
	 */
	 
	 
	 
	/* this is method for hashmap with duplicates */ 
	 private static void addValues(String key, String value)  
	 {   
	  ArrayList<String> tempList = null;        
	  if(hashMap.containsKey(key)){    
	   tempList=hashMap.get(key);   
	   if(tempList == null)      
	     tempList = new ArrayList<String>();       
	   tempList.add(value);      
	  }  
	  else  
	  {       
	   tempList = new ArrayList();    
	   tempList.add(value);       
	   }       
	  hashMap.put(key,tempList);  
	 } 
	
	public void finalize_parse(List<String[]> myList)
	{
		
		
		
		String userdir = System.getProperty("user.dir"); 	
		
		
		/* handler for read wikipedia files*/
		PageCallbackHandler handler = new Disambiguation_WikipediaHandler_parse();
		System.out.println(myList.size()+" Final parse - unique key - duplicate");
		for(int i = 0;i<myList.size();i++){
			
			
			String[] help = myList.get(i);
			
			
			
			String parent = help[0];
			String nameFile = help[1];
			String searchName = help[2];
			/* we add names, parent to the file 
			 * Like en-wikipedia-1.xml  - item one, item two, item three
			 * en-wikipedia-2.xml - item four, item five, item six
			 */
			addValues(nameFile,searchName+"|"+parent);
			
			
		}
		
		
	
	
		
		
		System.out.println("Iterate in hashmap and search for pages ...");
		 Iterator<String> it = hashMap.keySet().iterator();   
		  ArrayList<String> tempList = null;  
		  
		       while(it.hasNext()){   
		    	   /* iterate in hashmap
		    	    * pick one file - en-wikipedia-1.xml 
		    	    * iterate in templist and create findme with pages which are in this file
		    	    * we call parsing final 
		    	    * and we read en-wikipedia-1.xml .. in findme are links which we must find in this file
		    	    * 
		    	    * this process repeat for every single file
		    	    */
		           String key = it.next().toString();  
		             tempList = hashMap.get(key);  
		             
		             String FINDFILE = key;
		             findme.clear();
		             findme2.clear();
		             
		            if(tempList != null){  
		            	
		                for(String value: tempList){ 
		                	
		                  String[] rozdel = value.split("\\|");
		                  String FINDER = rozdel[0];
		                  findme.add(FINDER);
		                   WHERE = rozdel[1];
		                   
		                  findme2.add(WHERE);
		                  
		                	}  
		               }  
		         
		            
		            
		            
		            String cesta = key;
		            System.out.println("PARSING FINAL ... PLEASE WAIT | cesta:"+cesta);
	                if(new File(cesta).exists()){
	                WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(cesta);
	                
                    
                    try {
                    	
                    	    
                            wxsp.setPageCallback(handler);
                            wxsp.parse();
                            
                    }catch(Exception e) {
                            e.printStackTrace();
                    }
		            
	                }
		            
		        }  
		
		       Date date = new Date();
               
               // display time and date using toString()
               System.out.println(date.toString());
		
		
		
	}
	
	
}
