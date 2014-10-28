package skCzStemmer.test.extractor;

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
public class MediaWikiExtractorTest extends AbstractTestNGSpringContextTests{
    
    @Autowired
    private ExtractorService extractorService;
    
    @Test
    public ConcurrentHashMap<String, String> extractMediaWikiAnchors() throws Exception {
        ConcurrentHashMap<String, String> anchorMap = extractorService.extractMediaWikiAnchors(new File(MyFileUtils.SAMPLEDATA));
        
        Writer writer = new FileWriter("MediaWikiExtractorTest.test");
        for(String s : anchorMap.keySet()){
        writer.write(s + " === " +anchorMap.get(s));
        writer.write("\r\n");
        }
        writer.close();
        
        Assert.assertTrue(anchorMap.size() > 0);
        return null;
    }

    
}
