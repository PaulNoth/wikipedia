package com.eriksuta.parser;


import com.eriksuta.data.Indexer;
import com.eriksuta.data.ParserImpl;
import com.eriksuta.page.SearchOptions;
import com.eriksuta.search.SearchService;
import com.eriksuta.search.SearchServiceImpl;
import com.eriksuta.data.search.SearchResultType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.Assert;

import java.io.*;

/**
 *  @author Erik Suta
 * */
public class BasicTest {

    private SearchService searchService = SearchServiceImpl.getInstance();

    private static final String DIR = "src/test/resources/test/";

    private static final String F_TEST_01_BASIC_SEARCH_BRATISLAVA = DIR + "test_01_basicSearch_Bratislava.txt";
    private static final String F_TEST_02_BASIC_SEARCH_BIOWARE = DIR + "test_02_basicSearch_Bioware.txt";
    private static final String F_TEST_03_BASIC_SEARCH_MASS_EFFECT = DIR + "test_03_basicSearch_MassEffect.txt";
    private static final String F_TEST_04_BASIC_SEARCH_STAR_WARS = DIR + "test_04_basicSearch_StarWars.txt";

    @BeforeClass
    public static void beforeClass(){
        System.out.println("==========|BASIC SEARCH TEST SUIT START|==========");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("==========|BASIC SEARCH TEST SUIT END|==========");
    }

    /**
     *  Run this test to perform complete parsing process
     *  !!WARNING!! - may take long time
     * */
    @Test
    public void performParseProcess(){
        ParserImpl parser = new ParserImpl();
        parser.parseSlovakDBPedia();
        parser.parseCzechDBPedia();
    }

    /**
     *  Run this test to perform complete indexing process
     *  !!WARNING!! - may take long time
     * */
    @Test
    public void performIndexProcess(){
        Indexer indexer = new Indexer();
        indexer.performIndexing();
    }

    @Test
    public void testBratislava(){
        SearchResultType result = searchService.search("Bratislava", new SearchOptions());
        System.out.println(result.toString());
    }

    @Test
    public void testBioware(){
        SearchResultType result = searchService.search("Bioware", new SearchOptions());
        System.out.println(result.toString());
    }

    @Test
    public void testMassEffect(){
        SearchResultType result = searchService.search("Mass Effect", new SearchOptions());
        System.out.println(result.toString());
    }

    @Test
    public void testStarWars(){
        SearchResultType result = searchService.search("Star_Wars", new SearchOptions());
        System.out.println(result.toString());
    }

    @Test
    public void test_01_basicSearch_Bratislava(){
        SearchResultType result = searchService.search("Bratislava", new SearchOptions());

        String expectedResult = readFile(F_TEST_01_BASIC_SEARCH_BRATISLAVA);

        Assert.assertEquals(result.toStringWithoutTime().trim(), expectedResult.trim());
    }

    @Test
     public void test_02_basicSearch_Bioware(){
        SearchResultType result = searchService.search("Bioware", new SearchOptions());

        String expectedResult = readFile(F_TEST_02_BASIC_SEARCH_BIOWARE);

        Assert.assertEquals(result.toStringWithoutTime().trim(), expectedResult.trim());
    }

    @Test
    public void test_03_basicSearch_MassEffect(){
        SearchResultType result = searchService.search("Mass Effect", new SearchOptions());

        String expectedResult = readFile(F_TEST_03_BASIC_SEARCH_MASS_EFFECT);

        Assert.assertEquals(result.toStringWithoutTime().trim(), expectedResult.trim());
    }

    @Test
    public void test_04_basicSearch_StarWars(){
        SearchResultType result = searchService.search("Star_Wars", new SearchOptions());

        String expectedResult = readFile(F_TEST_04_BASIC_SEARCH_STAR_WARS);

        Assert.assertEquals(result.toStringWithoutTime().trim(), expectedResult.trim());
    }

    private String readFile(String file) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            String ls = System.getProperty("line.separator");

            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
