package skCzStemmer.services.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

public class DataDefaultService {

    private File TREE_FILE ;
    
    public DataDefaultService(File filePath){
        this.TREE_FILE = filePath;
    }
    
    public WordTreeItem createTreeFromAnchorFile(File anchorFile) throws IOException {

        File treeFile = TREE_FILE;
        if(treeFile.exists()){
            return unserializeTree(treeFile);
        }
        
        long start = System.currentTimeMillis();
        
        WordTreeItem root = new WordTreeItem();
        BufferedReader br = null;
        String cvsSplitBy = ";";
        String line = "";
        try {
            br = new BufferedReader(new FileReader(anchorFile));
            line = br.readLine();
            while (line != null) {
                String[] words = line.split(cvsSplitBy);
                if(words.length > 3)
                    root = learnTreeLine(words[2], root);
                line = br.readLine();
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
        
        serializeTree(root);
        
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        
        return root;
    }

    private WordTreeItem learnTreeLine(String line, WordTreeItem root) {
            List<String> tokenList = new ArrayList<String>();
            TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
            Tokenizer tokenizer = tokenizerFactory.tokenizer(line.toCharArray(), 0, line.length());
            tokenizer.tokenize(tokenList, new ArrayList<String>());
            for (String s : tokenList) {
                try {
                    createTreeItem(new StringBuilder(s).reverse().toString(), root);
                } catch (IOException | XMLStreamException | JAXBException e) {
                    System.err.println(e);
                }
            }
        return root;
    }

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
    
    private WordTreeItem unserializeTree(File treeFile) {
     // deserialize subtree
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        JAXBContext jaxbContext;
        try {
            reader = factory.createXMLStreamReader(new FileInputStream(TREE_FILE));
            jaxbContext = JAXBContext.newInstance(WordTreeItem.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            
            return  (WordTreeItem) jaxbUnmarshaller.unmarshal(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void serializeTree(WordTreeItem root) {
        JAXBContext jaxbContext = null;
        Marshaller jaxbMarshaller = null;
        try {
            jaxbContext = JAXBContext.newInstance(WordTreeItem.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(root, TREE_FILE);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}