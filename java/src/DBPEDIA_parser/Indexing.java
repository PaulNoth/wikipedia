/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpedia_parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Zuzka
 */
public class Indexing {

    public static IndexWriter writer;
    public static StandardAnalyzer analyzer;
    public static Directory dir;
    
    //Parsing DUMP files, prepare regex and paths

    public static void parser() throws IOException {

        Regex r = new Regex();

        String indexpath = "index\\main_index";
        dir = FSDirectory.open(new File(indexpath));
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        writer = new IndexWriter(dir, iwc);
        file_reader(r.get_filePath_labels(), r.get_regex_labels(), 3);
        file_reader(r.get_filePath_shortAbstract(), r.get_regex_shortAbstract(), 0);
        file_reader(r.get_filePath_category(), r.get_regex_category(), 1);
        writer.close();

        indexpath = "index\\redirect_index";
        dir = FSDirectory.open(new File(indexpath));
        analyzer = new StandardAnalyzer(Version.LUCENE_40);
        iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        writer = new IndexWriter(dir, iwc);
        file_reader(r.get_filePath_redirect(), r.get_regex_redirect(), 2);
        writer.close();
    }

    //Method used to reading file, sign is used to get know what kind of file (short abstract, redirect, category) are we going to read
    public static void file_reader(String filePath, String regex, int sign) throws IOException {

        String line;
        try (FileReader fr = new FileReader(filePath); BufferedReader bufferReader = new BufferedReader(fr)) {

            String Lines = "";
            int counter = 0;

            while ((line = bufferReader.readLine()) != null) {

                //!!!zbytocne counter - pouzi ako premennu
                Lines = Lines + line + "\n";
                counter++;
                if (counter == 1) {
                    get_result(regex, Lines, sign);
                    counter = 0;
                    Lines = "";
                }
            }

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    //Create index file with LUCENE
    public static void get_result(String regex, String Lines, int sign) throws IOException {

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(Lines);

        while (matcher.find()) {

            //Depending on sign decide 
            if (sign == 0) {
                Document doc = new Document();
                //System.out.println(matcher.group(2).trim());
                doc.add(new StringField("short_abstract", matcher.group(2).trim(), Field.Store.YES));
                doc.add(new StringField("title", matcher.group(1).trim(), Field.Store.YES));
                writer.addDocument(doc);
            } else if (sign == 1) {
                Document doc = new Document();
                doc.add(new StringField("category", matcher.group(2).trim(), Field.Store.YES));
                doc.add(new StringField("title", matcher.group(1).trim(), Field.Store.YES));
                writer.addDocument(doc);
            } else if (sign == 2) {
                Document doc = new Document();
                doc.add(new StringField("redirect", matcher.group(2).trim(), Field.Store.YES));
                doc.add(new StringField("title", matcher.group(1).trim(), Field.Store.YES));
                writer.addDocument(doc);
            } else if (sign == 3) {
                Document doc = new Document();
                String s = matcher.group(1).trim().replace(" ", "_");
                doc.add(new StringField("title", s, Field.Store.YES));
                writer.addDocument(doc);
            }
        }
    }
}
