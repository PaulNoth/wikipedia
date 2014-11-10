package skCzStemmer.test.stemmer;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.services.stemmer.StemmerDefaultService;
import skCzStemmer.utils.MyFilePaths;

public class StemmerServiceTest{

    private File sample_f = new File(MyFilePaths.SAMPLE_SK_DATA_XML);
    private File full_f = new File(MyFilePaths.FULL_SK_DATA_XML);
    
    boolean isSampleFile = true;
    
    private StemmerDefaultService stemmerService = new StemmerDefaultService(5);
    
    private MediaWikiExtractor extractorService = new MediaWikiExtractor(5,sample_f);
//    private MediaWikiExtractor extractorService = new MediaWikiExtractor(5,full_f);
    
    @Test
    public void processAnchors(){

        try {
            File sample_file = null;
            if (isSampleFile) {
                String name = sample_f.getName();
                 sample_file = new File(MyFilePaths.DATALOCATION + File.separator + name.substring(0, name.indexOf(".xml")) + ".owner");
            } else {
                String name = sample_f.getName();
                 sample_file = new File(MyFilePaths.DATALOCATION + File.separator + name.substring(0, name.indexOf(".xml")) + ".owner");
            }
            stemmerService.processAnchors(sample_file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
