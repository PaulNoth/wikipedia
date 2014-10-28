package skCzStemmer.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
/**
 * Util class containing used file paths.
 * @author Tomáš Nemeèek
 */
public class MyFileUtils extends FileUtils {

    public static final String LETTERSFILENAME = "letters.xml";
    public static final String LETTERSLOCATION = "data" + File.separator ;
    public static final String LETTERS_TREE_LOCATION = System.getProperty("user.dir") + File.separator + LETTERSLOCATION + File.separator + "letters";
    
    public static final String SAMPLEDATA = "data/sample_skwiki_latest_pages_articles.xml";
    
    public static final String TRAINED_DATA = System.getProperty("user.dir") + File.separator + LETTERSLOCATION + File.separator + LETTERSFILENAME;
}
