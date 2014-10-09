package sk.fiit.nemecek.jolana.server.utils;

import java.io.File;

import org.apache.commons.io.FileUtils;
/**
 * This class is util class for work with files.
 * You can implement here all method used for some "special" operation with files.
 * @author Tomáš Nemeèek
 *
 */
public class MyFileUtils extends FileUtils {

    public static final String FILENAME = "letters.xml";
    public static final String LETTERSLOCATION = File.separator + "data" + File.separator + "letters";
    
    public static final int DOC = 1;
    public static final int DOCX = 2;
    public static final int PDF = 3;
    public static final int WIKI = 4;

    public static final String SAMPLEDATA = "data/sample_skwiki_latest_pages_articles.xml";
    

    
    /**
     * Method for decide if input file can be processed.
     * @param file
     * @return boolean
     */
    public static boolean isAcceptedType(File file) {
        String type = getFileExtension(file);
        if (!type.equals("null") && checkAcceptedTypes(type) != 0) {
            return true;
        }
        return false;
    }
    
    /**
     * This method is logic  method for isAcceptedType. Here are defined all accepted file extensions.
     * @param type
     * @return int
     */
    public static int checkAcceptedTypes(String type) {
        if (type.toLowerCase().equals("docx")) {
            return MyFileUtils.DOCX;
        }
        if (type.toLowerCase().equals("doc")) {
            return MyFileUtils.DOC;
        }
        if (type.toLowerCase().equals("pdf")) {
            return MyFileUtils.PDF;
        }
        if (type.toLowerCase().equals("xml")) {
            return MyFileUtils.WIKI;
        }
        return 0;
    }

    /**
     * method for plain file name. Name is decided by delimeter.
     * @param String fileName
     * @param String delimeter
     * @return String
     */
    public static String preparePlainName(String fileName, String delimeter) {
        return fileName.substring(0, fileName.lastIndexOf(delimeter));
    }
    
    /**
     * Method for getting file extension from file name.
     * @param File file
     * @return String
     */
    public static String getFileExtension(File file) {
        String name =file.getName();
        return name.substring(name.lastIndexOf('.')+1,name.length());
    }
    /**
     * Method for preparing new name of file.
     * @param String name
     * @param int counter
     * @return String
     */
    public static String prepareName(String name,int counter) {
        int index = name.lastIndexOf(".");
        return name.substring(0,index)+"_"+counter+name.substring(index,name.length());
    }
}
