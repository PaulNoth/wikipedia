import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestPages {

	 private BufferedReader in = null;
	 
	@Before
	public void setUp() throws Exception {
		in = new BufferedReader(
	            new InputStreamReader(getClass().getResourceAsStream("/output-skwiki-latest-page.txt")));
	}

	@After
	public void tearDown() throws Exception {
		if (in != null)
        {
            in.close();
        }

        in = null;
	}
/**
 * Testing the output from parsing file skwiki-latest-pages. Controlling first row if it has the input of main page,
 * and if it has 3 columns.
 */
	@Test
	public void test() {
		
		try {
			String line = in.readLine();
			String[] bits = line.split(";");
			if(!bits[0].equals("1") && !bits[1].equals("0") && !bits[1].equals("Hlavná_stránka"))
			{
				fail("Hlavná_stránka is inputed first. Is this the right file?");
			}
			while((line = in.readLine()) != null)
			{
				bits = line.split(";");
				if(bits.length < 3)
				{
					fail("Each row should contain id, namespace id and name");
				}
			}
			
			
		} catch (IOException e) {
			fail("Could not opend file");
		}
	}

}
