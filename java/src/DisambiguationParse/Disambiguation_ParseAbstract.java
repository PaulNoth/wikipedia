package DisambiguationParse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.jhu.nlp.wikipedia.PageCallbackHandler;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;


public class Disambiguation_ParseAbstract {
	 private static StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

	  private static IndexWriter writer;
	  private ArrayList<File> queue = new ArrayList<File>();
	public void parse() throws IOException
	{
		
		//nutne sparsovat cely abstract ... 
		
	String userdir = System.getProperty("user.dir");
	File path = new File(userdir+"/abstract");
	String indexPath = "abstractParse";
    
	FileWriter kk = null;
	try {
		kk = new FileWriter(userdir+"/abstractParse/abstracts.txt", true);
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
	
	
	  Directory dir = FSDirectory.open(new File(indexPath));
      Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_43);
      IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43,analyzer);
      if (true) {
          iwc.setOpenMode(OpenMode.CREATE);
      } else {
          // update index
          iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
      }
      iwc.setRAMBufferSizeMB(256.0);
                                          // creating the index writer
      IndexWriter writer = null;
      try {
          writer = new IndexWriter(dir, iwc);
      } catch (IOException e) {
          if (!new File(indexPath).exists()) {
              new File(indexPath).mkdir();
              writer = new IndexWriter(dir, iwc);
          }
      }
	
	
	
	
	
	PrintWriter out = new PrintWriter(new BufferedWriter(kk));
	
	
    File [] files = path.listFiles();
    for (int i = 0; i < files.length; i++){
    	String spracovanysubor = files[i].toString();	
    	System.out.println("parsujem abstract:"+spracovanysubor);
    	//out.println(eElement.getElementsByTagName("title").item(0).getTextContent().substring(11,eElement.getElementsByTagName("title").item(0).getTextContent().length())+"<..>"+abs);
		
    	 BufferedReader reader2 = null;
			try {
				reader2 = new BufferedReader(new FileReader(spracovanysubor));
				final StringBuilder contents2 = new StringBuilder();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            int j = 0;
            try {
            	
            	String abstext ="";
            	String abstext2 ="";
            	
				while(reader2.ready()) {
				String riadok = reader2.readLine();	
				
					
					 Pattern p1 = Pattern.compile("<title>Wikipedia:(.*?)</title>",Pattern.DOTALL);
		    			
		    			Matcher matcher2 = p1.matcher(riadok);
		    	
		    			while(matcher2.find())
		    			{	
		    			abstext = abstext + ""+matcher2.group(1).trim();
		    			j++;
		    			}
				
					 Pattern p2= Pattern.compile("<abstract>(.*?)</abstract>",Pattern.DOTALL);
		    			
		    			Matcher matcher3 = p2.matcher(riadok);
		    	
		    			while(matcher3.find())
		    			{	
		    			abstext2 = abstext2 + "<..>"+matcher3.group(1);
		    			
		    			
		    			
		    			
		    			 Document doc = new Document();
	 	                    doc.add(new TextField("path", abstext, Field.Store.YES));
	 	                   doc.add(new TextField("content", abstext2, Field.Store.YES));
	 	          
	 	                        writer.addDocument(doc);
		    			
		    			
		    			

		    			abstext="";
		    			abstext2="";
		    			}
					//		    		
					
				
					
				
				}
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    
    	
    	
    	
    }
    writer.close();
		
	System.exit(0);	
	
	}
}
