package wiki;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;


public class executing {

	static String strPagesWikiFile;
	static String strTypesFile;
	
	public static void main(String[] args) {
		try
		{
		//open file with articles
		Path fileWiki = Paths.get("C:/Users/Domi/Documents/GitHub/wikipedia/data/sample_skwiki-latest-pages-articles.xml");
		byte[] fileArray;
		fileArray = Files.readAllBytes(fileWiki);
		strPagesWikiFile = new String(fileArray, "UTF-8");
		
		//open file with articles
		Path fileTypes = Paths.get("C:/Users/Domi/Documents/GitHub/wikipedia/data/instance_types_sk.ttl");
		fileArray = Files.readAllBytes(fileTypes);
		strTypesFile = new String(fileArray, "UTF-8");

		//just one sentence or now. Later there will be whole text read from articles
		//String sentence ="Medzi filozofov bežne spájaných s empirizmom patria tiež [[Aristoteles]], [[Tomáš Akvinský]], [[Francis Bacon]], [[Thomas Hobbes]], [[John Locke]], [[David Hume]] a [[John Stuart Mill]].";
		String sentence1 = "'''Subjektívny idealizmus''' je [[filozofický smer]], ktorého predstavitelia odmietajú oprávnenost tézy o existencii [[objektívna realita|objektívnej reality]] [[Platón]]ovej.";
		String sentence2 = "'''Konfucianizmus''' = '''konfuciánstvo''' je [[filozofický smer]] - jedna z dvoch vetví osobitného [[Cína (civilizácia)|cínskeho]] polonáboženského kultu univerza a obcianskych cností; prúd cínskej filozofie spájaný s osobou [[Konfucius|Konfucia]], ktorý už nezastáva vieru vo viacerých bohov, vyhýba sa metafyzickým otázkam, pestuje skôr obciansky kult a moralistné normovanie vlastností užitocných pre štát.";
		//String sentence = "Svoju teóriu rozvinul v reakcii na [[John Locke|Lockeov]] [[materializmus]]";
		
		List<Sentence> parsedSentences = new ArrayList<Sentence>();
		
		//for each sentence will be done following
		Sentence oneSent1 = new Sentence();
		oneSent1.setFullSent(sentence1);
		oneSent1.setLinks(getLinks(sentence1));		
		parsedSentences.add(oneSent1);
		
		Sentence oneSent2 = new Sentence();
		oneSent2.setFullSent(sentence2);
		oneSent2.setLinks(getLinks(sentence2));
		
		parsedSentences.add(oneSent2);
		
		String output = getOutput(parsedSentences);
		Path file = Paths.get("C:/Users/Domi/Documents/GitHub/wikipedia/data/output1.xml");
		//Path file = Paths.get("C:/Users/Domi/Documents/GitHub/wikipedia/data/sample_output_parsed_sentences_and_links.xml");
		byte[] buf = output.getBytes("UTF-8");
		Files.write(file, buf);

		System.out.println("Unt test result: " + Boolean.toString(UnitTest(output)));
		
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**unit test - porovnanie so vzorovym suborom/
	 */
	public static Boolean UnitTest(String output)
	{
		Boolean bResult = false;
		try
		{
			//get pattern file as string
			Path fileTypes = Paths.get("C:/Users/Domi/Documents/GitHub/wikipedia/data/sample_output_parsed_sentences_and_links.xml");
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
	
	/**
	 * gets all links marked as [[..]] from string sentence
	 * returns List<String>
	 * */
	public static List<Link> getLinks(String sentence)
	{
		List<Link> result = new ArrayList<Link>();
		
		//pattern to match link
		Pattern p = Pattern.compile("\\[{2}[\\w\\s\\|\\p{L}]+\\]{2}\\w*");
		Matcher m = p.matcher(sentence);

		List<String> strLinks = new ArrayList<String>();
		while (m.find()) {
			String strFound = m.group();
			strLinks.add(strFound);
			result.add(new Link(strFound));
		}
		
		for(Link l:result)
		{
			getLinkType(l);
			if(l.getType()==null || l.getType()=="")
				getLinkTypeFromInfobox(l);
		}
		
		return result;
	}
	
	public static void getLinkType(Link l)
	{
		String linkType = "";
		
		//pattern to match db link in Types file
		Pattern p = Pattern.compile("<{1}" + l.getDbLnk() + ">{1}.*\\.");
		Matcher m = p.matcher(strTypesFile);

		while (m.find()) {
			String strLineFound = m.group();
			String[] parts = strLineFound.split("> <");
			if(parts.length==3)
			{
				String typeLink = parts[2];
				String[] splittedType = typeLink.split("/");
				linkType += splittedType[splittedType.length-1].replace("> .", ", ");
			}
		}
		
		//remove final ", "
		if(linkType.endsWith(", "))
		{
			linkType = linkType.substring(0,linkType.length()-3);
		}
		l.setType(linkType);
	}
	
	public static void getLinkTypeFromInfobox(Link l)
	{
		String linkType = "";
		
		
		String[] lines = strPagesWikiFile.split("\\r\\n");
		
		//finding page with specific title
		//Pattern p = Pattern.compile("<page>.*<title>" + l.getLemma() + "</title>.*</page>");
		Pattern pTitle = Pattern.compile("<title>"+ l.getLemma() + "</title>");
		Pattern pInfobox = Pattern.compile("\\{\\{Infobox");
		Pattern pTextEnd = Pattern.compile("</text>");
		Boolean pageFound = false;
		
		String infoboxLine = "";
		
		for(int i=0;i<lines.length;i++)
		{
			if(!pageFound)
			{
				Matcher m = pTitle.matcher(lines[i]);			
				String titleFound = "";			
				while (m.find()) {
					titleFound = m.group();
				}
				if(titleFound!="")
					pageFound=true;
			}	
			else
			{
				Matcher m = pInfobox.matcher(lines[i]);
				Matcher mEnd = pTextEnd.matcher(lines[i]);
				String infoboxFound = "";			
				while (m.find()) {
					infoboxFound = m.group();
				}
				if(infoboxFound!="")
				{
					infoboxLine = lines[i];
					break;
				}
				else if(mEnd.find())
					break;					
			}
		}
		
		
		if(infoboxLine!="")
		{
			String[] splittedIB = infoboxLine.split("\\s");
			linkType = splittedIB[splittedIB.length-1];
		}
		l.setType(linkType);
	}
		
	public static String getOutput(List<Sentence> sentences)
	{
		String strResult = "";
		strResult+="<output>";
		
		for(int i=0;i<sentences.size();i++)
		{
			Sentence currentSent=sentences.get(i);
			strResult+="<sentence id=\"" + Integer.toString(i) + "\">";
			strResult+="<allSent>" + currentSent.getFullSent() + "</allSent>";
			
			for(Link l : currentSent.getLinks())
			{
				strResult+="<linkLabel>" + l.getLabel() + "</linkLabel>";
				strResult+="<lemma>" + l.getLemma() + "</lemma>";
				strResult+="<wklnk>" + l.getWLnk() + "</wklnk>";
				strResult+="<dblnk>" + l.getDbLnk() + "</dblnk>";
				strResult+="<abstract>" + l.getArticleAbstract() + "</abstract>";
				strResult+="<type>" + l.getType() + "</type>";
				strResult+="<vc>" + l.getSyntax() + "</vc>";
			}
			
			strResult+="</sentence>";
		}
		
		strResult+="</output>";
		
		return strResult;
	}

}
