package sk.fiit.nemecek.jolana.server.services.database.service;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import sk.fiit.nemecek.jolana.server.services.database.data.KorpusItem;
import sk.fiit.nemecek.jolana.server.services.database.data.KorpusItemList;

public interface DatabaseService {

    public void marshallDataToXMLFile(KorpusItemList list) throws JAXBException;

    public KorpusItem findSubTreeByLetter(String letter)  throws JAXBException, XMLStreamException, IOException;

    public KorpusItem findKorpusItemByLetter(String letter);

    public String extractMediaWikiTextForLearning() throws Exception;

}
