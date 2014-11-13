package skCzStemmer.test.stemmer;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.stemmer.StemmerDefaultService;
import skCzStemmer.utils.MyFilePaths;

public class StemmerServiceTest {

    private File testSKFile = new File(MyFilePaths.DATALOCATION + "testSK.test");
    private File testCZFile = new File(MyFilePaths.DATALOCATION + "testCZ.test");

    private File skTreeFile = new File(MyFilePaths.FULL_SK_TREE_FILE);
    private File czTreeFile = new File(MyFilePaths.FULL_CZ_TREE_FILE);


    private StemmerDefaultService stemmerService1 = new StemmerDefaultService(100, skTreeFile);
    private StemmerDefaultService stemmerService2 = new StemmerDefaultService(100, czTreeFile);

    @Test
    public void stemmAnchors() {    
            stemmerService1.stemmAnchors(testSKFile);
            
//            stemmerService2.stemmAnchors(testCZFile);
    }
}
