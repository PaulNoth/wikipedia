package skCzStemmer.services.stemmer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import skCzStemmer.services.data.DataDefaultService;
import skCzStemmer.services.data.WordTreeItem;
import skCzStemmer.services.extractor.MediaWikiExtractor;
import skCzStemmer.utils.MyFilePaths;

/**
 * This is implementation of PreprocessorService.
 * 
 * @see sk.fiit.nemecek.jolana.server.services.stemmer.PreprocessorService
 * @author Tomáš Nemeèek
 * 
 */
public class StemmerDefaultService {

    private int threadCount;
    private File output = new File(MyFilePaths.DATALOCATION + File.separator + "anchors.output");

    public StemmerDefaultService(int threadCount) {
        this.threadCount = threadCount;
    }

    public void processAnchors(File anchorOwnerFile) throws InterruptedException, IOException {

        ExecutorService exService = Executors.newCachedThreadPool();
        List<Future<?>> runnables = new ArrayList<Future<?>>();

        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(anchorOwnerFile));
            line = br.readLine();
            while (line != null) {
                if (runnables.size() < threadCount) {
                    Future<?> future = exService.submit(new AnchorStemmerTask(line));
                    runnables.add(future);
                    line = br.readLine();
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
    }

    /**
     * Method for preprocessing of word s at position index. This method return
     * preprocessed word or the original word.
     * 
     * @param String
     *            s
     * @return String
     * @throws IOException
     * @throws XMLStreamException
     * @throws JAXBException
     */
    private String processWord(String s, WordTreeItem root) throws JAXBException, XMLStreamException, IOException {
        // TODO upravit prepocet entropie
        s = s.toLowerCase();
        if (s.matches("//d+")) {
            return "NUM";
        }

        String processingString = new StringBuffer(s).toString();
        List<CharacterItem> characters = new ArrayList<CharacterItem>();
        WordTreeItem item = null;
        // for all chars in the word
        for (int i = 0; i < processingString.length(); i++) {
            String character = Character.toString(processingString.charAt(i));
            if (i == 0) {
                // search in DB for end char
                item = DataDefaultService.findSubTreeByLetter(character, root);
                // if we cannot find end char return as unknown
                if (item == null) {
                    return s;
                }
            } else {
                // search for next char
                WordTreeItem temp = null;
                for (WordTreeItem child : item.getChildren()) {
                    if (child.getPismeno().equals(character)) {
                        temp = child;
                        break;
                    }
                }
                item = temp;
            }

            CharacterItem charItem = null;
            if (item != null) {
                // set item into character object
                // item = databaseService.findSubTreeByLetter(item.getId());
                charItem = new CharacterItem(character, computeEntropy(item));
            } else {
                // if we cannot find item in DB create mock object
                charItem = new CharacterItem(character, 10);
                item = new WordTreeItem();
            }
            characters.add(charItem);
        }
        // test debug of computation
        // for (CharacterItem i : characters) {
        // System.out.println(i.getPrecision());
        // }
        // System.out.println();

        // compute difference entropy
//        characters = recalculateEntropy(characters);
        // compute base of the word
        String output = chooseBase(characters);

        // test debug of computation
        // for (CharacterItem i : characters) {
        // System.out.println(i.getPrecision());
        // }
        // System.out.println();
        if (output == null || output.length() == 0) {
            output = s;
        }
        return output;
    }

    /**
     * Method for determining the root of the word. Input of this method is list
     * of words letters with the probability value. It returns the choosen root
     * of the word.
     * 
     * @param List
     *            <CharacterItem>characters
     * @return List<CharacterItem>
     */
    private List<CharacterItem> recalculateEntropy(List<CharacterItem> characters) {
        List<CharacterItem> outList = new ArrayList<CharacterItem>();
        // copy input list for modifications
        for (CharacterItem i : characters) {
            outList.add(i.clone());
        }
        // compute entropy
        for (int i = characters.size() - 1; i > 0;  i--) {
            outList.get(i).setPrecision(characters.get(i).getPrecision() - characters.get(i - 1).getPrecision());
        }
        return outList;
    }

    /**
     * Method for choosing the base of the word. It returns the root of the word
     * as String.
     * 
     * @param List
     *            <CharacterItem> list
     * @return String
     */
    private String chooseBase(List<CharacterItem> list) {
        StringBuilder base = new StringBuilder();
        int k = 0;

        for (int i = 1; i < list.size() - 1; i++) {
            CharacterItem currentItem = list.get(i);
            CharacterItem nextItem = list.get(i + 1);
            if (currentItem.getPrecision() < nextItem.getPrecision()) {
                k = i + 1;
                break;
            }
        }
        for (int i = 0; i < k; i++) {
            base.append(list.get(i).getCharacter());
        }

        return base.toString();
    }

    /**
     * Method for computing final entropy of each letter.
     * 
     * @param WordTreeItem
     *            item
     * @return double
     */
    private double computeEntropy(WordTreeItem item) {
        double probability = new Double(item.getProbability()) / new Double(item.getParent().getProbability());
        double finalPrecision = computeSequence(probability);
        return (-1) * finalPrecision;
    }

    /**
     * Method for computing the equation p(s|r)log_2p(s|r).
     * 
     * @param probability
     * @return
     */
    private double computeSequence(double probability) {
        return probability * log(probability, 2);
    }

    private double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }

    class AnchorStemmerTask implements Runnable {

        private String anchorKey;
        private DataDefaultService dataService = new DataDefaultService();

        public AnchorStemmerTask(String anchorKey) {
            this.anchorKey = anchorKey;
        }

        public void run() {
            try {
                String ownerAnchorTextNormalized = Normalizer.normalize(anchorKey, Normalizer.Form.NFD);
                ownerAnchorTextNormalized = ownerAnchorTextNormalized.replaceAll("[^\\p{ASCII}]", "");
                ownerAnchorTextNormalized = ownerAnchorTextNormalized.replaceAll("\\s+", "");

                List<String[]> docs = MediaWikiExtractor.searchIndex(ownerAnchorTextNormalized);
                if(docs != null){
                    WordTreeItem root = dataService.createTreeFromAnchorFile(anchorKey, docs);
                    if (root.getChildren().size() > 0) {
                        processText(root, anchorKey, docs);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void processText(WordTreeItem root, String owner, List<String[]> docs) throws Exception {
            Map<String, Boolean> helpMap = new HashMap<String, Boolean>();
            for (String[] doc : docs) {
                if (doc[1].trim().length() > 0) {
                    if(helpMap.get(doc[1]) == null){
                        helpMap.put(doc[1], Boolean.FALSE);
                    }
                }
            }
            
            List<String> retList = new ArrayList<String>();
            for (String[] doc : docs) {
                if (doc[1].trim().length() > 0) {
                    if(helpMap.get(doc[1]) == Boolean.FALSE){
                        // tokenize input text
                        ClassicTokenizer tokenizer = new ClassicTokenizer(new StringReader(doc[1]));
                        CharTermAttribute charTermAttrib = tokenizer.getAttribute(CharTermAttribute.class);
    
                        tokenizer.reset();
                        String ret = "";
                        while (tokenizer.incrementToken()) {
                            String term = charTermAttrib.toString();
                            if (term.matches(".*\\d.*")) {
                                ret += term + " ";
                            } else {
                                ret += processWord(term, root) + " ";
                            }
                        }
                        // System.out.println(owner + " | " + doc[1] + " | " + ret);
                        retList.add(owner + ";" + doc[0] + ";" + doc[1] + ";" + ret);
                        tokenizer.end();
                        tokenizer.close();
                        helpMap.put(doc[1], Boolean.TRUE);
                    }
                }
            }

            synchronized (output) {
                FileWriter outputWriter = null;
                try {
                    outputWriter = new FileWriter(output, true);
                    for (String output : retList) {
                        outputWriter.write(output);
                        outputWriter.write("\r\n");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    try {
                        outputWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}