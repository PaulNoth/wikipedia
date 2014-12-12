package vi_dictionary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
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
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;


public class VI_Dictionary 
{
    private static HashMap<Integer,DictionaryEntry> Dictionary;
    /*
    
    Metoda otvori zakladny subor z wikipedia vyextrahuje data
    a vytvori XML subor s tymito datami
    */
    private static void GenerateAndLoad() throws UnsupportedEncodingException, IOException
    {
       Dictionary = new HashMap<Integer,DictionaryEntry>();//new LinkedList<DictionaryEntry>();
            
       FileReader fr = new FileReader();
       Dictionary = fr.ConstructDictionary(Dictionary);
       
       XMLWriter xw = new XMLWriter();
       if(Dictionary.size()>0)
       xw.CreateXML(Dictionary);  
    }
    
    private static Directory index;
    
    
    /* Metoda vyhladava v indexovanom zozname a vrati top 10 vysledkov
    ``pre vyhladavany vyraz vo vsetkych jazykoch.
    */
    private static void Search(String querystr, String dictionary) throws IOException, ParseException, QueryNodeException
    {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        
        
        if(analyzer==null || index == null)
        {
            System.out.println("Je potrebne vytvorit index.");
            return;
        } 
        
        QueryParser qp = new QueryParser(Version.LUCENE_CURRENT, dictionary, analyzer);
        qp.setLowercaseExpandedTerms(false);
        Query q = qp.parse(querystr);      

        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
    
        System.out.println("Nájdených " + hits.length + " záznamov.");
        if(hits.length>0)
        {
        System.out.println("No\t|ID\t|SK názov\t|FI názov\t|NO názov");
        System.out.println("-----------------------------------------");
            
        }
        
        for(int i=0;i<hits.length;++i) 
        {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
                
            String nameFI = d.get("nameFI"); 
            String nameNO = d.get("nameNO");
            String nameSK = d.get("nameSK");
            
            if(nameFI.equalsIgnoreCase("not available")) nameFI = "N/A";
            if(nameSK.equalsIgnoreCase("not available")) nameSK = "N/A";
            if(nameNO.equalsIgnoreCase("not available")) nameNO = "N/A";
            
            System.out.println((i + 1) + ".\t" + d.get("id") + "\t" + nameSK + "\t" + nameFI +"\t" + nameNO);
        }

        reader.close();
    
    }

    /*
    Metoda main v slucke spusta jednotlive metody na zaklade
    prikazov zadanych pouzivatelom.
    */
    
    public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParserConfigurationException, SAXException, ParseException, QueryNodeException 
    {
        Scanner keyboard = new Scanner(System.in);
        boolean quit = false; 
        
         while(!quit)
       {
           System.out.println("Zadaj prikaz: ");           
           String command = keyboard.nextLine();
           String name;
           
           switch(command.toLowerCase())
           {
               case "search fi":
                   System.out.println("Zadaj hladany vyraz: ");
                   name = keyboard.nextLine();
                   Search(name,"nameFI");
                   break;
               case "search sk":
                   System.out.println("Zadaj hladany vyraz: ");
                   name = keyboard.nextLine();
                   Search(name,"nameSK");
                   break;
               case "search no":
                   System.out.println("Zadaj hladany vyraz: ");
                   name = keyboard.nextLine();
                   Search(name,"nameNO");
                   break;
               case "load":
                   System.out.println("Nacitavam...");
                   FileReader fr = new FileReader();
                   index = fr.ConstructDictionaryFromXML();
                   System.out.println("Slovnik bol uspesne nacitany z XML suboru.");
                   break;
               case "stats":
                   Statistics stats = new Statistics();
                   stats.CalculateAllStats(Dictionary);
                   break;
               case "generate":
                   GenerateAndLoad();
                   System.out.println("XML subor bol uspesne vygenerovany.");
                   break;
               case "quit":
                   quit = true;
                   break;
               default: 
                   System.out.println("Neznamy prikaz.");
           }
           
       }
       
    }
    
}
