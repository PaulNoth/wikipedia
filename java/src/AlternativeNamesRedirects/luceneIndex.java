package xonder_VINF;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class luceneIndex {
	
	// method to create lucene index in specified folder lucene
	public luceneIndex() throws IOException{
		int i =0;
		
		File directory = new File(".\\lucene");
		
		Directory index = null;
		try {
			deleteLuceneIndex(directory);
			index = FSDirectory.open(directory);
		} catch (Exception ex) {

			System.out.println("\n\n---------------------------------------"+ex.getMessage());
		}
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter w = new IndexWriter(index, config);
		BufferedReader in = null;
		try{
			
    		
    		in = new BufferedReader(new InputStreamReader(new FileInputStream("Preprocessing.csv"), "UTF-8"),100000);
			while (in.ready()) {			
				String[] splittedString = in.readLine().split(",",6);
i++;
			    addDoc(w, splittedString[0], splittedString[1], splittedString[4],splittedString[5]);

			}
			in.close();
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		w.close();
		System.out.println("Number of pages indexed by lucene: "+i);
		
			
		
		
		
	}
	
	// method to create line in lucene indexing files with all neccessary atributes
	private static void addDoc(IndexWriter w, String id, String title, String Count, String redirects) throws IOException {
		  Document doc = new Document();
		  doc.add(new StringField("id", id, Field.Store.YES));
		  doc.add(new TextField("title", title, Field.Store.YES));
		  doc.add(new StringField("count", Count, Field.Store.YES));
		  doc.add(new StringField("redirects", redirects, Field.Store.YES));
		  w.addDocument(doc);
		}
	
	//delete existing lucene index
	private static boolean deleteLuceneIndex(File lucene){
		if(lucene.exists()){
	        File[] files = lucene.listFiles();
	        if(null!=files){
	            for(int i=0; i<files.length; i++) {
	                if(files[i].isDirectory()) {
	                    deleteLuceneIndex(files[i]);
	                }
	                else {
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    return(lucene.delete());
	}
}
