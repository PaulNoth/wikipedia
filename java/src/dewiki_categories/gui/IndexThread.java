package dewiki_categories.gui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;

public class IndexThread implements Runnable {

	protected final String suff;
	protected final String pathToFile;
	protected final IndexWriter idw;
	
	public IndexThread(String suffix, String fileName, IndexWriter indexw)
	{
		suff = suffix;
		pathToFile = fileName;
		idw = indexw;
	}
	
	public void run() {	
		
		System.out.println("Thread '" + suff + "' parsing started");
		
		BufferedReader bfr;
		try {
			 bfr = new BufferedReader(new FileReader(pathToFile));
		} catch (FileNotFoundException fnfe) {
			return;
		}
		 
		try {
			String line = "";
						   
			// make a new, empty document
			Document doc = null;
			Field pathField = new StringField("path", pathToFile, Field.Store.YES);
			Field textField = new TextField("contents-" + suff, line, Store.YES );
						   
			while((line = bfr.readLine()) != null)
			{
				doc = new Document();
				// Add the path of the file as a field named "path".  Use a
				// field that is indexed (i.e. searchable), but don't tokenize 
				// the field into separate words and don't index term frequency
				// or positional information:
				doc.add(pathField);
				
				// Add the contents of the file to a field named "contents".
				textField.setStringValue(line);
				doc.add(textField);
				
				if (idw.getConfig().getOpenMode() == OpenMode.CREATE) 
				{
					// New index, so we just add the document (no old document can be there):
					//System.out.println("adding " + file);
					idw.addDocument(doc);
				} 
				else 
				{
					// Existing index (an old copy of this document may have been indexed) so 
					// we use updateDocument instead to replace the old one matching the exact 
					// path, if present:
					System.out.println("updating " + pathToFile);
					idw.updateDocument(new Term("contents", line), doc);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			try {
				bfr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Thread '" + suff + "' parsing done");
		}
	}

}
