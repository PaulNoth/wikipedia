package ApacheLucene;
import Enums.Book_enum;
import Enums.Country_enum;
import Enums.Person_enum;
import Enums.Settlement_enum;
import Parser.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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

public class index {
	
	//Funkcia na zaindexovanie knihy 
	private static void addBook(IndexWriter w, String name, String translator, String image,
			String captation, String author, String country, String language,
			String subject, String[] genre, String published, 
		    String pages,String isbn,  String get_followed_by, String get_preceded_by) throws IOException {
		  Document doc = new Document();
		  if (name != null) {
			  doc.add(new TextField("name", name, Field.Store.YES));
		  }
		  if (translator != null) {
			  doc.add(new TextField("translator", translator, Field.Store.YES));
		  }
		  if (image != null) {
			  doc.add(new TextField("image", image, Field.Store.YES));
		  }
		  if (captation != null) {
			  doc.add(new TextField("captation", captation, Field.Store.YES));
		  }
		  if (author != null) {
			  doc.add(new TextField("author", author, Field.Store.YES));
		  }
		  if (country != null) {
			  doc.add(new TextField("country", country, Field.Store.YES));
		  }
		  
		  if (language != null) {
			  doc.add(new TextField("language", language, Field.Store.YES));
		  }
		  if (subject != null) {
			  doc.add(new TextField("subject", subject, Field.Store.YES));
		  }
		
		  if (genre != null) {
			 
		  	  for (int i = 0;i<genre.length;i++){
		  		doc.add(new TextField("genre",genre[i],Field.Store.YES));
			  }
			 
		  }
		  if (published != null) {
			  doc.add(new TextField("published", published, Field.Store.YES));
		  }
		 
		  if (isbn != null) {
			  doc.add(new TextField("isbn", isbn, Field.Store.YES));
		  }
		  
		  if (pages != null) {
			  doc.add(new TextField("pages", pages, Field.Store.YES));
		  }
		  if (get_followed_by != null) {
			  doc.add(new TextField("get_followed_by", get_followed_by, Field.Store.YES));
		  }
		  if (get_preceded_by != null) {
			  doc.add(new TextField("get_preceded_by", get_preceded_by, Field.Store.YES));
		  }
		  w.addDocument(doc);
		}
	
	//Funkcia na zaindexovanie osoby 
	private static void addPerson(IndexWriter w, String name, String image, String image_size,
			String birth_date, String birth_place, String death_date, String death_place,
			String birth_day, String birth_month, String birth_year, String death_year, String death_month, String death_day ) throws IOException {
		  Document doc = new Document();
		  
		  if (name != null) {
			  doc.add(new TextField("name", name, Field.Store.YES));
		  }
		  if (image != null) {
			  doc.add(new TextField("image", image, Field.Store.YES));
		  }
		  if (image_size != null) {
			  doc.add(new TextField("image_size", image_size, Field.Store.YES));
		  }
		  if (birth_date != null) {
			  doc.add(new TextField("birth_date", birth_date, Field.Store.YES));
		  }
		  if (birth_day != null) {
			  doc.add(new TextField("birth_day", birth_day, Field.Store.YES));
		  }
		  if (birth_month != null) {
			  doc.add(new TextField("birth_month", birth_month, Field.Store.YES));
		  }
		  if (birth_year != null) {
			  doc.add(new TextField("birth_year", birth_year, Field.Store.YES));
		  }
		  if (birth_place != null) {
			  doc.add(new TextField("birth_place", birth_place, Field.Store.YES));
		  }
		  
		  if (death_date != null) {
			  doc.add(new TextField("death_date", death_date, Field.Store.YES));
		  }
		  if (death_day != null) {
			  doc.add(new TextField("death_day", death_day, Field.Store.YES));
		  }
		  if (death_month != null) {
			  doc.add(new TextField("death_month", death_month, Field.Store.YES));
		  }
		  if (death_year != null) {
			  doc.add(new TextField("death_year", death_year, Field.Store.YES));
		  }
		  
		  if (death_place != null) {
			  doc.add(new TextField("death_place", death_place, Field.Store.YES));
		  }
		 
		  w.addDocument(doc);
		}

	//Funkcia na zaindexovanie krajiny 
	private static void addCountry(IndexWriter w, String title, String common_name, String image_flag,
			String image_coat, String capital, String[] official_religion, String[] official_languages,
			String[] government_type, String area_km2, String area_sq_mi, String population_estimate,
		    String population_estimate_rank,String currency, String currency_code) throws IOException {
		  Document doc = new Document();
		  
		  	  
		  
		  if (title != null) {
			  doc.add(new TextField("title", title, Field.Store.YES));
		  }
		  if (common_name != null) {
			  doc.add(new TextField("common_name", common_name, Field.Store.YES));
		  }
		  if (image_flag != null) {
			  doc.add(new TextField("image_flag", image_flag, Field.Store.YES));
		  }
		  if (image_coat != null) {
			  doc.add(new TextField("image_coat", image_coat, Field.Store.YES));
		  }
		  if (capital != null) {
			  doc.add(new TextField("capital", capital, Field.Store.YES));
		  }
		  if (official_religion != null) {
				 
		  	  for (int i = 0;i<official_religion.length;i++){
		  		doc.add(new TextField("official_religion",official_religion[i],Field.Store.YES));
			  }
			 
		  }
		  
		  if (official_languages != null) {
				 
		  	  for (int i = 0;i<official_languages.length;i++){
		  		doc.add(new TextField("official_languages",official_languages[i],Field.Store.YES));
			  }
			 
		  }
		  
		  if (government_type != null) {
				 
		  	  for (int i = 0;i<government_type.length;i++){
		  		doc.add(new TextField("government_type",government_type[i],Field.Store.YES));
			  }
			 
		  }
		  if (area_km2 != null) {
			  doc.add(new TextField("area_km2", area_km2, Field.Store.YES));
		  }
		  if (area_sq_mi != null) {
			  doc.add(new TextField("area_sq_mi", area_sq_mi, Field.Store.YES));
		  }
		  if (population_estimate != null) {
			  doc.add(new TextField("population_estimate", population_estimate, Field.Store.YES));
		  }
		  if (population_estimate_rank != null) {
			  doc.add(new TextField("population_estimate_rank", population_estimate_rank, Field.Store.YES));
		  }
		  if (currency != null) {
			  doc.add(new TextField("currency", currency, Field.Store.YES));
		  }
		  if (currency_code != null) {
			  doc.add(new TextField("currency_code", currency_code, Field.Store.YES));
		  }
		  w.addDocument(doc);
		}
	
	//Funkcia na zaindexovanie mesta 
	private static void addSettlement(IndexWriter w, String official_name, String nickname, String map_caption,
			String coordinates_region, String leader_title, String unit_pref, String area_total_km2,
			String area_land_km2, String population_total, String population_density_km2, String timezone,
		    String website,String[] postal_code) throws IOException {
		  Document doc = new Document();

		  if (official_name != null) {
			  doc.add(new TextField("official_name", official_name, Field.Store.YES));
		  }
		  if (nickname != null) {
			  doc.add(new TextField("nickname", nickname, Field.Store.YES));
		  }
		  if (map_caption != null) {
			  doc.add(new TextField("map_caption", map_caption, Field.Store.YES));
		  }
		  if (coordinates_region != null) {
			  doc.add(new TextField("coordinates_region", coordinates_region, Field.Store.YES));
		  }
		  if (leader_title != null) {
			  doc.add(new TextField("leader_title", leader_title, Field.Store.YES));
		  }
		  if (unit_pref != null) {
			  doc.add(new TextField("unit_pref", unit_pref, Field.Store.YES));
		  }
		  
		  if (area_total_km2 != null) {
			  doc.add(new TextField("area_total_km2", area_total_km2, Field.Store.YES));
		  }
		  if (area_land_km2 != null) {
			  doc.add(new TextField("area_land_km2", area_land_km2, Field.Store.YES));
		  }
		  if (population_total != null) {
			  doc.add(new TextField("population_total", population_total, Field.Store.YES));
		  }
		  if (population_density_km2 != null) {
			  doc.add(new TextField("population_density_km2", population_density_km2, Field.Store.YES));
		  }
		  if (timezone != null) {
			  doc.add(new TextField("timezone", timezone, Field.Store.YES));
		  }/*
		  if (postal_code != null) {
			  doc.add(new TextField("postal_code", postal_code, Field.Store.YES));
		  }*/
		  if (postal_code != null) {
				 
		  	  for (int i = 0;i<postal_code.length;i++){
		  		doc.add(new TextField("postal_code",postal_code[i],Field.Store.YES));
			  }
			 
		  }
		  
		  if (website != null) {
			  doc.add(new TextField("website", website, Field.Store.YES));
		  }
		 
		  w.addDocument(doc);
		}
	
	
	public static void main(String[] args) throws FileNotFoundException, ParseException, ClassNotFoundException {
		//jednotlive listy do ktorych si nacitam serializovane - oparsovane informacie
		List<Infobox_country> InfoboxList;
        List<Infobox_settlement> InfoboxSettlementList;
        List<Infobox_book> InfoboxbookList;
        List<Infobox_person> InfoboxPersonList;
        
		FileInputStream inputFileStream = new FileInputStream("Objects.ser");
	    ObjectInputStream objectInputStream;
		try {
			//deserializacia
			objectInputStream = new ObjectInputStream(inputFileStream);
			InfoboxPersonList = (List<Infobox_person>)objectInputStream.readObject();
			InfoboxList = (List<Infobox_country>)objectInputStream.readObject();
			InfoboxSettlementList = (List<Infobox_settlement>)objectInputStream.readObject();
			InfoboxbookList = (List<Infobox_book>)objectInputStream.readObject();
		
		    objectInputStream.close();
		    inputFileStream.close();
		    
		    //indexovanie
		    StandardAnalyzer analyzerB = new StandardAnalyzer(Version.LUCENE_40);
			StandardAnalyzer analyzerS = new StandardAnalyzer(Version.LUCENE_40);
			StandardAnalyzer analyzerP = new StandardAnalyzer(Version.LUCENE_40);
			StandardAnalyzer analyzerC = new StandardAnalyzer(Version.LUCENE_40);
	        Directory indexB = new RAMDirectory();
	        Directory indexS = new RAMDirectory();  
	        Directory indexP = new RAMDirectory();  
	        Directory indexC = new RAMDirectory();  
	        IndexWriterConfig configB = new IndexWriterConfig(Version.LUCENE_40, analyzerB);
	        IndexWriterConfig configS = new IndexWriterConfig(Version.LUCENE_40, analyzerS);
	        IndexWriterConfig configP = new IndexWriterConfig(Version.LUCENE_40, analyzerP);
	        IndexWriterConfig configC = new IndexWriterConfig(Version.LUCENE_40, analyzerC);
	        IndexWriter b = new IndexWriter(indexB, configB);
	        IndexWriter s = new IndexWriter(indexS, configS);
	        IndexWriter p = new IndexWriter(indexP, configP);
	        IndexWriter c = new IndexWriter(indexC, configC);
		    
	        //indexovanie jednotlivych listov
	        for(Infobox_book book : InfoboxbookList){
		        addBook(b, book.getName(), book.getTranslator(), book.getImage(), book.getCaption(), book.getAuthor(), 
		        		book.getCountry(),book.getLanguage(), book.getSubject(), book.getGenre(), book.getPublished(),
		        		 book.getPages(),  book.getIsbn(), book.getFollowed_by() , book.getPreceded_by());
	        }
	        
	        for(Infobox_country country : InfoboxList){
		        addCountry(c, country.getTitle(), country.getCommon_name(), country.getImage_flag(), country.getImage_coat(), country.getCapital(), 
		        		country.getOfficial_religion(),country.getOfficial_languages(), country.getGovernment_type(), country.getArea_km2(), country.getArea_sq_mi(),
		        		country.getPopulation_estimate(), country.getPopulation_estimate_rank(), country.getCurrency(), country.getCurrency_code());
		    }
	        
	        for(Infobox_settlement settlement : InfoboxSettlementList){
		        addSettlement(s, settlement.getOfficial_name(), settlement.getNickname(), settlement.getMap_caption(), settlement.getCoordinates_region(), settlement.getLeader_title(), 
		        		settlement.getUnit_pref(),settlement.getArea_total_km2(), settlement.getArea_land_km2(), settlement.getPopulation_total(), settlement.getPopulation_density_km2(),
		        		settlement.getTimezone(), settlement.getWebsite(), settlement.getPostal_code());
		    }
	        
	        for(Infobox_person person : InfoboxPersonList){
		        addPerson(p, person.getName(), person.getImage(), person.getImage_size(), person.getBirth_date(), person.getBirth_place(), 
		        		person.getDeath_date(),person.getDeath_place(), person.getBirth_day(), person.getBirth_month(), person.getBirth_year(), person.getDeath_year(), person.getDeath_month(), person.getDeath_day());
		    }
		    
	        b.commit();
	        s.commit();
	        p.commit();
	        c.commit();
	        
	        //vyhladavanie
	        int myint=0;
	        int hitsPerPage = 500;
	        Scanner keyboard = new Scanner(System.in);
	        System.out.println("Vyber druh objektu na vyhladavanie:");
	        System.out.println("1:	Books \n" +
	        				   "2:	Settlement\n" +
	        				   "3:	Person\n" +
	        				   "4:	Country\n" +
	        				   "5:	Ukonci vyhladavanie\n");
	        
	        while(myint != 5){
		        myint = keyboard.nextInt();
		        if (myint == 5){
		        	break;
		        }
		       
		        System.out.println("Zadaj vyhladavaciu frazu:");
		        String fraza = keyboard.next();
		        System.out.print(fraza);		
		  
		        if ( myint == 1){
		        	
		        	System.out.println("Zadaj vyhladavaci atribut:");
			        System.out.println("" +
			        		"*:	" +Book_enum.name +"\n" +
			        		"*:	" +Book_enum.translator +"\n" +
			        		"*:	" +Book_enum.image +"\n" +
			        		"*:	" +Book_enum.caption +"\n" +
			        		"*:	" +Book_enum.author +"\n" +
			        		"*:	" +Book_enum.country +"\n" +
			        		"*:	" +Book_enum.language +"\n" +
			        		"*:	" +Book_enum.subject +"\n" +
			        		"*:	" +Book_enum.genre +"\n" +
			        		"*:	" +Book_enum.published +"\n" +
			        		"*:	" +Book_enum.media_type +"\n" +
			        		"*:	" +Book_enum.pages +"\n" +
			        		"*:	" +Book_enum.isbn +"\n" +
			        		"*:	" +Book_enum.preceded_by +"\n"+ 
			        		"*:	" +Book_enum.followed_by +"\n" 
			        		);
			        String atribut = keyboard.next();
			        
		        	String querystrB = args.length > 0 ? args[0] : fraza;
			        Query qB = new QueryParser(Version.LUCENE_40, atribut, analyzerB).parse(querystrB);
			        IndexReader readerB = DirectoryReader.open(indexB);
			        IndexSearcher searcherB = new IndexSearcher(readerB);
			        TopScoreDocCollector collectorB = TopScoreDocCollector.create(hitsPerPage, true);
			        searcherB.search(qB, collectorB);
			        ScoreDoc[] hitsB = collectorB.topDocs().scoreDocs;
			        
			        System.out.println("Found books:" + hitsB.length + " hits.");
			        for(int i=0;i<hitsB.length;++i) {
			          int docId = hitsB[i].doc;
			          Document d = searcherB.doc(docId);
			          
			          System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("name") + "\t" + d.get("translator") + "\t" + d.get("image")
			        		  + "\t" + d.get("captation")  + "\t" + d.get("author")  + "\t" + d.get("country")  + "\t" + d.get("language")
			        		  + "\t" + d.get("subject") + "\t" + d.get("genre") + "\t" + d.get("published") + "\t" + d.get("media_type")
			        		  + d.get("get_followed_by") + d.get("pages") + d.get("get_preceded_by")
			        		  );
			        }
			        readerB.close();
		        }
		        
		        else if (myint == 2){
		        	System.out.println("Zadaj vyhladavaci atribut:");
			        System.out.println("" +
			        		"*:	" +Settlement_enum.official_name +"\n" +
			        		"*:	" +Settlement_enum.nickname +"\n" +
			        		"*:	" +Settlement_enum.map_caption +"\n" +
			        		"*:	" +Settlement_enum.coordinates_region +"\n" +
			        		"*:	" +Settlement_enum.leader_title +"\n" +
			        		"*:	" +Settlement_enum.unit_pref +"\n" +
			        		"*:	" +Settlement_enum.area_total_km2 +"\n" +
			        		"*:	" +Settlement_enum.area_land_km2 +"\n" +
			        		"*:	" +Settlement_enum.population_total +"\n" +
			        		"*:	" +Settlement_enum.population_density_km2 +"\n" +
			        		"*:	" +Settlement_enum.timezone +"\n" +
			        		"*:	" +Settlement_enum.website +"\n" +
			        		"*:	" +Settlement_enum.postal_code +"\n"
			        		);
			        String atribut = keyboard.next();
		        	
		        	String querystrS = args.length > 0 ? args[0] : fraza;
			        Query qS = new QueryParser(Version.LUCENE_40, atribut, analyzerS).parse(querystrS);
			        IndexReader readerS = DirectoryReader.open(indexS);
			        IndexSearcher searcherS = new IndexSearcher(readerS);
			        TopScoreDocCollector collectorS = TopScoreDocCollector.create(hitsPerPage, true);
			        searcherS.search(qS, collectorS);
			        ScoreDoc[] hitsS = collectorS.topDocs().scoreDocs;
			        System.out.println("Found Settlements:" + hitsS.length + " hits.");
			        for(int i=0;i<hitsS.length;++i) {
			          int docId = hitsS[i].doc;
			          Document d = searcherS.doc(docId);
			          System.out.println((i + 1) + ". " + d.get("official_name") + "\t" + d.get("nickname") + "\t" + d.get("map_caption") + "\t" + d.get("coordinates_region")
			        		  + "\t" + d.get("leader_title")  + "\t" + d.get("unit_pref")  + "\t" + d.get("area_total_km2")  + "\t" + d.get("area_land_km2")
			        		  + "\t" + d.get("population_total") + "\t" + d.get("population_density_km2") + "\t" + d.get("timezone") + "\t" + d.get("postal_code")
			        		  + d.get("website")
			        		  );
			        }
			        readerS.close();
		        }   
		        else if (myint == 3){
		        	System.out.println("Zadaj vyhladavaci atribut:");
			        System.out.println("" +
			        		"*:	" +Person_enum.name +"\n" +
			        		"*:	" +Person_enum.image+"\n" +
			        		"*:	" +Person_enum.image_size +"\n" +
			        		"*:	" +Person_enum.birth_date+"\n" +
			        		"*:	" +Person_enum.birth_day+"\n" +
			        		"*:	" +Person_enum.birth_month+"\n" +
			        		"*:	" +Person_enum.birth_year+"\n" +
			        		"*:	" +Person_enum.birth_place+"\n" +
			        		"*:	" +Person_enum.death_date+"\n" +
			        		"*:	" +Person_enum.death_day+"\n" +
			        		"*:	" +Person_enum.death_month+"\n" +
			        		"*:	" +Person_enum.death_year+"\n" +
			        		"*:	" +Person_enum.death_place+"\n"
			        		);
			        String atribut = keyboard.next();
		        	
		        	String querystrP = args.length > 0 ? args[0] : fraza;
		 	        Query qP = new QueryParser(Version.LUCENE_40, atribut, analyzerP).parse(querystrP);
		 	        IndexReader readerP = DirectoryReader.open(indexP);
		 	        IndexSearcher searcherP = new IndexSearcher(readerP);
		 	        TopScoreDocCollector collectorP = TopScoreDocCollector.create(hitsPerPage, true);
		 	       searcherP.search(qP, collectorP);
		 	      ScoreDoc[] hitsP = collectorP.topDocs().scoreDocs;
		 	     System.out.println("Found Persons:" + hitsP.length + " hits.");
			        for(int i=0;i<hitsP.length;++i) {
			          int docId = hitsP[i].doc;
			          Document d = searcherP.doc(docId);
			          System.out.println((i + 1) + ". " + d.get("name") + "\t" + d.get("image") + "\t" + d.get("image_size") + "\t" + d.get("birth_date") + "\t" + d.get("birth_day") + "\t" + d.get("birth_month") + "\t" + d.get("birth_year")
			        		  + "\t" + d.get("birth_place")  + "\t" + d.get("death_date") + "\t" + d.get("death_day") + "\t" + d.get("death_month") + "\t" + d.get("death_year")  + "\t" + d.get("death_place")  + "\t"
			        		  );
			        }
			        readerP.close();
		        }  
		        
		        else if (myint == 4){
		        	System.out.println("Zadaj vyhladavaci atribut:");
			        System.out.println("" +
			        		"*:	" +Country_enum.title +"\n" +
			        		"*:	" +Country_enum.common_name +"\n" +
			        		"*:	" +Country_enum.image_flag +"\n" +
			        		"*:	" +Country_enum.image_coat +"\n" +
			        		"*:	" +Country_enum.capital +"\n" +
			        		"*:	" +Country_enum.official_religion +"\n" +
			        		"*:	" +Country_enum.official_languages +"\n" +
			        		"*:	" +Country_enum.government_type +"\n" +
			        		"*:	" +Country_enum.area_km2 +"\n" +
			        		"*:	" +Country_enum.area_sq_mi +"\n" +
			        		"*:	" +Country_enum.population_estimate +"\n" +
			        		"*:	" +Country_enum.population_estimate_rank +"\n" +
			        		"*:	" +Country_enum.currency +"\n"+
			        		"*:	" +Country_enum.currency_code +"\n"
			        		);
			        String atribut = keyboard.next();
		        	String querystrC = args.length > 0 ? args[0] : fraza;
		 	        Query qC = new QueryParser(Version.LUCENE_40, atribut, analyzerC).parse(querystrC);
		 	        IndexReader readerC = DirectoryReader.open(indexC);
		 	        IndexSearcher searcherC = new IndexSearcher(readerC);
		 	        TopScoreDocCollector collectorC = TopScoreDocCollector.create(hitsPerPage, true);
		 	        searcherC.search(qC, collectorC);
		 	        ScoreDoc[] hitsC = collectorC.topDocs().scoreDocs;
		 	        System.out.println("Found Countries:" + hitsC.length + " hits.");
		 	        for(int i=0;i<hitsC.length;++i) {
		 	          int docId = hitsC[i].doc;
		 	          Document d = searcherC.doc(docId);
		 	          System.out.println((i + 1) + ". " + d.get("title") + "\t" + d.get("common_name") + "\t" + d.get("image_flag") + "\t" + d.get("image_coat")
		 	        		  + "\t" + d.get("capital")  + "\t" + d.get("official_religion")  + "\t" + d.get("official_languages")  + "\t" + d.get("government_type")
		 	        		  + "\t" + d.get("area_km2") + "\t" + d.get("area_sq_mi") + "\t" + d.get("population_estimate") + "\t" + d.get("population_estimate_rank")
		 	        		  + d.get("currency") + d.get("currency_code")
		 	        		  );
		 	        }
		 	       readerC.close();
		        }
		     }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	
}
