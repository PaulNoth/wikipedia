package ner_dictionary;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import ner_dictionary.indexing.Indexer;
import ner_dictionary.rules.RuleSet;
import ner_dictionary.rules.Rule;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * This test require three files to be present: 
 *  - Input file: Part of Wikipedia XML dump file to parse pages. This should contain at least one Wikipedia page.
 *  - Input rules file: This file should contain at least one rule, which is used to parse input file.
 *  - Output file: This file must correspond to Input file content and rules in Input rules file (For each page there must be a pair of page title and category id based on rules specified in Input rules file)
 *	
 */
public class NERDictionaryTest {

	private static NERDictionary app;
	// To accept new inputs, the content, or the following names of input files can be changed. 
	private static final String INPUT_FILE = "sample_alts_enwiki-latest-pages-articles1.xml";
	private static final String INPUT_RULES_FILE = "Category_recognition_rules.xml";
	private static final String OUTPUT_FILE = "sample_output_NER_dictionary.tsv";
	
	@org.junit.Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
	
	@BeforeClass
	public static void setUp() {
		app = new NERDictionary();
	}
	
	@Test
	public void testParseRules() throws IOException {
		// Temporary output file
		String categoryMappingFilePath = testFolder.newFile().getPath();
		
		// Load rules
		URL url = this.getClass().getClassLoader().getResource(INPUT_RULES_FILE);;
		String rulesFilePath = url.getPath();
		RuleSet ruleSet = app.parseRules(rulesFilePath, categoryMappingFilePath);
		
		assertFalse(ruleSet.getRuleList().isEmpty());
		
		for (Rule rule : ruleSet.getRuleList()) {
			assertNotNull(rule.getPattern());
			assertNotEquals(rule.getPattern(), "");
			assertNotNull(rule.getCategoryName());
			assertNotEquals(rule.getCategoryName(), "");
			assertNotNull(rule.getTypeAsString());
			assertNotEquals(rule.getTypeAsString(), "");
			assertNotNull(rule.getCategoryId());
		}
	}

	@Test
	public void testParseAndIndexWikipedia() throws IOException {
		// Create temporary index directory
		File indexDirectory = testFolder.newFolder();
		Indexer indexer = new Indexer(indexDirectory.getPath());
		
		// Load input file
		URL sourceUrl = this.getClass().getClassLoader().getResource(INPUT_FILE);
		String sourceFilePath = URLDecoder.decode(sourceUrl.getPath(), StandardCharsets.UTF_8.displayName());
		List<String> sourceList = new ArrayList<String>();
		sourceList.add(sourceFilePath);
		
		// Create temporary output file for category mapping
		String categoryMappingFilePath = testFolder.newFile().getPath();
		
		// Load rules
		URL rulesUrl = this.getClass().getClassLoader().getResource(INPUT_RULES_FILE);
		String rulesFilePath = URLDecoder.decode(rulesUrl.getPath(), StandardCharsets.UTF_8.displayName());
		RuleSet ruleSet = app.parseRules(rulesFilePath, categoryMappingFilePath);
		
		// Create temporary output file for dictionary
		File outputFile = testFolder.newFile();
		
		// Do parsing
		app.parseAndIndexWikipedia(sourceList, outputFile.getPath(), ruleSet, indexer);
		List<String> outputLines = Files.readAllLines(outputFile.toPath(), StandardCharsets.UTF_8);
		
		// Load expected output file to compare with created one
		URL expectedOutputUrl = this.getClass().getClassLoader().getResource(OUTPUT_FILE);
		String expectedOutputFilePath = URLDecoder.decode(expectedOutputUrl.getPath(), StandardCharsets.UTF_8.displayName());
		File expectedOutputFile = new File(expectedOutputFilePath);
		List<String> expectedLines = Files.readAllLines(expectedOutputFile.toPath(), StandardCharsets.UTF_8);
		
		// Compare each line (page)
		for (int i = 0; i < expectedLines.size(); i++) {
			assertEquals(expectedLines.get(i), outputLines.get(i));
		}
	}
}
