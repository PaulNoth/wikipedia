package sk.fiit.nemecek.jolana.server.services.extractor.factory.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidParameterException;

import sk.fiit.nemecek.jolana.server.utils.MyFileUtils;
/**
 * Factory implementation
 * @author Tomáš Nemeèek
 *
 */
public class ExtractorFactoryCreator implements ExtractorFactory {

    @Override
    public String getInstance(File file) throws Exception {
        String type = MyFileUtils.getFileExtension(file);
        InputStream fis = null;
        try {

            fis = new FileInputStream(file.getAbsolutePath());
            switch (MyFileUtils.checkAcceptedTypes(type)) {
                case MyFileUtils.DOC: {
                	return null;
                }

                case MyFileUtils.DOCX: {
                	return null;
                }

                case MyFileUtils.PDF: {
                	return null;
                }
                case MyFileUtils.WIKI: {
                	return "WikiMediaExtractor";
                }
                default:
                    throw new InvalidParameterException("Parser for " + type.toUpperCase() + " is not implemented yet.");
            }
        } catch (Exception e) {
            throw e;
        }
        finally{
            fis.close();
        }
    }

}
