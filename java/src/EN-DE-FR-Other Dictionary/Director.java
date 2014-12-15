package dataManipulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Director {
	static String[] allLang = {"dbpedia","de","fr"};
	static String[] langLinks = new String[2];
	static String lang;
	static String word;
	static String intlinksPath = "C:\\Users\\Michal\\Vyhladavanie-informacii\\data_sample\\sample_input_interlanguage_links_";
	String[] Languages = { "af", "an", "ar", "arz", "ast", "az", "be", "bg",
			"bn", "br", "bs", "ca", "cs", "cv", "cy", "da", "de", "el", "eo",
			"es", "et", "eu", "fa", "fi", "fr", "fy", "ga", "gd", "gl", "gu",
			"he", "hi", "hr", "ht", "hu", "hy", "ia", "id", "io", "is", "it",
			"ja", "jv", "ka", "kn", "ko", "ku", "la", "lb", "lt", "lv", "mk",
			"ml", "mn", "mr", "ms", "nap", "nl", "nn", "no", "oc", "pa", "pl",
			"pms", "pnb", "pt", "qu", "ro", "ru", "scn", "sco", "sh", "simple",
			"sk", "sl", "sq", "sr", "su", "sv", "ta", "tg", "th", "tl", "tr",
			"tt", "uk", "ur", "vec", "vi", "war", "yi", "yo", "zh" };
	
	public static void main(String[] args) throws IOException {
	//Parser p = new Parser();
	//p.parse();
	Lucene l = new Lucene("index","Dictionary.txt");
	//l.createIndex();
	l.openIndex();
	l.search("ar","Vi");
	//ShowStat();
	//Show("sk");
	//l.search(args[1],args[2]);
	}
	public static void ShowStat()
	{
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Statistiky.txt"));
			while((line=reader.readLine())!=null)
			{
				System.out.println(line);
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public static void Show(String lang)
	{
		String line;
		int counter = 0;
		
		try{
		BufferedReader reader = new BufferedReader (new FileReader("Dictionary.txt"));
		while((line = reader.readLine())!=null)
		{
			if(line.contains(lang+":")) counter++;
		}
		System.out.println("Slovnik obsahuje " + counter + " slov vo zvolenom jazyku");
		reader.close();
		}catch(Exception e) {};
	}	
}
