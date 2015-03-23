/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpedia_parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Class for searching for labels 
 * @author Zuzka
 */
public class Searching {
    
    public static IndexWriter writer;
    public static StandardAnalyzer analyzer;
    public static Directory dir;
    
    //method for searching label "name", type of output is defined by boolean xmlout, 
    //if xmlout is true, output.xml created
    //if xmlout is false, console output created
    public static String search(String name, boolean xmlout) throws IOException {

        String indexpath = "index\\main_index";
        dir = FSDirectory.open(new File(indexpath));
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        List<String> shortabstract_list = new ArrayList();
        List<String> categories_list = new ArrayList();
        List<String> redirects_list = new ArrayList();
        String label = "";

        redirects_list = redirect(name);
        
        TermQuery tq = new TermQuery((new Term("title", name)));
        TopDocs results = searcher.search(tq, 100);
        
        //if theres no result it means theres no match. 
        if (results.scoreDocs.length == 0) {
            System.out.println("Could not find page with title " + name + ", please change the input");
        } //1 result means that we found only label, then try to redirect
        else if (results.scoreDocs.length == 1) {
            System.out.println("Could not find page with title " + name + ", trying to redirect ...");
            redirect(name, xmlout);
        } else {
            for (ScoreDoc scoredoc : results.scoreDocs) {
                Document doc = reader.document(scoredoc.doc);
                //String title = doc1.get("short_abstract");
                if (doc.get("short_abstract") != null) {
                    shortabstract_list.add(doc.get("short_abstract"));
                }
                if (doc.get("category") != null) {
                    categories_list.add(doc.get("category"));
                }
                if ((doc.get("short_abstract") == null) && (doc.get("category") == null) && (doc.get("title") != null)) {
                    label = doc.get("title");
                }
            }
            reader.close();

            if ((categories_list.size() == 1) && ("Unprintworthy_redirects".equals(categories_list.get(0)))) {
                System.out.println("Could not find page with title " + name + ", trying to redirect ...");
                redirect(name, xmlout);
            } else {
                if (xmlout == true) {
                    return Output.create_output_xml(label, shortabstract_list, categories_list, redirects_list);
                } else {
                    return Output.create_output_console(label, shortabstract_list, categories_list, redirects_list);
                }
            }
        }

        return "";
    }

    //trying to redirect means that when there is no match we try to find match in redirects and then search for label given in redirect
    public static List<String> redirect(String name, boolean xmlout) throws IOException {

        String indexpath = "index\\redirect_index";
        dir = FSDirectory.open(new File(indexpath));
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        List<String> redirects_list = new ArrayList();

        TermQuery tq = new TermQuery((new Term("title", name)));
        TopDocs results = searcher.search(tq, 10);

        for (ScoreDoc scoredoc : results.scoreDocs) {
            Document doc = reader.document(scoredoc.doc);
            if (doc.get("redirect") != null) {
                String out = search(doc.get("redirect"), xmlout);
                if (xmlout == false) {
                    System.out.print(out);
                }
                redirects_list.add(doc.get("redirect"));
            }
        }
        return redirects_list;
    }
    
    public static List<String> redirect(String name) throws IOException {

        String indexpath = "index\\redirect_index";
        dir = FSDirectory.open(new File(indexpath));
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        List<String> redirects_list = new ArrayList();

        TermQuery tq = new TermQuery((new Term("title", name)));
        TopDocs results = searcher.search(tq, 10);

        for (ScoreDoc scoredoc : results.scoreDocs) {
            Document doc = reader.document(scoredoc.doc);
            if (doc.get("redirect") != null) {
                redirects_list.add(doc.get("redirect"));
            }
        }
        return redirects_list;
    }
}
