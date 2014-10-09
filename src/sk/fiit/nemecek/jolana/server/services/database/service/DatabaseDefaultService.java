package sk.fiit.nemecek.jolana.server.services.database.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.springframework.stereotype.Service;

import sk.fiit.nemecek.jolana.server.services.database.data.KorpusItem;
import sk.fiit.nemecek.jolana.server.services.database.data.KorpusItemList;
import sk.fiit.nemecek.jolana.server.utils.MyFileUtils;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

@Service
public class DatabaseDefaultService implements DatabaseService {
    
    @Override
    public void marshallDataToXMLFile(KorpusItemList list) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItemList.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(list, new File(MyFileUtils.FILENAME));
    }

    @Override
    public KorpusItem findSubTreeByLetter(String character) throws JAXBException, XMLStreamException, IOException {
        File f = new File(MyFileUtils.FILENAME);
        if(!f.canRead()){
            return null;
        }
        // find subtree by character
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(MyFileUtils.FILENAME));

        boolean breaking = false;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                if ("letter".equals(reader.getLocalName())) {
                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        switch (i) {
                        case 0:
                            if (character.charAt(0) == new Integer(reader.getAttributeValue(i).trim()).intValue())
                                breaking = true;
                            break;
                        }
                    }
                }
            }
            if(breaking){
                break;
            }
        }
        
        if(breaking == false){
            reader = factory.createXMLStreamReader(new FileInputStream(MyFileUtils.FILENAME));
            reader.next();
        }
        // deserialize subtree
        JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // We had written this file in marshalling example
        KorpusItem item = (KorpusItem) jaxbUnmarshaller.unmarshal(reader);

        // return list;
        return item;
    }
    
    @Override
    public String extractMediaWikiTextForLearning() throws Exception {

        // find subtree by character
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(MyFileUtils.SAMPLEDATA));

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                if ("text".equals(reader.getLocalName())) {
                    //process data to SuffixTree ;
                    prepareSuffixStructure(reader.getElementText());
                }
            }
        }
        return null;
    }

    private KorpusItem prepareSuffixStructure(String text) throws IOException, InterruptedException {
        text = text.replaceAll("\\[\\[Súbor:.+\\]\\]", "");
        List<String> tokenList = new ArrayList<String>();
        TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
        Tokenizer tokenizer = tokenizerFactory.tokenizer(text.toCharArray(), 0, text.length());
        tokenizer.tokenize(tokenList,new ArrayList<String>());
        
        for(String s : tokenList){
            s = s.replaceAll("\\p{Punct}", "");
            s = s.replaceAll("\\p{Digit}", "");
            if(s.length() > 3){
                KorpusItem root = processWord(s);
                KorpusItemList list = new KorpusItemList();
                list.addItem(root);
                try {
                    marshallDataToXMLFile(list);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public KorpusItem findKorpusItemByLetter(String letter) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Method for creating suffix tree.
     * 
     * @param String wordString
     * @return String
     */
    private KorpusItem processWord(String wordString) {
        wordString = wordString.toLowerCase();

        String processingString = new StringBuffer(wordString).reverse().toString();
        KorpusItem root = null;
        KorpusItem item = null;
        // for all chars in the word
        for (int i = 0; i < processingString.length(); i++) {
            String character = Character.toString(processingString.charAt(i));
            if (i == 0) {
                // search in DB for end char
                try {
                    item = findSubTreeByLetter(character);
                } catch (JAXBException | XMLStreamException | IOException e) {
                    e.printStackTrace();
                }
                // if we cannot find end char return as unknown
                if (item == null) {
                    root = new KorpusItem();
                    KorpusItem it = new KorpusItem();
                    it.setEnd(true);
                    it.setPismeno(character);
                    it.setProbability(1);
                    root.getChildren().addItem(it);
                }
                item = root;
            } else {
                // search for next char
                KorpusItem temp = null;
                for (KorpusItem child : item.getChildren().getList()) {
                    if (child.getPismeno().equals(character)) {
                        temp = child;
                        break;
                    }
                }
                if (temp != null) {
                    temp.setProbability(item.getProbability() + 1);
                } else {
                    temp = new KorpusItem();
                    temp.setEnd(false);
                    temp.setPismeno(character);
                    temp.setProbability(1);
                    item.getChildren().addItem(temp);
                }
                item = temp;
            }
        }
        return root;
    }
}
