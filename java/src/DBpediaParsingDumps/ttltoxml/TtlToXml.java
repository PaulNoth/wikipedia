package ttltoxml;


/**
 * @author Skrisa  Július
 * 
 * This is the main class of ttltoxml package. It manages the parsing and saving records into xml file.
 * For each ttl file there is data parser class which parsing the file record by record and pushing it to xml handler class
 */
public class TtlToXml {
	public static void GenerateXml() {
		XmlHandlerOfParsedTtlRecords xml = new XmlHandlerOfParsedTtlRecords();
		for(int i = 0; i < SettingsOfTtlFiles.Files.size() ; i++){  //run parser for each file
			
			DataParserOfTtlDumps parser = new DataParserOfTtlDumps(SettingsOfTtlFiles.Files.get(i));  
			while (parser.hasNext()) {
				RecordModel r = parser.Parse();  // RecordModel contains single record from file
				if(r != null)
					XmlHandlerOfParsedTtlRecords.handle(r);   // Parsed record is saved to xml document
			}
			
		}
		
		
		xml.saveXmlFile();    //saving xml document on disk
		System.out.println("File Generated!");
	}
}