package skCzStemmer.services.database.service;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import skCzStemmer.services.database.data.KorpusItem;

public interface DatabaseService {

    public KorpusItem findSubTreeByLetter(String letter)  throws JAXBException, XMLStreamException, IOException;

    public void extractMediaWikiTextForLearning() throws Exception;
}
