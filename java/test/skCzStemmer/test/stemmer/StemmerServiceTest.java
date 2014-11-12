package skCzStemmer.test.stemmer;

import java.io.File;

import org.testng.annotations.Test;

import skCzStemmer.services.stemmer.StemmerDefaultService;
import skCzStemmer.utils.MyFilePaths;

public class StemmerServiceTest{

    private File testFile = new File(MyFilePaths.DATALOCATION + "test.test");
    
    private StemmerDefaultService stemmerService = new StemmerDefaultService(100);
    
    @Test
    public void processAnchors(){

        try {
            stemmerService.processAnchors(testFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
