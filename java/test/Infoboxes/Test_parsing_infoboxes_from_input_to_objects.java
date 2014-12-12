package Unit_tests;
import Parser.*;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
 
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
 
import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;
import java.util.Scanner;

/*
 * Pre opatovne spustenie testu je potrebne vytvorit nove xml
 * nakopirovat do neho 4 stranky zo suboru: enwiki-latest-pages-articles1.xml-p000000010p000010000.bz2
 * kazda zo 4 stranok obsahuje iny druh infoboxu. konkretne ide o :
 * Stranka : title = Allan Dwan -> na vyhladanie pouzit "{{Infobox person | name = Allan Dwan"
 * Stranka : title = The Cider House Rules -> na vyhladanie pouzit "| name          = The Cider House Rules" 
 * Stranka : title = Bishkek -> na vyhladanie pouzit "|official_name    = Bishkek"
 * Stranka : title = Cook Islands -> na vyhladanie pouzit "|conventional_long_name = Cook Islands"
 * Nasledne len test spustit... overime ci sa nezmenil format dat !
 */

public class Test_parsing_infoboxes_from_input_to_objects  {
	  
	  @Test
	  public void TestParsovaniaPerson() throws FileNotFoundException, ParseException, Throwable, SAXException {
		 	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		    HandlerPerson handler_person = new HandlerPerson();
		    SAXParser saxParser = saxParserFactory.newSAXParser();
		 try {
		        saxParser.parse(new File("C:\\Users\\Dokonaly\\Google Drive\\FIIT rok 5\\VI\\Infobox projekt\\-VI-Infoboxy\\data\\infoboxes_example_input_test.xml"), handler_person);
		        List<Infobox_person> InfoboxPersonList = handler_person.getInfoboxList();
		        
		        for(Infobox_person person : InfoboxPersonList){
		        	Assert.assertEquals(person.getName(), "Allan Dwan");
		        	Assert.assertEquals(person.getImage(), "AllanDwan.jpg");
		        	Assert.assertEquals(person.getImage_size(), null);
		        	
		        	Assert.assertEquals(person.getBirth_place(), "Toronto, Canada");
		        	Assert.assertEquals(person.getDeath_date(), "ZNAMY FORMAT");
		        	Assert.assertEquals(person.getDeath_place(), "Los Angeles, United States");
		        	
		        	Assert.assertEquals(person.getBirth_day(), "3");
		        	Assert.assertEquals(person.getBirth_month(), "4");
		        	Assert.assertEquals(person.getBirth_year(), "1885");
		        	Assert.assertEquals(person.getDeath_day(), "28");
		        	Assert.assertEquals(person.getDeath_month(), "12");
		        	Assert.assertEquals(person.getDeath_year(), "1981");
		        }
		 } catch (IOException e) {
		        e.printStackTrace();
		  }  
	  }
	 
	  @Test
	  public void TestParsovaniaCountry() throws FileNotFoundException, ParseException, Throwable, SAXException {
		 	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			HandlerCountry handler_country = new HandlerCountry();
		    SAXParser saxParser = saxParserFactory.newSAXParser();
		 try {
			 	saxParser.parse(new File("C:\\Users\\Dokonaly\\Google Drive\\FIIT rok 5\\VI\\Infobox projekt\\-VI-Infoboxy\\data\\infoboxes_example_input_test.xml"), handler_country);
		        List<Infobox_country> InfoboxList = handler_country.getInfoboxList();

		        for(Infobox_country country : InfoboxList){
		        	
		        	Assert.assertEquals(country.getTitle(), "Cook Islands");   	
		        	Assert.assertEquals(country.getCommon_name(), "the Cook Islands");   
		        	Assert.assertEquals(country.getImage_flag(), "Flag of the Cook Islands.svg");   
		        	Assert.assertEquals(country.getImage_coat(), "Coat of arms of cook islands.gif");   
		        	Assert.assertEquals(country.getCapital(), "Avarua");   
		        	
		        	String[] pole5 = country.getOfficial_religion();
		        	String[] pole6 = null;
		        	Assert.assertEquals(pole5, pole6);
		        	
		        		        	
		        	String[] pole = country.getOfficial_languages();
		        	String[] pole2 = {"Cook Islands Maori"};
		        	for (int i = 0; i<pole.length;i++ ){
		        		Assert.assertEquals(pole[i], pole2[i]);
		        	}
		        	
		        	String[] pole3 = country.getGovernment_type();
		        	String[] pole4 = {"Constitutional monarchy"};
		        	for (int i = 0; i<pole3.length;i++ ){
		        		Assert.assertEquals(pole3[i], pole4[i]);
		        	}
		        	  
		        	Assert.assertEquals(country.getArea_km2(), "240");   
		        	Assert.assertEquals(country.getArea_sq_mi(), "91");  
		        	Assert.assertEquals(country.getPopulation_estimate(), null);   
		        	Assert.assertEquals(country.getPopulation_estimate_rank(), "218th small2005small"); 
		        	Assert.assertEquals(country.getCurrency(), "New Zealand dollar");   
		        	Assert.assertEquals(country.getCurrency_code(), null); 
		        }
		 } catch (IOException e) {
		        e.printStackTrace();
		  }  
	  }
	 
	  @Test
	  public void TestParsovaniaBook() throws FileNotFoundException, ParseException, Throwable, SAXException {
		 	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			HandlerBook handler_book = new HandlerBook();
		    SAXParser saxParser = saxParserFactory.newSAXParser();
		 try {
			 	saxParser.parse(new File("C:\\Users\\Dokonaly\\Google Drive\\FIIT rok 5\\VI\\Infobox projekt\\-VI-Infoboxy\\data\\infoboxes_example_input_test.xml"), handler_book);
		        List<Infobox_book> InfoboxbookList = handler_book.getInfoboxList();
 
		        for(Infobox_book book : InfoboxbookList){
		        	
		        	Assert.assertEquals(book.getName(), "The Cider House Rules");
		        	Assert.assertEquals(book.getTranslator(), null);
		        	Assert.assertEquals(book.getImage(), "Image:CiderHouseRules.jpg");
		        	Assert.assertEquals(book.getCaption(), "First edition cover");
		        	Assert.assertEquals(book.getAuthor(), "John Irving");
		        	Assert.assertEquals(book.getCountry(), "United States");
		        	Assert.assertEquals(book.getLanguage(), "English language|English");
		        	Assert.assertEquals(book.getSubject(), null);
		        	
		        	String[] pole = book.getGenre();
		        	String[] pole2 = {""};
		        	for (int i = 0; i<pole.length;i++ ){
		        		Assert.assertEquals(pole[i], pole2[i]);
		        	}

		        	Assert.assertEquals(book.getPublished(), null);
		        	
		        	Assert.assertEquals(book.getPages(), null);
		        	Assert.assertEquals(book.getIsbn(), "ISBN 0-688-03036-X");
		        	Assert.assertEquals(book.getPreceded_by(), "The Hotel New Hampshire");
		        	Assert.assertEquals(book.getFollowed_by(), "A Prayer for Owen Meany");
		        }
		 } catch (IOException e) {
		        e.printStackTrace();
		  }
		 
		  
	  }
	  
	  @Test
	  public void TestParsovaniaSettlement() throws FileNotFoundException, ParseException, Throwable, SAXException {
		 	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			HandlerSettlement handler_settlement = new HandlerSettlement();
		    SAXParser saxParser = saxParserFactory.newSAXParser();
		 try {
			 	saxParser.parse(new File("C:\\Users\\Dokonaly\\Google Drive\\FIIT rok 5\\VI\\Infobox projekt\\-VI-Infoboxy\\data\\infoboxes_example_input_test.xml"), handler_settlement);
		        List<Infobox_settlement> InfoboxSettlementList = handler_settlement.getInfoboxList();

		        for(Infobox_settlement sett : InfoboxSettlementList){
		        	Assert.assertEquals(sett.getOfficial_name(), "Bishkek");
		        	Assert.assertEquals(sett.getNickname(), null);
		        	Assert.assertEquals(sett.getMap_caption(), null);
		        	Assert.assertEquals(sett.getCoordinates_region(), "KG");
		        	Assert.assertEquals(sett.getLeader_title(), "Mayor");
		        	Assert.assertEquals(sett.getUnit_pref(), null);
		        	Assert.assertEquals(sett.getArea_total_km2(), "127");
		        	Assert.assertEquals(sett.getArea_land_km2(), null);
		        	Assert.assertEquals(sett.getPopulation_total(), "874400");
		        	Assert.assertEquals(sett.getPopulation_density_km2(), "auto");
		        	Assert.assertEquals(sett.getTimezone(), "UTC+6");
		        	Assert.assertEquals(sett.getWebsite(), "http://meria.kg/index.php?lang=kg");
		        	String[] pole = sett.getPostal_code();
		        	String[] pole2 = {"720000","720085"};
		        	for (int i = 0; i<pole.length;i++ ){
		        		Assert.assertEquals(pole[i], pole2[i]);
		        	}
		        	
		        }
		 } catch (IOException e) {
		        e.printStackTrace();
		  }
	  }
	
}
