package test;
import java.util.List;

import ttltoxml.DataParserOfTtlDumps;
import ttltoxml.RecordModel;
import ttltoxml.SettingsOfTtlFiles;
import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * @author Skrisa Július
 * 
 * This junit test is testing availability of test data in dump files.
 * Test will be searching for sample data in whole specified dumps to ensure the validity of these dumps.
 * Test data and dump files needs to be specified in ttltoxml.SettingsOfTtlFiles 
 * 			-  Files - real dump files - test will be looking for the sample data in these files
 * 			-  TestFiles - sample test files with data to be looked for 
 *
 */
@SuppressWarnings("deprecation")
public class TestInputPresenceOfTestEntitiesInDumps extends TestCase {
	
	public void testValidity() throws Exception {
	   
	   for(int j = 0; j < SettingsOfTtlFiles.Files.size() ; j++){
		   DataParserOfTtlDumps File = new DataParserOfTtlDumps(SettingsOfTtlFiles.Files.get(j),"test"); // real ttl dump files - defined in ttltoxml.settings
			List<RecordModel> listP = File.ParseTest();
			DataParserOfTtlDumps TestFile = new DataParserOfTtlDumps(SettingsOfTtlFiles.TestFiles.get(j),"test"); // test input files - defined in ttltoxml.settings
			List<RecordModel> listTF = TestFile.ParseTest();
		   
			boolean flag = true;
			 for(int k = 0; k < listTF.size(); k++){
				 RecordModel model = listTF.get(k);
				 flag = containsId(listP, model.getProperty(), model.getResource(), model.getValue());  // test if there are tested entities in real ttl dump files
				  if(flag){
				  	  break;
				  }
			  }
			 if(flag)
				 System.out.println("Each entity in file " + SettingsOfTtlFiles.TestFiles.get(j) + " was found in dump files");
			 Assert.assertEquals(true, flag);

	   }
	}
	
	public static boolean containsId(List<RecordModel> list, String p, String r, String v) {
	    for (RecordModel object : list) {
	        if (object.getProperty().equals(p) && object.getResource().equals(r)) {
	        	
	            return true;
	        }
	    }
	    return false;
	}
}
