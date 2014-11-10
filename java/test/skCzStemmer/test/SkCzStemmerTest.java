package skCzStemmer.test;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.utils.MyFilePaths;

public class SkCzStemmerTest {

    private File f = new File(MyFilePaths.SAMPLE_SK_DATA_XML);
    private File full_f = new File(MyFilePaths.FULL_SK_DATA_XML);
    
    private MediaWikiExtractor extractorService = new MediaWikiExtractor(100,f);

    @Test
    public void processText() throws Exception {
        extractorService.extractMediaWikiAnchorsFromAnchorFile();

    }
}
