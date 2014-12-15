package control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.WikiData;

public class StaxParse {
	
	public String Infobox, Name = "", strAlternativeName = "";
	public ArrayList<String> Names;
	public ArrayList<ArrayList<String>> AlternativeNames;
	
	public ArrayList<WikiData> Wikis;

	public void parse(String filename){	
	
		Names = new ArrayList<String>();
		Wikis = new ArrayList<WikiData>();
		
		
		
		
		 
		final JSONArray listOfAlternatives = new JSONArray();
		
		
		try {
			 
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
		 
			DefaultHandler handler = new DefaultHandler() {
		 
				boolean bpage = false;
				boolean brevision = false;
				boolean btext = false;
				
				WikiData Wiki;
				JSONObject alternativeObj;
				
				Node nNode;
				Element eElement;
				NodeList pageList; 
				String text;
				String parsedString;
				Pattern r;
				Matcher m;
				int i;
				char c;
				int leftBrace;
				int rightBrace;
				int endOfString;
				 
				
				StringBuffer textBuffer = null;
				
				ArrayList<String> Alt;
				
							 
				public void startElement(String uri, String localName,String qName, 
			                Attributes attributes) throws SAXException {
			 
					//System.out.println("Start Element :" + qName);
			 
					if (qName.equalsIgnoreCase("PAGE")) {
						bpage = true;
					}
			 
					if (qName.equalsIgnoreCase("REVISION")) {
						brevision = true;
					}
			 
					if (qName.equalsIgnoreCase("TEXT")) {
						btext = true;
					}
			  
				}
			 
				public void endElement(String uri, String localName, String qName) throws SAXException {
			 
					//System.out.println("End Element :" + qName);
					if (qName.equalsIgnoreCase("PAGE")) {
						bpage = false;
					}
			 
					if (qName.equalsIgnoreCase("REVISION")) {
						brevision = false;
					}
			 
					if (qName.equalsIgnoreCase("TEXT")) {
						btext = false;
						r = Pattern.compile("(\\{\\{\\s*Infobox)(.*)(\\}\\})", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
						m = r.matcher(textBuffer);//
						textBuffer = null;
						//textBuffer je zle, musim ho vyprazdnovat
						
					    //find Infobox
					    if(m.find()) {
					    	parsedString = m.group(2);
					    	//System.out.println("fachame '"+parsedString);
					    	leftBrace = 0; 
					    	rightBrace = 0;
					    	endOfString = 0;
					    	
					    	//get just clean Infobox
					    	for (i = 0; i < parsedString.length(); i++) {
					            c = parsedString.charAt(i);
					            if (c == '{') {
					            	++leftBrace; 
					            } else if (c == '}' ) {
					            	if(leftBrace > 0)	
					            		--leftBrace;
					            	else{
					            		++rightBrace;
					            		if( rightBrace == 2){
					            			endOfString = i;
					            			break;
					            		}
					            	}						                
					            }
					        }

					    	if(endOfString > 0)
					    		Infobox = parsedString.substring(0, --endOfString);
					    	else
					    		Infobox = parsedString;
					    	//System.out.println("fachame '"+Infobox);

					    	//parse Name 
					    	r = Pattern.compile("(\\|\\s*Name\\s*=)(.*?)(\n)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
						    m = r.matcher(Infobox);//
						    if(m.find()) {
						    	Name = m.group(2).trim();
						    	//System.out.println("nazov '"+Name);
						    	Names.add(Name);
						    	
						    	
							    //parse AKA
							    r = Pattern.compile("(\\|\\s*AKA\\s*=)(.*?)(\\|)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
							    m = r.matcher(Infobox);//
							    JSONArray listOfAlternativeNames = new JSONArray();
							    
							    if(m.find()) {
							    	strAlternativeName = m.group(2).trim();
							    	//System.out.println("alternative names '"+strAlternativeName );
							    	
							    	String[] split = strAlternativeName.split("<br>");
							    	
							    	
									for(String alternative: split){
										listOfAlternativeNames.add(alternative);
										//System.out.print(alternative);
							    	}
									
							    
							    	//listOfAlternativeNames.add(strAlternativeName);
							    	//listOfAlternativeNames.add("socka");		
							    	//parse more names, teraz su iba v 1 stringu vsetky, asi uvidim ake to je, ale to bude jednoduche					  
							        
							    }else{
							    	listOfAlternativeNames.add("");
							         
							    }
							    //,
							    //<br \/>
							    
							    Wiki = new WikiData(Name,strAlternativeName);
						    	Wikis.add(Wiki);
						    
						    	alternativeObj = new JSONObject();
						    	alternativeObj.put("Name", Name);
						    	alternativeObj.put("Alternative", listOfAlternativeNames);
						    	
						    	listOfAlternatives.add(alternativeObj);
						    	//strAlternativeName = "";
						    	//Name = "";
						    }
					    }
					    
						
					}
			 
				}
			 
				public void characters(char ch[], int start, int length) throws SAXException {
			 
					if (btext) {
						String text = new String(ch, start, length);
						
						//System.out.println("obsha textu : " + new String(ch, start, length));
					
						
						if (!text.trim().equals("")) {
							if (textBuffer == null) {
								textBuffer = new StringBuffer(text);
							} else {
								textBuffer.append(text);
							}
						}
					}
				}
		 
		    };
		 
		    saxParser.parse(filename, handler);
		    
			    try {  
		              
		            // Writing to a file  
		            File file = new File("output_big_all.json");  
		            file.createNewFile();  
		            FileWriter fileWriter = new FileWriter(file);      

		            
		            fileWriter.write(listOfAlternatives.toJSONString());  
		            fileWriter.flush();  
		            fileWriter.close();  	
	
		        }catch(IOException  E){
		            E.printStackTrace();  	
		        }
		 
		   } catch (Exception e) {
		      e.printStackTrace();
		   }
		 	
	}	
}
