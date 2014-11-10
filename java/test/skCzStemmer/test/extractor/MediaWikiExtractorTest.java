package skCzStemmer.test.extractor;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.utils.MyFilePaths;

public class MediaWikiExtractorTest{
    
    private File f = new File(MyFilePaths.SAMPLE_SK_DATA_XML);
    private File full_f = new File(MyFilePaths.FULL_SK_DATA_XML);
    
    private MediaWikiExtractor extractorService = new MediaWikiExtractor(100, f);
//    private MediaWikiExtractor extractorService = new MediaWikiExtractor(1000, full_f);
    
    private static final boolean ENABLE = false;
    
    @Test(enabled=ENABLE)
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
    
    @Test(enabled=!ENABLE)
    public void processExtractedAnchors(){
        try {
            MediaWikiExtractor.searchIndex("zidiaetnikum");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
