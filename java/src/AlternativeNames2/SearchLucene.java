package control;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class SearchLucene 
{
	 String indexPath;
	 String jsonFilePath;
	 IndexWriter indexWriter = null;

	 
	public SearchLucene(String indexPath, String jsonFilePath) {
		 this.indexPath = indexPath;
		 this.jsonFilePath = jsonFilePath;
	}
	 
	public JSONArray parseJSONFile(){
		
		InputStream jsonFile;
		JSONArray arrayObjects = null;
		
		try {
			jsonFile = new FileInputStream(jsonFilePath);
			Reader readerJson = new InputStreamReader(jsonFile);
			Object fileObjects = JSONValue.parse(readerJson);
			
			arrayObjects=(JSONArray)fileObjects;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return arrayObjects;
	}
	 
	 public boolean openIndex(){
		 try {
			 Directory dir = FSDirectory.open(new File(indexPath));
			 Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
			 IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_48, analyzer);
			 //Always overwrite the directory
			 iwc.setOpenMode(OpenMode.CREATE);
			 indexWriter = new IndexWriter(dir, iwc);
			 return true;
		 } catch (Exception e) {
			 System.err.println("Error opening the index. " + e.getMessage());
		 }
		 return false;
	}
	
	 public void finish(){
		 try {
			 indexWriter.commit();
			 indexWriter.close();
		 } catch (IOException ex) {
			 System.err.println("We had a problem closing the index: " + ex.getMessage());
		 }
	 }
	
	public void addDocuments(JSONArray jsonObjects) {
		
		for(JSONObject object : (List<JSONObject>) jsonObjects){
			Document doc = new Document();
			for(String field : (Set<String>) object.keySet()){		
				if(field.compareTo("Alternative") != 0){
					doc.add(new StringField(field, (String) object.get(field),Field.Store.YES));//NO
				}else{
					JSONArray arr = (JSONArray) object.get(field);
					for (int i=0; i < arr.size(); i++) {
						doc.add(new StringField(field, (String) arr.get(i),Field.Store.YES));//NO
					}
				}
				//System.out.println(doc);
				//tu je problem s arrayfield
			}
			try {
				indexWriter.addDocument(doc);
			} catch (IOException ex) {
				System.err.println("Error adding documents to the index. "
						+ ex.getMessage());
			}
		}
	}
	 
	 
	 public void createIndex(){
		 JSONArray jsonObjects = parseJSONFile();
		 openIndex();
		 addDocuments(jsonObjects);
		 finish();
	 }
	 
	 public void Stats(){
		 
		 try {
			 Directory indexDirectory = FSDirectory.open(new File("indexDir"));
			 IndexReader indexReader = DirectoryReader.open(indexDirectory);
			 int numDocs = indexReader.numDocs();
			 int allALternatives = 0;
			 int currAlternatives = 0;
			 int altLength = 0;
			 int max = 0, min = 1000, maxA = 0, minA = 1000;
			 double avgLengthCurrAlt,altRatio = 0;
			 
			 NumberFormat formatter = new DecimalFormat("#0.0000"); 
			 
			 for ( int i = 0; i < numDocs; i++){
				 Document document = indexReader.document( i);
				 //System.out.println( "d=" +document);
				 
				 if (max < document.getFields("Alternative").length)
					 max = document.getFields("Alternative").length;
				 

				 if (min > document.getFields("Alternative").length)
					 min = document.getFields("Alternative").length;
				 
				 altLength = 0;
				 int j = 0;
				 for(IndexableField s :document.getFields("Alternative") ){	  
			    	 if(s.stringValue().compareTo("") != 0){//exists alternative name
			    		 currAlternatives += s.stringValue().length();
			    		 if (maxA < s.stringValue().length())
							 maxA = s.stringValue().length();
			    		 if (minA > s.stringValue().length())
							 minA = s.stringValue().length();
			    		 
			    		 System.out.println(s.stringValue());
			    		 ++allALternatives;
			    		 altLength +=  s.stringValue().length();
			    		 j++;
			    	 }
				 }
				 if(j != 0){
					 avgLengthCurrAlt = ((double)altLength/(double)j);
					 altRatio += (document.get("Name").length()/avgLengthCurrAlt);
				 }
				 int pes = 5;
				 
			 }
			 double avgAlt = ((double)allALternatives)/((double)numDocs);
			 double avgAltlength = ((double)currAlternatives)/((double)allALternatives);
			 double avgAltlengthRatio = ((double)altRatio)/((double)allALternatives);
			 
			 System.out.println("All documents: "+numDocs);
			 System.out.println("All alternatives: "+allALternatives);
			 System.out.println("Avg num of alternatives: "+formatter.format(avgAlt));
			 System.out.println("Max num of alternatives: "+max);
			 System.out.println("Min num of alternatives: "+min);
			 System.out.println("Avg length of alternatives: "+formatter.format(avgAltlength));
			 System.out.println("Max length of alternatives: "+maxA);
			 System.out.println("Min length of alternatives: "+minA);
			 System.out.println("Avg ratio of alternatives and names: "+formatter.format(avgAltlengthRatio));
		 
	 	} catch (Exception e) {
	 		e.printStackTrace();
		}
		 
	 }
	
	public void search(String type, String what, int hitsPerPage) {
		
		Directory indexDirectory;
		try {
			indexDirectory = FSDirectory.open(new File("indexDir"));
		
			IndexReader indexReader = DirectoryReader.open(indexDirectory);
			final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Term t = new Term(type,what);
			Query query = new TermQuery(t);

			//TopDocs topDocs = indexSearcher.search(query, 10);	
			 /*
			String[] phrase = what.split(" ");
			PhraseQuery query = new PhraseQuery();
			query.setSlop(0);
			for (String word : phrase) {
				query.add(new Term(type, word));
			}
			
						
			 TopDocs topDocs = indexSearcher.search(query, 10);
		        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
		            Document doc = indexSearcher.doc(scoreDoc.doc);
		            System.out.println(doc);
		        }
		        
			*/
			    
			IndexSearcher searcher = new IndexSearcher(indexReader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(query, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    //	Code to display the results of search
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ". " + d.get("Name") + "\t" );
		     
			  for(IndexableField s :d.getFields("Alternative") ){	  
			    	 System.out.println(s.stringValue());
			  }
		    }
			 
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
