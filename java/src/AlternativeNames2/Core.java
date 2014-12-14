package control;

import java.util.List;


public class Core {

	public static void main(String[] args) {
				
		StaxParse p = new StaxParse();
		p.parse("sample_text.xml");
		
		/*
		StaxParse sp = new StaxParse();
		sp.parse("enwiki-latest-pages-articles5_best.xml");//enwiki-latest-pages-articles5_best.xml; sample_orig.xml, enwiki-latest-pages-articles2.xml
		
		*/
		
		SearchLucene SL = new SearchLucene("indexDir","output_big_all.json");
		
		//SL.createIndex();
		SL.Stats();
		
		//SL.search("Name", "Eskrima",10);
		//SL.search("Alternative", "pes",500);
		
		

		//uz to facha, este musim zistit ako sa ma vyhladavat
		
	}

}
