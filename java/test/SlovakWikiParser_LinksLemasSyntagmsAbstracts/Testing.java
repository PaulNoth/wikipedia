package test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Testing {

	/**unit test - porovnanie so vzorovym suborom/
	 */
	public static Boolean unitTestContent(String output)
	{
		Boolean bResult = false;
		try
		{
			//get pattern file as string
			Path fileTypes = Paths.get("C:/Users/Domi/workspace/WikiParser/data/sample_output_parsed_sentences_and_links.xml");
			byte[] fileArray = Files.readAllBytes(fileTypes);
			String strPatternFile = new String(fileArray, "UTF-8");
			if(strPatternFile.equals(output))
				bResult=true;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return bResult;
	}
	
	public static Boolean unitTestStructure(String output)
	{
		Boolean bResult = false;
		try
		{
			Path fileTypes = Paths.get("C:/Users/Domi/workspace/WikiParser/data/sample_output_parsed_sentences_and_links.xml");
			byte[] fileArray = Files.readAllBytes(fileTypes);
			String strPatternFile = new String(fileArray, "UTF-8");
			if(strPatternFile.equals(output))
				bResult=true;
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		return bResult;
	}
	
}
