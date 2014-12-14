package dewiki_categories.parser;

public class Main {

	public static void main(String[] args) {
		try {
			
			ParserSQL psql = ParserSQL.getInstance();
			ParserXML pxml = ParserXML.getInstance();
			
			pxml.setPathToXML("some path");
			psql.setPathToCategoriesSQL("some path");
			psql.setPathToLinksSQL("some path");
			
			psql.parseCategFile();
			psql.parseLinksFile();
			
			WriteOutputFile.WriteSQLCategoriesMap(psql.getSqlHashmap());
			
			pxml.parseFile();
			
			WriteOutputFile.WriteXMLCategoriesMap(pxml.getCategHashmap(), pxml.getStats());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
