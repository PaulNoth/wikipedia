package sk.fiit.vi.nemecek.SkCzStemmer.services.database.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import sk.fiit.vi.nemecek.SkCzStemmer.services.database.data.KorpusItem;
import sk.fiit.vi.nemecek.SkCzStemmer.utils.MyFileUtils;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

@Service
public class DatabaseDefaultService implements DatabaseService {

    /**
     * Method for searching subtree by letter in suffix tree xml(letters.xml).
     * @param String character
     * @return KorpusItem
     * @throws JAXBException
     * @throws XMLStreamException
     * @throws IOException
     */
    @Override
    public KorpusItem findSubTreeByLetter(String character) throws JAXBException, XMLStreamException, IOException {
        File f = new File(MyFileUtils.LETTERSLOCATION + MyFileUtils.LETTERSFILENAME);
        if (!f.canRead()) {
            return null;
        }
        // find subtree by character
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(MyFileUtils.LETTERSLOCATION + MyFileUtils.LETTERSFILENAME));

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
            if (breaking) {
                break;
            }
        }

        // deserialize subtree
        JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItem.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        // We had written this file in marshalling example
        KorpusItem item = (KorpusItem) jaxbUnmarshaller.unmarshal(reader);
        return item;
    }

    /**
     * Method for preparing train data for stemming from MediaWiki page-text
     */
    @Override
    public void extractMediaWikiTextForLearning() throws Exception {

        // find subtree by character
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(MyFileUtils.SAMPLEDATA));

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
            case XMLStreamConstants.START_ELEMENT:
                if ("text".equals(reader.getLocalName())) {
                    // process data to SuffixTree ;
                    prepareSuffixStructure(reader.getElementText());
                }
            }
        }

        // merge created suffix subtrees
        String[] extensions = { "xml" };
        Collection<File> files = FileUtils.listFiles(new File(MyFileUtils.LETTERS_TREE_LOCATION), extensions, false);
        mergeXMLLetters(files);
    }

    /**
     * Method for processing train text from page data into subtree xml (letters.xml)
     * @param String text
     * @return KorpusItem
     * @throws IOException
     * @throws InterruptedException
     * @throws JAXBException 
     * @throws XMLStreamException 
     */
    private KorpusItem prepareSuffixStructure(String text) throws IOException, InterruptedException, JAXBException, XMLStreamException {
        text = text.replaceAll("\\[\\[Súbor:.+\\]\\]", "");
        List<String> tokenList = new ArrayList<String>();
        TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
        Tokenizer tokenizer = tokenizerFactory.tokenizer(text.toCharArray(), 0, text.length());
        tokenizer.tokenize(tokenList, new ArrayList<String>());

        for (String s : tokenList) {
            s = s.replaceAll("\\p{Punct}", "");
            s = s.replaceAll("\\p{Digit}", "");
            if (s.length() > 3) {
                KorpusItem root = processWord(s);
                marshallDataToXMLSuffixTree(root);
            }
        }
        return null;
    }

    /**
     * Method for marshaling prepared subtree
     * @param KorpusItem root
     * @throws JAXBException
     */
    private void marshallDataToXMLSuffixTree(KorpusItem root) throws JAXBException {
        // create dir for suffix trees
        File dir = new File(System.getProperty("user.dir") + MyFileUtils.LETTERSLOCATION + File.separator);
        dir.mkdir();
        String fileName = root.getChildren().getList().get(0).getPismeno() + ".xml";
        // check if tree exists
        if (new File(dir.getAbsolutePath() + fileName).exists()) {
            new File(dir.getAbsolutePath() + fileName).delete();
        }

        JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItem.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(root, new File(dir.getAbsolutePath() + File.separator + fileName));
    }

    /**
     * Method for creating suffix tree from one word.
     * 
     * @param String wordString
     * @return KorpusItem
     * @throws JAXBException 
     * @throws XMLStreamException 
     * @throws IOException 
     */
    private KorpusItem processWord(String wordString) throws IOException, XMLStreamException, JAXBException {
        wordString = wordString.toLowerCase();

        String processingString = new StringBuffer(wordString).reverse().toString();
        KorpusItem root = null;
        KorpusItem item = null;
        // for all chars in the word
        for (int i = 0; i < processingString.length(); i++) {
            String character = Character.toString(processingString.charAt(i));
            if (i == 0) {
                // search in DB for end char
                item = findSubTreeXMLByLetter(character);
                // if we cannot find end char return as unknown
                if (item == null) {
                    KorpusItem it = new KorpusItem();
                    it.setEnd(true);
                    it.setPismeno(character);
                    it.setProbability(1);
                    item = it;
                } else {
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

    /**
     * Method for merging letters into one structure
     * @param List<File> files
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    void mergeXMLLetters(Collection<File> files) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
        File lettersFile = new File(MyFileUtils.TRAINED_DATA);

        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(lettersFile), "UTF8"));

        boolean isfirstTime = true;
        for (File f : files) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));
            String line;
            if (isfirstTime) {
                writer.write(br.readLine());
                writer.write("\r\n");
                writer.write("<letters>");
                writer.write("\r\n");
                isfirstTime = false;
            } else {
                br.readLine();
            }
            while ((line = br.readLine()) != null) {
                writer.write(line);
                writer.write("\r\n");
            }
            br.close();
        }
        writer.write("</letters>");
        writer.close();

        // check xml validity
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();

        // the "parse" method also validates XML, will throw an exception if misformatted
        builder.parse(lettersFile);
    }

    /**
     * Method for searching for subtree in XML structure
     * 
     * @param character
     * @return
     * @throws IOException
     * @throws XMLStreamException
     * @throws JAXBException
     */
    private KorpusItem findSubTreeXMLByLetter(String character) throws IOException, XMLStreamException, JAXBException {
        String[] extensions = { "xml" };
        Collection<File> files = FileUtils.listFiles(new File(System.getProperty("user.dir") + MyFileUtils.LETTERSLOCATION + File.separator), extensions, false);
        for (File f : files) {
            if ((character + ".xml").equals(f.getName())) {

                XMLInputFactory factory = XMLInputFactory.newInstance();
                XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(f));
                JAXBContext jaxbContext = JAXBContext.newInstance(KorpusItem.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                KorpusItem list = (KorpusItem) jaxbUnmarshaller.unmarshal(reader);
                return list.getChildren().getList().get(0);
            }
        }
        return null;
    }
}