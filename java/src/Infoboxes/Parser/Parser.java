package Parser;
import Enums.*;


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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import java.util.Scanner;

public class Parser {
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
	    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
	    
	    try {
	        SAXParser saxParser = saxParserFactory.newSAXParser();
	        
	        //handleri na parsovanie
	        HandlerCountry handler_country = new HandlerCountry();
	        HandlerSettlement handler_settlement = new HandlerSettlement();
	        HandlerBook handler_book = new HandlerBook();
	        HandlerPerson handler_person = new HandlerPerson();
	        
	        //oparsovanie pomocou kniznice SAX
	        String cesta = "data/ukazka-velka.xml";
	        saxParser.parse(new File(cesta), handler_settlement);
	        saxParser.parse(new File(cesta), handler_country);
	        saxParser.parse(new File(cesta), handler_book);
	        saxParser.parse(new File(cesta), handler_person);

	        //zoznamy jednotlivych objektov
	        List<Infobox_country> InfoboxList = handler_country.getInfoboxList();
	        System.out.println("Pocet spracovanych: "+InfoboxList.size());
	        
	        List<Infobox_settlement> InfoboxSettlementList = handler_settlement.getInfoboxList();
	        System.out.println("Pocet spracovanych: "+InfoboxSettlementList.size());
	        
	        List<Infobox_book> InfoboxbookList = handler_book.getInfoboxList();
	        System.out.println("Pocet spracovanych: "+InfoboxbookList.size());
	        
	        List<Infobox_person> InfoboxPersonList = handler_person.getInfoboxList();
	        System.out.println("Pocet spracovanych: "+InfoboxPersonList.size());
	        
	        //serializacia
	        FileOutputStream fileOut = new FileOutputStream("Objects.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(InfoboxPersonList);
			out.writeObject(InfoboxList);
			out.writeObject(InfoboxSettlementList);
			out.writeObject(InfoboxbookList);
			out.close();
			fileOut.close();
			
			//vypocitanie statistik
	        Stats statisky = new Stats();
	        statisky.vypocitaj_statistiky(InfoboxbookList, InfoboxList, InfoboxSettlementList, InfoboxPersonList);

	    } catch (ParserConfigurationException | SAXException | IOException e) {
	        e.printStackTrace();
	    }
	    }
	
	
	
}
