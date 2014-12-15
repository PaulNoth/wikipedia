package infoboxNames;

import java.io.File;
import java.io.IOException;

//import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestOutput {


		@Test
		public void compareInfoboxNames() throws IOException {
		    final File expected = new File("C:/Temp/sample_output_enwiki-latest-pages-articles3.xml-p000025001p000055000.txt");
		    final File output = new File("C:/Temp/infobox_output.txt");
		    Assert.assertEquals(FileUtils.readLines(expected), FileUtils.readLines(output));
		}
			
}
