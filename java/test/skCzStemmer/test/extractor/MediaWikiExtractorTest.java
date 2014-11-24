package skCzStemmer.test.extractor;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.utils.MyFilePaths;

public class MediaWikiExtractorTest{
    
    private static final boolean SAMPLE = true;
    private static final boolean SK = false;
    private static final boolean CZ = false;
    
    private File sampleDump = new File(MyFilePaths.SAMPLE_SK_DATA_XML);
    private MediaWikiExtractor extractorService1 = new MediaWikiExtractor(100, sampleDump);
    
    private File fullSKDump = new File(MyFilePaths.FULL_SK_DATA_XML);
    private MediaWikiExtractor extractorService2 = new MediaWikiExtractor(1000, fullSKDump);
    
    private File fullCZDump = new File(MyFilePaths.FULL_CZ_DATA_XML);
    private MediaWikiExtractor extractorService3 = new MediaWikiExtractor(1000, fullCZDump);
    
    
    @Test(enabled = SAMPLE)
    public void extractMediaWikiAnchorsSample() throws Exception {
        extractorService1.extractMediaWikiAnchorsFromAnchorFile();
    }
    
    @Test(enabled = SK)
    public void extractMediaWikiAnchorsSK() throws Exception {
        extractorService2.extractMediaWikiAnchorsFromAnchorFile();
    }
    
    @Test(enabled = CZ)
    public void extractMediaWikiAnchorsCZ() throws Exception {
        extractorService3.extractMediaWikiAnchorsFromAnchorFile();
    }
}
