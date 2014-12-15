package dictionary;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Specifies basic parsing operations for DBPedia dumps.
 * 
 * @author Pidanic
 *
 */
public interface DbpediaParser
{
    /**
     * <p>
     * Parses given file and returns pairs of parsed data.
     * </p>
     * <p>
     * Pairs mostly consist of some identificator as key and dbpedia url as
     * value.
     * </p>
     * 
     * @param file
     *            File to parse.
     * @return <code>Map</code> with parsed data.
     * @throws IOException
     *             if problem opening file
     */
    Map<String, String> parse(File file) throws IOException;

    /**
     * <p>
     * Parses given file and writes to given file.
     * </p>
     * <p>
     * Parser output is represented as would be returned from
     * {@link #parse(File)}.
     * 
     * </p>
     * 
     * @param from
     *            File to parse.
     * @param to
     *            File to write parsed output.
     * @throws IOException
     *             if problem opening file
     */
    void parseToFile(File from, File to) throws IOException;

    /**
     * Parses file with given path and returns pairs of parsed data.
     * 
     * @param path
     *            path to given file.
     * @return <code>Map</code> with parsed data.
     * @throws IOException
     *             if problem opening file
     */
    Map<String, String> parse(String path) throws IOException;

    /**
     * Parses file with path and writes output to file specified with path.
     * 
     * @param from
     *            File to parse.
     * @param to
     *            File to write parsed output.
     * @throws IOException
     *             if problem opening file
     */
    void parseToFile(String pathFrom, String pathTo) throws IOException;
}
