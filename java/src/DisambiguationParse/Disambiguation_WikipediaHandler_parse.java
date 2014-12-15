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

import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;




public class Disambiguation_WikipediaHandler_parse extends Disambiguation_FinalParse implements PageCallbackHandler {
	
        public void process(WikiPage page) {
        /*
         * In this method we process links we find .. 
         * We parse wikifile and compare with list of links (titles)
         * 	
         */
       
        String userdir = System.getProperty("user.dir"); 	
    	
        if(findme.contains(page.getTitle().trim())){
        /* if list contains title*/
        	
        	/* store important info*/
        	int index =findme.indexOf(page.getTitle().trim());
        	String rodic = findme2.get(index);
            String wikitext = page.getWikiText();
        	
            
            /* search for anchor - regex 
             * we use two regex, because anchors are stored differently
             * {{anchor|example
             * <span id="example"
             * 
             */
        	Pattern p = Pattern.compile("\\{anchor(.*?)\\}",Pattern.DOTALL);
    		Matcher matcher = p.matcher(wikitext);
    		String anchor = "";
    		while(matcher.find())
    		{
    		 anchor = anchor+matcher.group(1);
    		}
    		
    		
    		String parse_title = page.getTitle().trim();
    		
    		/* search for anchor2 - regex - but serach for <span id=*/
    		   Pattern p1 = Pattern.compile("&lt;span id=&quot;(.*?)&quot;&gt;",Pattern.DOTALL);
    		   Matcher matcher2 = p1.matcher(wikitext);
    	       while(matcher2.find())
    			{
    			 anchor = anchor+"|"+matcher2.group(1);
    			}
    			
    	String desc = "";
    	 
    	
    
    	
    	  /* we create description from abstract Index, we use lucene*/
    	  String queryStr = parse_title; 
    	  String field = "path";
	      IndexReader reader = null;
	      String indexPath = "abstractParse";
		 
		try {
			reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}     
		 
		IndexSearcher searcher = new IndexSearcher(reader);
	    StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);  
		QueryParser parser = new QueryParser(Version.LUCENE_43, field,analyzer);
    	  
    	  
    	  int maxHits = 1;
    	  Query query;
		try {
			query = parser.parse(queryStr);
			  TopDocs topDocs = searcher.search(query, maxHits);
	          ScoreDoc[] hits = topDocs.scoreDocs;
	       if(hits.length>0){
	              int docId = hits[0].doc;
	              Document d = searcher.doc(docId);
	        desc = d.get("content").toString();
	      }
	        
		} catch (ParseException | IOException e1) {
			
		}
        /* end of lucene use*/
          
    	
    		
		/* store the data to the file */
        	try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(userdir+"/sparsovane.txt", true)))) {
    		   out.println("<dspage>");
        	   out.println("  <title>"+rodic.trim()+"</title>");
    		   out.println("  <link>"+""+parse_title+"</link>");
    		   
    		   out.println("  <anchors>"+anchor+"</anchors>");
    		   out.println("  <desc>"+desc+"</desc>");
    		   out.println("</dspage>");
    		   out.println(" ");
    		}catch (IOException e) {
    		    //exception handling left as an exercise for the reader
    		}
        	
        	
        	
        }
        	
                    
        	}
        
        
   	
       
        
        
        }

