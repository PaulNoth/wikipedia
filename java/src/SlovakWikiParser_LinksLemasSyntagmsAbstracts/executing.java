package wiki;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import test.Testing;


public class executing {

	static String strPagesWikiFile;
	static String strTypesFile;
	
	public static void main(String[] args) {
		try
		{
		//open file with articles
		Path fileWiki = Paths.get("../../../data/sample_skwiki-latest-pages-articles.xml");
		//Path fileWiki = Paths.get("../../../data/skwiki-latest-pages-articles.xml");
		byte[] fileArray;
		fileArray = Files.readAllBytes(fileWiki);
		strPagesWikiFile = new String(fileArray, "UTF-8");
		
		//open file with articles
		Path fileTypes = Paths.get("../../../data/instance_types_sk.ttl");
		fileArray = Files.readAllBytes(fileTypes);
		strTypesFile = new String(fileArray, "UTF-8");

		//just one sentence or now. Later there will be whole text read from articles
		//String sentence ="Medzi filozofov bežne spájaných s empirizmom patria tiež [[Aristoteles]], [[Tomáš Akvinský]], [[Francis Bacon]], [[Thomas Hobbes]], [[John Locke]], [[David Hume]] a [[John Stuart Mill]].";
		String sentence1 = "Spomedzi stredovekej [[Scholastika (filozofia)|scholastiky]], [[Tomáš Akvinský]] tvrdí, že existencia Boha môže byt overitelná zmyslovým pozorovaním. Používa Aristotelovu myšlienku &quot;aktívneho intelektu&quot;, ktorá je interpretovaná ako schopnost abstraktne a všeobecne uvažovat z ciastocne empirických informácií.";
		String sentence4 = "'''Subjektívny idealizmus''' je [[filozofický smer]], ktorého predstavitelia odmietajú oprávnenost tézy o existencii [[objektívna realita|objektívnej reality]] [[Platón]]ovej.";
		String sentence2 = "'''Konfucianizmus''' = '''konfuciánstvo''' je [[filozofický smer]] - jedna z dvoch vetví osobitného [[Čína (civilizácia)|čínskeho]] polonáboženského kultu univerza a obcianskych cností; prúd cínskej filozofie spájaný s osobou [[Konfucius|Konfucia]], ktorý už nezastáva vieru vo viacerých bohov, vyhýba sa metafyzickým otázkam, pestuje skôr obciansky kult a moralistné normovanie vlastností užitocných pre štát.";
		String sentence3 = "Svoju teóriu rozvinul v reakcii na [[John Locke|Lockeov]] [[materializmus]]";
		
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
		
		Sentence oneSent3 = new Sentence();
		oneSent3.setFullSent(sentence3);
		oneSent3.setLinks(getLinks(sentence3));
		parsedSentences.add(oneSent3);
		
		Sentence oneSent4 = new Sentence();
		oneSent4.setFullSent(sentence4);
		oneSent4.setLinks(getLinks(sentence4));
		parsedSentences.add(oneSent4);
		
		
		String output = getOutput(parsedSentences);
		Path file = Paths.get("../../../data/output1.xml");
		//Path file = Paths.get("../../../data/sample_output_parsed_sentences_and_links.xml");
		byte[] buf = output.getBytes("UTF-8");
		Files.write(file, buf);

		System.out.println("Unit test results: ");
		System.out.println("Structure test - " + Boolean.toString(Testing.unitTestStructure(output)));
		System.out.println("Content test - " + Boolean.toString(Testing.unitTestContent(output)));
		
		
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * gets all links marked as [[..]] from string sentence
	 * returns List<String>
	 * */
	public static List<Link> getLinks(String sentence)
	{
		List<Link> result = new ArrayList<Link>();
		
		//pattern to match link
		Pattern p = Pattern.compile("\\[{2}[\\w\\s\\|\\p{L}\\(\\)]+\\]{2}\\w*");
		Matcher m = p.matcher(sentence);

		List<String> strLinks = new ArrayList<String>();
		while (m.find()) {
			String strFound = m.group();
			//ignore image links
			if(!strFound.startsWith("[[Súbor:"))
			{
				strLinks.add(strFound);
				result.add(new Link(strFound));
			}
		}
		
		for(Link l:result)
		{
			getLinkType(l);
			getLinkText(l);
			if(l.getType()==null || l.getType()=="")
				getLinkTypeFromInfobox(l);
			getLinkAbstract(l);
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

	/**finds infobox in age text and marks infobox beginning line and end line
	 * */
	public static void getLinkTypeFromInfobox(Link l)
	{
		String linkType = "";
		
		
		//String[] lines = strPagesWikiFile.split("\\r\\n");
		String[] lines = l.getText().split("\\r\\n");
		
		Pattern pInfobox = Pattern.compile("\\{\\{Infobox");
		String strInfoboxEnd = "}}";
		Boolean infoboxFound = false;
		
		String infoboxLine = "";
		
		for(int i=0;i<lines.length;i++)
		{
			if(!infoboxFound)
			{
				Matcher m = pInfobox.matcher(lines[i]);
				String strInfoboxFound = "";			
				while (m.find()) {
					strInfoboxFound = m.group();
				}
				if(strInfoboxFound!="")
				{
					infoboxLine = lines[i];
					l.setInfoboxStartLine(i);
					infoboxFound=true;
				}
			}
			else
			{
				if(lines[i].trim().equals(strInfoboxEnd))
				{
					l.setInfoboxEndLine(i);
					break;
				}
			}
				
		}
		
		if(infoboxLine!="")
		{
			String[] splittedIB = infoboxLine.split("\\s");
			linkType = splittedIB[splittedIB.length-1];
		}
		l.setType(linkType);
	}
	
	/**finds abstract in page text content
	 * */
	public static void getLinkAbstract(Link l)
	{
		String strAbstract = "";		
		
		//String[] lines = strPagesWikiFile.split("\\r\\n");
		String[] lines = l.getText().split("\\r\\n");
		
		Pattern pSection = Pattern.compile("== ");
		Boolean newSectionFound = false;
		
		for(int i=l.getInfoboxEndLine()+1;i<lines.length;i++)
		{
			if(!newSectionFound)
			{
				Matcher m = pSection.matcher(lines[i]);
				while (m.find()) {
					newSectionFound = true;
				}
				if(!newSectionFound)
				{
					strAbstract += lines[i] + "\r\n";
				}
			}
			else
			{
				break;
			}
				
		}
		
		l.setArticleAbstract(strAbstract.replaceAll("\r\n", " "));
	}
	
	/**gets element text from a page, the link refers to  
	 * */
	public static void getLinkText(Link l)
	{
		String textFound = "";			
		
		String[] lines = strPagesWikiFile.split("\\r\\n");
		
		//finding page with specific title
		//Pattern p = Pattern.compile("<page>.*<title>" + l.getLemma() + "</title>.*</page>");
		Pattern pTitle = Pattern.compile("<title>"+ l.getLemma() + "</title>");
		Pattern pTextStart = Pattern.compile("<text");
		Pattern pTextEnd = Pattern.compile("</text>");
		Boolean pageFound = false;
		Boolean textStartFound = false;
		Boolean textEndFound = false;
		
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
			else if(!textStartFound)
			{
				Matcher m = pTextStart.matcher(lines[i]);
				while (m.find()) {
					textStartFound = true;
				}
				if(textStartFound)
				{
					textFound += lines[i] + "\r\n";
				}
			}
			else if(!textEndFound)
			{
				Matcher m = pTextEnd.matcher(lines[i]);
				while (m.find()) {
					textEndFound = true;
				}
				if(textEndFound)
				{
					textFound += lines[i];
					break;
				}
				else
				{
					textFound += lines[i] + "\r\n";
				}
			}
		}
		
		
		if(textFound!="")
		{
			l.setText(textFound);
		}
		else
		{
			l.setText("");
		}	
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
			
			strResult+="<links>";
			
			for(Link l : currentSent.getLinks())
			{
				strResult+="<link>";
				
				strResult+="<linkLabel>" + l.getLabel() + "</linkLabel>";
				strResult+="<lemma>" + l.getLemma() + "</lemma>";
				strResult+="<wklnk>" + l.getWLnk() + "</wklnk>";
				strResult+="<dblnk>" + l.getDbLnk() + "</dblnk>";
				strResult+="<abstract>" + l.getArticleAbstract() + "</abstract>";
				strResult+="<type>" + l.getType() + "</type>";
				strResult+="<vc>" + l.getSyntax() + "</vc>";
				
				strResult+="</link>";
			}
			
			strResult+="</links>";
			strResult+="</sentence>";
		}
		
		strResult+="</output>";
		
		return strResult;
	}

}
