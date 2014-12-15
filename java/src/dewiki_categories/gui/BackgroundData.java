package dewiki_categories.gui;

import java.awt.Dialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.SAXException;

import dewiki_categories.com.scireum.open.xml.NodeHandler;
import dewiki_categories.com.scireum.open.xml.StructuredNode;
import dewiki_categories.com.scireum.open.xml.XMLReader;

public class BackgroundData {
	
	private static final String SUFFIX_ARTICLE = "article";
	private static final String SUFFIX_SQLLINK = "link";
	
	private static final String pathToXmlCategoriesXML = ".\\data\\output-dewiki-categories.xml";
	private static final String pathToSqlCategoriesXML = ".\\data\\output-dewiki-SQLcategories.xml";
	private static final String pathToArticlesTXT = ".\\data\\output-dewiki-articles.txt";
	private static final String pathToCategoryLinksTXT = ".\\data\\output-dewiki-SQLlinks.txt";
	
	private IndexWriter indexw = null;
	
	private long[] stats = new long[2];
	private HashMap<String, Integer> xmlCategoriesHash = new HashMap<String, Integer>();
	private HashMap<String, Integer> sqlCategoriesHash = new HashMap<String, Integer>();
	
	//CONSTRUCTOR
	public BackgroundData(final Dialog d)
	{
		new Thread(new Runnable() {		
			@Override
			public void run() {
				
				// XML category reader
				final XMLReader reader1 = new XMLReader();
				reader1.addHandler("category", new NodeHandler() {			
					@Override
					public void process(StructuredNode node) {
						try {
							// query subtree
							String categName = node.queryString("name");
							String categID = node.queryString("count");
							
							xmlCategoriesHash.put(categName, Integer.parseInt(categID));
							
						} catch (XPathExpressionException e) {
							e.printStackTrace();
						}
					}
				});
				reader1.addHandler("stats", new NodeHandler() {			
					@Override
					public void process(StructuredNode node) {
						try {
							// query subtree
							String name = node.queryString("name");
							String count = node.queryString("count");
							
							if("articles number".equals(name))
								stats[0] = Integer.parseInt(count);
							else if("categories number".equals(name))
								stats[1] = Integer.parseInt(count);							
							
						} catch (XPathExpressionException e) {
							e.printStackTrace();
						} catch (NumberFormatException nfe) {
							nfe.printStackTrace();
						}
					}
				});
				
				//SQL categories reader
				final XMLReader reader3 = new XMLReader();
				reader3.addHandler("category", new NodeHandler() {			
					@Override
					public void process(StructuredNode node) {
						try {
							// query subtree
							String categName = node.queryString("name");
							String noOfPages = node.queryString("pages");
							
							sqlCategoriesHash.put(categName, Integer.parseInt(noOfPages));
							
						} catch (XPathExpressionException e) {
							e.printStackTrace();
						}
					}
				});
				
				//parse all XML input files
				Thread t1 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("Thread 1 parsing started");
							reader1.parse(new FileInputStream(pathToXmlCategoriesXML));
							System.out.println("Thread 1 parsing done");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				});
				
				Thread t2 = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							System.out.println("Thread 2 parsing started");
							reader3.parse(new FileInputStream(pathToSqlCategoriesXML));
							System.out.println("Thread 2 parsing done");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
				//indexing SQL category links file			
				String indexPath = ".\\index";
				    
				Date start = new Date();
				System.out.println("Start time: " + start.getTime());
				try {
					System.out.println("Indexing to directory '" + indexPath + "'...");
				
				    Directory dir = FSDirectory.open(new File(indexPath));
				    // :Post-Release-Update-Version.LUCENE_XY:
				    Analyzer analyzer = new StandardAnalyzer();
				    IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);
				    iwc.setRAMBufferSizeMB(50.0);
				     // Add new documents to an existing index:
				    iwc.setOpenMode(OpenMode.CREATE);

				    indexw = new IndexWriter(dir, iwc);
				    
				    // start INDEXing documents here
				    Thread t3 = new Thread(new IndexThread(SUFFIX_ARTICLE, pathToArticlesTXT, indexw));
				    
				    Thread t4 = new Thread(new IndexThread(SUFFIX_SQLLINK, pathToCategoryLinksTXT, indexw));		
				    
				    t4.start();
				    t3.start();
				    t2.start();
					t1.start();
					
					t1.join();
					t2.join();
					t3.join();
					t4.join();
				
					System.out.println("All files processing done.");
					Date end = new Date();
					System.out.println("End time: " + new Date().getTime());
					System.out.println("Total time: " + (end.getTime() - start.getTime()) / 1000 + "s");
					
					indexw.close();
					d.dispose();
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public List<String> categoriesComparison()
	{
		List<String> lines = new ArrayList<String>();
		
		lines.add("XML kategorie: " + xmlCategoriesHash.size());
		lines.add("SQL kategorie: " + sqlCategoriesHash.size());
		lines.add("XML subor obsahoval " + String.format("%.3f", ((double)xmlCategoriesHash.size() / (double)sqlCategoriesHash.size()) * 100) + "% kategórií.");
		
		return lines;
	}
	
	public List<String> averageCategories()
	{
		List<String> lines = new ArrayList<String>();
		
		lines.add("Pocet vsetkych clankov: " + stats[0]);
		lines.add("Pocet vsetkych pouzitych kategorii: " + stats[1]);
		lines.add("Priemerny pocet kategorii na clanok: " + String.format("%.3f", ((double)stats[1] / (double)stats[0])) );
		
		return lines;
	}
	
	public List<String> maxminCategoriesFromXML()
	{
		String str1 = "";
		int max = 0;
		String str2 = "";
		int min = Integer.MAX_VALUE;
		
		List<String> lines = new ArrayList<String>();
		
		Collection<Entry<String, Integer>> vals = xmlCategoriesHash.entrySet();
		for(Entry<String, Integer> entry : vals)
		{
			if(entry.getValue() < min)
			{
				min = entry.getValue();
				str2 = entry.getKey();
			}
			
			if(entry.getValue() > max)
			{
				max = entry.getValue();
				str1 = entry.getKey();
			}
		}
		
		lines.add("Podla vstupneho XML");
		lines.add("Kategoria s najvacsim zastupenim:");
		lines.add(str1 + ", pouzita: " + max + "x");
		lines.add("Kategoria s najmensim zastupenim:");
		lines.add(str2 + ", pouzita: " + min + "x");
		
		return lines;
	}
	
	public List<String> mostAndLeastUsedCategories()
	{
		String str1 = "";
		int max = 0;
		String str2 = "";
		int min = Integer.MAX_VALUE;
		
		List<String> lines = new ArrayList<String>();
		
		Collection<Entry<String, Integer>> vals = sqlCategoriesHash.entrySet();
		for(Entry<String, Integer> entry : vals)
		{
			if(entry.getValue() < min)
			{
				min = entry.getValue();
				str2 = entry.getKey();
			}
			
			if(entry.getValue() > max)
			{
				max = entry.getValue();
				str1 = entry.getKey();
			}
		}
		
		lines.add("Podla vstupneho SQL");
		lines.add("Kategoria s najvacsim zastupenim:");
		lines.add(str1 + ", pouzita: " + max + "x");
		lines.add("Kategoria s najmensim zastupenim:");
		lines.add(str2 + ", pouzita: " + min + "x");
		
		return lines;
	}
	
	public List<String> getArticleOrCategory(String str)
	{
		boolean nothingFound = true;
		List<String> lines = new ArrayList<String>();
		str = str.toLowerCase();
		
		try {
			
			List<String> articles = searchUsingLucene(str, SUFFIX_ARTICLE);
			//if we found given string in articles
			if(!articles.isEmpty())
			{
				nothingFound = false;
				
				for(String article : articles)
				{
					String name = article.split(" \\| ")[0];
					String id = article.split(" \\| ")[1];
					
					lines.add("Najdeny clanok: " + name);
					lines.add("Jeho kategorie:");
					
					//search the same ID in links
					List<String> links = searchUsingLucene(id, SUFFIX_SQLLINK);
					for(String link : links)
					{
						lines.add("\t" + link.split(" \\| ")[0]);
					}
				}
			}

			List<String> links = searchUsingLucene(str, SUFFIX_SQLLINK);
			//if we found given string in links
			if(!links.isEmpty())
			{
				nothingFound = false;
				
				for(String link : links)
				{
					String name = link.split(" \\| ")[0];
					String id = link.split(" \\| ")[1];
					
					//search articles for ID
					List<String> foundArticles = searchUsingLucene(id, SUFFIX_ARTICLE);
					//exactly one hit, if successful
					for(String article : foundArticles)
					{
						lines.add("Najdena kategoria: " + name + " --> v clanku: " + article.split(" \\| ")[0]);
					}
				}
			}
				
			if(nothingFound) // if we didn't find the string anywhere
			{
				lines.add("Ziadne vysledky, skus vyhladat ine slovo");
			}		
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	@SuppressWarnings("deprecation")
	private List<String> searchUsingLucene(String query, String suff) throws org.apache.lucene.queryparser.classic.ParseException, CorruptIndexException, IOException
	{
		List<String> list = new ArrayList<String>();
		
		File indexDir = new File(".\\index\\");
		
		Directory fsDir = FSDirectory.open(indexDir);
		IndexReader reader = IndexReader.open(fsDir);
		IndexSearcher searcher = new IndexSearcher(reader);
		String dField = "contents-" + suff;
		Analyzer stdAn = new StandardAnalyzer();
		QueryParser parser = new QueryParser(dField,stdAn);
		
		Query q = parser.parse(query);
		TopDocs hits = searcher.search(q, 10);
		ScoreDoc[] scoreDocs = hits.scoreDocs;
		
		for (int n = 0; n < scoreDocs.length; ++n) {
			ScoreDoc sd = scoreDocs[n];
			int docId = sd.doc;
			Document d = searcher.doc(docId);
			//String fileName = d.get("path");
			String cont = d.get("contents-" + suff);
			//System.out.println(cont + ", path: " + fileName);
			
			list.add(cont);
		}
		
		return list;

	}
	
	public void close() throws IOException
	{
		if (indexw != null)
			indexw.close();
	}
	
}
