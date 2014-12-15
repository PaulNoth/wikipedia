package skCzStemmer.test.stemmer;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.stemmer.StemmerDefaultService;
import skCzStemmer.utils.MyFilePaths;

public class StemmerServiceTest {

    private static final boolean SAMPLE = false;
    private static final boolean SK = true;
    private static final boolean CZ = false;
    
    private File testSampleFile = new File(MyFilePaths.DATALOCATION + "testSK.test");
    private File testSKFile = new File(MyFilePaths.DATALOCATION + "testSK.test");
    private File testCZFile = new File(MyFilePaths.DATALOCATION + "testCZ.test");

    private File sampleTreeFile = new File(MyFilePaths.SAMPLE_TREE_FILE);
    private File skTreeFile = new File(MyFilePaths.FULL_SK_TREE_FILE);
    private File czTreeFile = new File(MyFilePaths.FULL_CZ_TREE_FILE);

    private StemmerDefaultService stemmerService1 = new StemmerDefaultService(100, sampleTreeFile);
    private StemmerDefaultService stemmerService2 = new StemmerDefaultService(100, skTreeFile);
    private StemmerDefaultService stemmerService3 = new StemmerDefaultService(100, czTreeFile);

    @Test(enabled = SAMPLE)
    public void stemmAnchorsSample() {    
            stemmerService1.stemmAnchors(testSampleFile);
    }
    
    @Test(enabled = SK)
    public void stemmAnchorsSK() {    
            stemmerService2.stemmAnchors(testSKFile);
    }
    
    @Test(enabled = CZ)
    public void stemmAnchorsCZ() {    
            stemmerService3.stemmAnchors(testCZFile);
    }
}
