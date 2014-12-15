package skCzStemmer.utils;

import java.io.File;

/**
 * Util class containing used file paths.
 * 
 * @author Tomáš Nemeèek
 */
public class MyFilePaths{

    public static final String DATALOCATION = "data" + File.separator;
    
    public static final String SAMPLE_SK_DATA_XML = DATALOCATION + "sample" + File.separator + "sample_skwiki_latest_pages_articles.xml";
    public static final String FULL_SK_DATA_XML = DATALOCATION + "sk" + File.separator + "skwiki-latest-pages-articles.xml";
    public static final String FULL_CZ_DATA_XML = DATALOCATION + "cz" + File.separator + "cswiki-latest-pages-articles.xml";
    
    public static final String SAMPLE_TREE_FILE_LOCATION = DATALOCATION  + "sample" + File.separator +"sample_treeFile.xml";
    public static final String FULL_SK_TREE_FILE_LOCATION = DATALOCATION + "sk" + File.separator + "full_sk_treeFile.xml";
    public static final String FULL_CZ_TREE_FILE_LOCATION = DATALOCATION + "cz" + File.separator + "full_cz_treeFile.xml";
    
    public static final String TREE_FILE_NAME = "treeFile.xml";
    
}
