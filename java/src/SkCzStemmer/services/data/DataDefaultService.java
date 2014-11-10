package skCzStemmer.services.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

public class DataDefaultService {

    public WordTreeItem createTreeFromAnchorFile(String anchorOwner, List<String[]> docs) throws IOException {

        WordTreeItem root = new WordTreeItem();
        root = learnTreeLine( anchorOwner, root);
        for(String[] anchor : docs) {
            root = learnTreeLine(anchor[1], root);
        }
        return root;
    }

    private WordTreeItem learnTreeLine(String line, WordTreeItem root) {
            List<String> tokenList = new ArrayList<String>();
            TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
            Tokenizer tokenizer = tokenizerFactory.tokenizer(line.toCharArray(), 0, line.length());
            tokenizer.tokenize(tokenList, new ArrayList<String>());
            for (String s : tokenList) {
                try {
                    createTreeItem(s, root);
                } catch (IOException | XMLStreamException | JAXBException e) {
                    System.err.println(e);
                }
            }
            
        return root;
    }

    /**
     * Method for creating suffix tree from one word.
     * 
     * @param String word
     * @param WordTreeItem root
     * @return KorpusItem
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws IOException
     */
    private WordTreeItem createTreeItem(String wordString, WordTreeItem root) throws IOException, XMLStreamException, JAXBException {
        if (wordString.matches(".*\\d.*") || Pattern.matches("\\p{Punct}", wordString))
            return root;

        String processingString = wordString.toLowerCase();
        WordTreeItem item = null;
        // for all chars in the word
        for (int i = 0; i < processingString.length(); i++) {
            String character = Character.toString(processingString.charAt(i));
            if (i == 0) {
                // search in DB for end char
                item = findSubTreeByLetter(character, root);
                // if we cannot find end char return as unknown
                if (item == null) {
                    WordTreeItem it = new WordTreeItem();
                    it.setEnd(true);
                    it.setPismeno(character);
                    it.setProbability(1);
                    item = it;
                    root.getChildren().add(item);
                    item.setParent(root);
                } else {
                    item.setProbability(item.getProbability() + 1);
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
                if (temp != null) {
                    temp.setProbability(temp.getProbability() + 1);
                } else {
                    temp = new WordTreeItem();
                    temp.setEnd(false);
                    temp.setPismeno(character);
                    temp.setProbability(1);
                    temp.setParent(item);
                    item.getChildren().add(temp);
                }
                item = temp;
            }
        }
        return root;
    }

    /**
     * Method for searching for subtree in structure
     * 
     * @param character
     * @return WordTreeItem
     * @throws IOException
     * @throws XMLStreamException
     * @throws JAXBException
     */
    public static  WordTreeItem findSubTreeByLetter(String character, WordTreeItem root) throws IOException, XMLStreamException, JAXBException {
        if (root.getChildren().size() != 0)
            for (WordTreeItem item : root.getChildren()) {
                if (character.equals(item.getPismeno())) {
                    return item;
                }
            }
        return null;
    }
}