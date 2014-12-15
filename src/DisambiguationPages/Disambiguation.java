import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Disambiguation {
	private static String output;
	private static PrintWriter writer;
	private static DisambiguationSet set;
	private static int descriptionCount = 0;
	private static int disambBranchesCount = 0;
	
	
  public static void main(String[] args) throws XMLStreamException, IOException {
	  
	  
	  output = null;
	  
	  if(args.length == 0) {
		  parseDisambPages();
	  }
	  else if(args.length == 1 && args[0].equals("-h")) {
		  System.out.println("Program parses disambiguation pages.");
		  System.out.println("-m: Export set of disambiguations.");
		  System.out.println("-h: Help.");
	  }
	  else if(args.length == 1 && args[0].equals("-m")) {
		  makeSet();
	  }
	  else {
		  System.out.println("Incorrect attributes. See -h for help.");
	  }
		  

  }
  
  static void parseDisambPages() throws XMLStreamException, IOException
  {
	writer = new PrintWriter("data/enwiki_latest_pages-articles-output.ttl", "UTF-8");
    
	XMLInputFactory xmlif = XMLInputFactory.newInstance();
   
    int disambPagesCount = 0;
	int pagesCount = 0;
	
    
    String title = null;
    String textS = null;
    
    boolean isTitle = false;
    boolean isText = false;

    StringBuilder text = new StringBuilder();
    
    System.out.println("start");
    
    fillSet();
    
    
    for(int k = 1; k <= 27; k++)
    {
    	
    	XMLStreamReader reader = xmlif.createXMLStreamReader("data/wikipedia/enwiki-latest-pages-articles" + k + ".xml",
                new FileInputStream("data/wikipedia/enwiki-latest-pages-articles" + k + ".xml"));
    	
    
    	System.out.println("Iteration: " + k);
    	
    while(reader.hasNext()){
      int event = reader.next();

      switch(event){
        case XMLStreamConstants.START_ELEMENT: 
          if ("title".equals(reader.getLocalName())){
        	isTitle = true;
          }
          if("text".equals(reader.getLocalName()) && isText == false){
        	isText = true;
        	text.setLength(0);
          }
          break;

        case XMLStreamConstants.CHARACTERS:
          if (isTitle)
          {
        	  title = reader.getText().trim();
        	  isTitle = false;
          }
          if (isText)
          {
        	  text.append(reader.getText());
          }
          break;

        case XMLStreamConstants.END_ELEMENT:

          
          if(isText)
          {
        	  textS = text.toString();
        	  pagesCount++;
        	  if(isDisambig(title, textS))
        	  {        	  	
        	  	Page page = new Page();
        	  	page.title = title;
        	  	page.parseDisambigDescription(textS);
        	  	printPage(page);
        	  	
        	  	disambPagesCount++;        	  	      		  
        	  }
          }
          
          isText = false;
          
          break;
      }

    }
    
    PrintWriter statistics = new PrintWriter("data/statistics.txt", "UTF-8");
    statistics.println("Pages count:\t" + pagesCount);
    statistics.println("Disab pages count:\t" + disambPagesCount);
    statistics.println("Disamb branches count:\t" + disambBranchesCount);
    statistics.println("Descriptions found count:\t" + descriptionCount);
    statistics.close();
    
    
    reader.close();
    
    }

    writer.close();
  
    System.out.println("Disambiguation pages successfully parsed.");
  }
  
  
  
  static void printPage(Page p) throws FileNotFoundException, UnsupportedEncodingException
  {
	  
	  
	  StringBuilder outputBuilder = new StringBuilder();
	  String out;
	  SubPage s = null;
	  
	  for(int j = 0; j < p.subPages.size(); j++ )
	  {
		  s = (SubPage) p.subPages.get(j);
		  outputBuilder.append(s.subtitle + "\t" + p.title + "\t" +  s.description + "\n");
		  
		  disambBranchesCount++;
		  if(s.description != null)
		  {
			  descriptionCount++;
		  }
	  }

	  out = outputBuilder.toString();	  
	  writer.println(out.trim());
	  
  }
  
  static boolean isDisambig(String title, String text) throws IOException
  {
	  int i;
	  DisambiguationUnit u;
	  
	  if(text.contains("disamb"))
      {
		  for(i = 0; i < set.set.size(); i++)
		  {
			  u = (DisambiguationUnit) set.set.get(i);
			  
			  if(u.title.equals(title))
			  {
				  if(u.isDisamb)
				  {
					  return true;
				  }
				  else
				  {
					  return false;
				  }
			  }
		  }
      }
	  
	  return false;
  }
  
  public static void makeSet() throws IOException
  {
	Pattern pattern = Pattern.compile((".*<http://dbpedia.org/resource/(\\S+).*>.*"));
	String lastDisambTitle = "";
	String delims = "[/,>]";
	String[] tokens;
	int disambPagesCount = 0;
	int disambBranchesCount = 0;
	
	PrintWriter writer = new PrintWriter("data/disamb_set.ttl", "UTF-8");
	
	  
	BufferedReader scanner = new BufferedReader(new FileReader(new File("data/disambiguations_en.ttl")));
	String line;
	while ((line = scanner.readLine()) != null) {
		if (pattern.matcher(line).matches()) {
			tokens = line.split(delims);
			if(!tokens[4].equals(lastDisambTitle))
			{
				writer.println(tokens[4] + "\td");
				lastDisambTitle = tokens[4];
				
				disambPagesCount++;
			}
			
			writer.println(tokens[14]);
			disambBranchesCount++;
        }
	}
	
	writer.println();
	writer.println("Disamb pages count: " + disambPagesCount);
	writer.println("Disamb branches count: " + disambBranchesCount);
	
	scanner.close();
	writer.close();
	
  }
  
  public static void fillSet() throws IOException
  {
	  set = new DisambiguationSet();
	  set.fillSet("data/disamb_set.ttl");
	  
	  System.out.println("Set made.");
  }
}