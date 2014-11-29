package dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Simple reader for CSV files of parsed data.
 * </p>
 * <p>
 * Expected files are outputs from basic parsing operations from implementations
 * of {@link DbpediaParser}s.
 * </p>
 * 
 * @author Pidanic
 *
 */
public final class DbpediaParsedDataCsvReader
{
    /**
     * Parses given csv file and return <code>Map</code> of result data. Parser
     * expects lines with only 2 values separated with comma. Values at 3rd,...,
     * nth positions are ignored.
     * 
     * @param file
     *            File to parse.
     * @return <code>Map</code> of parsed data.
     * @throws IOException
     *             if problem manipulating file.
     */
    public Map<String, String> readCsv(File file) throws IOException
    {
        Map<String, String> result = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null)
        {
            String[] s = line.split(",");
            result.put(s[0], s[1]);
        }
        br.close();
        return result;
    }
}
