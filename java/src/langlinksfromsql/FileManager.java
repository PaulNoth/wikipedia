package langlinksfromsql;
/**
 *
 * @author Daniel
 */
public class FileManager {
    public static String constructFilePath(String Language, String fileName, boolean isLangLink, boolean realSample){
        
        StringBuilder file = new StringBuilder();
        
        /// construct file pathname
        file.append(fileName).append("/data/");                                     
        if(!realSample) file.append("sample_");
        file.append("input_").append(Language).append("wiki-latest-");
        if(isLangLink) file.append("langlinks.sql");   
        else file.append("page.sql");
        /// construct file pathname
        
        fileName = file.toString();
        return fileName;
    }
}
