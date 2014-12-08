package control;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testParser() {
		StaxParse p = new StaxParse();
		p.parse("data/test_data.xml");
		
		assertEquals("XML should be parsed correctly name = Bigfoot","Bigfoot" , p.Name);
		assertEquals("XML should be parsed correctly alternative = Sasquatch","Sasquatch" , p.strAlternativeName);
	}

}
