package dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Class for matching parsed data.
 * </p>
 * <p>
 * The purpose is to find a match between data from parsed files
 * <tt>interlanguage_links_*</tt> and <tt>wikipedia_links_*</tt> and creates
 * output with corresponding matches.
 * </p>
 * 
 * @author Pidanic
 *
 */
final class InterlanguageWikipediaLinksMatcher
{
    /**
     * <p>
     * Creates temporary mapping between parsed <tt>interlanguage_links</tt> and
     * <tt>wikipedia_links</tt>
     * </p>
     * <p>
     * Output is <tt>csv</tt> file that contains idenficator in
     * <tt>interlanguage_links</tt>, Dbpedia URL and match on Wikipedia URL.
     * </p>
     * 
     * @param dbPediaMappingPath
     *            path for DBPedia mapping.
     * @param wikipediaMappingPath
     *            path for Wikipedialinks mapping.
     * @throws IOException
     *             if problem with manipulating files.
     * 
     */
    public void createDbpediaWikipediaMapping(String dbPediaMappingPath,
            String wikipediaMappingPath) throws IOException
    {
        File slovak = new File(dbPediaMappingPath);
        DbpediaParsedDataCsvReader reader = new DbpediaParsedDataCsvReader();
        Map<String, String> slovakDbpedia = reader.readCsv(slovak);
        Map<String, String> slovakDbpediaReverse = new HashMap<>();
        for (Map.Entry<String, String> entry : slovakDbpedia.entrySet())
        {
            slovakDbpediaReverse.put(entry.getValue(), entry.getKey());
        }
        slovakDbpedia = null;

        String lang = dbPediaMappingPath.substring(
                dbPediaMappingPath.lastIndexOf("_") + 1,
                dbPediaMappingPath.indexOf("."));
        File output = new File("data" + File.separator + "temp"
                + File.separator + "sample_output_dbpedia_wikipedia_links_"
                + lang + ".csv");
        BufferedReader br = new BufferedReader(new FileReader(
                wikipediaMappingPath));
        String line = null;
        BufferedWriter outWr = new BufferedWriter(new FileWriter(output));
        while ((line = br.readLine()) != null)
        {
            String[] tokens = line.split(",");
            if(slovakDbpediaReverse.containsKey(tokens[0]))
            {
                String id = slovakDbpediaReverse.get(tokens[0]);
                String dbpediaLink = tokens[0];
                String wikiLink = tokens[1];
                String lineOut = id + "," + dbpediaLink + "," + wikiLink + "\n";
                outWr.write(lineOut);
            }
        }
        br.close();

        outWr.flush();
        outWr.close();
    }
}
