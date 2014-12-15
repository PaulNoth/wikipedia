package wikical.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

import wikical.dataclasses.SearchData;
import wikical.settings.Settings;

public class SearchParsed {

	private String txtDate;
	private String txtName;
	private String txtEvent;
	private File selectedFolder;
	private File selectedFilePeople;
	private File selectedFileEvents;

	public SearchParsed(String txtDate, String txtName, String txtEvent, File selectedFolder) {
		this.txtDate = txtDate;
		this.txtName = txtName;
		this.txtEvent = txtEvent;
		this.selectedFolder = selectedFolder;
		selectedFilePeople = new File(selectedFolder + Settings.nameOfPeopleFile);
		selectedFileEvents = new File(selectedFolder + Settings.nameOfEventsFile);
	}

	/**
	 * Executing of searching itself. When data is found they will be returned in List of SearchData
	 */
	public List<SearchData> searchParsedIndexed() throws Exception {

		if (selectedFilePeople.exists() && selectedFileEvents.exists()) {
			if (!txtDate.equals("")) {
				String parsedDate = modifyDateToLucene(txtDate);
				List<SearchData> foundData = new ArrayList<SearchData>();
				foundData.addAll(getQueryData(parsedDate, Settings.nameOfLuceneIndexPeople, "birthStartDate"));	//people, birth
				foundData.addAll(getQueryData(parsedDate, Settings.nameOfLuceneIndexPeople, "deathEndDate"));	//people, birth
				foundData.addAll(getQueryData(parsedDate, Settings.nameOfLuceneIndexEvents, "birthStartDate"));	//people, birth
				foundData.addAll(getQueryData(parsedDate, Settings.nameOfLuceneIndexEvents, "deathEndDate"));	//people, birth
				
				return foundData;															// searching by both dates (start and end) inside both files
				
			} else if (!txtName.equals("")) {
				return getQueryData(txtName, Settings.nameOfLuceneIndexPeople, "name");		// search by name in people folder
			} else if (!txtEvent.equals("")) {
				return getQueryData(txtEvent, Settings.nameOfLuceneIndexEvents, "name");	// serch by name in events folder
			}
		} else {
			System.out.println("Path " + selectedFolder.getAbsolutePath() + " doesn't contain required files.");
		}

		return null;
		
	}

	/**
	 * Will find query data from selected Index
	 */
	private List<SearchData> getQueryData(String queryString, String nameOfLuceneIndex, String queryParameter) throws Exception{

		// 0. Specify the analyzer for tokenizing text. The same analyzer should be used for indexing and searching
				StandardAnalyzer analyzer = new StandardAnalyzer();
				File luceneIndexDirectory = new File(selectedFolder + nameOfLuceneIndex);
				if (!luceneIndexDirectory.exists()) {
					System.out.println("File does not exist");
					analyzer.close();
					return null;
				}
				Directory index = FSDirectory.open(luceneIndexDirectory);

				// the "name" arg specifies the default field to use when no field is explicitly specified in the query.
				
				Query q;
				
				try {
					q = new QueryParser(queryParameter, analyzer).parse(queryString);
				} catch(Exception e) {
					System.out.println("Problem with date parsing. Please check date field.");
					return new ArrayList<SearchData>();
				}

				// 3. search
				int hitsPerPage = 200;
				IndexReader reader = DirectoryReader.open(index);
				IndexSearcher searcher = new IndexSearcher(reader);
				TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
				searcher.search(q, collector);

				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				List<SearchData> foundData = new ArrayList<SearchData>();

				// 4. display results
				//System.out.println("Found " + hits.length + " hits.");
				for (int i = 0; i < hits.length; ++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					//System.out.println((i + 1) + ". " + d.get("name") + "\t" + d.get("birthStartDate") + "\t" + d.get("deathEndDate"));
					foundData.add(new SearchData(d.get("name"), d.get("birthStartDate"), d.get("deathEndDate")));
				}
				reader.close();
		
		return foundData;
	}

	/**
	 * There are 3 types of dates - every one of them is converted here to String which Lucene query can find
	 */
	private String modifyDateToLucene(String inputDate) {
		
		if(inputDate.matches("[0-9]+\\.[0-9]+\\.[0-9]+")) {			//day, month, year - all good
			return inputDate;
		} else if(inputDate.matches("[0-9]+\\.[0-9]+\\.")) {		//day, month - year must be ignored
			return inputDate + "*";
		} else if(inputDate.matches("[0-9]+")) {
			return "..*" + inputDate;
		}
		else {
			System.out.println("Problem with input of date.");
			return "";
		}
	}
}
