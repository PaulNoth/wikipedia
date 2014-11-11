package skCzStemmer.services.extractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import skCzStemmer.utils.MyFilePaths;

public class MediaWikiExtractor{

    private int threadCount;
    private File fileToProcess;
    private File anchorFile;
    private static File indexDir;
    
    private Set<String> ownerSet = new HashSet<String>();
    
    public MediaWikiExtractor(int threads, File fileToProcess) {
        this.threadCount = threads;
        this.fileToProcess = fileToProcess;
        anchorFile = new File( MyFilePaths.DATALOCATION + File.separator + fileToProcess.getName().substring(0, fileToProcess.getName().lastIndexOf(".xml")) + ".anchor");
        indexDir = new File(MyFilePaths.INDEX_DIRECTORY + File.separator + anchorFile.getName().subSequence(0, anchorFile.getName().indexOf(".anchor")));
    }
    
    public static List<String[]> searchIndex(String searchString) throws IOException {
        Query query;
        try {
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(indexDir));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new KeywordAnalyzer();
        QueryParser queryParser = new QueryParser("key", analyzer);

            query = queryParser.parse(searchString);
        
        TopFieldDocs results = indexSearcher.search(query, Integer.MAX_VALUE, Sort.INDEXORDER);
        ScoreDoc[] fields = results.scoreDocs;
        
        // content of indexes
//        for (int i=0; i<indexReader.maxDoc(); i++) {
//            Document doc = indexReader.document(i);
//            String docId = doc.get("key");
//            System.out.println(docId);
//        }
        
        //content of searched indexes
//        for(ScoreDoc f : fields){
//            System.out.print(indexSearcher.doc(f.doc).get("anchorOwner") + " ");
//            System.out.println(indexSearcher.doc(f.doc).get("purifiedAnchor"));
//        }
        List<String[]> anchors = new ArrayList<String[]>();
        for(ScoreDoc f : fields){
            String[] doc = new String[2];
            doc[0] =indexSearcher.doc(f.doc).get("anchorOccurence");
            doc[1] =indexSearcher.doc(f.doc).get("purifiedAnchor");
            anchors.add(doc);
        }
        
        return anchors;
        } catch (org.apache.lucene.queryparser.classic.ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    private void createIndex(File fileToIndex) throws CorruptIndexException, LockObtainFailedException, IOException {
        
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexDir), config);
        try {

            br = new BufferedReader(new FileReader(fileToIndex));
            while ((line = br.readLine()) != null) {
                String[] text = line.split(cvsSplitBy);
                //create indexes
                if(text.length > 1){
                    Document document = new Document();
                    String ownerAnchorTextNormalized = Normalizer.normalize(text[0], Normalizer.Form.NFD);
                    ownerAnchorTextNormalized = ownerAnchorTextNormalized.replaceAll("[^\\p{ASCII}]", "");
                    ownerAnchorTextNormalized = ownerAnchorTextNormalized.replaceAll("\\s+","");
                    document.add(new StringField("key", ownerAnchorTextNormalized, Field.Store.YES));
                    document.add(new StringField("anchorOwner", text[0], Field.Store.YES));
                    document.add(new TextField("anchorOccurence", text[1], Field.Store.YES));
                    document.add(new TextField("purifiedAnchor", text[2], Field.Store.YES));
                    document.add(new TextField("originalIndex", text[3], Field.Store.YES));
                    indexWriter.addDocument(document);
                }
            }
            indexWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void extractMediaWikiAnchorsFromAnchorFile() throws Exception {
        if (!fileToProcess.canRead()) {
            return;
        }
        long start = System.currentTimeMillis();
        
        ExecutorService exService = Executors.newCachedThreadPool();
        List<Future<?>> runnables = new ArrayList<Future<?>>(); 
        
        // find subtree by character
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(fileToProcess));

        String pageName = "";
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                if ("title".equals(reader.getLocalName())) {
                    pageName = reader.getElementText();
                }
                if ("text".equals(reader.getLocalName())) {
                    if (runnables.size() < threadCount) {
                        Future<?> future = exService.submit(new ExtractorTask(reader.getElementText(), pageName));
                        runnables.add(future);
                        break;
                    } else {
                        Iterator<Future<?>> it = runnables.iterator();
                        while(it.hasNext()){
                            Future<?> future = (Future<?>) it.next();
                            if(future.isDone()){
                                it.remove();
                            }
                        }
                        Thread.sleep(100);
                    } 
                }
            }
        }
        
        //wait for all threads
        while(true){
            Iterator<Future<?>> it = runnables.iterator();
            while(it.hasNext()){
                Future<?> future = (Future<?>) it.next();
                if(future.isDone()){
                    it.remove();
                }
            }
            if(runnables.size() == 0){
                break;
            }
        }
        
        //create owner file
        BufferedWriter ownerWriter = null;
        try {
            String ownerFileName = anchorFile.getName();
            FileWriter writer = new FileWriter(MyFilePaths.DATALOCATION + File.separator + 
                    ownerFileName.substring(0,ownerFileName.indexOf(".anchor")) + ".owner", true);
            ownerWriter = new BufferedWriter(writer);
            
            for (String ownerName : ownerSet) {
                ownerWriter.write(ownerName);
                ownerWriter.write("\r\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                ownerWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
        
        //index output of processing
        createIndex(anchorFile);
        return;
    }

    class ExtractorTask implements Runnable {

        private String readerText;
        private String pageName;
        private Map<String, List<String>> dataMap = new HashMap<String, List<String>>();

        public ExtractorTask(String reader, String pageName) {
            this.readerText = reader;
            this.pageName = pageName;
        }

        @Override
        public void run() {
                extractTextFromMediaWikiPage(readerText, pageName);
                
                synchronized (anchorFile) {
                    BufferedWriter anchorWriter = null;
                    try { 
                        FileWriter writer = new FileWriter(anchorFile, true);
                        anchorWriter = new BufferedWriter(writer);
                        for(String key :dataMap.keySet()){
                            if( ownerSet.contains(key) == false){
                                ownerSet.add(key);
                            }
                            for(String line: dataMap.get(key)){
                                anchorWriter.write(line);
                                anchorWriter.write("\r\n");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }finally{
                        try {
                            anchorWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        }

        private HashMap<String, List<String[]>> extractTextFromMediaWikiPage(String text, String pageName){
            TokenStream ts = null;
            Tokenizer tokenizer = null;
            try {
            StringReader in = new StringReader(text);
            tokenizer = new WhitespaceTokenizer(in);
            ts = tokenizer;
            CharTermAttribute charTermAttribute = ts.getAttribute(CharTermAttribute.class);
            ts.reset();
            Integer charCounter = null;
            String dirtyAnchorText = "";
            while (ts.incrementToken()) { // loop over tokens
                String term = charTermAttribute.toString();
                try{
                if (term.contains("[")) {
                    if(charCounter == null )
                        charCounter = 0;
                    charCounter += StringUtils.countMatches(term, "[");
                }
                if (charCounter != null && charCounter != 0) {
                    dirtyAnchorText += " " + term;
                }
                if (term.contains("]") && charCounter != null) {
                    charCounter -= StringUtils.countMatches(term, "]");
                }
                if (charCounter != null && charCounter == 0) {
                    if (dirtyAnchorText.length() > 2) {
                        //clean left site and right site of anchorText
                        String anchorText = cleanAnchorText(dirtyAnchorText);
                        String pureAnchorText = purifyAnchorText(parseAnchorText(anchorText));
                        String ownerAnchorText = "";
                        if (anchorText.length() > 0) {
                            ownerAnchorText = purifyAnchorOwnerName(anchorText);
                            }
                            String data = ownerAnchorText + ";" +pageName + ";" + pureAnchorText + ";" + anchorText;
                            if(dataMap.get(ownerAnchorText) == null){
                                dataMap.put(ownerAnchorText, new ArrayList<String>());
                            }
                            dataMap.get(ownerAnchorText).add(data);
                            dirtyAnchorText = "";
                            charCounter = null;
                        }
                    }
            }catch(Exception e2){
                e2.printStackTrace();
            }
            }
            } catch (Exception e1) {
                e1.printStackTrace();
            }finally{
                try {
                    ts.close();
                    tokenizer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               
            } 
            return null;
        }
        
        private String cleanAnchorText(String dirtyAnchorText) {
            // cleaning left site of anchor text
            String anchorTextLeft = dirtyAnchorText.substring(dirtyAnchorText.indexOf("["));
            // cleaning right site of anchor text
            String endAnchorText = anchorTextLeft.substring(anchorTextLeft.lastIndexOf("]") + 1);
            int border = 0;
            if (endAnchorText.length() > 0) {
                for (int j = 0; j < endAnchorText.length(); j++) {
                    String originalCh = Character.toString(endAnchorText.charAt(j));
                    String character = Normalizer.normalize(originalCh, Normalizer.Form.NFD);
                    character = character.replaceAll("[^\\p{ASCII}]", "");
                    character = character.replaceAll("\\s+", "");

                    if (!character.matches("[a-zA-Z]")) {
                        border = j;
                        break;
                    }
                    border++;
                }
            }
            return anchorTextLeft.substring(0, anchorTextLeft.lastIndexOf("]") + 1) + endAnchorText.substring(0, border);
        }

        private String parseAnchorText(String anchorText1) {
            try {

                if (anchorText1.indexOf("[[") != -1) {
                    String anchorText = anchorText1;
                    anchorText = anchorText.substring(anchorText.indexOf("[[") + 2);
                    String suffix = anchorText.subSequence(anchorText.lastIndexOf("]]") + 2, anchorText.length()).toString();
                    anchorText = anchorText.subSequence(0, anchorText.lastIndexOf("]]")).toString();
                    String ret = anchorText + suffix;
                    return ret;
                }
                if (anchorText1.indexOf("[") != -1) {
                    String anchorText = anchorText1;
                    anchorText = anchorText.substring(anchorText.indexOf("[") + 1);
                    String suffix = anchorText.subSequence(anchorText.lastIndexOf("]") + 1, anchorText.length()).toString();
                    anchorText = anchorText.subSequence(0, anchorText.lastIndexOf("]") ).toString();
                    String[] suffixParts = suffix.split("\\P{Alpha}+");
                    if (suffixParts.length > 0) {
                        anchorText = anchorText + suffixParts[0];
                    }
                    return anchorText;
                }

            } catch (Exception e) {
                printErrorMessage("parseAnchorText " + anchorText1);
            }
            return "";
        }
    }

    private static void printErrorMessage(String anchorText) {
        if (!anchorText.contains("http"))
            if (!anchorText.contains("irc"))
                if (!anchorText.contains("news"))
                    if (!anchorText.contains("mailto"))
                        System.err.println(anchorText);
    }
    
    private static String purifyAnchorOwnerName(String anchorText1) {
        try {
            String anchorText = anchorText1;
            anchorText = anchorText.substring(anchorText.indexOf("["));
            String ret = "";
            String[] a = anchorText.split("\\|");
            if (a.length > 1) {
                a[0] = a[0].substring(a[0].indexOf("["));
                a[0] = a[0].replaceAll("\\]", "");
                a[0] = a[0].replaceAll("\\[", "");
                ret = a[0];
            } else {
                String[] b = anchorText.split("\\:");
                if (b.length > 1) {
                        b[b.length - 1] = b[b.length - 1].replaceAll("\\]", "");
                        b[b.length - 1] = b[b.length - 1].replaceAll("\\[", "");
                        ret = b[b.length - 1];
                } else {
                    b[0] = b[0].replaceAll("\\]", "");
                    b[0] = b[0].replaceAll("\\[", "");
                    ret = b[0];
                }
            }
            return ret;
        } catch (Exception e) {
            MediaWikiExtractor.printErrorMessage("processAnchorOwnerName " + anchorText1);
        }
        return "";
    }

    public static String purifyAnchorText(String anchorText1) {
        try {
            String anchorText = anchorText1;
            String[] a = anchorText.split("\\|");
            String ret = "";
            if (a.length > 1) {
                    a[a.length - 1] = a[a.length - 1].replaceAll("\\]", "");
                    a[a.length - 1] = a[a.length - 1].replaceAll("\\[", "");
//                    a[a.length - 1] = a[a.length - 1].replaceAll("[(){},.;!?<>%\\„\\“\\']", "");
                    ret = a[a.length - 1];
            } else {
                String[] b = anchorText.split("\\:");
                if (b.length > 1) {
                        b[b.length - 1] = b[b.length - 1].replaceAll("\\]", "");
                        b[b.length - 1] = b[b.length - 1].replaceAll("\\[", "");
//                        b[b.length - 1] = b[b.length - 1].replaceAll("[(){},.;!?<>%\\„\\“\\']", "");
                        ret = b[b.length - 1];
                } else {
                    b[0] = b[0].replaceAll("\\]", "");
                    b[0] = b[0].replaceAll("\\[", "");
//                    b[0] = b[0].replaceAll("[(){},.;!?<>%\\„\\“\\']", "");
                    ret = b[0];
                }
            }

            return ret;
        } catch (Exception e) {
            MediaWikiExtractor.printErrorMessage("processAnchorText " + anchorText1);
        }
        return "";
    }
}