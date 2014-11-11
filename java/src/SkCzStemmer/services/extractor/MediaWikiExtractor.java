package skCzStemmer.services.extractor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import skCzStemmer.utils.MyFilePaths;

public class MediaWikiExtractor {

    private int threadCount;
    private File fileToProcess;
    private File anchorFile;

    private Set<String> ownerSet = new HashSet<String>();

    public MediaWikiExtractor(int threads, File fileToProcess) {
        this.threadCount = threads;
        this.fileToProcess = fileToProcess;
        anchorFile = new File(MyFilePaths.DATALOCATION + File.separator + fileToProcess.getName().substring(0, fileToProcess.getName().lastIndexOf(".xml")) + ".anchor");
    }

    public void extractMediaWikiAnchorsFromAnchorFile() throws Exception {
        if (!fileToProcess.canRead()) {
            return;
        }
        long start = System.currentTimeMillis();

        ExecutorService exService = Executors.newCachedThreadPool();
        List<Future<?>> runnables = new ArrayList<Future<?>>();

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
                        while (it.hasNext()) {
                            Future<?> future = (Future<?>) it.next();
                            if (future.isDone()) {
                                it.remove();
                            }
                        }
                        Thread.sleep(100);
                    }
                }
            }
        }

        // wait for all threads
        while (true) {
            Iterator<Future<?>> it = runnables.iterator();
            while (it.hasNext()) {
                Future<?> future = (Future<?>) it.next();
                if (future.isDone()) {
                    it.remove();
                }
            }
            if (runnables.size() == 0) {
                break;
            }
        }

        long end = System.currentTimeMillis();
        System.out.println(end - start);
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
                    for (String key : dataMap.keySet()) {
                        if (ownerSet.contains(key) == false) {
                            ownerSet.add(key);
                        }
                        for (String line : dataMap.get(key)) {
                            anchorWriter.write(line);
                            anchorWriter.write("\r\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    try {
                        anchorWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private HashMap<String, List<String[]>> extractTextFromMediaWikiPage(String text, String pageName) {
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
                    if (term.contains("[")) {
                        if (charCounter == null)
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
                            // clean left site and right site of anchorText
                            String anchorText = cleanAnchorText(dirtyAnchorText);
                            String pureAnchorText = purifyAnchorText(parseAnchorText(anchorText));
                            String ownerAnchorText = "";
                            if (anchorText.length() > 0) {
                                ownerAnchorText = purifyAnchorOwnerName(anchorText);
                            }
                            String data = ownerAnchorText + ";" + pageName + ";" + pureAnchorText + ";" + anchorText;
                            if (dataMap.get(ownerAnchorText) == null) {
                                dataMap.put(ownerAnchorText, new ArrayList<String>());
                            }
                            dataMap.get(ownerAnchorText).add(data);
                            dirtyAnchorText = "";
                            charCounter = null;
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
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
                    anchorText = anchorText.subSequence(0, anchorText.lastIndexOf("]")).toString();
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
                ret = a[a.length - 1];
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
            MediaWikiExtractor.printErrorMessage("processAnchorText " + anchorText1);
        }
        return "";
    }
}