package langlinksfromsql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Daniel
 * test class - each test method has its own 1 line documentation at runtime
 */
public class LangLinksFromSQLTest {
    
    private static BufferedReader br,br2,br3;
    private static String line;
    
    public LangLinksFromSQLTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        
        File OutputFile = new File("../data/output_data_sk-hu-matches.txt");
        FileInputStream fis = new FileInputStream(OutputFile);
        
        br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        line = br.readLine();
        System.out.println("* @BeforeClass method - sets up the reader");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        
        br.close();
        System.out.println("* @AfterClass method");
    }

    //@Ignore
    @Test
    public void testOutputIsNotNull() throws Exception {
        
        System.out.println("* @Test - testOutputIsNotNull method - if it passes, first line of generated output is not null");
        assertNotNull(line);
    }
 
    //@Ignore
    @Test
    public void testIsValidOutputStructure() throws IOException{
        
        System.out.println("* @Test - testIsValidOutputStructure method - checks first and last char for each line");
        String lines;
        char firstChar,lastChar;
        char expectedFirstChar = '\'';
        char expectedLastChar = ')';
        boolean endOfFirstLangMatches = false;
        
        while((lines = br.readLine())!= null){
            firstChar = lines.charAt(0);
            if(firstChar != '\''){                                              /// line where starts the opposite search
                if(lines.contains("->")) endOfFirstLangMatches = true;
                assertTrue(endOfFirstLangMatches);
                return;                                                         
            }
            lastChar = lines.charAt(lines.length()-1);
            assertEquals(expectedFirstChar,firstChar);
            assertEquals(expectedLastChar,lastChar);
        }
    }
    
    @Test
    public void TestSkHuMatches() throws FileNotFoundException, UnsupportedEncodingException, IOException{
        
        System.out.println("* @Test - TestSkHuMatches method - some obvious matches were created manually to test the functionality");        
        boolean testResult = false;     // asserting this value in test
        
        if(!(line.contains("(sk) matches") || line.contains("(hu) matches"))){
            System.out.println("    Wrong Output Data - this test is suited only for HU and SK matches");
            assertTrue(testResult);
        }
        
        else{
            List<String> ArrayOfLines = new ArrayList<String>();
            String lines;
            int linecounter = 0;
            File TestMatches = new File("../data/unit_test.txt");
            FileInputStream fis2 = new FileInputStream(TestMatches);
            br2 = new BufferedReader(new InputStreamReader(fis2, "UTF-8"));

            File OutputFile2 = new File("../data/output_data_sk-hu-matches.txt");
            FileInputStream fis3 = new FileInputStream(OutputFile2);
            br3 = new BufferedReader(new InputStreamReader(fis3, "UTF-8"));

            while((lines = br2.readLine()) != null){        /// contain in an arraylist each line we expect in the real output
                ArrayOfLines.add(lines);
            }
            while((lines = br3.readLine())!= null) {        /// search if current line is in the unit test data
                for(String s : ArrayOfLines) {
                    if(s.equals(lines)) {
                    linecounter++;
                    if(linecounter == ArrayOfLines.size()) {    /// test passes
                        testResult = true;
                        break;
                        }
                    }
                }
            }
            assertTrue(testResult);

            br2.close();
            br3.close();          
        }
    }
}
