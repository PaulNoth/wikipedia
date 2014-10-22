import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class Disambigation {
	
 
  public static void main(String[] args) {
	  
	String title = new String();
 
    try {
 
	File file = new File("data/sample-enwiki-latest-pages-articles2.xml");
 
	DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                             .newDocumentBuilder();
 
	Document doc = dBuilder.parse(file);
 
	if (doc.hasChildNodes()) {
 
		findNote(doc.getChildNodes(), title);
 
	}
 
    } catch (Exception e) {
	System.out.println(e.getMessage());
    }
 
  }
  
  private static boolean test(String output) throws FileNotFoundException
  {
	  //String pattern = new Scanner(new File("sample-enwiki-latest-pages-articles2_output.xml")).useDelimiter("\\A").next();

	  StringBuilder patternBuilder = new StringBuilder();
	  
	  Scanner scanner = new Scanner(new File("data/sample-enwiki-latest-pages-articles2_output.xml"));
      String line;
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
  
  private static String printNote(String text, String title)
  {
	  StringBuilder output = new StringBuilder();
	  
	  Pattern pattern = Pattern.compile("\\*\\[\\[.*");
	  Matcher matcher = pattern.matcher(text);
	  
	  String tempItem;
	  
	  while(matcher.find())
	  {
		  tempItem = matcher.group();
		  
		  tempItem = tempItem.replace("*","");
		  tempItem = tempItem.replace("[[","");
		  tempItem = tempItem.replace("]]","");
		  
		  String[] element = tempItem.split(",");
		  
		  element[0] = element[0].trim();
		  element[1] = element[1].trim();
		  
		  output.append(element[0] + "\t" + title + "\t" +  element[1] + "\n");
	  }
	  
	  return output.toString();
  }
  
  private static void findNote(NodeList nodeList, String title) throws FileNotFoundException {
	  
	  
	  
	    for (int count = 0; count < nodeList.getLength(); count++) {
	 
		Node tempNode = nodeList.item(count);
	 
		// make sure it's element node.
		if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
	 	
	 
			if(tempNode.getNodeName().equals("title"))
			{
				title = tempNode.getTextContent();
			}
			else if(tempNode.getNodeName().equals("text") && tempNode.getTextContent().contains("disamb"))
			{
				boolean found = false;
				
				//check if it is in disambiguation list from DBpedia
				Pattern pattern = Pattern.compile((".*<http://dbpedia.org/resource/" + title + ".*>.*"));
				Scanner scanner = new Scanner(new File("data/disambiguations_en.ttl"));
				        String line;
				        while (scanner.hasNextLine()) {
				            line = scanner.nextLine();
				            if (pattern.matcher(line).matches()) {
				                scanner.close();
				                found = true;
				                break;
				            }
				        }
				        
				if(found = true)
				{
					String output = printNote(tempNode.getTextContent(), title);
					test(output);
				}			
			}
			
			if (tempNode.hasChildNodes()) {
	 
				// loop again if has child nodes
				findNote(tempNode.getChildNodes(), title); 
			}
			
		}
	 
	    }
	 
	  }
   
}