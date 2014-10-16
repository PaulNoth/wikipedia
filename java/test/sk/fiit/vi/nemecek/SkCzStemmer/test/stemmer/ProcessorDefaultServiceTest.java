package sk.fiit.vi.nemecek.SkCzStemmer.test.stemmer;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import sk.fiit.vi.nemecek.SkCzStemmer.services.extractor.ExtractorService;
import sk.fiit.vi.nemecek.SkCzStemmer.services.stemmer.ProcessorService;
import sk.fiit.vi.nemecek.SkCzStemmer.utils.MyFileUtils;

@Test
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ProcessorDefaultServiceTest extends AbstractTestNGSpringContextTests{

    @Autowired
    private ProcessorService processorService;
    @Autowired
    private ExtractorService extractorService;
    
    @Test
    public String processText() throws Exception {
        
       ConcurrentHashMap<String, String> map = extractorService.extractMediaWikiAnchors(new File(MyFileUtils.LETTERSLOCATION + "test.test"));
       for(String key : map.keySet()){
           System.out.print(key + "     ");
           System.out.println(processorService.processText(map.get(key)));
       }
       
        return null;
    }
}
