package skCzStemmer.utils;

import java.io.File;

/**
 * Util class containing used file paths.
 * 
 * @author Tomáš Nemeèek
 */
public class MyFilePaths{

    public static final String DATALOCATION = "data" + File.separator;
    
    public static final String SAMPLE_SK_DATA_XML = DATALOCATION + "sample_skwiki_latest_pages_articles.xml";
    public static final String FULL_SK_DATA_XML = DATALOCATION + "skwiki-latest-pages-articles.xml";
    
    public static final String SAMPLE_TREE_FILE = DATALOCATION + "sample_treeFile.xml";
    public static final String FULL_TREE_FILE = DATALOCATION + "full_treeFile.xml";
    
}
