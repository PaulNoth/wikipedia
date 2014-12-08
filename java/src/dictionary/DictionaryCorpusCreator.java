package dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for creating corpus of different types of dictionaries from all parsed
 * data.
 * 
 * @author Pidanic
 *
 */
public class DictionaryCorpusCreator
{
    private static final String PATH_DICTIONARY_ENHANCED = "data"
            + File.separator + "sample_dictonary_enhanced.csv";

    private static final String PATH_DICTIONARY_SIMPLE = "data"
            + File.separator + "sample_dictionary.csv";

    private static final String SK_PATH = "data" + File.separator + "temp"
            + File.separator + "sample_output_dbpedia_wikipedia_links_sk.csv";

    private static final String EN_PATH = "data" + File.separator + "temp"
            + File.separator + "sample_output_dbpedia_wikipedia_links_en.csv";

    private static final String DE_PATH = "data" + File.separator + "temp"
            + File.separator + "sample_output_dbpedia_wikipedia_links_de.csv";

    private static final String FR_PATH = "data" + File.separator + "temp"
            + File.separator + "sample_output_dbpedia_wikipedia_links_fr.csv";

    private static final File[] FILES_INTERLANGUAGE;
    static
    {
        FILES_INTERLANGUAGE = new File[4];
        FILES_INTERLANGUAGE[0] = new File("data" + File.separator
                + "sample_interlanguage_links_fr.ttl");
        FILES_INTERLANGUAGE[1] = new File("data" + File.separator
                + "sample_interlanguage_links_de.ttl");
        FILES_INTERLANGUAGE[2] = new File("data" + File.separator
                + "sample_interlanguage_links_en.ttl");
        FILES_INTERLANGUAGE[3] = new File("data" + File.separator
                + "sample_interlanguage_links_sk.ttl");
    }

    private static final File[] FILES_WIKILINKS;
    static
    {
        FILES_WIKILINKS = new File[4];
        FILES_WIKILINKS[0] = new File("data" + File.separator
                + "sample_wikipedia_links_fr.ttl");
        FILES_WIKILINKS[1] = new File("data" + File.separator
                + "sample_wikipedia_links_de.ttl");
        FILES_WIKILINKS[2] = new File("data" + File.separator
                + "sample_wikipedia_links_en.ttl");
        FILES_WIKILINKS[3] = new File("data" + File.separator
                + "sample_wikipedia_links_sk.ttl");
    }

    /**
     * <p>
     * Creates corpus of enhanced dictionary.
     * </p>
     * <p>
     * Parses all interlanguage_links and wikipedia_links input files, and
     * matches parsed word through temporary files until required corpus file is
     * created.
     * </p>
     * <p>
     * Because of memory requirements this was done with temporary files.
     * </p>
     * 
     * @throws IOException
     */
    public void createEnhancedDictionary() throws IOException
    {
        File tempDir = new File("data" + File.separator + "temp");
        tempDir.mkdir();

        parseAll();

        matchAll();

        Map<String, Map<String, String>> result = new HashMap<>();

        BufferedReader sk = new BufferedReader(
                new FileReader(new File(SK_PATH)));
        String line = null;
        while ((line = sk.readLine()) != null)
        {
            String[] tokens = line.split(",");
            String key = tokens[0];
            Map<String, String> value = new LinkedHashMap<>();
            value.put(tokens[1], tokens[2]);
            result.put(key, value);
        }
        sk.close();

        BufferedReader fr = new BufferedReader(
                new FileReader(new File(FR_PATH)));
        String frLine = null;
        while ((frLine = fr.readLine()) != null)
        {
            String[] tokens = frLine.split(",");
            String key = tokens[0];
            if(result.containsKey(key))
            {
                result.get(key).put(tokens[1], tokens[2]);
            }
        }
        fr.close();

        BufferedReader en = new BufferedReader(
                new FileReader(new File(EN_PATH)));
        String enLine = null;
        while ((enLine = en.readLine()) != null)
        {
            String[] tokens = enLine.split(",");
            String key = tokens[0];
            if(result.containsKey(key))
            {
                result.get(key).put(tokens[1], tokens[2]);
            }
        }
        en.close();

        BufferedReader de = new BufferedReader(
                new FileReader(new File(DE_PATH)));
        String deLine = null;
        while ((deLine = de.readLine()) != null)
        {
            String[] tokens = deLine.split(",");
            String key = tokens[0];
            if(result.containsKey(key))
            {
                result.get(key).put(tokens[1], tokens[2]);
            }
        }
        de.close();

        BufferedWriter dictionary = new BufferedWriter(new FileWriter(new File(
                PATH_DICTIONARY_ENHANCED)));
        for (Map.Entry<String, Map<String, String>> map : result.entrySet())
        {
            if(map.getValue().size() == 4)
            {
                StringBuilder dictLine = new StringBuilder();
                for (Map.Entry<String, String> entry : map.getValue()
                        .entrySet())
                {
                    dictLine.append(LinksUtil.parseWord(LinksUtil
                            .makeWords(entry.getKey())));
                    dictLine.append(",");
                    dictLine.append(entry.getValue());
                    dictLine.append(",");
                }
                dictLine.replace(dictLine.lastIndexOf(","), dictLine.length(),
                        "\n");
                dictionary.write(dictLine.toString());
            }
        }
        dictionary.flush();
        dictionary.close();

        for (File temp : tempDir.listFiles())
        {
            temp.delete();
        }
        tempDir.delete();
    }

    /**
     * Parses all <tt>interlanguage_links</tt> and <tt>wikipedia_links</tt> in
     * all language versions and writes parsed output into temporary files.
     */
    private static void parseAll() throws IOException
    {
        DbpediaParser parser = new InterlanguageLinksParser();
        parser.parseToFile(FILES_INTERLANGUAGE[0], new File("data"
                + File.separator + "temp" + File.separator
                + "sample_output_interlanguage_links_fr.csv"));
        parser.parseToFile(FILES_INTERLANGUAGE[1], new File("data"
                + File.separator + "temp" + File.separator
                + "sample_output_interlanguage_links_de.csv"));
        parser.parseToFile(FILES_INTERLANGUAGE[2], new File("data"
                + File.separator + "temp" + File.separator
                + "sample_output_interlanguage_links_en.csv"));
        parser.parseToFile(FILES_INTERLANGUAGE[3], new File("data"
                + File.separator + "temp" + File.separator
                + "sample_output_interlanguage_links_sk.csv"));

        parser = new WikipediaLinksParser();
        parser.parseToFile(FILES_WIKILINKS[0], new File("data" + File.separator
                + "temp" + File.separator
                + "sample_output_wikipedia_links_fr.csv"));

        parser.parseToFile(FILES_WIKILINKS[1], new File("data" + File.separator
                + "temp" + File.separator
                + "sample_output_wikipedia_links_de.csv"));

        parser.parseToFile(FILES_WIKILINKS[2], new File("data" + File.separator
                + "temp" + File.separator
                + "sample_output_wikipedia_links_en.csv"));

        parser.parseToFile(FILES_WIKILINKS[3], new File("data" + File.separator
                + "temp" + File.separator
                + "sample_output_wikipedia_links_sk.csv"));
    }

    /**
     * Matches all From temporary files with parsed data from
     * {@link #parseAll()} into temporary files that contains an id, dbpedia
     * links and wikipedia links.
     * 
     * Matching means finding all pairs with same id. Words that do not have a
     * valid pair in other language files are ignored. Only word in all 4
     * language are written into temporary file.
     */
    private static void matchAll() throws IOException
    {
        InterlanguageWikipediaLinksMatcher match = new InterlanguageWikipediaLinksMatcher();
        match.createDbpediaWikipediaMapping("data" + File.separator + "temp"
                + File.separator + "sample_output_interlanguage_links_fr.csv",
                "temp" + File.separator
                        + "sample_output_wikipedia_links_fr.csv");
        match.createDbpediaWikipediaMapping("data" + File.separator + "temp"
                + File.separator + "sample_output_interlanguage_links_fr.csv",
                "temp" + File.separator
                        + "sample_output_wikipedia_links_fr.csv");
        match.createDbpediaWikipediaMapping("data" + File.separator + "temp"
                + File.separator + "sample_output_interlanguage_links_de.csv",
                "temp" + File.separator
                        + "sample_output_wikipedia_links_de.csv");
        match.createDbpediaWikipediaMapping("data" + File.separator + "temp"
                + File.separator + "sample_output_interlanguage_links_sk.csv",
                "temp" + File.separator
                        + "sample_output_wikipedia_links_sk.csv");
        match.createDbpediaWikipediaMapping("data" + File.separator + "temp"
                + File.separator + "sample_output_interlanguage_links_en.csv",
                "temp" + File.separator
                        + "sample_output_wikipedia_links_en.csv");
    }

    /**
     * <p>
     * Creates corpus of simple dictionary in <tt>csv</tt> file.
     * </p>
     * <p>
     * Parses all interlanguage_links and creates dictionary corpus. Words that
     * have valid translation in another language are put together. <br>
     * Note that output file may contain empty word between commas because not
     * all word have translations in all 4 languages.
     * </p>
     * 
     * @throws IOException
     */
    public void createSimpleDictionary() throws IOException
    {
        Map<String, List<String>> result = new HashMap<>();
        for (int i = 0; i < FILES_INTERLANGUAGE.length; i++)
        {
            File interlanguage = FILES_INTERLANGUAGE[i];
            Map<String, String> parsedData = parseInterlanguage(interlanguage);
            for (Map.Entry<String, String> entry : parsedData.entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();
                if(result.containsKey(key))
                {
                    result.get(key).set(i, value);
                }
                else
                {
                    List<String> list = empty();
                    list.set(i, value);
                    result.put(key, list);
                }
            }
            parsedData = null;
        }

        writeToFile(result);
    }

    /**
     * Implementation based on {@link WikipediaLinksParser#parse(File)} with
     * difference that values are only parsed words instead of dbpedia links.
     */
    private static Map<String, String> parseInterlanguage(File file)
            throws IOException
    {
        Map<String, String> result = new HashMap<>();

        String line = null;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String lastResource = "";
        while ((line = br.readLine()) != null)
        {
            if(!line.startsWith("#"))
            {
                String[] resources = line.split("\\s+");
                String resource = LinksUtil.removeBrackets(resources[0]);
                String idResource = LinksUtil.removeBrackets(resources[2]);
                if(!lastResource.equals(resource))
                {
                    String id = LinksUtil.parseWord(idResource);
                    String words = LinksUtil.makeWords(LinksUtil
                            .parseWord(resource));
                    result.put(id, words);
                    lastResource = resource;
                }
            }
        }
        br.close();
        return result;
    }

    private static List<String> empty()
    {
        List<String> result = new ArrayList<String>(4);
        result.add("");
        result.add("");
        result.add("");
        result.add("");
        return result;
    }

    private static void writeToFile(Map<String, List<String>> list)
            throws IOException
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
                PATH_DICTIONARY_SIMPLE)));
        for (List<String> val : list.values())
        {
            StringBuilder line = new StringBuilder();
            for (String word : val)
            {
                line.append(word);
                line.append(",");
            }
            line.replace(line.lastIndexOf(","), line.length(), "\n");
            bw.write(line.toString());
        }
        bw.flush();
        bw.close();
    }
}
