package dictionary;

/**
 * Utility class for manipulating with dbpedia resource urls.
 * 
 * @author Pidanic
 *
 */
public final class LinksUtil
{
    private LinksUtil()
    {
        throw new AssertionError(LinksUtil.class.getName()
                + " cannot be instatiated");
    }

    /**
     * Removes beginning and end angle brackets from input url
     * 
     * @param link
     * @return
     */
    public static String removeBrackets(String link)
    {
        return link.substring(1, link.length() - 1);
    }

    /**
     * Parses a word from URL. Word means string after last slash.
     * 
     * @param link
     * @return word.
     */
    public static String parseWord(String link)
    {
        int lastSlash = link.lastIndexOf("/");
        String word = link.substring(lastSlash + 1, link.length());
        return word;
    }

    /**
     * Creates string with words separated with space instead '_'.
     * 
     * @param dbWord
     * @return word
     */
    public static String makeWords(String dbWord)
    {
        return dbWord.replaceAll("_", " ");
    }
}
