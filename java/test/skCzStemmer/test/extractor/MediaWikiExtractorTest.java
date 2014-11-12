package skCzStemmer.test.extractor;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.utils.MyFilePaths;

public class MediaWikiExtractorTest{
    
//    private File f = new File(MyFilePaths.SAMPLE_SK_DATA_XML);
//    private MediaWikiExtractor extractorService = new MediaWikiExtractor(100, f);
    
    private File full_f = new File(MyFilePaths.FULL_SK_DATA_XML);
    private MediaWikiExtractor extractorService = new MediaWikiExtractor(1000, full_f);
    
    
    @Test
    public void extractMediaWikiAnchors() throws Exception {

        extractorService.extractMediaWikiAnchorsFromAnchorFile();
//        extractorService.extractMediaWikiAnchors(full_f);
//        BufferedReader br = new BufferedReader(new FileReader(f.getName().substring(0, f.getName().lastIndexOf(".xml") + 4)  + ".output"));
//        while ( br.readLine() != null) {
//            count++;
//        }
//        br.close();
//        Assert.assertTrue(count == 1163);
    }
}
