package langlinksfromsql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * This project preprocesses SQL page and langlinks dumps and 
 * outputs the language matches as well as provides some statistics upon them
 *
 * @author Daniel
 */
public class LangLinksFromSQL {

    /**
     * @param args the command line argument will be the path to folder data/...
     * @throws java.io.FileNotFoundException
     * @throws java.io.UnsupportedEncodingException
     */
    public static void main(String args[]) throws UnsupportedEncodingException, IOException  { // arg - CESTA SUBOROV!
        System.out.println("\nThis program checks English and Hungarian language links for Slovak matches");
        System.out.println("then does the same inversely - program output is the list of all matches in both ways saved into a");
        System.out.println("text file, plus statistics about the no. of matches saved into another text file;");
        System.out.println("this file also contains the list of those matches that were not matched back inversely in the same way for some reason");
        System.out.println("e.g., the language link on the page doest not link back, or missing element from the sql dump etc.");
        System.out.println();
        /// determine whether run on sample or whole data set
        System.out.printf("For running the program on the whole data set PRESS 1 (ALSO DEFAULT), otherwise PRESS 0 and the code will run on sample data: ");
        char c;
        boolean realSample = true;
        BufferedReader inStr = new BufferedReader(new InputStreamReader(System.in));
        c = inStr.readLine().charAt(0);
        if(c=='0') realSample = false;        
        
        /// determine languages
        System.out.printf("Type in the language you want to match with SK language links (only EN or HU is valid - case insensitive): ");
        String line,lang1,lang2;
        line = inStr.readLine();
        lang2 = line.substring(0,2).toLowerCase();
        lang1 = "sk";
        
        /// preprocessing sql dumps to the wanted format - one page on one line
        if(realSample){
            ProcessWikiDumps.processLangLinksSQL(lang1,args[0]);
            ProcessWikiDumps.processLangLinksSQL(lang2,args[0]);
            ProcessWikiDumps.processPageSQL(lang1,args[0]);
            ProcessWikiDumps.processPageSQL(lang2,args[0]);            
        }
        
        // get path and filename
        String fileFirstLang, fileSecondLang, fileFirstLangTitles, fileSecondLangTitles;
                
        fileFirstLang = FileManager.constructFilePath(lang1, args[0], true, realSample);
        fileSecondLang = FileManager.constructFilePath(lang2, args[0], true, realSample);
        fileFirstLangTitles = FileManager.constructFilePath(lang1, args[0], false, realSample);
        fileSecondLangTitles = FileManager.constructFilePath(lang2, args[0], false, realSample);
        
        TitleMatchingLangHashMaps titlematchusinghashmaps = 
                new TitleMatchingLangHashMaps(fileFirstLang,fileSecondLang,fileFirstLangTitles,fileSecondLangTitles,args[0]);
        
        ProvideStats.provideStats(lang2,args[0]);
        ProvideStats.getDifferences(lang2,args[0]);
        
        System.out.println("\nResults successfully obtained");
    }
}
