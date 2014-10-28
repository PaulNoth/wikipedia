package skCzStemmer.test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import skCzStemmer.services.extractor.ExtractorService;
import skCzStemmer.utils.MyFileUtils;

@Test
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SkCzStemmerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ExtractorService extractorService;

    @Test
    public void processText() throws Exception {
        ConcurrentHashMap<String, String> anchorMap = extractorService.extractMediaWikiAnchors(new File(MyFileUtils.SAMPLEDATA));

        for(String key : anchorMap.keySet()){
            System.out.println(key + "     " +anchorMap.get(key));
        }

        Writer writer = new FileWriter("ProcessorServiceTest.test");
        for (String s : anchorMap.keySet()) {
            writer.write(s + " === " + anchorMap.get(s));
            writer.write("\r\n");
        }
        writer.close();

        Assert.assertTrue(anchorMap.size() > 0);
    }
}
