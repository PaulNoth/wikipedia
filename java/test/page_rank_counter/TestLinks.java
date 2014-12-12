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
		            new InputStreamReader(getClass().getResourceAsStream("/output-pageRank.txt")));
		}

		@After
		public void tearDown() throws Exception {
			if (in != null)
	        {
	            in.close();
	        }

	        in = null;
		}

		@Test
		public void test() {
			
			try {
				String line = in.readLine();
				String[] bits = line.split(";");
				if(bits.length < 3)
				{
					fail("Each row should contain id, name and pagerank");
				}
				if(!bits[2].contains(","))
				{
					fail("Page rank should be a double number");
				}
			} catch (IOException e) {
				fail("Could not opend file");
			}
		}

}
