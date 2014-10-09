package sk.fiit.nemecek.jolana.server.services.stemmer;

import org.springframework.stereotype.Service;
/**
 * Interface for preprocessing by maximum entrophy method.
 * 
 * @author Tomáš Nemeèek
 *
 */
@Service
public interface ProcessorService {

    /**
     * Method serves for preprocessing of extracted document. It return preprocessed document.
     * @param ExtractedDocument doc
     * @return ExtractedDocument
     * @throws Exception
     */
    public String processText(String doc,boolean isStopWord) throws Exception;
}
