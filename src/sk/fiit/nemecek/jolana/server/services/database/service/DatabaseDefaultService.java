package sk.fiit.nemecek.jolana.server.services.database.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
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
                    marshallDataToXMLSuffixTree(list);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void marshallDataToXMLSuffixTree(KorpusItemList list) throws JAXBException {
        //create dir for suffix trees
        File dir = new File(System.getProperty("user.dir") + MyFileUtils.LETTERSLOCATION + File.separator);
        dir.mkdir();
        String fileName = list.getList().get(0).getChildren().getList().get(0).getPismeno() + ".xml";
        //check if tree exists
        if(new File(dir.getAbsolutePath() + fileName).exists()){
            new File(dir.getAbsolutePath() + fileName).delete();
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItemList.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(list, new File(dir.getAbsolutePath() + File.separator + fileName));
        
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
                    item = findSubTreeXMLByLetter(character);
                } catch ( IOException | XMLStreamException | JAXBException e) {
                    e.printStackTrace();
                }
                // if we cannot find end char return as unknown
                    if (item == null) {
                    KorpusItem it = new KorpusItem();
                    it.setEnd(true);
                    it.setPismeno(character);
                    it.setProbability(1);
                    item = it;
                }else{
                    item.setProbability(item.getProbability() + 1);
                }
                    root = new KorpusItem();
                    root.getChildren().addItem(item);
               
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

    private KorpusItem findSubTreeXMLByLetter(String character) throws IOException, XMLStreamException, JAXBException{
        String[] extensions = {"xml"};
        Collection<File> files = FileUtils.listFiles(new File(System.getProperty("user.dir") + MyFileUtils.LETTERSLOCATION + File.separator), extensions, false);
        for(File f: files){
            if((character + ".xml").equals(f.getName())){
                
                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(f));
                JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItemList.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                KorpusItemList list = (KorpusItemList) jaxbUnmarshaller.unmarshal(reader);
                return list.getList().get(0).getChildren().getList().get(0);
            }
        }
        return null;
    }
}
