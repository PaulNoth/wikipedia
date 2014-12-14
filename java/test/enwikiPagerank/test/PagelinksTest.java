package enwikiPagerank.test;
/*
 * unit test for pagelinks
 */

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import enwikiPagerank.parseToText.PagelinksFileRW;

public class PagelinksTest {
	public static String inputFile;
	public static String output;
	public static String expected;

	public static void populateParameters(String a, String b, String c){
		inputFile = a;
		output = b;
		expected = c;
	}
	
	@Test
	public void testForEqualOutput() {

		//;
		ExOutFileReader ex = new ExOutFileReader();
		List<String> got = ex.readParsedFileToStringList(output); //read newly parsed file
		ExOutFileReader ex2 = new ExOutFileReader();
		List<String> expe = ex2.readFileToStringList(expected); //read expected file
		for(String s : got){
			System.out.println(s);
		}
		
		System.out.println(expe.size());
		System.out.println(got.size());
		assertArrayEquals(expe.toArray(), got.toArray());

	}

}
