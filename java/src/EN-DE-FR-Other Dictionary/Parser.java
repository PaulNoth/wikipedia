package dataManipulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.HashMap;

public class Parser {

	static String sintlinksPath = "data\\interlanguage_links_en.ttl";
	static String sintlinksPathFR = "data\\interlanguage_links_fr.ttl";
	static String sintlinksPathDE = "data\\interlanguage_links_de.ttl";
	static String intlinksPath = "data\\sample_en.txt";
	static String intlinksPathFR = "data\\sample_fr.txt";
	static String intlinksPathDE = "data\\sample_de.txt";

	BufferedReader reader = null;
	String line;
	Writer writer;
	String firstStatistik;
	int numb = 0;
	HashMap<String, Languages> German = new HashMap<String, Languages>();
	HashMap<String, Languages> Lang = new HashMap<String, Languages>();
	HashMap<String, Languages> French = new HashMap<String, Languages>();

	// hashmap
	public static int findIndex(String temp, int occurence, String word) {
		int index = 0;
		int ret = 0;
		for (int i = 0; i < occurence; i++) {
			index = temp.indexOf(word);
			ret += index;
			temp = temp.substring(index + 1);
		}
		return ret;
	}

	public void parse() {
		// test();
		System.setProperty("file.encoding","UTF-8");
		try {
		Field charset = Charset.class.getDeclaredField("defaultCharset");
		charset.setAccessible(true);
		charset.set(null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parseEnglish();
		parseGerman();
		parseFrench();
		writeToFile();
	}

	public void parseEnglish() {
		String german = null;
		String english = "";
		String french = null;
		String other = "";
		int i = 0;

		try {

			writer = new BufferedWriter(new FileWriter("Other.txt"));
			reader = new BufferedReader(new FileReader(sintlinksPath));
			while ((line = reader.readLine()) != null) {
				if (line.contains("started"))
					line = reader.readLine();

				try {
					while (line.contains("wikidata")) {
						line = reader.readLine();
					}

					int index1 = line.indexOf("resource");
					int index2 = line.indexOf(">");
					german = null;
					french = null;

					if (!english.equals(english = line.substring(index1 + 9,
							index2))) {
						english = line.substring(index1 + 9, index2);
					}
					
					while (english.equals(line.substring(index1 + 9, index2))) {
						
						int index3 = findIndex(line, 2, "resource");
						
						int index4 = findIndex(line, 3, ">");

						if (line.contains("de."))
							german = line.substring(index3 + 10, index4 + 2);

						else if (line.contains("fr."))
							french = line.substring(index3 + 10, index4 + 2);

						else if (line.contains("dbpedia")) {
							String lang = line.substring(
									findIndex(line, 3, "http://") + 9,
									findIndex(line, 2, ".dbpedia") + 1);
							other += "\t" + lang + ":"
									+ line.substring(index3 + 10, index4 + 2);
						}

						line = reader.readLine();

						if (line.contains("completed"))
							break;

						index1 = line.indexOf("resource");
						index2 = line.indexOf(">");
					}
					if(other.equals("")) other = "\t_null";
					
					
					writer.write(english+other);
					writer.write(System.getProperty("line.separator"));

					Languages l = new Languages();
					// l.setOther(other);
					if (french != null)
						l.setFrench("ano");
					if (german != null)
						l.setGerman("ano");
					// vlozenie anglickeho slovicka do
					Lang.put(english, l);
					//i++;
					//System.out.println(i);
					other = "";

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(line);
					break;
				}
			}
			reader.close();
			writer.close();
			System.out.println("koniec");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void parseFrench() {

		String german = "";
		String english = "";
		String french = "";
		String other = "";
		int frenchStat = 0;
		int i = 0;

		try {

			reader = new BufferedReader(new FileReader(sintlinksPathFR));
			while ((line = reader.readLine()) != null) {
				if (line.contains("started"))
					line = reader.readLine();

				try {

					int index1 = line.indexOf("resource");
					int index2 = line.indexOf(">");
					if (line.contains("completed"))
						break;
					if (!french.equals(french = line.substring(index1 + 9,
							index2))) {
						frenchStat++;
						french = line.substring(index1 + 9, index2);
					}

					while (line.contains("wikidata")) {
						line = reader.readLine();
					}

					while (line.contains(french)) {

						if (line.contains("wikidata.org"))
							break;
						if (line.contains("completed"))
							break;

						int index3 = findIndex(line, 2, "resource");
						;
						int index4 = findIndex(line, 3, ">");

						if (line.contains("de."))
							german = line.substring(index3 + 10, index4 + 2);

						else if (line.contains("//dbpedia.org"))
							english = line.substring(index3 + 10, index4 + 2);

						line = reader.readLine();
						if (line.contains("completed"))
							break;

						index1 = line.indexOf("resource");
						index2 = line.indexOf(">");
					}

					i++;
					if (english.equals(""))
						;

					else if (Lang.get(english) == null) {
						numb++;
						Languages l = new Languages();
						l.setFrench(french);
						Lang.put(Integer.toString(numb), l);
						System.out.println("neobsahuje: " + english);
					}

					// if the english word is there, just add the french
					else if (("ano").equals(Lang.get(english).getFrench())) {
						Lang.get(english).setFrench(french);
					}
					
					Languages l = new Languages();
					// l.setOther(other);
					if (!english.equals(""))
						l.setBenglish(true);
					if (!german.equals(""))
						l.setBgerman(true);
					// vlozenie francuzskeho slovicka do franc slovnika
					French.put(french, l);
					
					english = "";
					german = "";
					
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(line + " line: " + i);
					break;
				}
				
			}
			reader.close();
			firstStatistik += System.getProperty("line.separator") + "Francuzskych slov je: " + frenchStat;
			
			System.out.println("French parser complete");
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void parseGerman() {
		String german = "";
		String english = "";
		String french = "";
		String other = "";
		int germanStat = 0;
		int i = 0;

		try {

			reader = new BufferedReader(new FileReader(sintlinksPathDE));
			while ((line = reader.readLine()) != null) {
				if (line.contains("started"))
					line = reader.readLine();

				try {

					int index1 = line.indexOf("resource");
					int index2 = line.indexOf(">");
					if (line.contains("completed"))
						break;
					if (!german.equals(german = line.substring(index1 + 9,
							index2))) {
						germanStat++;
						german = line.substring(index1 + 9, index2);
					}

					while (line.contains("wikidata")) {
						line = reader.readLine();
					}

					while (line.contains(german)) {

						if (line.contains("wikidata.org"))
							break;
						if (line.contains("completed"))
							break;

						int index3 = findIndex(line, 2, "resource");
						
						int index4 = findIndex(line, 3, ">");

						if (line.contains("fr."))
							french = line.substring(index3 + 10, index4 + 2);

						else if (line.contains("//dbpedia.org"))
							english = line.substring(index3 + 10, index4 + 2);

						line = reader.readLine();
						if (line.contains("completed"))
							break;

						index1 = line.indexOf("resource");
						index2 = line.indexOf(">");
					}

					i++;
					if (english.equals(""))
						;

					else if (Lang.get(english) == null) {
						numb++;
						Languages l = new Languages();
						l.setGerman(german);
						Lang.put(Integer.toString(numb), l);
						System.out.println("neobsahuje: " + english);
					}

					// if the english word is there, just add the french
					else if (("ano").equals(Lang.get(english).getGerman())) {
						Lang.get(english).setGerman(german);
					}
					
					Languages l = new Languages();
					// l.setOther(other);
					if (!french.equals(""))
						l.setBfrench(true);
					// vlozenie francuzskeho slovicka do franc slovnika
					German.put(german, l);
					
					english = "";
					french = "";
					
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(line + " line: " + i);
					break;
				}
			}
			firstStatistik += "Nemeckych slov je: " + germanStat + System.getProperty("line.separator");
			System.out.println("German parser complete");
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void write(String sentence, boolean close) {
		try {
			writer = new BufferedWriter(new FileWriter("Other.txt"));

			writer.write(sentence);
			writer.write(System.getProperty("line.separator"));
			if (close)
				writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void writeToFile() {
		int onlyGerman = 0;
		int onlyFrench = 0;
		int both = 0;
		int frenchOnGerman = 0;
		int germanOnFrench = 0;
		String french;
		String german;
		String english = "";
		String other;
		String writ = "";
		int lines = 0;
		boolean Fukazuje = false;
		boolean Gukazuje = false;
		
		try {
			reader = new BufferedReader(new FileReader("Other.txt"));
			writer = new BufferedWriter(new FileWriter("Dictionary.txt"));

			while((line = reader.readLine())!=null)
			{
			english = line.substring(0, line.indexOf("\t"));
			other = line.substring(line.indexOf("\t")+1, line.length());
			
			writ = "";
				
				//zistenie ci v slovniku Lang je francuzsky preklad a nemecky
				if(Lang.get(english).getFrench() == null || Lang.get(english).getFrench().equals("ano")) french = "_null";
				else french = Lang.get(english).getFrench();
				
				if(Lang.get(english).getGerman() == null || Lang.get(english).getGerman().equals("ano")) german = "_null";
				else german = Lang.get(english).getGerman();
				
				//prvy riadok vsetky - aby boli aj other jazyky spomenute
				writ = english + "\t" + german + "\t"
						+ french + "\t" + other;
				lines++;
				if(!german.equals("_null") && french.equals("_null")) onlyGerman++;
				if(!french.equals("_null") && german.equals("_null")) onlyFrench++;
				
				writ = writ.replaceAll("_", " ");
				writer.write(writ);
				writer.write(System.getProperty("line.separator"));
				//dalej idu riadky kde jazyky na seba ukazuju
				
				
				//zistenie ci nemcina ukazuje na francuzstinu
				if(!german.equals("_null") && 
						German.get(german).isBfrench())
				{	
					Gukazuje = true;
					germanOnFrench++;
				}
					
				
				//francuzstina neukazuje na nemcinu
				if(!french.equals("_null") && 
						French.get(french).isBgerman())
				{
					Fukazuje = true;
					frenchOnGerman++;
				}
				
					
				
				if(Gukazuje && Fukazuje)
				{
				both++;
				writ = english+"\t"+german+"\t"+french+"\t";
				writ = writ.replaceAll("_", " ");
				writer.write(writ);
				writer.write(System.getProperty("line.separator"));
				}
				
				else{
				if(!german.equals("_null"))
				{
					writ = english+"\t"+german+"\t"+french+"\t";
					writ = writ.replaceAll("_", " ");
					writer.write(writ);	
					writer.write(System.getProperty("line.separator"));
				}
				if(!french.equals("_null") )
				{	
					writ = english +  "\t" +german+ "\t"+ french+"\t";
					writ = writ.replaceAll("_", " ");
					writer.write(writ);
					writer.write(System.getProperty("line.separator"));
				}
				}
				
				Gukazuje = false;
				Fukazuje = false;
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			System.out.println(english);
			
			e.printStackTrace();
		}
		System.out.println(firstStatistik + "\nNemecke ktore ukazuju na francuzske: " + germanOnFrench
				+"\nFrancuzske ktore ukazuju na nemecke: " + frenchOnGerman
				+"\nObojsmerne: " + both
				+"\nIba nemecke na anglicke a reverz: " + onlyGerman
				+"\nIba francuzske na anglicke a reverz:" + onlyFrench);
		
		WriteStatistik(firstStatistik + "\nNemecke ktore ukazuju na francuzske: " + germanOnFrench
				+System.getProperty("line.separator")+"Francuzske ktore ukazuju na nemecke: " + frenchOnGerman
				+System.getProperty("line.separator")+"Obojsmerne: " + both
				+System.getProperty("line.separator")+"Iba nemecke na anglicke a reverz: " + onlyGerman
				+System.getProperty("line.separator")+"Iba francuzske na anglicke a reverz: " + onlyFrench);
	}

	private void WriteStatistik(String string) {
		try {
		writer = new BufferedWriter(new FileWriter("Statistiky.txt"));
		writer.write(string);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
