package sk.fiit.nemecek.jolana.server.services.extractor;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import sk.fiit.nemecek.jolana.server.services.extractor.factory.core.ExtractorFactory;
import sk.fiit.nemecek.jolana.server.services.extractor.factory.core.ExtractorFactoryCreator;
import sk.fiit.nemecek.jolana.server.services.extractor.factory.extractor.Extractor;
import sk.fiit.nemecek.jolana.server.utils.exception.CannotProcessExcetion;
/**
 * Implementation of ExtractorService. 
 * @see sk.fiit.nemecek.jolana.server.services.extractor.ExtractorService
 * @author Tomáš Nemeèek
 *
 */
@Service
public class ExtractorDefaultService implements ExtractorService{

    @Autowired
    private ApplicationContext appContext;
    
    ExtractorFactory factory = new ExtractorFactoryCreator();
    String extractorString = null;
    
    Logger  logger = Logger.getLogger(ExtractorDefaultService.class);

    private String extractMediaWikiAnchors(File file) throws CannotProcessExcetion{
        try {
            extractorString= factory.getInstance(file);
            Extractor extractor = (Extractor)appContext.getBean(extractorString);
            Object extractedText=extractor.extractMediaWikiAnchors();
            return (String) extractedText;
        } catch (Exception e) {
            logger.fatal(e.getStackTrace().toString());
            throw new CannotProcessExcetion(e);
        }
    }
}
