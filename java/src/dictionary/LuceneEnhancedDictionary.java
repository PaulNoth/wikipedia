package dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * <p>
 * Class that implements searching and indexing logic for Enhanced dictionary.
 * </p>
 * <p>
 * Enhanced dictionary searches given input and returns translated result. Only
 * these translations are returned that have translations of all supported
 * languages.<br>
 * Translated result contains Wikipedia links to give more information about
 * given input and translated output.
 * </p>
 * 
 * @author Pidanic
 *
 */
public class LuceneEnhancedDictionary extends LuceneDbpediaDictionary
{
    private static final String WIKI = "Wiki";

    private static final String PATH_DICTIONARY_ENHANCED = "data"
            + File.separator + "dictionary_enhanced.csv";

    @Override
    public List<String> translate(Language from, Language to, String searchText)
            throws IOException, ParseException
    {
        // search index
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // parse simple query that searches for "text"
        QueryParser qp = new QueryParser(from.toString(), analyzer);
        Query query = qp.parse(searchText);
        TopDocs topDocs = isearcher.search(query, 20);
        ScoreDoc[] hits = topDocs.scoreDocs;
        // ScoreDoc[] hits = isearcher.search(query, 1000).scoreDocs; // moze
        // filtrovat,
        // sortovat
        List<String> result = new ArrayList<>();
        for (int i = 0; i < hits.length; i++)
        {
            Document hitDoc = isearcher.doc(hits[i].doc);
            StringBuilder resultLine = new StringBuilder();
            resultLine.append(hitDoc.get(from.toString()));
            resultLine.append(" -> ");
            resultLine.append(hitDoc.get(to.toString()));
            String fromWiki = hitDoc.get(from.toString().toLowerCase() + WIKI);
            String toWiki = hitDoc.get(to.toString().toLowerCase() + WIKI);
            resultLine.append("\n");
            resultLine.append(fromWiki);
            resultLine.append(" -> ");
            resultLine.append(toWiki);
            resultLine.append("\n");
            result.add(resultLine.toString());
        }
        ireader.close();

        return result;
    }

    @Override
    protected void initializeDictionaryData()
    {
        BufferedReader br = null;
        try
        {
            analyzer = new StandardAnalyzer();
            directory = new RAMDirectory();
            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
                    analyzer);
            IndexWriter iwriter = new IndexWriter(directory, config);

            File dictionary = new File(PATH_DICTIONARY_ENHANCED);
            br = new BufferedReader(new FileReader(dictionary));

            String line = null;
            while ((line = br.readLine()) != null)
            {
                Document doc = new Document();
                String[] words = line.split(",");
                doc.add(new Field(Language.SK.toString(), words[0],
                        TextField.TYPE_STORED));
                doc.add(new Field(Language.SK.toString().toLowerCase() + WIKI,
                        words[1], TextField.TYPE_STORED));

                doc.add(new Field(Language.FR.toString(), words[2],
                        TextField.TYPE_STORED));
                doc.add(new Field(Language.FR.toString().toLowerCase() + WIKI,
                        words[3], TextField.TYPE_STORED));

                doc.add(new Field(Language.EN.toString(), words[4],
                        TextField.TYPE_STORED));
                doc.add(new Field(Language.EN.toString().toLowerCase() + WIKI,
                        words[5], TextField.TYPE_STORED));

                doc.add(new Field(Language.DE.toString(), words[6],
                        TextField.TYPE_STORED));
                doc.add(new Field(Language.DE.toString().toLowerCase() + WIKI,
                        words[7], TextField.TYPE_STORED));
                iwriter.addDocument(doc);
            }

            iwriter.close();
        }
        catch (FileNotFoundException fnf)
        {
            System.err.println("File not found " + fnf.getMessage());
        }
        catch (IOException io)
        {
            System.err.println("IoException " + io.getMessage());
        }
        finally
        {
            try
            {
                if(br != null)
                {
                    br.close();
                }
            }
            catch (IOException e)
            {
                System.err.println("Problem while closing file "
                        + e.getMessage());
            }
        }
    }
}
