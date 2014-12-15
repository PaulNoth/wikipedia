package wikical.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import wikical.settings.Settings;

public class PeopleOutputTest {

	/**
	 * Compare tested output with hard coded strings - if they match, test will write it's OK. Otherwise will write test has failed.
	 * 
	 * Pre vytvorenie unit testu je nutné postupova pod¾a nasledujúcich krokov:

	1. Je nutné si vytvori XML súbor, ktorého zloenie je nasledujúce:

		Na zaèiatok treba vloi záznam root elementu Wikipédie
		<mediawiki xmlns="http://www.mediawiki.org/xml/export-0.9/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.mediawiki.org/xml/export-0.9/ http://www.mediawiki.org/xml/export-0.9.xsd" version="0.9" xml:lang="sk">
		Na slovenskej stránke Wikipédie je nutné nájs stránku:

		<page>
    		<title>Alan Mathison Turing</title>
		Od tohto bodu po koniec </page>, ktorá obsahovala title "George Boole" je nutné skopírova dáta do vytvoreného XML súboru.
		Na koniec je nutné element mediawiki ukonèi
		</mediawiki>
		Pre tento súbor je nutné, aby bol kódovanı pomocou UTF-8
 
	2. XML súbor, ktorı bude obsahova asi 340 riadkov treba následne v programe vybra a spusti pomocou tlaèidla  "Wikipedia source file".
	Následne pomocou tlaèidla "Generate" sa vygeneruje súbor WikiCalPeople.txt, ktorı sa automaticky zvolí ako zdroj v pravej strane aplikácie - "Folder 
	of parsed files". Pri zvolení tlaèidla "Run unity test" prebehne test správnosti vıstupu. V prípade úspechu bude v konzole programu vypísanı SUCCESS. 
	 * 
	 * 
	 * Tento navod sa nachadza aj na stranke http://vi.ikt.ui.sav.sk/User:Martin_Polak?view=home
	 * 
	 * @param sourceFolder
	 */
	public PeopleOutputTest(String sourceFolder) {
		File sourceFolderFile = new File(sourceFolder + Settings.nameOfPeopleFile);

		if (sourceFolderFile.exists()) {

			BufferedReader in;
			try {
				in = new BufferedReader(new FileReader(sourceFolderFile));

				int goodElements = 0;
				int i = 0;
				while (in.ready()) {
					String s = in.readLine();
					if (i == 0 && s.equals("Alan Mathison Turing;23.6.1912;7.6.1954")) {
						System.out.println("1. element OK");
						goodElements++;
					} else if (i == 1 && s.equals("George Boole;2.11.1815;8.12.1864")) {
						System.out.println("2. element OK");
						goodElements++;
					}
					i++;
				}

				if (goodElements == 2) {
					System.out.println("Test SUCCESSFULL");
				} else {
					System.out.println("Test FAILED");
				}

				in.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Testing source file "+ Settings.nameOfPeopleFile + " not found!");
		}
	}
}
