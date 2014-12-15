package dewiki_categories.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import dewiki_categories.parser.ParserXML;

public class TestXMLReturnValues {

	CharSequence seq;
	HashMap<String, Integer> result;
	
	@Before
	public void setUp() throws Exception {
		seq = new String("[]\\|");
		ParserXML p = ParserXML.getInstance();
		p.setPathToXML("C:\\Users\\Martin\\Downloads\\vi-sample\\input-xml.xml");
		p.parseFile();
		result = p.getCategHashmap();
	}

	@Test
	public void testSpecialCharacters() {
		for (String key : result.keySet()) {
			if(key.contains(seq)) {
				fail("Key " + key + " has some invalid characters!");
			}
		}
	}
	
	@Test
	public void testOccuranceNumbers() {
		for (int value : result.values()) {
			if(value != 1) {
				fail("Each key must have occurance of 1!");
			}
		}
	}
	
	@Test
	public void testNumberOfReturnedValues()
	{
		if(result.size() != 4)
			fail("Returned HashMap has " + result.size() + " values!");
	}

}
