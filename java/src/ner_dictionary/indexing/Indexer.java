package ner_dictionary.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	
	private IndexWriter indexWriter;
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_CATEGORY_ID = "category";
	
	public Indexer(String indexDirectory) {
		Directory dir;
		try {
			File f = new File(indexDirectory);
			if (!f.exists()) {
				if (!f.mkdir()) {
					System.err.println("Directory: " + indexDirectory + " for index could not be created!");
					System.exit(1);
				}
			}
			dir = FSDirectory.open(f);
			indexWriter = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer()));
		} catch (IOException e) {
			System.err.println("An error occured during inicialization of indexer!");
			e.printStackTrace();
		}
	}

	public void indexPage(String pageName, Integer categoryId) {
		Document doc = new Document();
		// Create two fields: one for wikipedia page title and one for its category id, store content of both, index only the title
		doc.add(new TextField(FIELD_TITLE, pageName, Store.YES));
		doc.add(new StoredField(FIELD_CATEGORY_ID, categoryId));
		try {
			indexWriter.addDocument(doc);
		} catch (IOException e) {
			System.err.println("An error occured during indexing!");
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.indexWriter.close();
		} catch (IOException e) {
			System.err.println("An error occured during indexing!");
			e.printStackTrace();
		}
	}
	
	public IndexWriter getIndexWriter() {
		return this.indexWriter;
	}
}
