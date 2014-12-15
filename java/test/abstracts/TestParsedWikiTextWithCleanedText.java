package abstracts;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author Matej Toma
 *
 */
public class TestParsedWikiTextWithCleanedText {

	@Test
	public void testParsedText(){
		String realText = "McConnell v.Federal Election Commission, 540 U.S. 93 (2003),<ref></ref> is a case in which the United States Supreme Court upheld the constitutionality of most of the Bipartisan Campaign Reform Act of 2002 (BCRA), often referred to as the McCain–Feingold Act.The case takes its name from Senator Mitch McConnell, Republican of Kentucky, and the Federal Election Commission, the federal agency that oversees U.S. campaign finance laws.It was partially overruled by Citizens United v.Federal Election Commission, 558 U.S. 310 (2010).";
		try{
			CosineSimilartyBasedOnParsedAbstracts.singleFileReader("sample_input_enwiki-latest-pages-articles9",false);
		}
		catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		
		}
		
		String parsedText = CosineSimilartyBasedOnParsedAbstracts.allPages.get(0).getTextTag();

		assertEquals("Zhoda stringov", realText, parsedText);
		System.out.println("@Test - testParsedText - prebehol uspesne");
	}
	
	@Test
	public void testNumberOfGoodResults(){
		assertEquals("Pocet dobrych zaznamom", 2, CosineSimilartyBasedOnParsedAbstracts.allPages.size());

		System.out.println("@Test - testNumberOfGoodResults - prebehol uspesne");
	}
}
