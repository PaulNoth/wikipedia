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
 * Class that implements searching and indexing logic for Simple dictionary.
 * </p>
 * <p>
 * Simple dictionary searches given input and returns translated result. As an
 * input can be used any of known dbpedia terms. It is not required that all
 * word have to have corresponding match in another language. If translation of
 * given input cannot be found, result contains information about it.
 * </p>
 * 
 * @author Pidanic
 *
 */
public class LuceneSimpleDictionary extends LuceneDbpediaDictionary
{
    // path to full simple dictionary, change if necessary
    private static final String PATH_DICTIONARY = "data" + File.separator
            + "dictionary.csv";

    @Override
    public List<String> translate(Language from, Language to, String searchText)
            throws IOException, ParseException
    {
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        QueryParser qp = new QueryParser(from.toString(), analyzer);
        Query query = qp.parse(searchText);
        TopDocs topDocs = isearcher.search(query, 10);
        ScoreDoc[] hits = topDocs.scoreDocs;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < hits.length; i++)
        {
            Document hitDoc = isearcher.doc(hits[i].doc);
            StringBuilder resultLine = new StringBuilder();
            resultLine.append(hitDoc.get(from.toString()));
            resultLine.append(" -> ");
            String translation = hitDoc.get(to.toString());
            if(translation == null || translation.isEmpty())
            {
                resultLine.append("Neexistuje preklad");
            }
            else
            {
                resultLine.append(translation);
            }
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

            br = new BufferedReader(new FileReader(new File(PATH_DICTIONARY)));
            String line = null;
            while ((line = br.readLine()) != null)
            {
                Document doc = new Document();
                String[] words = line.replaceAll(",", " , ").split(",");
                if(words.length == 4)
                {
                    doc.add(new Field(Language.EN.toString(), words[0].trim(),
                            TextField.TYPE_STORED));
                    doc.add(new Field(Language.FR.toString(), words[1].trim(),
                            TextField.TYPE_STORED));
                    doc.add(new Field(Language.DE.toString(), words[2].trim(),
                            TextField.TYPE_STORED));
                    doc.add(new Field(Language.SK.toString(), words[3].trim(),
                            TextField.TYPE_STORED));
                    iwriter.addDocument(doc);
                }
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
