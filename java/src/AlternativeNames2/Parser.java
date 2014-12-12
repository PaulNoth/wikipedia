package control;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;  
import org.json.simple.JSONObject;  

public class Parser {
	
	public String Infobox, Name, strAlternativeName;
	
	public Parser(String FileName){
		
		 try {
				File fXmlFile = new File(FileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
				NodeList nList = doc.getElementsByTagName("page");
				System.out.println("----------------------------"+nList.getLength());			
				
				Node nNode;
				Element eElement;
				NodeList pageList; 
				String text, parsedString;
				Pattern r;
				Matcher m;
				int i;
				char c;
				int leftBrace,rightBrace,endOfString;
				JSONObject alternativeObj = new JSONObject();  
				JSONArray listOfAlternatives = new JSONArray(); 
				
				for (int temp = 0; temp < nList.getLength(); temp++) {
					nNode = nList.item(temp);
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
					
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						eElement = (Element) nNode;
						
						pageList = eElement.getElementsByTagName("revision");
						
						for (int k = 0; k < pageList.getLength(); k++) {
							text = ((Element)pageList.item(k)).getElementsByTagName("text").item(0).getTextContent();
						    r = Pattern.compile("(\\{\\{\\s*Infobox)(.*)(\\}\\})", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
						    m = r.matcher(text);//

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
						    	System.out.println("fachame '"+Infobox);

						    	//parse Name 
						    	r = Pattern.compile("(\\|\\s*Name\\s*=)(.*?)(\n)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
							    m = r.matcher(Infobox);//
							    if(m.find()) {
							    	Name = m.group(2).trim();
							    	System.out.println("nazov '"+Name);
							    }
							    //parse AKA
							    r = Pattern.compile("(\\|\\s*AKA\\s*=)(.*?)(\\|)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
							    m = r.matcher(Infobox);//
							    if(m.find()) {
							    	strAlternativeName = m.group(2).trim();
							    	System.out.println("alternative names '"+strAlternativeName );
							    	//parse more names
							    	
							    	
							    	alternativeObj.put("Name", Name);
							        listOfAlternatives.add(strAlternativeName);  						  
							        alternativeObj.put("Alternatives", listOfAlternatives);  
							    }
						    	
						    }
						  
						}
						
						if(pageList != null) continue;
					}
				}
				
			 try {  
	              
		            // Writing to a file  
		            File file = new File("output.json");  
		            file.createNewFile();  
		            FileWriter fileWriter = new FileWriter(file);  

		            fileWriter.write(alternativeObj.toJSONString());  
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
