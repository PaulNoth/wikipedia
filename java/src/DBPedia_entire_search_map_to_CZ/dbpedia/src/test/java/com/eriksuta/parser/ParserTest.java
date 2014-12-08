package com.eriksuta.parser;

import com.eriksuta.data.IParser;
import com.eriksuta.data.ParserImpl;
import com.eriksuta.data.handler.*;
import com.eriksuta.data.types.InfoboxObject;
import com.eriksuta.data.types.SimpleMultiValueObject;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *  @author Erik Suta
 *
 *  !!!!!IMPORTANT - read before launching!!!!!
 *
 *  Before you run this test suite, make sure, that you place following RDF data samples to src/test/resourcec/test/data
 *  directory or define new directory. Just make sure there are some data that test suite will run with.
 *    - article_categories_sk.ttl
 *    - infobox_properties_sk.ttl
 *    - page_ids_sk.ttl
 *    - short_abstracts_sk.ttl
 *    - wikipedia_links_sk.ttl
 *
 *  !!!!!IMPORTANT - read before launching!!!!!
 *
 *  This test suite test the correctness of parsing process. Firt, we will parse provided files with
 *  created parsing algorithms and store them in JSON format in .txt files. Than we will manually
 *  search these files and locate predefined Strings. Since the data may be extended in newer
 *  version of DBPedia, the test will succeed if there is at least the number of found entries as in
 *  DBPedia version dated to 12.02.2014
 *
 * */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParserTest {

    private static final Logger LOGGER = Logger.getLogger(ParserTest.class);
    private static IParser parser;

    private static final String TEST_SEARCH_TERM = "Bratislava";

    //Data from: 02.12.2014 DBPedia dump
    private static final Integer EXP_OCC_LABELS_ARTICLE_CATEGORIES = 334;
    private static final Integer EXP_OCC_LABELS_INFOBOX_PROPERTIES = 180;
    private static final Integer EXP_OCC_LABELS_PAGE_IDS = 628;
    private static final Integer EXP_OCC_LABELS_SHORT_ABSTRACTS = 601;
    private static final Integer EXP_OCC_LABELS_WIKIPEDIA_LINKS = 596;
    private static final Integer EXP_OCC_LABELS_FREEBASE_LINKS = 145;

    //Data from: 02.12.2014
    private static final Integer EXP_OCC_ENTIRE_ARTICLE_CATEGORIES = 707;
    private static final Integer EXP_OCC_ENTIRE_INFOBOX_PROPERTIES = 4474;
    private static final Integer EXP_OCC_ENTIRE_PAGE_IDS = 628;
    private static final Integer EXP_OCC_ENTIRE_SHORT_ABSTRACTS = 5015;
    private static final Integer EXP_OCC_ENTIRE_WIKIPEDIA_LINKS = 1192;
    private static final Integer EXP_OCC_ENTIRE_FREEBASE_LINKS = 145;

    public static final String DATA_DIR = "src/test/resources/test/data/";
    public static final String TTL_ARTICLE_CATEGORIES_SK = DATA_DIR + "article_categories_sk.ttl";
    public static final String TTL_INFOBOX_PROPERTIES_SK = DATA_DIR + "infobox_properties_sk.ttl";
    public static final String TTL_PAGE_IDS_SK = DATA_DIR + "page_ids_sk.ttl";
    public static final String TTL_SHORT_ABSTRACTS_SK = DATA_DIR + "short_abstracts_sk.ttl";
    public static final String TTL_WIKIPEDIA_LINKS_SK = DATA_DIR + "wikipedia_links_sk.ttl";
    public static final String TTL_FREEBASE_LINKS_SK = DATA_DIR + "freebase_links_sk.ttl";

    public static final String ARTICLE_CATEGORIES_SK = DATA_DIR + "article_categories_sk.txt";
    public static final String INFOBOX_PROPERTIES_SK = DATA_DIR + "infobox_properties_sk.txt";
    public static final String PAGE_IDS_SK = DATA_DIR + "page_ids_sk.txt";
    public static final String SHORT_ABSTRACTS_SK = DATA_DIR + "short_abstracts_sk.txt";
    public static final String WIKIPEDIA_LINKS_SK = DATA_DIR + "wikipedia_links_sk.txt";
    public static final String FREEBASE_LINKS_SK = DATA_DIR + "freebase_links_sk.txt";

    @BeforeClass
    public static void beforeClass(){
        LOGGER.info("==========|BASIC PARSER TEST SUIT START|==========");

        parser = new ParserImpl();
    }

    @AfterClass
    public static void afterClass(){
        LOGGER.info("==========|BASIC PARSER TEST SUIT END|==========");
    }

    @Test
    public void test001_parse_articleCategories(){
        printTestName("test01_parse_articleCategories");
        CategoryLabelResultHandler handler = new CategoryLabelResultHandler();
        parser.parseRdfFile(new File(TTL_ARTICLE_CATEGORIES_SK), new File(ARTICLE_CATEGORIES_SK), handler);
        parser.sortStatementsInFile(new File(ARTICLE_CATEGORIES_SK));

        File parsedFile = new File(ARTICLE_CATEGORIES_SK);

        Assert.assertEquals(true, parsedFile.exists());
    }

    @Test
    public void test002_parse_infoboxProperties(){
        printTestName("test02_parse_infoboxProperties");
        InfoboxPropertiesHandler handler = new InfoboxPropertiesHandler();
        parser.parseRdfFile(new File(TTL_INFOBOX_PROPERTIES_SK), new File(INFOBOX_PROPERTIES_SK), handler);
        parser.sortStatementsInFile(new File(INFOBOX_PROPERTIES_SK));

        File parsedFile = new File(INFOBOX_PROPERTIES_SK);

        Assert.assertEquals(true, parsedFile.exists());
    }

    @Test
    public void test003_parse_pageIds(){
        printTestName("test03_parse_pageIds");
        SimpleSubjectHandler handler = new SimpleSubjectHandler();
        parser.parseRdfFile(new File(TTL_PAGE_IDS_SK), new File(PAGE_IDS_SK), handler);
        parser.sortStatementsInFile(new File(PAGE_IDS_SK));

        File parsedFile = new File(PAGE_IDS_SK);

        Assert.assertEquals(true, parsedFile.exists());
    }

    @Test
    public void test004_parse_shortAbstracts(){
        printTestName("test04_parse_shortAbstracts");
        SimpleSubjectHandler handler = new SimpleSubjectHandler();
        parser.parseRdfFile(new File(TTL_SHORT_ABSTRACTS_SK), new File(SHORT_ABSTRACTS_SK), handler);
        parser.sortStatementsInFile(new File(SHORT_ABSTRACTS_SK));

        File parsedFile = new File(SHORT_ABSTRACTS_SK);

        Assert.assertEquals(true, parsedFile.exists());
    }

    @Test
    public void test005_parse_wikipediaLinks(){
        printTestName("test05_parse_wikipediaLinks");
        WikipediaLinksHandler handler = new WikipediaLinksHandler();
        parser.parseRdfFile(new File(TTL_WIKIPEDIA_LINKS_SK), new File(WIKIPEDIA_LINKS_SK), handler);
        parser.sortStatementsInFile(new File(WIKIPEDIA_LINKS_SK));

        File parsedFile = new File(WIKIPEDIA_LINKS_SK);

        Assert.assertEquals(true, parsedFile.exists());
    }

    @Test
    public void test006_parse_freebaseLinks(){
        printTestName("test06_parse_freebaseLinks");
        SimpleLinksHandler handler = new SimpleLinksHandler();
        parser.parseRdfFile(new File(TTL_FREEBASE_LINKS_SK), new File(FREEBASE_LINKS_SK), handler);
        parser.sortStatementsInFile(new File(FREEBASE_LINKS_SK));

        File parsedFile = new File(FREEBASE_LINKS_SK);

        Assert.assertEquals(true, parsedFile.exists());
    }

    @Test
    public void test007_searchInLabels_articleCategories(){
        printTestName("test07_searchInLabels_articleCategories");
        Gson gson = new Gson();
        int numberOfOccurrences = 0;

        File file = new File(ARTICLE_CATEGORIES_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                SimpleMultiValueObject category = gson.fromJson(line, SimpleMultiValueObject.class);

                if(category.getLabel().contains(TEST_SEARCH_TERM)){
                    numberOfOccurrences++;
                }
            }

        } catch (Exception e){
            logException(e, "test07_searchInLabels_articleCategories");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "article_categories_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_LABELS_ARTICLE_CATEGORIES,
                "Oops, something is wrong with parsing articleCategories.");

    }

    @Test
    public void test008_searchInLabels_infoboxProperties(){
        printTestName("test08_searchInLabels_infoboxProperties");
        Gson gson = new Gson();
        int numberOfOccurrences = 0;

        File file = new File(INFOBOX_PROPERTIES_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                InfoboxObject infoboxPropertyObject = gson.fromJson(line, InfoboxObject.class);

                if(infoboxPropertyObject.getLabel().contains(TEST_SEARCH_TERM)){
                    numberOfOccurrences++;
                }
            }

        } catch (Exception e){
            logException(e, "test08_searchInLabels_infoboxProperties");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "infobox_properties_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_LABELS_INFOBOX_PROPERTIES,
                "Oops, something is wrong with parsing infoboxProperties.");
    }

    @Test
    public void test009_searchInLabels_pageIds(){
        printTestName("test09_searchInLabels_pageIds");
        Gson gson = new Gson();
        int numberOfOccurrences = 0;

        File file = new File(PAGE_IDS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                SimpleMultiValueObject pageIds = gson.fromJson(line, SimpleMultiValueObject.class);

                if(pageIds.getLabel().contains(TEST_SEARCH_TERM)){
                    numberOfOccurrences++;
                }
            }

        } catch (Exception e){
            logException(e, "test09_searchInLabels_pageIds");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "page_ids_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_LABELS_PAGE_IDS,
                "Oops, something is wrong with parsing page IDs.");
    }

    @Test
    public void test010_searchInLabels_shortAbstracts(){
        printTestName("test10_searchInLabels_shortAbstracts");
        Gson gson = new Gson();
        int numberOfOccurrences = 0;

        File file = new File(SHORT_ABSTRACTS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                SimpleMultiValueObject shortAbstractList = gson.fromJson(line, SimpleMultiValueObject.class);

                if(shortAbstractList.getLabel().contains(TEST_SEARCH_TERM)){
                    numberOfOccurrences++;
                }
            }

        } catch (Exception e){
            logException(e, "test10_searchInLabels_shortAbstracts");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "short_abstracts_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_LABELS_SHORT_ABSTRACTS,
                "Oops, something is wrong with parsing short abstracts.");
    }

    @Test
    public void test011_searchInLabels_wikipediaLinks(){
        printTestName("test11_searchInLabels_wikipediaLinks");
        Gson gson = new Gson();
        int numberOfOccurrences = 0;

        File file = new File(WIKIPEDIA_LINKS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                SimpleMultiValueObject wikiLinksObject = gson.fromJson(line, SimpleMultiValueObject.class);

                if(wikiLinksObject.getLabel().contains(TEST_SEARCH_TERM)){
                    numberOfOccurrences++;
                }
            }

        } catch (Exception e){
            logException(e, "test11_searchInLabels_wikipediaLinks");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "wikipedia_links_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_LABELS_WIKIPEDIA_LINKS,
                "Oops, something is wrong with parsing wikipedia links.");
    }

    @Test
    public void test012_searchInLabels_freebaseLinks(){
        printTestName("test12_searchInLabels_freebaseLinks");
        Gson gson = new Gson();
        int numberOfOccurrences = 0;

        File file = new File(FREEBASE_LINKS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                SimpleMultiValueObject freebaseLinksObject = gson.fromJson(line, SimpleMultiValueObject.class);

                if(freebaseLinksObject.getLabel().contains(TEST_SEARCH_TERM)){
                    numberOfOccurrences++;
                }
            }

        } catch (Exception e){
            logException(e, "test12_searchInLabels_freebaseLinks");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "freebase_links_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_LABELS_FREEBASE_LINKS,
                "Oops, something is wrong with parsing freebase links.");
    }

    @Test
    public void test013_searchInEntireFile_articleCategories(){
        printTestName("test013_searchInEntireFile_articleCategories");
        int numberOfOccurrences = 0;

        File file = new File(ARTICLE_CATEGORIES_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                numberOfOccurrences += StringUtils.countMatches(line, TEST_SEARCH_TERM);
            }

        } catch (Exception e){
            logException(e, "test013_searchInEntireFile_articleCategories");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "article_categories_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_ENTIRE_ARTICLE_CATEGORIES,
                "Oops, something is wrong with parsing article categories.");
    }

    @Test
    public void test014_searchInEntireFile_infoboxProperties(){
        printTestName("test014_searchInEntireFile_infoboxProperties");
        int numberOfOccurrences = 0;

        File file = new File(INFOBOX_PROPERTIES_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                numberOfOccurrences += StringUtils.countMatches(line, TEST_SEARCH_TERM);
            }

        } catch (Exception e){
            logException(e, "test014_searchInEntireFile_infoboxProperties");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "infobox_properties_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_ENTIRE_INFOBOX_PROPERTIES,
                "Oops, something is wrong with parsing infobox properties.");
    }

    @Test
    public void test015_searchInEntireFile_pageIds(){
        printTestName("test015_searchInEntireFile_pageIds");
        int numberOfOccurrences = 0;

        File file = new File(PAGE_IDS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                numberOfOccurrences += StringUtils.countMatches(line, TEST_SEARCH_TERM);
            }

        } catch (Exception e){
            logException(e, "test015_searchInEntireFile_pageIds");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "page_ids_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_ENTIRE_PAGE_IDS,
                "Oops, something is wrong with parsing page ids.");
    }

    @Test
    public void test016_searchInEntireFile_shortAbstracts(){
        printTestName("test016_searchInEntireFile_shortAbstracts");
        int numberOfOccurrences = 0;

        File file = new File(SHORT_ABSTRACTS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                numberOfOccurrences += StringUtils.countMatches(line, TEST_SEARCH_TERM);
            }

        } catch (Exception e){
            logException(e, "test016_searchInEntireFile_shortAbstracts");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "short_abstracts_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_ENTIRE_SHORT_ABSTRACTS,
                "Oops, something is wrong with parsing short abstracts.");
    }

    @Test
    public void test017_searchInEntireFile_wikipediaLinks(){
        printTestName("test017_searchInEntireFile_wikipediaLinks");
        int numberOfOccurrences = 0;

        File file = new File(WIKIPEDIA_LINKS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                numberOfOccurrences += StringUtils.countMatches(line, TEST_SEARCH_TERM);
            }

        } catch (Exception e){
            logException(e, "test017_searchInEntireFile_wikipediaLinks");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "wikipedia_links_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_ENTIRE_WIKIPEDIA_LINKS,
                "Oops, something is wrong with parsing wikipedia links.");
    }

    @Test
    public void test018_searchInEntireFile_freebaseLinks(){
        printTestName("test018_searchInEntireFile_freebaseLinks");
        int numberOfOccurrences = 0;

        File file = new File(FREEBASE_LINKS_SK);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                numberOfOccurrences += StringUtils.countMatches(line, TEST_SEARCH_TERM);
            }

        } catch (Exception e){
            logException(e, "test018_searchInEntireFile_freebaseLinks");
        }

        LOGGER.info("For search term: '" + TEST_SEARCH_TERM + "', there are: "
                + numberOfOccurrences + " in: " + "freebase_links_sk.txt file.");

        Assert.assertNotSame(0, numberOfOccurrences);
        Assert.assertTrue(numberOfOccurrences >= EXP_OCC_ENTIRE_FREEBASE_LINKS,
                "Oops, something is wrong with parsing freebase links.");
    }

    private void printTestName(String testName){
        LOGGER.info("Starting test: " + testName);
    }

    private void logException(Exception e, String testName){
        LOGGER.error("Exception occurred in test: " + testName + ", Reason: " + e.getMessage(), e);
    }
}
