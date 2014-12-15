package langlinksfromsql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;

/**
 * Contains only the constructor - main application logic
 *
 * @author Daniel
 */

public class TitleMatchingLangHashMaps   {
        
    /**
     * Writes into a file the sk->defined language matches and vice versa
     *
     * @param fileFirstLangLinks
     * @param fileSecondLangLinks
     * @param fileFirstLangTitles
     * @param fileSecondLangTitles
     * @param filepath
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public TitleMatchingLangHashMaps(String fileFirstLangLinks, String fileSecondLangLinks, String fileFirstLangTitles, String fileSecondLangTitles, String filepath) 
            throws FileNotFoundException, UnsupportedEncodingException, IOException {
        
/**
 * note: prefix 'hu' for many variables is not indicating the actual language; could be EN too
 * input parameter lang2 does indicate that
 */        
        
        String lang1,lang2;
        String line,lineOfskTitle,lineOfhuTitle;
        String language;
        String name;
        
        // get language abbreviation from the filepath
        lang1 = fileFirstLangLinks.substring(fileFirstLangLinks.indexOf("wiki")-2,fileFirstLangLinks.indexOf("wiki"));
        lang2 = fileSecondLangLinks.substring(fileSecondLangLinks.indexOf("wiki")-2,fileSecondLangLinks.indexOf("wiki"));
        
        /// declaring important variables
        Writer writer;                                                          /// file output

        HashMap<String, String> Titles = new HashMap<String, String>();         /// ID and title in the hash
        HashMap<String, String> RevertedTitles = new HashMap<String, String>(); /// other language's ID and title
        
        String sk_idFromPage,hu_idFromPage;
        String sk_TitleFromPage,hu_TitleFromPage,skTitleFromPage,huTitleFromPage;
        
        int idLastIndex, nameLastIndex;
        int sk_idLastIndexFromPage, sk_titleFirstIndex, sk_titleLastIndex,hu_idLastIndexFromPage,hu_titleFirstIndex,hu_titleLastIndex;
        /// declaring important variables
        
        /// open + init reader for all 4 files
        BufferedReader skLang = null, skTitle = null, huLang = null, huTitle = null;
        
        skLang = FileReadManager.initReader(fileFirstLangLinks, skLang);
        skTitle = FileReadManager.initReader(fileFirstLangTitles, skTitle);
        huLang = FileReadManager.initReader(fileSecondLangLinks, huLang);       
        huTitle = FileReadManager.initReader(fileSecondLangTitles, huTitle);     
        /// open + init reader for all 4 files
        
        String filepathWriter = filepath;
        filepathWriter = filepathWriter.concat("/data/output_data_sk-");
        filepathWriter = filepathWriter.concat(lang2).concat("-matches.txt");
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepathWriter), "utf-8"));

        /// construct the hashmap - page id and title name in the "lang2" language
        while((line = skLang.readLine()) != null){

            idLastIndex = line.indexOf(',');
            language = line.substring(idLastIndex+2,idLastIndex+4);             /// get language
    
            if(language.contentEquals(lang2)){                                   /// lang2 can be EN or HU
                nameLastIndex = line.lastIndexOf(')')-1;                
                name = line.substring(idLastIndex+7,nameLastIndex);             /// name means the title of wiki article
                name = name.replace("\\'", "\'").replace("’", "\'");            /// some actual \' chars might be in title
                Titles.put(line.substring(1, idLastIndex), name);   /// key - getID, value - title in 'language'; fill hmap
            }
        }
        /// construct the hashmap - page id and title name in the "lang2" language        
        
        while((lineOfskTitle = skTitle.readLine()) != null){                    /// get slovak title as well for found IDs
            sk_idLastIndexFromPage = lineOfskTitle.indexOf(',');
            sk_idFromPage = lineOfskTitle.substring(1, sk_idLastIndexFromPage);
                if(Titles.containsKey(sk_idFromPage)){              /// ID contained both in langlinks and titles.sql
                    sk_titleFirstIndex = lineOfskTitle.indexOf('\'')+1;
                    sk_titleLastIndex = lineOfskTitle.indexOf('\'',sk_titleFirstIndex+1);
                    if(lineOfskTitle.contains("\\'")){                          /// some titles contain actual "'" char
                        sk_titleLastIndex = lineOfskTitle.indexOf("\',\'",sk_titleLastIndex);
                        sk_TitleFromPage = lineOfskTitle.substring(sk_titleFirstIndex, sk_titleLastIndex);
                        sk_TitleFromPage = sk_TitleFromPage.replace("\\'", "\'");
                    }
                    else sk_TitleFromPage = lineOfskTitle.substring(sk_titleFirstIndex, sk_titleLastIndex);
                    sk_TitleFromPage = sk_TitleFromPage.replace("_"," ").replace("’", "\'");       /// convert name to suit format
                    huTitleFromPage = Titles.get(sk_idFromPage);                /// id that matches the slovak version
                    writer.write("'"+sk_TitleFromPage+"' ("+lang1+") matches "+"'"+huTitleFromPage+"' ("+lang2+")");
                    writer.append('\n');
                }
        }
        
        Titles.clear();     // empty hashmap
        skLang.close();     // close readers
        skTitle.close();
        
        writer.write(lang2.toUpperCase()+" -> "+lang1.toUpperCase()+" matches\n");  /// reverse linking 
                                                                                /// (e.g. HU -> SK instead of SK->HU)       
        // lang2->sk matching
        while((line = huLang.readLine()) != null){

            idLastIndex = line.indexOf(',');
            language = line.substring(idLastIndex+2,idLastIndex+4);             /// get language
            
            if(language.contentEquals(lang1)){                                   /// matching 'sk' for HU or EN langlinks
                nameLastIndex = line.lastIndexOf(')')-1;             
                name = line.substring(idLastIndex+7,nameLastIndex);             /// name means the title of wiki article
                name = name.replace("\\'", "\'").replace("’", "\'");
                RevertedTitles.put(line.substring(1, idLastIndex), name);   /// key - getID, value - title in 'language'
            }
        }
        
        while((lineOfhuTitle = huTitle.readLine()) != null){                    /// get HU/EN title if IDs are matched
            hu_idLastIndexFromPage = lineOfhuTitle.indexOf(',');
            hu_idFromPage = lineOfhuTitle.substring(1, hu_idLastIndexFromPage);
                if(RevertedTitles.containsKey(hu_idFromPage)){              /// ID contained both in langlinks and titles.sql
                    hu_titleFirstIndex = lineOfhuTitle.indexOf('\'')+1;
                    hu_titleLastIndex = lineOfhuTitle.indexOf('\'',hu_titleFirstIndex+1);
                    if(lineOfhuTitle.contains("\\'")){              /// some titles contain actual "'" char
                        hu_titleLastIndex = lineOfhuTitle.indexOf("\',\'",hu_titleLastIndex);
                        hu_TitleFromPage = lineOfhuTitle.substring(hu_titleFirstIndex, hu_titleLastIndex);
                        hu_TitleFromPage = hu_TitleFromPage.replace("\\'", "\'");   /// convert \' to '
                    }
                    else hu_TitleFromPage = lineOfhuTitle.substring(hu_titleFirstIndex, hu_titleLastIndex);
                    hu_TitleFromPage = hu_TitleFromPage.replace("_"," ").replace("’", "\'"); /// convert name to suit format
                    skTitleFromPage = RevertedTitles.get(hu_idFromPage);                /// id that matches the slovak version
                    writer.write("'"+hu_TitleFromPage+"' ("+lang2+") matches "+"'"+skTitleFromPage+"' ("+lang1+")");
                    writer.append('\n');
                }
        }
        RevertedTitles.clear();
        writer.close();
        huLang.close();
        huTitle.close();
    }  
}