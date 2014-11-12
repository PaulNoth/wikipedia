package skCzStemmer.services.stemmer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import skCzStemmer.services.data.DataDefaultService;
import skCzStemmer.services.data.WordTreeItem;
import skCzStemmer.utils.MyFilePaths;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

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

    public void processAnchors(File testFile) throws InterruptedException, IOException {

        WordTreeItem root = DataDefaultService.unserializeTree(new File(MyFilePaths.FULL_TREE_FILE));
//        WordTreeItem root = DataDefaultService.unserializeTree(new File(MyFilePaths.SAMPLE_TREE_FILE));

        ExecutorService exService = Executors.newCachedThreadPool();
        List<Future<?>> runnables = new ArrayList<Future<?>>();

        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(testFile));
            line = br.readLine();
            while (line != null) {
                if (runnables.size() < threadCount) {
                    Future<?> future = exService.submit(new AnchorStemmerTask(line, root));
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
    private String processWord(String s, WordTreeItem root) {

        s = s.toLowerCase();
        if (s.matches("//d+")) {
            return "NUM";
        }

        String processingString = new StringBuffer(s).reverse().toString();
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
//                 item = DataDefaultService.findSubTreeByLetter(item.getId());
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
         characters = recalculateEntropy(characters);
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
        for (int i = 0; i < characters.size() - 1; i++) {
            outList.get(i).setPrecision(characters.get(i).getPrecision() - characters.get(i + 1).getPrecision());
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
        int j = 0;

        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).getPrecision() < list.get(i + 1).getPrecision()) {
                j = i;
                break;
            }
        }
        for (int i = j + 1; i < list.size(); i++) {
            base.append(list.get(i).getCharacter());
        }

        return base.reverse().toString();
    }

    /**
     * Method for computing final entropy of each letter.
     * 
     * @param WordTreeItem
     *            item
     * @return double
     */
    private double computeEntropy(WordTreeItem item) {
        double finalPrecision = 0;
        double itemProbability = Double.valueOf(item.getProbability());
        for (WordTreeItem ki : item.getChildren()) {
            double childProbability = Double.valueOf(ki.getProbability()); 
            finalPrecision = finalPrecision + (computeSequence(childProbability / itemProbability));
        }
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

        private String line;
        private WordTreeItem root;

        public AnchorStemmerTask(String line, WordTreeItem root) {
            this.line = line;
            this.root = root;
        }

        public void run() {
            List<String> retList = new ArrayList<String>();
            if (line.length() > 2) {
                // tokenize input text
                List<String> tokenList = new ArrayList<String>();
                TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
                Tokenizer tokenizer = tokenizerFactory.tokenizer(line.toCharArray(), 0, line.length());
                tokenizer.tokenize(tokenList, new ArrayList<String>());

                String ret = "";
                for(String word : tokenList){
                    if (word.matches(".*\\d.*")) {
                        ret += word + " ";
                    } else {
                        ret += processWord(word, root) + " ";
                    }
                }
                retList.add(line  + ";" + ret) ;
            }

            synchronized (output) {
                BufferedWriter outputWriter = null;
                try {
                    FileWriter writer = new FileWriter(MyFilePaths.DATALOCATION + "output.output", true);
                    outputWriter = new BufferedWriter(writer);
                    for (String ret : retList) {
                        outputWriter.write(ret + "\r\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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