package ner_dictionary;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ner_dictionary.indexing.Indexer;
import ner_dictionary.query.Searcher;
import ner_dictionary.query.gui.QueryFrame;
import ner_dictionary.rules.CategorySet;
import ner_dictionary.rules.RuleSet;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Application for creating a Named Entity Recognition dictionary and querying it.
 *
 */
public class NERDictionary 
{
    public static void main( String[] args )
    {
    	NERDictionary app = new NERDictionary();
    	
    	// To see possible options see function parseArguments(args)
    	Namespace ns = app.parseArguments(args);
    	
    	// Determine which mode to start (parse or query)
    	if (ns.getBoolean("query")) {
    		// Load category set used to map category id to their names
    		CategorySet categorySet = new CategorySet(ns.getString("categories"));
    		// Searcher is initialized with directory of previously created index
    		final Searcher searcher = new Searcher(ns.getString("index"), categorySet.getCategoryMapping());
			// To ease query, a simple qui is started
    		EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						QueryFrame frame = new QueryFrame(searcher);
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			// Set of rules used to parse source Wikipedia files are loaded. File to store found category mappings is specified
			RuleSet ruleSet = app.parseRules(ns.getString("rules"), ns.getString("categories"));
			
			// Indexer used during parsing is initialized
			Indexer indexer = new Indexer(ns.getString("index"));
			
			// Main work
			app.parseAndIndexWikipedia(ns.<String> getList("source"), ns.getString("output"), ruleSet, indexer);
		}
    }
    
    private Namespace parseArguments(String[] args) {
    	ArgumentParser parser = ArgumentParsers.newArgumentParser("MainParser");
    	parser.defaultHelp(true);
    	parser.description("Create name entity recognition dictionary.");
    	
    	parser.addArgument("-q", "--query")
			.action(Arguments.storeTrue())
			.setDefault(false)
			.help("Start query mode if specified, otherwise start parse mode. Index directory and category mapping file must be specified if changed.");
    	
    	ArgumentGroup group = parser.addArgumentGroup("Parse mode arguments");
    	group.addArgument("-r", "--rules")
    		.help("Specify file in which are the rules for category recognition stored.");
    	
    	group.addArgument("-o", "--output")
    		.setDefault("NER_dictionary.tsv")
    		.help("Specify file in which the dictionary will be created.");
    	
    	group.addArgument("-c", "--categories")
			.setDefault("NER_categories.txt")
			.help("Specify file in which the category mapping will be stored.");
    	
    	group.addArgument("-i", "--index")
			.setDefault("Index")
			.help("Specify directory in which the index will be stored.");
    	
    	group.addArgument("source")
    		.nargs("*")
    		.help("Source files to parse. If the path specifies a directory, each file in the directory will be parsed.");
    	
    	Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
            if (!ns.getBoolean("query") && ns.get("rules") == null) {
            	System.err.println("Rules file not specified while in parse mode!");
            	System.exit(1);
			}
            if (!ns.getBoolean("query") && ns.getList("source").isEmpty()) {
            	System.err.println("Source file is not specified while in parse mode!");
            	System.exit(1);
			}
        } catch (ArgumentParserException e) {
        	parser.handleError(e);
            System.exit(1);
        }
        return ns;
    }
 
    public RuleSet parseRules(String rulesFilePath, String categoryMappingFileName) {
    	try {
			JAXBContext jaxbContext = JAXBContext.newInstance(RuleSet.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			RuleSet ruleSet = (RuleSet) jaxbUnmarshaller.unmarshal(new File(rulesFilePath));
			
			// Process found categories in rule set and assign an id to each category
			CategorySet categorySet = new CategorySet(ruleSet);
			// Save to file for future querying
			categorySet.saveSetToFile(categoryMappingFileName);
			return ruleSet;
		} catch (JAXBException e) {
			System.err.println("An error occured during parsing the rules file " + rulesFilePath);
			e.printStackTrace();
			System.exit(1);
			return null;
		}
    }
    
    public void parseAndIndexWikipedia(List<String> sources, String outputFileName, RuleSet ruleSet, Indexer indexer) {
    	PrintStream outputFileStream = null;
		try {
			outputFileStream = new PrintStream(new FileOutputStream(outputFileName));
			
			XMLReader reader = null;
			try {
				// Create SAX reader and its parser handler to parse files sequentially 
				reader = XMLReaderFactory.createXMLReader();
				WikiXMLParserHandler parserHandler = new WikiXMLParserHandler(outputFileStream, ruleSet, indexer);
				reader.setContentHandler(parserHandler);
				
				// Process all source files specified as an arguments of this application
				for (String path : sources) {
					// If the path specifies a directory process each file in this directory
					File fileOrDir = new File(path);
					List<File> files = new ArrayList<File>();
					if (fileOrDir.isDirectory()) {
						files = Arrays.asList(fileOrDir.listFiles());
					} else {
						// If the path specifies only one file and not a directory, only this file will be added to files array
						files.add(fileOrDir);
					}
					InputSource inputSource = null;
					for (File f : files) {
						try {
							inputSource = new InputSource(new FileInputStream(f));
							try {
								System.out.println("Parsing file " + f.getPath() + " ...");
								reader.parse(inputSource);
								System.out.println("Parsing of file " + f.getPath() + " finished");
							} catch (SAXException e1) {
								System.err.println("An error occured during parsing the file " + f.getPath());
								e1.printStackTrace();
							} catch (IOException e2) {
								System.err.println("An error occured during parsing the file " + f.getPath());
								e2.printStackTrace();
							}
						} catch (FileNotFoundException e) {
							System.err.println("Can not open the file " + f.getPath());
							e.printStackTrace();
						}
					}
				}
				System.out.println("Resolving redirects ...");
				
				// Redirects found during parsing are resolved finally
				int redirectsRemained = parserHandler.processRedirects();
				// Not all redirects can be resolved from already parsed dumps
				System.out.println("Redirects remained unresolved: " + redirectsRemained);
			} catch (SAXException e) {
				System.err.println("An error occured during inicialization of XML reader");
				e.printStackTrace();
				System.exit(1);
			}
			
			outputFileStream.close();
		} catch (FileNotFoundException e) {
			System.err.println("Can not open or create the output file.");
			e.printStackTrace();
			System.exit(1);
		}
		indexer.close();
    }
}
