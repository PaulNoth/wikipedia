package sk.fiit.nemecek.jolana.server.services.extractor.factory.core;

import java.io.File;
/**
 * Factory interface
 * @author Tomáš Nemeèek
 *
 */
public interface ExtractorFactory {

    public String getInstance(File file) throws Exception;
}
