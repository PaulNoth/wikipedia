package dataManipulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Lucene {
    static String indexPath;
    String textFilePath;
    static String intlinksPath = "C:\\Users\\Michal\\Vyhladavanie-informacii\\data_sample\\sample_input_interlanguage_links_";
    IndexWriter indexWriter = null;
    public static final String FILES_TO_INDEX_DIRECTORY = "filesToIndex";
	public static final String INDEX_DIRECTORY = "indexDirectory";

	public static final String FIELD_PATH = "path";
	public static final String FIELD_CONTENTS = "contents";
	
    public Lucene(String indexPath, String jsonFilePath) {
        this.indexPath = indexPath;
        this.textFilePath = jsonFilePath;
        
        System.setProperty("file.encoding","UTF-8");
		try {
		java.lang.reflect.Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void createIndex(){
        
        openIndex();
        addDocuments();
        finish();
    }
    
    public boolean openIndex(){
        try {
        	
        	System.setProperty("file.encoding","UTF-8");
    		try {
    		java.lang.reflect.Field charset = Charset.class.getDeclaredField("defaultCharset");
    		charset.setAccessible(true);
    		charset.set(null,null);
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
            Directory dir = FSDirectory.open(new File(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer); 
            //Always overwrite the directory
            iwc.setOpenMode(OpenMode.CREATE);
            indexWriter = new IndexWriter(dir, iwc);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error opening the index. " + e.getMessage());
        }
        return false;
    }
    
    public static int findIndex(String temp,int occurence,String word)
	{
		int index = 0;
		int ret = 0;
		for(int i =0;i<occurence;i++)
		{
		 index = temp.indexOf(word);
		 ret += index;
	     temp = temp.substring(index + 1);
		}
		return ret;
	}
    
    public void addDocuments(){
    	
		String line;
		String eng="", de=null,fr=null,other=null;
		BufferedReader reader;
		File f = new File(".\\Dictionary.txt",textFilePath);
		try {
				reader = new BufferedReader (new FileReader(textFilePath));
				while((line = reader.readLine())!=null)
				{
				String line2 = line;
				
				if(!eng.equals("")){
					while(line.contains(eng)){
						line=reader.readLine();
					}
				}
				int index1 = line.indexOf("\t");
				int index2 = findIndex(line,2,"\t");
				int index3 = findIndex(line,3,"\t");
				
				eng = line.substring(0, index1);
				de = line.substring(index1+1,index2+1);
				fr = line.substring(index2+2, index3+1);
				//ak obsahuje viac ; ako 3
				
				if(index3+10<line.length())
				other = line.substring(index3+2, line.length());
				else
				other = "_null";
				Document doc = new Document();
				//Field path = new StringField("path","filename.txt",Field.Store.YES);
				//doc.add(path);	
				
				doc.add(new TextField("english", eng, Store.YES ));
				doc.add(new TextField("german",de,Store.YES));
				doc.add(new TextField("french",fr,Store.YES));
				doc.add(new TextField("Other", other, Field.Store.YES));
				 if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE)
                 {
                         // New index, so we just add the document (no old document can be there):
                         System.out.println("adding " + doc.get("Other"));
                         System.out.println("adding " + doc.get("english"));
                         indexWriter.addDocument(doc);
                 }
                 else
                 {
                     // Existing index (an old copy of this document may have been indexed) so
                     // we use updateDocument instead to replace the old one matching the exact
                     // path, if present:
                     System.out.println("updating " + line2);
                     indexWriter.updateDocument(new Term("contents", line2), doc);
                 }
				
				}
				reader.close();
			}catch(Exception e){e.printStackTrace();};
			
			
        }
    public void finish(){
        try {
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException ex) {
            System.err.println("We had a problem closing the index: " + ex.getMessage());
        }
    }
    public void otherSearch(String line, String lang)
    {
    	if(line.equals("_null") || line.equals("null") || !line.contains(lang+":"));
    	
    	else
    	{
    		int lang_num = line.indexOf(lang+":");
    		line = line.substring(lang_num,line.length());
    		int tab = line.indexOf("\t");
    		
    		String word = line.substring(0+lang.length()+1,tab);
    		System.out.println(word);
    	}
    }
    
    public void search(String language,String s)
    {
    	boolean other = false;
    	s = s.replace("_", " ");
    	final int limitShow = 2;
    	String[] languages = {"english","german","french"};
    	TopDocs td = null;
    	ScoreDoc[] hits = null;
    	int max_hits = 0;
    	String lang = null;
    	Term term = null;
    	Query query = null;
    	int hit = 0;
    	String sn;
    	
    	switch (language){
    	
    	case "de": language = "german";
    				break;
    	case "en": language = "english";
    				break;
    	case "fr": language = "french";
    				break;
    	default : other = true;
    	}
    	
    	try{
    		s = s.toLowerCase();
    		File indexDire = new File("index");
            Directory fsDir = FSDirectory.open(indexDire);
            DirectoryReader reader = DirectoryReader.open(fsDir);
            IndexSearcher searcher = new IndexSearcher(reader);
            
            for(String l : languages)
            {
            	//using every language
            term = new Term(l, s);
            query = new TermQuery(term);
            System.out.println("Query: " + query.toString() + "\n");
            td = searcher.search(query, limitShow);
            
            //ziskanie jazyka v ktorom je zadane slovo
            if(td.totalHits > max_hits)
            {
            	max_hits = td.totalHits;
            	lang = l;
            }
            
            }
            
            
            term = new Term(lang, s);
            query = new TermQuery(term);
            System.out.println("Hladany vyraz: " + query.toString() + "\n");
            td = searcher.search(query, limitShow);
            hits = td.scoreDocs;
            // Take IDs and frequencies
            final int[] docIDs = new int[td.totalHits];
            int res;
            
            if(td.totalHits > 2) res = 2;
            else res = td.totalHits;
            
            for (int i = 0; i < res; i++) {
                final int docNum = hits[i].doc;
                final Document doc = searcher.doc(docNum);
                if(other) otherSearch(doc.get("Other"),language);
                else{
                	System.out.println("Vysledok: " + doc.get(language));
                	System.out.println("--------------------------------\n");
                	}
            }
            
    	}catch(Exception e){e.printStackTrace();};
    }
    }