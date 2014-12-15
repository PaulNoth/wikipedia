package dictionary;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;

/**
 * <p>
 * Template dictionary class that specifies methods for concrete dictionary
 * implementations.
 * </p>
 * 
 * @author Pidanic
 *
 */
public abstract class LuceneDbpediaDictionary
{
    protected Analyzer analyzer;

    protected Directory directory;

    protected abstract void initializeDictionaryData();

    /**
     * Method searches and translates given text in dictionary.
     * 
     * @param from
     *            {@link Language} to translate from.
     * @param to
     *            {@link Language} to translate.
     * @param searchText
     *            Text to translate.
     * 
     * @return <code>List</code> of found translations.
     * 
     * @throws IOException
     * @throws ParseException
     */
    public abstract List<String> translate(Language from, Language to,
            String searchText) throws IOException, ParseException;

    /**
     * Closes dictionary stream.
     * 
     * @throws IOException
     */
    public void close() throws IOException
    {
        if(directory != null)
        {
            directory.close();
        }
    }

    public LuceneDbpediaDictionary()
    {
        this.initializeDictionaryData();
    }
}
