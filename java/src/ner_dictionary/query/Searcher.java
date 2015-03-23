package ner_dictionary.query;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import ner_dictionary.indexing.Indexer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {
	private IndexSearcher indexSearcher;
	private QueryParser queryParser;
	private Map<Integer, String> categoryMapping;
	private static final int MAX_HITS = 25;

	public Searcher(String indexDirectory, Map<Integer, String> categoryMapping) {
		this.categoryMapping = categoryMapping;
		File f = new File(indexDirectory);
		try {
			Directory dir = FSDirectory.open(f);
			IndexReader indexReader = DirectoryReader.open(dir);
			indexSearcher = new IndexSearcher(indexReader);
		} catch (IOException e) {
			System.err.println("An error occured during inicialization of searcher, check if the path to the index directory is set correctly!");
			e.printStackTrace();
			System.exit(1);
		}
		// The same analyzer is used as for indexing
		queryParser = new QueryParser(Indexer.FIELD_TITLE, new StandardAnalyzer());
	}

	public Map<String, String> search(String queryString) {
		try {
			Query query = queryParser.parse(queryString);
			// Take only MAX_HITS of best results into account
			TopScoreDocCollector docCollector = TopScoreDocCollector.create(MAX_HITS, true);
			indexSearcher.search(query, docCollector);
			ScoreDoc[] hits = docCollector.topDocs().scoreDocs;
			Map<String, String> results = new LinkedHashMap<String, String>();
			for (int i = 0; i < hits.length; i++) {
				Document doc = indexSearcher.doc(hits[i].doc);
				results.put(doc.get(Indexer.FIELD_TITLE), categoryMapping.get(Integer.valueOf(doc.get(Indexer.FIELD_CATEGORY_ID))));
			}
			return results;
		} catch (ParseException e) {
			System.err.println("An error occured during parsing the query: " + queryString + " !");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("An error occured during searching the query: " + queryString + " !");
			e.printStackTrace();
		}
		return null;
	}
}
