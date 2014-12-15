/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package langlinksfromsql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * ProcessWikiDumps class executes the preprocessing to get the expected input formats of SQL dumps
 *
 * @author Daniel
 */
public class ProcessWikiDumps {
    
    private static BufferedReader br;
    
    /**
     * Preprocessing page.sql files containing the titles to get one insert into one line
     *
     * @param language
     * @param filepath
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void processPageSQL(String language, String filepath) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        if(language.equals("en")) filepath = filepath.concat("/data/enwiki-latest-page.sql");
        else if(language.equals("sk")) filepath = filepath.concat("/data/skwiki-latest-page.sql");
        else filepath = filepath.concat("/data/huwiki-latest-page.sql");
        br = FileReadManager.initReader(filepath, br);
        Writer writer;
        filepath = filepath.replace("/data/", "/data/input_");
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "utf-8"));
        
        String line;
        
        while((line = br.readLine())!= null){
            if(line.contains("INSERT INTO")){
                line = line.replace("INSERT INTO `page` VALUES ","");
                line = line.replaceAll(",NULL[)],[(]",",NULL)\n(");
                writer.write(line);
                writer.append('\n');
            }
        }
        br.close();
        writer.close();
    }
    
    /**
     * Same as the other method but preprocessing langlinks sql files; one insert one line
     * Considering only language inserts which are relevant for our project
     *
     * @param language
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void processLangLinksSQL(String language, String filepath) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        if(language.equals("en")) filepath = filepath.concat("/data/enwiki-latest-langlinks.sql");
        else if(language.equals("sk")) filepath = filepath.concat("/data/skwiki-latest-langlinks.sql");
        else filepath = filepath.concat("/data/huwiki-latest-langlinks.sql");
        br = FileReadManager.initReader(filepath, br);
        Writer writer;
        filepath = filepath.replace("/data/", "/data/input_");
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "utf-8"));
        
        String line;
        String[] langlinks = new String[2];
        langlinks[1] = null;
        if(language.equals("sk")) {
            langlinks[0] = "en";
            langlinks[1] = "hu";
        }
        else langlinks[0] = "sk";
        
        while((line = br.readLine()) != null){
            if(line.contains(",'"+langlinks[0]+"',") || line.contains(",'"+langlinks[1]+"',")){
                line = line.replace("INSERT INTO `langlinks` VALUES ","");
                line = line.replaceAll("'[)],[(]","')\n(");
                writer.write(line);
                writer.append('\n');
            }
        }
        writer.close();
        br.close();
    }
    
}
