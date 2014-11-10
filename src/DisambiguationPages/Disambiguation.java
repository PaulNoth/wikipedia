import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
	private static ArrayList pagesList;
	private static String output;
	
	
  public static void main(String[] args) throws XMLStreamException, IOException {
	  
	  Scanner input = new Scanner(System.in);
	  int i;
	  
	  boolean isParsed = false;
	  
	  output = null;
	  
	  while(true)
	  {
	  System.out.println("\nChoose your option:");
	  System.out.println("\t(1)parse and find disamb. pages;");
	  System.out.println("\t(2)print all found disamb. pages;");
	  System.out.println("\t(3)find disambiguation page;");
	  System.out.println("\t(4)test - compare with sample output;");
	  System.out.println("\t(5)end;");
	  
	  i = input.nextInt();
	  
	  switch(i)
	  {
	  	case 1 :
	  		parseDisambPages();
	  		isParsed = true;
	  		break;
	  		
	  	case 2 :
	  		if(isParsed)
	  			printPagesList();
	  		else
	  			System.out.println("The pages must be parsed first!");
	  		break;
	  		
	  	case 3 :
	  		if(isParsed)
	  			findDisambPage();
	  		else
	  			System.out.println("The pages must be parsed first!");
	  		break;
	  		
	  	case 4 :
	  		if(isParsed)
	  			test();
	  		else
	  			System.out.println("The pages must be parsed first!");
	  		break;
	  		
	  	case 5 :
	  		return;
	  }
	  }
  }
  
  static void parseDisambPages() throws XMLStreamException, IOException
  {
    
    XMLInputFactory xmlif = XMLInputFactory.newInstance();
    XMLStreamReader reader = xmlif.createXMLStreamReader("data/sample-enwiki-latest-pages-articles2.xml",
            new FileInputStream("data/sample-enwiki-latest-pages-articles2.xml"));
    
    pagesList = new ArrayList<Page>();
    
    String title = null;
    String textS = null;
    
    boolean isTitle = false;
    boolean isText = false;
    
    StringBuilder text = new StringBuilder();
    
    System.out.println("start");

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
        	  if(isDisambig(title, textS))
        	  {        	  	
        	  	Page page = new Page();
        	  	page.title = title;
        	  	page.parseDisambigDescription(textS);
        	  	pagesList.add(page);
        	  }
          }
          
          isText = false;
          
          break;
      }

    }
    
    System.out.println("Disambiguation pages successfully parsed.");
  }
  
  static void findDisambPage()
  {
	  Scanner input = new Scanner(System.in);
	  int i,j;
	  String title;
	  Page page;

	  System.out.println("\nChoose your find option:");
	  System.out.println("\t(1)find by id;");
	  System.out.println("\t(2)find by title;");
	  
	  i = input.nextInt();	  
	  
	  switch(i)
	  {
	  	case 1 :
	  		System.out.println("Insert the ID:");
	  		j = input.nextInt();
	  		page = (Page) pagesList.get(j);
	  		printPage(page);
	  		break;
	  	case 2 :
	  		System.out.println("Insert the ID:");
	  		title = input.next();
	  		page = findByTitle(title);
	  		if(page != null)
	  			printPage(page);
	  		else
	  			System.out.println("No such page with title " + title + "!");
	  		break;
	  }
  }
  
  static Page findByTitle(String title)
  {
	  Page p = null;
	  SubPage s = null;
	  
	  for(int i = 0; i < pagesList.size(); i++ )
	  {
		  p = (Page) pagesList.get(i);
		  if(p.title.equals(title))
		  {
			  return p;
		  }
		  
	  }
	  
	  return null;
  }
  
  static boolean test() throws FileNotFoundException
  {
	  StringBuilder patternBuilder = new StringBuilder();
	  
	  Scanner scanner = new Scanner(new File("data/sample-enwiki-latest-pages-articles2_output.xml"));
      String line;
      
      if(output == null)
	  {
		  printPagesList();
	  }
      
      while (scanner.hasNextLine()) {
    	  line = scanner.nextLine();
    	  patternBuilder.append(line + "\n");
      }
	  
      
	  String pattern = patternBuilder.toString().trim();
	  output = output.trim();
	  
	  for (int i = 0; i < Math.min(output.length(), pattern.length()); i++)
	  {
		  if(output.charAt(i) != pattern.charAt(i))
		  {
			  System.out.println("char at: " + i);
			  System.out.println("output char: " + output.charAt(i));
			  System.out.println("pattern char: " + pattern.charAt(i));
			  System.out.println("Test failed!");
			  return false;
		  }
	  }
	  
	  System.out.println("Test succeeded!");
	  
	  return true;
  }
  
  static void printPagesList()
  {
	  StringBuilder outputBuilder = new StringBuilder();
	  Page p = null;
	  SubPage s = null;
	  
	  for(int i = 0; i < pagesList.size(); i++ )
	  {
		  p = (Page) pagesList.get(i);
		  for(int j = 0; j < p.subPages.size(); j++ )
		  {
			  s = (SubPage) p.subPages.get(j);
			  outputBuilder.append(s.subtitle + "\t" + p.title + "\t" +  s.description + "\n");
		  }
		  
	  }
	  
	  output = outputBuilder.toString();
	  
	  System.out.println(output);
  }
  
  static void printPage(Page p)
  {
	  StringBuilder outputBuilder = new StringBuilder();
	  String out;
	  SubPage s = null;
	  
	  for(int j = 0; j < p.subPages.size(); j++ )
	  {
		  s = (SubPage) p.subPages.get(j);
		  outputBuilder.append(s.subtitle + "\t" + p.title + "\t" +  s.description + "\n");
	  }
	  
	  out = outputBuilder.toString();	  
	  System.out.println(out);
  }
  
  static boolean isDisambig(String title, String text) throws IOException
  {
	  if(text.contains("disamb"))
      {
		  boolean found = false;

			
			//check if it is in disambiguation list from DBpedia
			Pattern pattern = Pattern.compile((".*<http://dbpedia.org/resource/" + title + ".*>.*"));
			BufferedReader scanner = new BufferedReader(new FileReader(new File("data/disambiguations_en.ttl")));
			        String line;
			        while ((line = scanner.readLine()) != null) {
			        	
			            if (pattern.matcher(line).matches()) {
			                scanner.close();
			                found = true;
			                break;
			            }
			        }
			        
			return found;	
      }
	  
	  return false;
  }
}