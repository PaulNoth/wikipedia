package ttltoxml;

import java.util.Arrays;
import java.util.List;

/**
 * @author Skrisa Július
 * 
 * In settings class are defined names of files to be parsed and tested. Also names of outputed files and accepted predicates during parsing.
 */
public class SettingsOfTtlFiles {
	
	public static List<String> Predicates = Arrays.asList( //list of accepted predicates
		"label",
		"isPrimaryTopicOf",
		"comment",
		"wikiPageExternalLink",
		"subject",
		"wikiPageWikiLink",
		"wikiPageRedirects"
		);
	
	public static List<String> Files = Arrays.asList( // list of files to parse
			"labels_sk.ttl",
			"wikipedia_links_sk.ttl",
			"short_abstracts_sk.ttl",
			"page_links_sk.ttl",
			"redirects_sk.ttl",
			"external_links_sk.ttl",
			"article_categories_sk.ttl"
			);
	
	public static List<String> TestFiles = Arrays.asList( // list of testing files
			"sample_input_labels_sk.ttl",
			"sample_input_wikipedia_links_sk.ttl",
			"sample_input_short_abstracts_sk.ttl",
			"sample_input_page_links_sk.ttl",
			"sample_input_redirects_sk.ttl",
			"sample_input_external_links_sk.ttl",
			"sample_input_article_categories_sk.ttl"
			);
	
	public static String OutputFile = "data/outputFiles/OutFromTTLdumpsDBpedia32.xml"; //output xml file
			
	public static String OutputTemplate = "data/sample_output_DBpediaParsedDumps.xml"; // template xml file 
}
