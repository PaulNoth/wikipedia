package skCzStemmer.test.extractor;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.utils.MyFilePaths;

public class MediaWikiExtractorTest{
    
    private File sampleDump = new File(MyFilePaths.SAMPLE_SK_DATA_XML);
    private MediaWikiExtractor extractorService1 = new MediaWikiExtractor(100, sampleDump);
    
    private File fullSKDump = new File(MyFilePaths.FULL_SK_DATA_XML);
    private MediaWikiExtractor extractorService2 = new MediaWikiExtractor(1000, fullSKDump);
    
    private File fullCZDump = new File(MyFilePaths.FULL_CZ_DATA_XML);
    private MediaWikiExtractor extractorService3 = new MediaWikiExtractor(1000, fullCZDump);
    
    
    @Test
    public void extractMediaWikiAnchors() throws Exception {

//        extractorService1.extractMediaWikiAnchorsFromAnchorFile();
        
//        extractorService2.extractMediaWikiAnchorsFromAnchorFile();
        
        extractorService3.extractMediaWikiAnchorsFromAnchorFile();
    }
}
