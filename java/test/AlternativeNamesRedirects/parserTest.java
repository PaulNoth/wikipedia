package xonder_VINF;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/*
 * This parser is doing UNITY test, it checks the format of given XML dump of wikipedia and looks for 2 specific pages: "Main page" which redirects to "Hlavna stranka" and disambigous page Vitoria, which must contain given sample
 * Please make sure your xml file is similiar to one in documentation and these pages are in same format and existing
 */
public class parserTest {
	public dataSending unityTest(String fileName){
		String filePath = fileName, help = null;
		int success =0;
		StringBuilder redirectHelp;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		String pattern = "(\\[\\[[^\\]\\]]*\\]\\])";

	     Pattern r = Pattern.compile(pattern);
	    try {           
	            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
	            while(xmlEventReader.hasNext()){
	                XMLEvent xmlEvent = xmlEventReader.nextEvent();
	                
	                if (xmlEvent.isStartElement()){
	                    StartElement startElement = xmlEvent.asStartElement();
	                    if(startElement.getName().getLocalPart().toLowerCase().equals("title")){
	                    	 xmlEvent = xmlEventReader.nextEvent();
	                    	 
	                    	 help = xmlEvent.asCharacters().getData();
	                    }
	                    else if(startElement.getName().getLocalPart().toLowerCase().equals("redirect")){
	                    	Iterator<Attribute> attributes = startElement.getAttributes();
	                    	while (attributes.hasNext()) {
	    						Attribute attribute = attributes.next();
	    						if (attribute.getName().toString().equals("title")) {
	    							if(attribute.getValue().equals("Hlavná stránka") && help.equals("Main Page")){ // program expects at least one page named Main Page redirecting to Hlavna Stranka
	    								success++;
	    								System.out.println("Main page cool");
	    							}
	    							
	    						}
	    					}
	    				}else if(startElement.getName().getLocalPart().toLowerCase().equals("text")){
	                        String line = xmlEventReader.getElementText();
	                        Matcher m = r.matcher(line);
	                        if(line.toLowerCase().contains("{{rozlišovacia stránka}}")){
	                        	
	                        	while(m.find()){
	                        		String[] hlp = m.group(1).split("[|]");
	                        		
	                        		redirectHelp = new StringBuilder(hlp[0].replace(',', ' '));
	                        		if(hlp.length<2) redirectHelp = redirectHelp.delete(redirectHelp.length()-2, redirectHelp.length());
	                        		redirectHelp = redirectHelp.delete(0, 2);
	                        		String finalHelp = redirectHelp.toString();
	                        		if((help.equals("Vitoria")) && (finalHelp.equals("Francisco de Vitoria")))  { // program expects at least one disambig.page with title Vitoria and redirect link Francisco de Vitoria
	                        			success++;  
	                        			System.out.println("Disambigous page cool");
	                        		}
	                        	}
	                        }
	                        
	                    }
	                 }
	             }
	    }catch(Exception e){
	        	System.out.println("error: "+e);
		    	e.printStackTrace();
		    	return null; // otherwise, xml structure is not right
	    }
	    if(success >= 2){
	    	dataSending Result = new dataSending();
	    	return Result;
	    }else return null; // if the the pages wasnt found, test have failed
	}
}
