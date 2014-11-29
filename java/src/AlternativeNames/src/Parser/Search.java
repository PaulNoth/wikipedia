package Parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Celá funkcionalita ktorá prebieha pri hladaný výrazu.
 * @author Samuel Benkovic
 *
 */
public class Search implements ActionListener {
	JTextField searchExpression;
	JComboBox<String> tagCombo;
	static JTextArea parseredData;
	static String folderIndex;
	
	/**
	 * Klasicky seter volaný z triedy GUI 
	 * @param searchExpression - Výraz ktorý sa hladá 
	 * @param parseredData - Co vypluje appka po skoncený searchu
	 * @param tagCombo - v ktorom tagu hladame vyraz ? 
	 */
	public void set(JTextField searchExpression, JTextArea parseredData, JComboBox<String> tagCombo) {
		this.searchExpression = searchExpression;
		this.tagCombo = tagCombo;
		this.parseredData = parseredData;
	}

	/**
	 * Akcia ktorá sa vykoná po stisknuti tlacidla Search
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		String workingDir = System.getProperty("user.dir");
		folderIndex = workingDir + "/index";
		CleanDir(folderIndex);
		File folderParsered = new File(workingDir + "/parsered");
		
		File[] listOfFiles = folderParsered.listFiles();
		try {
			createIndex(listOfFiles);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			searchIndex(searchExpression.getText(),tagCombo.getSelectedItem().toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metoda ktora vytvory indexy pre vsetky subory 
	 * @param listOfFiles - subory ktore indexujeme
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
	public static void createIndex(File[] listOfFiles) throws CorruptIndexException, LockObtainFailedException,IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		
		FSDirectory directory = FSDirectory.open(new File(folderIndex));
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
				analyzer);
		IndexWriter w = new IndexWriter(directory, config);
		
		for (File xmlFile : listOfFiles) {
			try {
				/// Prehladavanie vsetkych XML
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				org.w3c.dom.Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("article");
				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);

					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						addArticle(w,eElement.getElementsByTagName("title"),eElement.getElementsByTagName("aka"));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		w.close();
	}

	private static void CleanDir(String path) {
		File CleanDir = new File(path);
		File[] CleanDirFiles = CleanDir.listFiles();
		for (File file : CleanDirFiles) {
			file.delete();
		}	
	}
	/**
	 * Metoda ktora hlada vyraz v jednom konkretnom tagu z indexov ktoré vytvorila metoda createIndex 
	 * @see createIndex
	 * @param searchString - Vyraz ktory hladame
	 * @param tag - pod ktorym tagom budeme tento vyraz bude hladat
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void searchIndex(String searchString, String tag) throws IOException, ParseException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		int hitsPerPage = 10;
		FSDirectory FSDIndex = FSDirectory.open(new File(folderIndex));
		IndexReader reader = IndexReader.open(FSDIndex);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		QueryParser parser=new QueryParser(tag,analyzer);
		Query query=parser.parse(searchString);
		searcher.search(query, collector);
		
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		parseredData.setText("Found " + hits.length + " hits.\n");
		for(int i=0;i<hits.length;++i) {
		    int docId = hits[i].doc;
		    Document d = searcher.doc(docId);
		    	parseredData.append((i + 1) + ".\ntitle:"+ d.get("title")+"\n");
		    if(d.get("aka")!=null)
		    	parseredData.append("aka: " + d.get("aka")+"\n");
		    if(d.get("akaText")!=null)
			    parseredData.append("akaText: \n" + d.get("akaText"));
		 
		}
		reader.close();
	}
	 
/**
 * Metoda vytvarajuca jednotlive indexy
 * @param w - IndexWriter ktorý zapisuje na lokalitu /indexy
 * @param akaList - List vsetkých AKA ktoré clánok má 
 * @param akaTextList - List vsetkých Aka ktoré som nasiel v clánku
 * @throws IOException 
 */
	private static void addArticle(IndexWriter w, NodeList titleList, NodeList akaList) throws IOException {
		 Document doc = new Document();
		 
		String akaText = "";
		doc.add(new TextField("title",titleList.item(0).getTextContent().trim(), Field.Store.YES));
		 if(akaList!= null){
			int j =1;
			
				 for (int i=0;i<akaList.getLength();i++)
				 {
					 String parent = akaList.item(i).getParentNode().getNodeName();
					 if(parent == "article")
					 {
						 doc.add(new TextField("aka",akaList.item(i).getTextContent().trim(), Field.Store.YES));
					 }
					 else
					 {
						 doc.add(new TextField("akaTextAka",akaList.item(i).getTextContent().trim() + " ", Field.Store.YES));
						 doc.add(new TextField("akaTextTitle",titleList.item(j).getTextContent().trim() + " ", Field.Store.YES));
						 akaText += "Title : " + titleList.item(j).getTextContent().trim() + "\t";
						 akaText += "Aka : "+ akaList.item(i).getTextContent().trim() + "\n";
						 j++;
						 
					 }
				 }	
		 }
			 doc.add(new StringField("akaText",akaText, Field.Store.YES));	
		 
		 
		 w.addDocument(doc);
	}
}
