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
 * Class for parsing <tt>wikipedia_links_*.ttl</tt> from DBpedia dump.
 * 
 * @author Pidanic
 *
 */
final class WikipediaLinksParser implements DbpediaParser
{
    @Override
    public Map<String, String> parse(File file) throws IOException
    {
        Map<String, String> result = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        String lastResource = "";
        while ((line = br.readLine()) != null)
        {
            if(!line.startsWith("#"))
            {
                String[] links = line.split("\\s+");
                if(links[0].contains("dbpedia"))
                {
                    String dbpediaUrl = LinksUtil.removeBrackets(links[0]);
                    if(!lastResource.equals(dbpediaUrl))
                    {
                        String wikiUrl = LinksUtil.removeBrackets(links[2]);
                        result.put(dbpediaUrl, wikiUrl);
                    }
                }
            }
        }
        br.close();
        return result;
    }

    @Override
    public void parseToFile(File from, File to) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(from));
        String line = null;
        String lastResource = "";
        BufferedWriter bw = new BufferedWriter(new FileWriter(to));
        while ((line = br.readLine()) != null)
        {
            if(!line.startsWith("#"))
            {
                String[] links = line.split("\\s+");
                if(links[0].contains("dbpedia"))
                {
                    String dbpediaUrl = LinksUtil.removeBrackets(links[0]);
                    if(!lastResource.equals(dbpediaUrl))
                    {
                        String wikiUrl = LinksUtil.removeBrackets(links[2]);
                        dbpediaUrl = dbpediaUrl.replaceAll(",", "_");
                        wikiUrl = wikiUrl.replaceAll(",", "_");
                        String outline = dbpediaUrl + "," + wikiUrl + "\n";
                        bw.write(outline);
                    }
                }
            }
        }
        bw.flush();
        bw.close();
        br.close();
    }

    @Override
    public Map<String, String> parse(String path) throws IOException
    {
        return parse(new File(path));
    }

    @Override
    public void parseToFile(String pathFrom, String pathTo) throws IOException
    {
        parseToFile(new File(pathFrom), new File(pathTo));
    }

}
