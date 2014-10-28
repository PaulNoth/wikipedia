package skCzStemmer.services.stemmer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skCzStemmer.services.database.data.KorpusItem;
import skCzStemmer.services.database.service.DatabaseService;

/**
 * This is implementation of PreprocessorService.
 * 
 * @see sk.fiit.nemecek.jolana.server.services.stemmer.PreprocessorService
 * @author Tomáš Nemeèek
 * 
 */
@Service
public class ProcessorDefaultService implements ProcessorService {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public String processText(String text) throws Exception {

        // tokenize input text
        ClassicTokenizer tokenizer = new ClassicTokenizer(Version.LUCENE_48, new StringReader(text));
        CharTermAttribute charTermAttrib = tokenizer.getAttribute(CharTermAttribute.class);

        tokenizer.reset();
        String ret = "";
        while (tokenizer.incrementToken()) {
            String term = charTermAttrib.toString();
            if (term.matches(".*\\d.*")) {
                ret += term + " ";
            }else{
                ret += processWord(term) + " ";
            }
        }
        tokenizer.end();
        tokenizer.close();
        return ret;
    }

    /**
     * Method for preprocessing of word s at position index. This method return
     * preprocessed word or the original word.
     * 
     * @param String s
     * @return String
     * @throws IOException
     * @throws XMLStreamException
     * @throws JAXBException
     */
    private String processWord(String s) throws JAXBException, XMLStreamException, IOException {
        s = s.toLowerCase();
        if (s.matches("//d+")) {
            return "NUM";
        }

        String processingString = new StringBuffer(s).reverse().toString();
        List<CharacterItem> characters = new ArrayList<CharacterItem>();
        KorpusItem item = null;
        // for all chars in the word
        for (int i = 0; i < processingString.length(); i++) {
            String character = Character.toString(processingString.charAt(i));
            if (i == 0) {
                // search in DB for end char
                item = databaseService.findSubTreeByLetter(character);
                // if we cannot find end char return as unknown
                if (item == null) {
                    return s;
                }
            } else {
                // search for next char
                KorpusItem temp = null;
                for (KorpusItem child : item.getChildren().getList()) {
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
                item = new KorpusItem();
            }
            characters.add(charItem);
        }
        //test debug of computation
//        for (CharacterItem i : characters) {
//            System.out.println(i.getPrecision());
//        }
//        System.out.println();
        
        // compute difference entropy
        characters = recalculateEntropy(characters);
        // compute base of the word
        String output = chooseBase(characters);
        
      //test debug of computation
//        for (CharacterItem i : characters) {
//            System.out.println(i.getPrecision());
//        }
//        System.out.println();
        if (output == null) {
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
     * @param List<CharacterItem> list
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
     * @param KorpusItem item
     * @return double
     */
    private double computeEntropy(KorpusItem item) {
        double finalPrecision = 0;
        for (KorpusItem ki : item.getChildren().getList()) {
            finalPrecision = finalPrecision + (computeSequence(ki.getProbability() / item.getProbability()));
        }
        return (-1) * finalPrecision;
    }

    /**
     * Method for computing the equation p(s|r)log_2p(s|r).
     * @param probability
     * @return
     */
    private double computeSequence(double probability) {
        return probability * log(probability, 2);
    }

    private double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }
    
    /**
     * Class for temporar saving result of preprocessing for each letter in word.
     * @author Tomáš Nemeèek
     */
    public class CharacterItem {

        private double precision;
        private String character;

        public CharacterItem(String s, double entrophy) {
            this.precision = entrophy;
            this.character = s;
        }
        
        public CharacterItem clone(){
            CharacterItem i = new CharacterItem(this.character,this.precision);
            return i;
        }

        @Override
        public String toString() {
            return "Word [precision=" + precision + ", character=" + character + "]";
        }

        public double getPrecision() {
            return precision;
        }
        public void setPrecision(double precision) {
            this.precision = precision;
        }

        public String getCharacter() {
            return character;
        }
        public void setCharacter(String character) {
            this.character = character;
        }
    }
}