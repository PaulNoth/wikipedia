package com.eriksuta.data;

import com.eriksuta.data.handler.*;
import org.apache.log4j.Logger;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *  @author Erik Suta
 * */
public class ParserImpl implements IParser{

    private static final Logger LOGGER = Logger.getLogger(ParserImpl.class);

    private static ParserImpl parser;

    public static final String OUT_DIR = "src/main/webapp/parsed/";
    public static final String TTL_DIR = "src/main/resources/rdf/";
    public static final String TTL_DIR_CZ = "src/main/resources/rdf/cz/";

    public static final String TTL_EXTERNAL_LINKS_CZ = TTL_DIR_CZ + "external_links_cs.ttl";
    public static final String TTL_FREEBASE_LINKS_CZ = TTL_DIR_CZ + "freebase_links_cs.ttl";
    public static final String TTL_INTER_LANGUAGE_LINKS_CZ = TTL_DIR_CZ + "interlanguage_links_cs.ttl";
    public static final String TTL_PAGE_LINKS_CZ = TTL_DIR_CZ + "page_links_cs.ttl";
    public static final String TTL_PAGE_LINKS_UNREDIRECTED_CZ = TTL_DIR_CZ + "page_links_unredirected_cs.ttl";
    public static final String TTL_WIKIPEDIA_LINKS_CZ = TTL_DIR_CZ + "wikipedia_links_cs.ttl";
    public static final String TTL_REDIRECTS_CZ = TTL_DIR_CZ + "redirects_cs.ttl";
    public static final String TTL_REDIRECTS_TRANSITIVE_CZ = TTL_DIR_CZ + "redirects_transitive_cs.ttl";

    public static final String TTL_ARTICLE_CATEGORIES_EN_URIS_SK = TTL_DIR + "article_categories_en_uris_sk.ttl";
    public static final String TTL_ARTICLE_CATEGORIES_SK = TTL_DIR + "article_categories_sk.ttl";
    public static final String TTL_EXTERNAL_LINKS_SK = TTL_DIR + "external_links_sk.ttl";
    public static final String TTL_FREEBASE_LINKS = TTL_DIR + "freebase_links_sk.ttl";
    public static final String TTL_INFOBOX_PROPERTIES_SK = TTL_DIR + "infobox_properties_sk.ttl";
    public static final String TTL_INTER_LANGUAGE_LINKS = TTL_DIR + "interlanguage_links_sk.ttl";
    public static final String TTL_LABELS_SK = TTL_DIR + "labels_sk.ttl";
    public static final String TTL_LONG_ABSTRACTS_SK = TTL_DIR + "long_abstracts_sk.ttl";
    public static final String TTL_OUT_DEGREE_SK = TTL_DIR + "out_degree_sk.ttl";
    public static final String TTL_PAGE_IDS_SK = TTL_DIR + "page_ids_sk.ttl";
    public static final String TTL_PAGE_LENGTH_SK = TTL_DIR + "page_length_sk.ttl";
    public static final String TTL_PAGE_LINKS_SK = TTL_DIR + "page_links_sk.ttl";
    public static final String TTL_PAGE_LINKS_UNREDIRECTED_SK = TTL_DIR + "page_links_unredirected_sk.ttl";
    public static final String TTL_REDIRECTS_SK = TTL_DIR + "redirects_sk.ttl";
    public static final String TTL_REDIRECTS_TRANSITIVE_SK = TTL_DIR + "redirects_transitive_sk.ttl";
    public static final String TTL_REVISION_IDS_SK = TTL_DIR + "revision_ids_sk.ttl";
    public static final String TTL_REVISION_URIS_SK = TTL_DIR + "revision_uris_sk.ttl";
    public static final String TTL_SHORT_ABSTRACTS_SK = TTL_DIR + "short_abstracts_sk.ttl";
    public static final String TTL_WIKIPEDIA_LINKS_SK = TTL_DIR + "wikipedia_links_sk.ttl";
    public static final String TTL_SKOS_CATEGORIES_EN_URIS_SK = TTL_DIR + "skos_categories_en_uris_sk.ttl";
    public static final String TTL_SKOS_CATEGORIES_SK = TTL_DIR + "skos_categories_sk.ttl";

    public static final String EXTERNAL_LINKS_CZ = OUT_DIR + "external_links_cs.txt";
    public static final String FREEBASE_LINKS_CZ = OUT_DIR + "freebase_links_cs.txt";
    public static final String INTER_LANGUAGE_LINKS_CZ = OUT_DIR + "interlanguage_links_cs.txt";
    public static final String PAGE_LINKS_CZ = OUT_DIR + "page_links_cs.txt";
    public static final String PAGE_LINKS_UNREDIRECTED_CZ = OUT_DIR + "page_links_unredirected_cs.txt";
    public static final String WIKIPEDIA_LINKS_CZ = OUT_DIR + "wikipedia_links_cs.txt";
    public static final String REDIRECTS_CZ = OUT_DIR + "redirects_cs.txt";
    public static final String REDIRECTS_TRANSITIVE_CZ = OUT_DIR + "redirects_transitive_cs.txt";

    public static final String ARTICLE_CATEGORIES_EN_URIS_SK = OUT_DIR + "article_categories_en_uris_sk.txt";
    public static final String ARTICLE_CATEGORIES_SK = OUT_DIR + "article_categories_sk.txt";
    public static final String EXTERNAL_LINKS_SK = OUT_DIR + "external_links_sk.txt";
    public static final String FREEBASE_LINKS_SK = OUT_DIR + "freebase_links_sk.txt";
    public static final String INFOBOX_PROPERTIES_SK = OUT_DIR + "infobox_properties_sk.txt";
    public static final String INTER_LANGUAGE_LINKS = OUT_DIR + "interlanguage_links_sk.txt";
    public static final String LABELS_SK = OUT_DIR + "labels_sk.txt";
    public static final String LONG_ABSTRACTS_SK = OUT_DIR + "long_abstracts_sk.txt";
    public static final String OUT_DEGREE_SK = OUT_DIR + "out_degree_sk.txt";
    public static final String PAGE_IDS_SK = OUT_DIR + "page_ids_sk.txt";
    public static final String PAGE_LENGTH_SK = OUT_DIR + "page_length_sk.txt";
    public static final String PAGE_LINKS_SK = OUT_DIR + "page_links_sk.txt";
    public static final String PAGE_LINKS_UNREDIRECTED_SK = OUT_DIR + "page_links_unredirected_sk.txt";
    public static final String REDIRECTS_SK = OUT_DIR + "redirects_sk.txt";
    public static final String REDIRECTS_TRANSITIVE_SK = OUT_DIR + "redirects_transitive_sk.txt";
    public static final String REVISION_IDS_SK = OUT_DIR + "revision_ids_sk.txt";
    public static final String REVISION_URIS_SK = OUT_DIR + "revision_uris_sk.txt";
    public static final String SHORT_ABSTRACTS_SK = OUT_DIR + "short_abstracts_sk.txt";
    public static final String WIKIPEDIA_LINKS_SK = OUT_DIR + "wikipedia_links_sk.txt";
    public static final String SKOS_CATEGORIES_EN_URIS_SK = OUT_DIR + "skos_categories_en_uris_sk.txt";
    public static final String SKOS_CATEGORIES_SK = OUT_DIR + "skos_categories_sk.txt";

    private static BufferedWriter bufferedWriter;
    private RDFParser rdfParser = Rio.createParser(RDFFormat.TURTLE);

    public void parseSlovakDBPedia(){

        long startTime = System.currentTimeMillis();
        System.out.println("Started parsing entire Slovak DBPedia dump at: " + new Date());
        LOGGER.info("Started parsing entire Slovak DBPedia dump at: " + new Date());

        //Article Categories
        parseArticleCategories(new File(TTL_ARTICLE_CATEGORIES_SK), new File(ARTICLE_CATEGORIES_SK));
        sortStatementsInFile(new File(ARTICLE_CATEGORIES_SK));

        parseArticleCategories(new File(TTL_ARTICLE_CATEGORIES_EN_URIS_SK), new File(ARTICLE_CATEGORIES_EN_URIS_SK));
        sortStatementsInFile(new File(ARTICLE_CATEGORIES_EN_URIS_SK));

        //Infobox Properties
        parseInfoboxProperties(new File(TTL_INFOBOX_PROPERTIES_SK), new File(INFOBOX_PROPERTIES_SK));
        sortStatementsInFile(new File(INFOBOX_PROPERTIES_SK));

        //Labels
        parseLabels(new File(TTL_LABELS_SK), new File(LABELS_SK));
        sortStatementsInFile(new File(LABELS_SK));

        //Long Abstracts
        parseLongAbstracts(new File(TTL_LONG_ABSTRACTS_SK), new File(LONG_ABSTRACTS_SK));
        sortStatementsInFile(new File(LONG_ABSTRACTS_SK));

        //Out Degree
        parseOutDegree(new File(TTL_OUT_DEGREE_SK), new File(OUT_DEGREE_SK));
        sortStatementsInFile(new File(OUT_DEGREE_SK));

        //Page IDs
        parsePageIds(new File(TTL_PAGE_IDS_SK), new File(PAGE_IDS_SK));
        sortStatementsInFile(new File(PAGE_IDS_SK));

        //Page Length
        parsePageLengths(new File(TTL_PAGE_LENGTH_SK), new File(PAGE_LENGTH_SK));
        sortStatementsInFile(new File(PAGE_LENGTH_SK));

        //Links - External
        parseLinks(new File(TTL_EXTERNAL_LINKS_SK), new File(EXTERNAL_LINKS_SK));
        sortStatementsInFile(new File(EXTERNAL_LINKS_SK));

        parseLinks(new File(TTL_FREEBASE_LINKS), new File(FREEBASE_LINKS_SK));
        sortStatementsInFile(new File(FREEBASE_LINKS_SK));

        parseLinks(new File(TTL_INTER_LANGUAGE_LINKS), new File(INTER_LANGUAGE_LINKS));
        sortStatementsInFile(new File(INTER_LANGUAGE_LINKS));

        //Links - Page Links
        parseLinks(new File(TTL_PAGE_LINKS_SK), new File(PAGE_LINKS_SK));
        sortStatementsInFile(new File(PAGE_LINKS_SK));

        parseLinks(new File(TTL_PAGE_LINKS_UNREDIRECTED_SK), new File(PAGE_LINKS_UNREDIRECTED_SK));
        sortStatementsInFile(new File(PAGE_LINKS_UNREDIRECTED_SK));

        //Redirects
        parseLinks(new File(TTL_REDIRECTS_SK), new File(REDIRECTS_SK));
        sortStatementsInFile(new File(REDIRECTS_SK));

        parseLinks(new File(TTL_REDIRECTS_TRANSITIVE_SK), new File(REDIRECTS_TRANSITIVE_SK));
        sortStatementsInFile(new File(REDIRECTS_TRANSITIVE_SK));

        //Revision URIs
        parseLinks(new File(TTL_REVISION_URIS_SK), new File(REVISION_URIS_SK));
        sortStatementsInFile(new File(REVISION_URIS_SK));

        //Wikipedia Links
        parseWikipediaLinks(new File(TTL_WIKIPEDIA_LINKS_SK), new File(WIKIPEDIA_LINKS_SK));
        sortStatementsInFile(new File(WIKIPEDIA_LINKS_SK));

        //Short Abstracts
        parseShortAbstracts(new File(TTL_SHORT_ABSTRACTS_SK), new File(SHORT_ABSTRACTS_SK));
        sortStatementsInFile(new File(SHORT_ABSTRACTS_SK));

        //Revision Ids
        parseRevisionIds(new File(TTL_REVISION_IDS_SK), new File(REVISION_IDS_SK));
        sortStatementsInFile(new File(REVISION_IDS_SK));

        //Skos Categories
        parseSkosCategories(new File(TTL_SKOS_CATEGORIES_SK), new File(SKOS_CATEGORIES_SK));
        sortStatementsInFile(new File(SKOS_CATEGORIES_SK));

        parseSkosCategories(new File(TTL_SKOS_CATEGORIES_EN_URIS_SK), new File(SKOS_CATEGORIES_EN_URIS_SK));
        sortStatementsInFile(new File(SKOS_CATEGORIES_EN_URIS_SK));

        long endTime = System.currentTimeMillis();
        System.out.println("Parsing took: " + (endTime - startTime) + " milliseconds, resp.: " + (endTime - startTime)/1000.0 + " seconds.");
        LOGGER.info("Parsing took: " + (endTime - startTime) + " milliseconds, resp.: " + (endTime - startTime)/1000.0 + " seconds.");
    }

    /**
     *  In this implementation, we are only parsing links and redirects from
     *  Czech dump of DB-Pedia. However, adding further parts to parse is
     *  very simple with usage of existing and implemented mechanisms
     * */
    @Override
    public void parseCzechDBPedia() {

        long startTime = System.currentTimeMillis();
        System.out.println("Started parsing entire Slovak DBPedia dump at: " + new Date());
        LOGGER.info("Started parsing entire Slovak DBPedia dump at: " + new Date());

        //Redirects and Transitive Redirects
        parseLinks(new File(TTL_REDIRECTS_CZ), new File(REDIRECTS_CZ));
        sortStatementsInFile(new File(REDIRECTS_CZ));

        parseLinks(new File(TTL_REDIRECTS_TRANSITIVE_CZ), new File(REDIRECTS_TRANSITIVE_CZ));
        sortStatementsInFile(new File(REDIRECTS_TRANSITIVE_CZ));

        //External Links
        parseLinks(new File(TTL_EXTERNAL_LINKS_CZ), new File(EXTERNAL_LINKS_CZ));
        sortStatementsInFile(new File(EXTERNAL_LINKS_CZ));

        //Page Links
        parseLinks(new File(TTL_PAGE_LINKS_CZ), new File(PAGE_LINKS_CZ));
        sortStatementsInFile(new File(PAGE_LINKS_CZ));

        //Page Links - Unredirected
        parseLinks(new File(TTL_PAGE_LINKS_UNREDIRECTED_CZ), new File(PAGE_LINKS_UNREDIRECTED_CZ));
        sortStatementsInFile(new File(PAGE_LINKS_UNREDIRECTED_CZ));

        //Freebase Links
        parseLinks(new File(TTL_FREEBASE_LINKS_CZ), new File(FREEBASE_LINKS_CZ));
        sortStatementsInFile(new File(FREEBASE_LINKS_CZ));

        //Inter-Language Links
        parseLinks(new File(TTL_INTER_LANGUAGE_LINKS_CZ), new File(INTER_LANGUAGE_LINKS_CZ));
        sortStatementsInFile(new File(INTER_LANGUAGE_LINKS_CZ));

        //Wikipedia Links
        parseWikipediaLinks(new File(TTL_WIKIPEDIA_LINKS_CZ), new File(WIKIPEDIA_LINKS_CZ));
        sortStatementsInFile(new File(WIKIPEDIA_LINKS_CZ));

        long endTime = System.currentTimeMillis();
        System.out.println("Parsing took: " + (endTime - startTime) + " milliseconds, resp.: " + (endTime - startTime)/1000.0 + " seconds.");
        LOGGER.info("Parsing took: " + (endTime - startTime) + " milliseconds, resp.: " + (endTime - startTime)/1000.0 + " seconds.");
    }

    public static ParserImpl getParserInstance(){
        if(parser == null){
            parser = new ParserImpl();
        }

        return parser;
    }

    @Override
    public void parseRdfFile(File input, File output, BasicRdfHandler handler){
        if(input == null || output == null || handler == null){
            return;
        }

        System.out.println("Parsing file: " + input);
        System.out.println("Output file: " + output);

        rdfParser.setRDFHandler(handler);

        try{
            //Define output .txt file
            bufferedWriter = new BufferedWriter(new FileWriter(output));

            //Define input .ttl file
            FileInputStream fileInputStream = new FileInputStream(input);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

            rdfParser.parse(inputStreamReader, "");

            System.out.println("Statements: " + handler.getNumberOfStatements());
            System.out.println("Unique categories: " + handler.getNumberOfStatementsAfter());
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseSkosCategories(File input, File output){
        SkosCategoriesHandler skosCategoriesHandler = new SkosCategoriesHandler();
        parseRdfFile(input, output, skosCategoriesHandler);
    }

    public void parseWikipediaLinks(File input, File output){
        WikipediaLinksHandler wikipediaLinksHandler = new WikipediaLinksHandler();
        parseRdfFile(input, output, wikipediaLinksHandler);
    }

    public void parseRevisionIds(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parseOutDegree(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parsePageIds(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parsePageLengths(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parseLongAbstracts(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parseShortAbstracts(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parseLabels(File input, File output){
        SimpleSubjectHandler simpleSubjectHandler = new SimpleSubjectHandler();
        parseRdfFile(input, output, simpleSubjectHandler);
    }

    public void parseLinks(File input, File output){
        SimpleLinksHandler simpleLinksHandler = new SimpleLinksHandler();
        parseRdfFile(input, output, simpleLinksHandler);
    }

    public void parseArticleCategories(File input, File output){
        CategoryLabelResultHandler categoryHandler = new CategoryLabelResultHandler();
        parseRdfFile(input, output, categoryHandler);
    }

    public void parseInfoboxProperties(File input, File output){
        InfoboxPropertiesHandler infoboxPropertiesHandler = new InfoboxPropertiesHandler();
        parseRdfFile(input, output, infoboxPropertiesHandler);
    }

    @Override
    public void sortStatementsInFile(File fileToSort){
        if(fileToSort == null){
            return;
        }

        try {
            FileReader fileReader = new FileReader(fileToSort);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            List<String> lines = new ArrayList<String>();
            String inputLine;

            while((inputLine = bufferedReader.readLine()) != null){
                lines.add(inputLine);
            }

            bufferedReader.close();
            fileReader.close();

            Collections.sort(lines);

            FileWriter fileWriter = new FileWriter(fileToSort);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for(String line: lines){
                printWriter.println(line);
            }

            printWriter.flush();
            printWriter.close();
            fileWriter.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeToFile(String text){
        if(bufferedWriter == null){
            return;
        }

        try {
            bufferedWriter.write(text);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
