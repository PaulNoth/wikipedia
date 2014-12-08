package langlinksfromsql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Daniel
 */
public class FileReadManager {
    
    /**
     *
     * @param filepath
     * @param br
     * @return buffered reader
     */
    public static BufferedReader initReader(String filepath, BufferedReader br) throws FileNotFoundException, UnsupportedEncodingException{
        
        // init reader          
        File file = new File(filepath);
        FileInputStream fis = new FileInputStream(file);
        br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        
        return br;
    }    
}
