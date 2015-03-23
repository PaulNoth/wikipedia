package unit;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import dataManipulation.Lucene;

public class LuceneTest {

	static final String INDEX_PATH = "index";
	static final String DICTIONARY_PATH = "Dictionary.txt";

	@Test
	public void testWriteIndex() {
		try {
			Lucene lw = new Lucene(INDEX_PATH, DICTIONARY_PATH);
			lw.createIndex();
			Directory indexDirectory = FSDirectory.open(new File(INDEX_PATH));
			IndexReader indexReader = DirectoryReader.open(indexDirectory);
			int numDocs = indexReader.numDocs();
			//testovanie pre sample
			assertEquals(numDocs, 12);
			for (int i = 0; i < numDocs; i++) {
				Document document = indexReader.document(i);
				System.out.println("d=" + document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testQueryLucene() throws IOException, ParseException {
		Directory indexDirectory = FSDirectory.open(new File(INDEX_PATH));
		IndexReader indexReader = DirectoryReader.open(indexDirectory);
		final IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		Term t = new Term("en", "Japan");
		Query query = new TermQuery(t);
		TopDocs topDocs = indexSearcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);
		
		t = new Term("fr", "Luxembourg");
		query = new TermQuery(t);
		topDocs = indexSearcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);
		
		t = new Term("de", "Japan");
		query = new TermQuery(t);
		topDocs = indexSearcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);
		
		t = new Term("af", "Japan");
		query = new TermQuery(t);
		topDocs = indexSearcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);
		
		t = new Term("cs", "Japan");
		query = new TermQuery(t);
		topDocs = indexSearcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);
		
		t = new Term("sk", "Japan");
		query = new TermQuery(t);
		topDocs = indexSearcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);
	}
	
}
