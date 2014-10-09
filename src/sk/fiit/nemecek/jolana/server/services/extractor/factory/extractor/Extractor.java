package sk.fiit.nemecek.jolana.server.services.extractor.factory.extractor;

/**
 * Interface for Extractor classes
 * @author Tomáš Nemeèek
 *
 */
public interface Extractor {
   
   /**
    * Method for extracting metada from document.
    * @return
    * @throws Exception
    */
   public String extractMediaWikiAnchors() throws Exception;
}
