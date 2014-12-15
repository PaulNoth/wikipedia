package dictionary;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that tests {@link WikipediaLinksParser} against sample output.
 * 
 * @author Pidanic
 * 
 */
public class WikipediaLinksParserTest
{
    private static final File SAMPLE_OUTPUT_EN = new File("data" + File.separator
            + "sample_output_wikipedia_links_en.csv");

    private static final File SAMPLE_INPUT_EN = new File("data" + File.separator
            + "sample_wikipedia_links_en.ttl");

    private static final File SAMPLE_OUTPUT_DE = new File("data" + File.separator
            + "sample_output_wikipedia_links_de.csv");

    private static final File SAMPLE_INPUT_DE = new File("data" + File.separator
            + "sample_wikipedia_links_de.ttl");

    private static final File SAMPLE_OUTPUT_FR = new File("data" + File.separator
            + "sample_output_wikipedia_links_fr.csv");

    private static final File SAMPLE_INPUT_FR = new File("data" + File.separator
            + "sample_wikipedia_links_fr.ttl");

    private static final File SAMPLE_OUTPUT_SK = new File("data" + File.separator
            + "sample_output_wikipedia_links_sk.csv");

    private static final File SAMPLE_INPUT_SK = new File("data" + File.separator
            + "sample_wikipedia_links_sk.ttl");

    private DbpediaParser wikipediaLinks;

    private DbpediaParsedDataCsvReader reader;

    @Before
    public void init()
    {
        wikipediaLinks = new WikipediaLinksParser();
        reader = new DbpediaParsedDataCsvReader();
    }

    @Test
    public void testParseWikipediaLinksEn() throws IOException
    {
        Map<String, String> expected = reader.readCsv(SAMPLE_OUTPUT_EN);
        Map<String, String> actual = wikipediaLinks.parse(SAMPLE_INPUT_EN);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWikipediaLinksDe() throws IOException
    {
        Map<String, String> expected = reader.readCsv(SAMPLE_OUTPUT_DE);
        Map<String, String> actual = wikipediaLinks.parse(SAMPLE_INPUT_DE);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWikipediaLinksFr() throws IOException
    {
        Map<String, String> expected = reader.readCsv(SAMPLE_OUTPUT_FR);
        Map<String, String> actual = wikipediaLinks.parse(SAMPLE_INPUT_FR);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testParseWikipediaLinksSk() throws IOException
    {
        Map<String, String> expected = reader.readCsv(SAMPLE_OUTPUT_SK);
        Map<String, String> actual = wikipediaLinks.parse(SAMPLE_INPUT_SK);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFileNotFound() throws IOException
    {
        Map<String, String> expected = reader.readCsv(new File("wrong_file"));
        Map<String, String> actual = wikipediaLinks.parse(SAMPLE_INPUT_SK);
        Assert.assertEquals(expected, actual);
    }
}
