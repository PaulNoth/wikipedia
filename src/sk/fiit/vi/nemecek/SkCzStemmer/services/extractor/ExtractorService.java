package sk.fiit.vi.nemecek.SkCzStemmer.services.extractor;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface for Extractor classes
 * @author Tomáš Nemeèek
 *
 */
public interface ExtractorService {
   
   /**
    * Method for extracting metada from document.
    * @return
    * @throws Exception
    */
   public ConcurrentHashMap<String, String> extractMediaWikiAnchors(File f) throws Exception;
}
