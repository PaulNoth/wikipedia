package anchors;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.Test;

import anchors.File_with_anchor_txt;
import anchors.Record_list;
import anchors.XML_wiki;



public class Anchor_extraction_test {
	
	//prepare program output file for testing
	public void setUp() throws Exception
	{
		String file_input = "../data/sample_input_enwiki-pages-articles-123.xml";
		String file_output = "../data/anchor_test.txt";
		
		Record_list record_list = 	new Record_list();
		BufferedWriter bw = 		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_output, true),"UTF-8"));
		BufferedReader br = 		new BufferedReader(new InputStreamReader(new FileInputStream(file_output), "UTF8"));
		File_with_anchor_txt file = new File_with_anchor_txt(file_output, bw, br);
		XML_wiki xmlw = 			new XML_wiki(file_input, file, record_list);
		
		file.erase_file();	
		xmlw.read_wiki();
		bw.close();	//close text file
		
	}
	
	@Test
	public void test() throws Exception
	{
		setUp();		
	
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("../data/anchor_test.txt"), "UTF8"));
		BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("../data/sample_output_anchor_link.txt"), "UTF8"));
		
		String str_real_output = "";
		String str_expected_output = "";
		
		try {
			//real output
			String str;
			while((str = br.readLine()) != null)
			{	str_real_output = str_real_output + str + "\n";	}
			
			//expected output
			String str2;
			while((str2 = br2.readLine()) != null)
			{	str_expected_output = str_expected_output + str2 + "\n";	}
			
		}
		catch (UnsupportedEncodingException e) 
		{	System.out.println(e.getMessage());	} 
		catch (IOException e) 
		{	System.out.println(e.getMessage());	}
		catch (Exception e)
		{	System.out.println(e.getMessage());	}
		
		//System.out.println(str_real_output);
		//System.out.println(str_expected_output);
		
		assertEquals(str_expected_output, str_real_output);
	}
		

}
