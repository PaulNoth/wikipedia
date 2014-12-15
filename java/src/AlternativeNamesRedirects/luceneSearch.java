package xonder_VINF;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class luceneSearch {
	private ScoreDoc[] hits;
	private IndexSearcher searcher;
	// returning all results for this query
	public ScoreDoc[] getScore(){
		return hits;
	}
	// return indexsearcher for easy access
	public IndexSearcher getSearcher(){
		return searcher;
	}
	// method initialising lucene search on inserted key
	public luceneSearch(String key){
		StandardAnalyzer analyzer = new StandardAnalyzer();
		File directory = new File(".\\lucene");
		
		Directory index = null;
		try {

			index = FSDirectory.open(directory);
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
		
		Query q ;
		try {
			if(key != ""){
			q = new QueryParser("title", analyzer).parse(key);
			int hitsPerPage = 10;
			IndexReader reader = DirectoryReader.open(index);
			searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			hits = collector.topDocs().scoreDocs;
			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
