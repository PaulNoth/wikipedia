
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileReader;
import java.io.StringWriter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author patrik
 */
public class ParserJUnitTest {

    static String regexLinks = null;
    static String regexPage = null;

    public ParserJUnitTest() throws Exception {
    }

    @BeforeClass
    public static void setUpClass() {
        regexLinks = Parser.getRegexLinks();
        regexPage = Parser.getRegexPage();
    }

    @Test
    public void firstEntryTest() throws Exception {
        SQLParser s = new SQLParser(Program.INPUT_LINKS, "categorylinks", regexLinks);

        String[] data = s.getRow("from", "to");

        Assert.assertEquals("Testovanie prveho zaznamu: page_id", "0", data[0]);
        Assert.assertEquals("Testovanie prveho zaznamu: category", "''", data[1]);

        data = s.getRow("from", "to");

    }
    
    @Test
    public void EntryTest() throws Exception {
        SQLParser s = new SQLParser(Program.INPUT_LINKS, "categorylinks", regexLinks);

        String[] data = s.getRow("from", "to");
        data = s.getRow("from", "to");

        Assert.assertEquals("Testovanie druheho zaznamu: page_id", "10", data[0]);
        Assert.assertEquals("Testovanie druheho zaznamu: category", "'Redirects_with_old_history'", data[1]);
    }
    
    @Test
    public void NEntryTest() throws Exception {
        SQLParser s = new SQLParser(Program.INPUT_LINKS, "categorylinks", regexLinks);

        String[] data;
        for(int i=0; i < 10; i++)
            data = s.getRow("from", "to");
        
        // 11 riadok
        data = s.getRow("from", "to");


        Assert.assertEquals("Testovanie druheho zaznamu: page_id", "12", data[0]);
        Assert.assertEquals("Testovanie druheho zaznamu: category", "'Articles_with_inconsistent_citation_formats'", data[1]);
    }
    
    
    @Test
    public void CSVTest() throws Exception {
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw, ',');
        
        
        String[] tmp = new String[2];
        tmp[0] = "Example string1";
        tmp[1] = "Example String2";
        writer.writeNext(tmp);
        
        String testString = "\"Example string1\",\"Example String2\"\n"; // ukoncenie znakom noveho riadku
        
        Assert.assertEquals("Otestovanie zakladneho csv exportu", testString, sw.getBuffer().toString());
    }
    
    @Test
    public void CSVAdvancedTest() throws Exception {
        StringWriter sw = new StringWriter();
        CSVWriter writer = new CSVWriter(sw, ',');
        
        
        String[] tmp = new String[1];
        tmp[0] = "\"-_'";
        writer.writeNext(tmp);
        
        String testString = "\"\"\"-_'\"\n"; // ukoncenie znakom noveho riadku

        Assert.assertEquals("Otestovanie rozsireneho csv exportu o specialne znaky", testString, sw.getBuffer().toString());
    }
    
    @Test
    public void ExportedCSV() throws Exception {
        CSVReader reader = new CSVReader(new FileReader("export.csv"));
        String[] nextLine;
        
        nextLine = reader.readNext();
        
        Assert.assertEquals("Pocet prvkov", 3, nextLine.length);
        
        Assert.assertEquals("Prvy prvok na prvom riadku", "AccessibleComputing", nextLine[0]);
        Assert.assertEquals("Druhy prvok na prvom riadku", "Redirects_with_old_history", nextLine[1]);
        Assert.assertEquals("Treti prvok na prvom riadku", "Unprintworthy_redirects", nextLine[2]);
    }
}
