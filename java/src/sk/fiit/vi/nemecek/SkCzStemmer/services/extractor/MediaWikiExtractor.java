package sk.fiit.vi.nemecek.SkCzStemmer.services.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import sk.fiit.vi.nemecek.SkCzStemmer.services.stemmer.ProcessorService;

@Component
public class MediaWikiExtractor implements ExtractorService {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private ProcessorService processorService;

    private ConcurrentHashMap<String, String> anchorMap = new ConcurrentHashMap<String, String>();

    @Override
    public ConcurrentHashMap<String, String> extractMediaWikiAnchors(File f) throws Exception {
        if (!f.canRead()) {
            return null;
        }
        // find subtree by character
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(f));

        Integer pageCounter = 0;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                if ("text".equals(reader.getLocalName())) {
                    int count = taskExecutor.getActiveCount();
                    if (count < taskExecutor.getMaxPoolSize()) {
                        pageCounter++;
                        taskExecutor.execute(new ProcessTask(reader.getElementText()));
                        break;
                    } else {
                        Thread.sleep(1000);
                    }
                }
            }
        }
        
//      wait for all threads to finish
      while (taskExecutor.getActiveCount() > 0){
          Thread.sleep(1000);
      }
        return anchorMap;
    }

    /**
     * Method for extracting pure text from anchor text
     * @param text
     * @throws Exception
     */
    private void processPage(String text) throws Exception {
        Analyzer analyzer = new WhitespaceAnalyzer(Version.LATEST);
        StringReader in = new StringReader(text);
        TokenStream ts = analyzer.tokenStream(null, in);
        CharTermAttribute charTermAttribute = ts.getAttribute(CharTermAttribute.class);
        ts.reset();
        int charCounter = 0;
        String anchorText = "";
        while (ts.incrementToken()) { // loop over tokens
            String term = charTermAttribute.toString();
            if (term.contains("[")) {
                charCounter += StringUtils.countMatches(term, "[");
            }
            if (charCounter != 0) {
                anchorText += " " + term;
            }
            if (term.contains("]")) {
                charCounter -= StringUtils.countMatches(term, "]");
            }
            if (charCounter == 0) {
                if (anchorText.length() > 0) {
                    int start = anchorText.indexOf("[");
                    int end = anchorText.lastIndexOf("]") + 1;
                    anchorText = anchorText.substring(start, end);
                    if(
                       anchorText.contains("mailto:") ||
                       anchorText.contains(":meta:") ||
                       anchorText.contains("de:") ||
                       anchorText.contains("th:")){
                        continue;
                    }
                    String valueText = "";
                    //PATTERN1 [link text1]
                    if(anchorText.contains("http")){
                        valueText = processPattern1(anchorText);
                    }
                    //PATTERN2 [[text1|]]
                    //PATTERN3 [[text1|text2]]
                    else if(anchorText.matches("\\[(?!http).*\\|.*\\]")){
                        valueText = processPattern3(anchorText);
                    }
                    //PATTERN4 [text1:text2]
                    else if(anchorText.matches("\\[(?!http).*:.*\\]")){
                        valueText = processPattern5(anchorText);
                    }
                    //PATTERN4 [[text1]] 
                    else if(anchorText.matches("\\[(?!http).*\\]")){
                        valueText = processPattern4(anchorText);
                    }
                    
                    synchronized (anchorMap) {
                        anchorMap.put(anchorText, processorService.processText(valueText));
                  }
                }
                anchorText = "";
            }
        }
        ts.close();
    }

    private String processPattern1(String anchorText) {
        String ret = "";
        String[] a = anchorText.split(" ");
        for(int i = 1; i < a.length; i ++){
            a[i] = a[i].replaceAll("]", "");
            ret += a[i] + " ";
        }
        return ret;
    }
    
    private String processPattern3(String anchorText) {
        String ret = "";
        String[] a = anchorText.split("\\|");
        for(int i =0 ; i < a.length; i++){
            if(a[i].length() > 0 && !a[i].contains("Súbor") && !a[i].contains("File")){
                a[i] = a[i].replaceAll("\\]", "");
                a[i] = a[i].replaceAll("\\[", "");
                ret +=  a[i]+ " ";
            }
        }
        return ret;
    }
    
    private String processPattern4(String anchorText) {
        String ret = "";
        String[] a = anchorText.split("\\p{Punct}");
        for(int i =0 ; i < a.length; i++){
            if(a[i].length() > 0)
                ret += a[i] + " ";
        }
        return ret;
    }
    
    private String processPattern5(String anchorText) {
        String ret = "";
        String[] a = anchorText.split("\\:");
        for(int i =0 ; i < a.length; i++){
            if(a[i].length() > 0){
                a[i] = a[i].replaceAll("\\]", "");
                a[i] = a[i].replaceAll("\\[", "");
                ret +=  a[i]+ " ";
            }
        }
        return ret;
    }

    class ProcessTask implements Runnable {

        private String readerText;
        
        public ProcessTask(String reader) {
            this.readerText = reader;
        }

        @Override
        public void run() {
            try {
                processPage(readerText);
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }
}
