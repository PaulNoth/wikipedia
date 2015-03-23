package LangLinksParsing;

public class Runner {

	public static void main(String[] args) throws InterruptedException {
		
		
		
//		 System.out.println("Reading langlinks cs. Please wait...");
//		 LangLinksCsParser llcp = new LangLinksCsParser("dumps/cswiki-latest-langlinks.sql");	
//		 
//		 System.out.println("Reading page cs. Please wait...");
//		 LatestPageParserCs lppc = new LatestPageParserCs("dumps/cswiki-latest-page.sql");
//		
//		 System.out.println("Begin");
//		 LatestPageParser lpp = new LatestPageParser("dumps/skwiki-latest-page.sql");		 
//		 
//		 @SuppressWarnings("unused")
//		 Linker linker = new Linker("dumps/skwiki-latest-langlinks.sql", lpp, llcp,lppc,"sk","cs");
//		//  Linker linker = new Linker("dumps/skwiki-latest-langlinks.sql", lpp, "sk","cs");
		

		switch (args[0]) {
		case "-h":
			System.out
					.println("Help: \nThis application searches for language links in language given as parameter and matching with"
							+ "domain language. With parameter -r, backmap.\nUsage:\n"
							+ "\tDefault: parsing.jar [FILE_PAGE] [FILE_LANGLINKS] [LANGUAGE_1] [LANGUAGE_2]\n\n"
							+ "\t\tFILE_PAGE - SQL dump from wikipedia where are stored all titles in domain language\n"
							+ "\t\tFILE_LANGLINKS - language links (SQL dump) from domain wikipedia where are stored all language links\n"
							+ "\t\tLANGUAGE_1 - domain language\n"
							+ "\t\tLANGUAGE_2 - language in which to search language links\n"
							+ "\t -r: parsing.jar [FILE_PAGE_1] [FILE_LANGLINKS_1] [FILE_PAGE_2] [FILE_LANGLINKS_2] [LANGUAGE_1] [LANGUAGE_2]\n\n"
							+ "\t\tFILE_LANGLINKS_2 - language links (SQL dump) from second wikipedia where are stored all language links\n"
							+ "\t\tFILE_PAGE_2 - language links (SQL dump) from second wikipedia where are stored all titles\n");
			break;

		case "-r":
			System.out.println("Creating map from " + args[1]
					+ " please wait...");
			DomainPageParser dpp = new DomainPageParser(args[1]);
			
			System.out.println("Creating map from " + args[3] + " please wait...");
			HostPageParser hpp = new HostPageParser(args[3]);
			
			System.out.println("Creating map from " + args[4]
					+ " please wait...");
			HostLangLinksParser hllp = new HostLangLinksParser(args[4], args[5]);	
			
			

			System.out.println("Operation started - searching for "+args[6]+" language links in "+args[5]+" wikipedia sql dump with back tra"
					+ "cking in "+args[4]+" .");
			@SuppressWarnings("unused")
			Linker linker = new Linker(args[2], dpp, hllp, hpp, args[5],args[6]);
			break;

		default: {
			System.out.println("Creating map from " + args[0] + " please wait...");
			DomainPageParser dpp_d = new DomainPageParser(args[0]);
			System.out.println("Operation started - searching for "+args[3]+" language links in "+args[1]+"\n");
			@SuppressWarnings("unused")
			Linker linker_d = new Linker(args[1], dpp_d, args[2], args[3]);
			break;
		}

		}
	}
}
