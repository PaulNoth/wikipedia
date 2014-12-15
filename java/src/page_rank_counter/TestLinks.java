import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class TestLinks {
	 private BufferedReader in = null;
	 
		@Before
		public void setUp() throws Exception {
			in = new BufferedReader(
		            new InputStreamReader(getClass().getResourceAsStream("/output-links.txt")));
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
 * Testing file output-links which is put out after parsing the input lastest-pagelinks. 
 * Tests if each row has 5 columns and tries to find a row with page with id 175361 
 * having page rank 1 and links count 91 pointing to page 8864.
 **/
		@Test
		public void test() {
			
			try {
				String line = null;
				while((line = in.readLine()) != null)
				{
					String[] bits = line.split(";");
					if(bits[0] == "175361")
					{
						if(bits[3]=="8864")
						{
							if(!(bits[1]=="91") && (bits[2]=="1,000"))
							{
								fail("In original file there was line with id 175361 with pagerank 1 " +
										"and was showing to 91 other pages as well as to page 8864");
							}
						}
						
					}
					if(bits.length < 5)
					{
						fail("Each row should contain id, name and pagerank");
					}
					if(!bits[2].contains(",") && !bits[4].contains(","))
					{
						fail("Page rank should be a double number");
					}
				}
			} catch (IOException e) {
				fail("Could not opend file");
			}
		}

}
