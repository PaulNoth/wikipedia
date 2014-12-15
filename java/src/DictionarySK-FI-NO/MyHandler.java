package vi_dictionary;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
public class MyHandler extends DefaultHandler
{
    private DictionaryEntry entry;
    private Directory index;
    private int counter=0;
    private boolean id;
    private boolean fi;
    private boolean no;
    private boolean sk;
    private boolean name;
    private boolean link;
    private boolean first;
    private IndexWriter w;
    
    public int GetCount()
    {
        return counter;
    }
    
    public Directory GetDictionary()
    {
        return index;
    }
    
    /*
    Metoda prida novy zaznam do slovnika
    */
    
    private static void addDoc(IndexWriter w, String id, String nameSK, String nameNO, String nameFI) throws IOException 
    {
        Document doc = new Document();
        doc.add(new TextField("id", id, Field.Store.YES));
        doc.add(new StringField("nameSK", nameSK, Field.Store.YES));      
        doc.add(new StringField("nameFI", nameFI, Field.Store.YES));
        doc.add(new StringField("nameNO", nameNO, Field.Store.YES));
        w.addDocument(doc);
    }
   
    /*
    Metoda spracovava otvaraci tag v xml atribute
    */
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
        if (qName.equalsIgnoreCase("Entry")) {
            entry = new DictionaryEntry();
            entry.SetFILang(new LanguageLink());
            entry.SetSKLang(new LanguageLink());
            entry.SetNOLang(new LanguageLink());
            
            if(!first)
            {
                StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
                index = new RAMDirectory();     
        
                IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
                try 
                {
                    w = new IndexWriter(index, config);
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(MyHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                first = true;
            }
     //       Dictionary = new HashMap<Integer,DictionaryEntry>();
        } 
        else if(qName.equalsIgnoreCase("ID"))
        {
            id = true;
        }
        else if (qName.equalsIgnoreCase("FI")) 
        {
            fi = true;
        } 
        else if (qName.equalsIgnoreCase("SK")) 
        {
            sk = true;
        } 
        else if (qName.equalsIgnoreCase("NO")) 
        {
            no = true;
        } 
        else if (qName.equalsIgnoreCase("Name")) 
        {
            name = true;
        }
        else if (qName.equalsIgnoreCase("Link"))
        {
            link = true;
        }
    }
 
    /*
    Metoda spracovava zatvaraci element v xml subore
    */
    
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Entry")) 
        {
               counter++;
               DictionaryEntry item = entry;
               String nameSK = "not available";
               String nameFI = "not available";
               String nameNO = "not available";
               
               if(item.GetSKLang()!=null && item.GetSKLang().GetName()!=null)
               {
                   nameSK = item.GetSKLang().GetName();
               }
               
               if(item.GetNOLang()!=null && item.GetNOLang().GetName()!=null)
               {
                   nameNO = item.GetNOLang().GetName();
               }
               
               if(item.GetFILang()!=null && item.GetFILang().GetName()!=null)
               {
                   nameFI = item.GetFILang().GetName();
               }
            
            try 
            {
                addDoc(w, Integer.toString(item.GetID()),nameSK,nameNO,nameFI);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(MyHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (qName.equalsIgnoreCase("FI")) 
        {
            fi = false;
        } 
        else if (qName.equalsIgnoreCase("SK")) 
        {
            sk = false;
        } 
        else if (qName.equalsIgnoreCase("NO")) 
        {
            no = false;
        }
        else if(qName.equalsIgnoreCase("Dictionary"))
        {
            try 
            {
                w.close();
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(MyHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
 
    /*
    Metoda spracovava obsah medzi dvoma tagmi v xml subore
    */
    
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
 
        if (id) 
        {
            entry.SetID(Integer.parseInt(new String(ch, start, length)));
            id = false;
        }
        else if (name)
        {
            if(fi)
            {
                entry.GetFILang().SetName(new String(ch, start, length));
            }
            else if(sk)
            {
                entry.GetSKLang().SetName(new String(ch, start, length));
            }
            
            else if(no)
            {
                entry.GetNOLang().SetName(new String(ch, start, length));
            }
            name = false;
        }
        else if (link) {
            if(fi)
            {
                entry.GetFILang().SetLink(new String(ch, start, length));
            }
            else if(sk)
            {
                entry.GetSKLang().SetLink(new String(ch, start, length));
            }
            
            else if(no)
            {
                entry.GetNOLang().SetLink(new String(ch, start, length));
            }
            
            link = false;
        }
    }
}
