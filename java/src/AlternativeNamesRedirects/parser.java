package xonder_VINF;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class parser {
	fileSave fileSave;
	
	// creating a file to save csv
	public parser(){
		fileSave = new fileSave();
	}
	
	// first analyzing of XML input, creating hash map for holding backfire redirects
	public dataSending firstAnalyzing(String fileName){
		Map<String, ReverseRedirects> map = new HashMap<String, ReverseRedirects>();
		int pages = 0;
		
			String filePath = fileName;
			XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		    try {           
		            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
		            while(xmlEventReader.hasNext()){
		                XMLEvent xmlEvent = xmlEventReader.nextEvent();
		                
		                if (xmlEvent.isStartElement()){
		                    StartElement startElement = xmlEvent.asStartElement();
		                    if(startElement.getName().getLocalPart().toLowerCase().equals("page")){
		                    	 pages++;
		                    }else if(startElement.getName().getLocalPart().toLowerCase().equals("title")){
		                    	 xmlEvent = xmlEventReader.nextEvent();
		                    	 map.put(xmlEvent.asCharacters().getData(), new ReverseRedirects(true));
		                    }
		                }
		            }

		    }catch(Exception e){
		        	System.out.println("error: "+e);
			    	e.printStackTrace();
			    	return null;
		    }
		        
		dataSending Result = new dataSending();
		Result.map = map;
		Result.pages = pages;
		return Result;		
	}
	
	// original unity test, basicly the same with the one in the test folder
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
	    							if(attribute.getValue().equals("Hlavná stránka") && help.equals("Main Page")){
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
	                        		if((help.equals("Vitoria")) && (finalHelp.equals("Francisco de Vitoria")))  {
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
		    	return null;
	    }
	    if(success >= 2){
	    	dataSending Result = new dataSending();
	    	return Result;
	    }else return null;
	}
	
	// preprocessing of data, in other words, extracting useful data from XML and writing them to csv file
	public dataSending preprocessing(String fileName, Map<String,ReverseRedirects> map){
		String filePath = fileName;
		StringBuilder sb,redirectHelp;
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		long i = 0, allRedirects=0, allDisamb=0, deadRedirects = 0, nameRedirects = 0, deadDisamb = 0, allDisambRedirects = 0;
		long start,end, time = 0;
		Preprocessing Current = new Preprocessing();
		ReverseRedirects help;

		start = System.currentTimeMillis();
		 String pattern = "(\\[\\[[^\\]\\]]*\\]\\])";

	     Pattern r = Pattern.compile(pattern);

	        try {
	
	             
	            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filePath));
	            while(xmlEventReader.hasNext()){
	                XMLEvent xmlEvent = xmlEventReader.nextEvent();
	                
	                if (xmlEvent.isStartElement()){
	                    StartElement startElement = xmlEvent.asStartElement(); 
	                    /************FOUND NEW PAGE, insert data if there are any to csv**********/
	                    if(startElement.getName().getLocalPart().toLowerCase().equals("page")){
	                    	 i++;
	                        
	                        if(i!=1)
	                        	{
	                        		sb = new StringBuilder();
	                        		sb.append(i);
	                        		if(Current.title != null) sb.append(","+Current.title); else sb.append(", ");
	                        		if(Current.redirTitle != null) sb.append(","+Current.redirTitle); else sb.append(", ");
	                        		if(Current.ambig) sb.append(",Y"); else sb.append(",N");
	                        		
	                        		sb.append(","+Current.numberRedirects);
	                        		for(int j=0;j<Current.numberRedirects;j++){
	                        		//	sb.append(","+Current.redirects.get(j));
	                        		}
	                        		//if(Current.numberRedirects == 0 || Current.redirects!=null || Current.redirects.size() == 0) {
	                        			sb.append(",no");
	                        		//}
	                        		fileSave.addLine(sb.toString());
	                       
	                        	}
	                       
	                         Current.ambig = false;
	                         Current.ambigs = null;
	                         Current.ambigs = new ArrayList<String>();
	                         Current.id = 0;
	                         Current.redirects = null;
	                         Current.redirects = new ArrayList<String>();
	                         Current.redirTitle = null;
	                         Current.title = null;
	                         Current.numberRedirects = 0;
	                         Current.numberAmbigs = 0;
	                    } /**********Tag title found, store its value to variable******/
	                    else if(startElement.getName().getLocalPart().toLowerCase().equals("title")){
	                    	xmlEvent = xmlEventReader.nextEvent();

	                    	Current.title = xmlEvent.asCharacters().getData();
	                    } 
	                    /************set id of page variable if found ***************/
	                    else if(startElement.getName().getLocalPart().toLowerCase().equals("id")){
	                    	xmlEvent = xmlEventReader.nextEvent();

	                    	if(Current.id == 0){
	                    		Current.id = Long.parseLong(xmlEvent.asCharacters().getData());
	                    	}
	                    }
	                    /***********set the redirect title variable if found ***********/
	                    else if(startElement.getName().getLocalPart().toLowerCase().equals("redirect")){
	                    	nameRedirects++;
	                    	//System.out.println("-------------------------------------");
	                    	Iterator<Attribute> attributes = startElement.getAttributes();
	                    	while (attributes.hasNext()) {
	    						Attribute attribute = attributes.next();
	    						if (attribute.getName().toString().equals("title")) {
	    							//System.out.println("redirect title " + attribute.getValue());
	    							Current.redirTitle = attribute.getValue();
	    							help = map.get(Current.redirTitle);
	    							if(!map.containsKey(Current.redirTitle) || help == null){
	    								help = new ReverseRedirects(false);
	    								help.count++;
	    								help.reversers = new String(Current.title);
	    								map.put(Current.redirTitle, help);
	    								deadRedirects++;
	    								//System.out.println("mrtvych "+deadRedirects);
	    							}else{
	    								//System.out.println("redir "+ Current.redirTitle);
	    								help.count++;
	    								if(help.reversers == null) help.reversers = new String(Current.title);
	    								else help.reversers += (", "+Current.title);
	    							}
	    						}
	    					}
	                    }
	                    /*******if text element is found, search it for disambigious pages, or search it for links to another pages, then write results*/
	                    else if(startElement.getName().getLocalPart().toLowerCase().equals("text")){
	                        String line = xmlEventReader.getElementText();
	                        Matcher m = r.matcher(line);
	                        if(line.toLowerCase().contains("{{rozlišovacia stránka}}")){
	                        	Current.ambig = true;
	                        	
	                        	help = map.get(Current.title);
	                        	help.isDis = true;
	                        	while(m.find()){
	                        		Current.numberRedirects++;
	                        		allRedirects++;
	                        		allDisambRedirects++;
	                        		Current.redirects.add(m.group(1));
	                        		
	                        		
	                        		help.countDisam++;
	                        		String[] hlp = m.group(1).split("[|]");
	                        		
	                        		redirectHelp = new StringBuilder(hlp[0].replace(',', ' '));
	                        		if(hlp.length<2) redirectHelp = redirectHelp.delete(redirectHelp.length()-2, redirectHelp.length());
	                        		redirectHelp = redirectHelp.delete(0, 2);
	                        		
	                        		if(!map.containsKey(redirectHelp)) {
	                        			deadDisamb++;
	                        			if(help.deadDisambigs == null) help.deadDisambigs = new String(redirectHelp.toString());
	                        			else help.deadDisambigs += ", "+redirectHelp.toString();
	                        		}
	                        		
	                        	   // System.out.println("lalalala ++++++++++ "+redirectHelp.toString());
	                        		if(help.disambgis == null) help.disambgis = new String(redirectHelp.toString());
	                        		else help.disambgis += ", "+redirectHelp.toString();
	                        		
	                        		
	                        	}
	                        	allDisamb++;
	                        }
	                        
	                        else {
	                        	while (m.find( )) {
	                        
	                        		allRedirects++;
	                        		Current.numberRedirects++;
	                        		Current.redirects.add(m.group(1).replace(',', ' '));
	                        		
	                        	}
		          		    }
	                    }
	                   		                    
	                }
	            } 
	            sb = new StringBuilder();
        		sb.append(i);
        		if(Current.title != null) sb.append(","+Current.title); else sb.append(", ");
        		if(Current.redirTitle != null) sb.append(","+Current.redirTitle); else sb.append(", ");
        		if(Current.ambig) sb.append(",Y"); else sb.append(",N");
        		
        		sb.append(","+Current.numberRedirects);
        		for(int j=0;j<Current.numberRedirects;j++){
        			sb.append(","+Current.redirects.get(j));
        		}
        		fileSave.addLine(sb.toString());
        		
	            end = System.currentTimeMillis();
	            time = end-start;
	            
	            

	        }catch(Exception e){
	        	System.out.println("error: "+e);
		    	e.printStackTrace();
	        }
		fileSave.close();
		dataSending Result = new dataSending();
		Result.allDisamb = allDisamb;
		Result.allDisambRedi = allDisambRedirects;
		Result.allRedirects = allRedirects;
		Result.deadDisambRedir = deadDisamb;
		Result.time = time;
		Result.nameRedirects = nameRedirects;
		Result.deadRedirects = deadRedirects;
		return Result;
	}
	
}
