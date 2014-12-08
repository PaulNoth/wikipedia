package dewiki_categories.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import dewiki_categories.parser.ParserSQL;

public class TestSQLReturnValues {

	HashMap<String, Integer> result;
	
	@Before
	public void setUp() throws Exception {
		ParserSQL parser = ParserSQL.getInstance();
		parser.setPathToCategoriesSQL("C:\\Users\\Martin\\Downloads\\vi-sample\\input-sql.sql");
		parser.parseCategFile();
		result = parser.getSqlHashmap();
	}
	
	@Test
	public void testNumberOfOccurances() {
		for (Integer count : result.values()) {
			if(count == -1 || count == 0)
				fail("Number of occurances should not be " + count);
		}
	}
	
	@Test
	public void testNumberOfReturnedValues()
	{
		if(result.size() != 4)
			fail("Returned SQL HashMap has " + result.size() + " values!");
	}

}
