package abstracts;

import java.io.IOException;
import java.io.Writer;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;

import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;



/**
 * 
 * @author Matej Toma
 *
 */
public class WikiPagesAndAbstractsParser {
		
	// Only mapping XMLEvents to local variables
	private static final int startElement = XMLEvent.START_ELEMENT;
	private static final int endElement = XMLEvent.END_ELEMENT;
	private static final int textOfElement = XMLEvent.CHARACTERS;
	
	private String articleText = "";
	private String title = "";
	private String abstractText = "";
	
	private String currentNamespace = "";

	private StringBuilder strBuild = new StringBuilder();
		
	// Entities will be created in code below
	public ParsedWikiArticleEntity newPage;
	public ParsedWikiAbstractEntity newDoc;
	
	private ArrayList<ParsedWikiArticleEntity> allPages = new ArrayList<ParsedWikiArticleEntity>();
	private HashMap<String,ParsedWikiAbstractEntity> allAbstracts = new HashMap<String,ParsedWikiAbstractEntity>();
	
	// Setted from outer, this set contains common IDs from articles and abstracts dataset.
	private LinkedHashSet<String> intersection = new LinkedHashSet<>();
	
	// Used only for statistical reasons
	public double totalPercentage = 0;
	public int totalComparedRecords = 0;
	public int unComparedAbstracts = 0;
	public int badAbstracts = 0;
	public int unComparedArticles = 0;
	
	// Setted to true automatically in code when input query is finded
	private Boolean queryFinished = false;
	
	//Setted to true from outer to indicate we want to find concrete title
	private Boolean queryOption = false;
	
	//Setted from outer when JUnit is processing.
	private Boolean testEnviroment = false;
	
	//Number of parsed sentences from text
	private final int numberOfParsedSentencesFromText = 3;
		
	//Devel enviroment
	private Boolean develEnviroment = true;
	
	private Writer develFileWithSimilarity;
	
	private ArrayList<Double> valueForMedianStats = new ArrayList<Double>();
	
	/**
	 * Reading XML files from input
	 * @param stream Stream
	 * @param isAbstractFile This is set to true when Abstract XMLs are readed.
	 * @throws XMLStreamException
	 */
	public void readXML(XMLStreamReader stream, Boolean isAbstractFile) throws XMLStreamException{
		while (stream.hasNext() && !queryFinished) {
        	int eventType = stream.next();
                switch (eventType) {
                	case XMLEvent.CDATA:
                	case XMLEvent.SPACE:
                	case XMLEvent.COMMENT:
	                case textOfElement:
	                	captureText(stream.getText());
	                    break;
	                case endElement:
	                	// Decision which dataset is inputed, see comments for methods below to understand.
	                	if(isAbstractFile)
	                		endCurrentElementDoc(stream.getLocalName());
	                	else
	                		endCurrentElementPage(stream.getLocalName());
	                    break;
	                case startElement:
	                	// See comments for this method below
	                	createElement(stream.getLocalName());
	                    break;
                }
        }
	}
	
	/**
	 * When new entity is readed from XML string builder is resetted
	 * @param elementName Name of matching tag from XML
	 */
	private void createElement(String elementName){
		strBuild.setLength(0);
	}
	
	/**
	 * Method is called when reader is reading content inside tags
	 * @param insideText Text inside of tags.
	 */
	private void captureText(String insideText){
		strBuild.append(insideText);
	}
	
	/**
	 * Method is called when reader is reading Abstract XML files. This method must be here because
	 * structure of abstract XML and article XML is simillar.
	 * @param elementName Name of matching tag from XML, only few is matched because only them
	 * are relevant for me.
	 */
	private void endCurrentElementDoc(String elementName){	
		switch (elementName) {
		case "title":
			title = strBuild.toString().trim();
			break;
		case "abstract":
			String temp = strBuild.toString().trim();
			// Check if parsed abstract text from wikipedia is really text of article, not part of some template.
			if(temp.matches("^[A-Za-z0-9].+"))
				abstractText = temp;
			else
				unComparedAbstracts++;
			break;
		case "doc":
			// Continue with processing only when abstract is not empty and is in common IDs
			if(!abstractText.equals("") && intersection.contains(title)){
				newDoc = new ParsedWikiAbstractEntity();
				newDoc.setTitleTag(title);
				newDoc.setAbstractTextTag(abstractText);
				allAbstracts.put(title,newDoc);
				abstractText = "";
				
				/* This part of code is executed only when user enter some wikipedia title. When engine find them, set queryFinished to true and
				reading XML is finished */
				if(queryOption && allAbstracts.size() > 0)
					queryFinished = true;
			}	
		default:
			break;
		}
		
	}
	
	/**
	 * This method is called when XML reader is reading article xml file in every
	 * situations when reader is at the end tag.
	 * @param elementName Name of matching tag from XML, only few is matched because only them
	 * are relevant for me.
	 */
	private void endCurrentElementPage(String elementName){	
		switch (elementName) {
			case "ns":
				currentNamespace = strBuild.toString();	
				break;
			case "title":
				title = "Wikipedia: "+strBuild.toString().trim();
				break;
			case "text":
				/* Continue only when namespace of article entity is zero, is in common list of IDS or test
				enviroment is set. List of all namespaces are listed in XML at the begginng. */
				if(currentNamespace.equals("0") && (intersection.contains(title) || testEnviroment)){
					// Continue only if text is not redirect
					if(!isRedirect(strBuild.toString().substring(0, 10)) ){
						// Continue only if article title is in abstract array list or test enviroment is setted to true
						if(allAbstracts.get(title) != null || testEnviroment){
							newPage = new ParsedWikiArticleEntity();
							newPage.setTitleTag(title);
							
							articleText = strBuild.toString();
							articleText = cleanWikiPageText(articleText).trim();
							
							if(testEnviroment){
								newPage.setTextTag(articleText);
								allPages.add(newPage);
							}  else {
								String abstractText = allAbstracts.get(title).getAbstractTextTag();
								/*System.out.println("-------------------");
								System.out.println("Abstrakt: "+allAbstracts.get(title).getTitleTag()+", Page: "+ newPage.getTitleTag());
								System.out.println("Abstrakt text: "+abstractText);
								System.out.println("Page text: "+elementText);*/
								try {
									//Text of abstract is compared to text of article
									CosineSimilarityLibByMarkButler cosSim = new CosineSimilarityLibByMarkButler(abstractText, articleText);
									
									/* Compute cosine similarity only if cosSim is not null, this happend for example when abstract text contain only one term 
									and that term is number. I dont know why but term vecter is not created in that case. See implementation of CosineSimilarityLibByMarkButler */
									if(cosSim != null){
										DecimalFormat df = new DecimalFormat("###.##");
										
										// Cosine similarity of two texts
										double similarity = cosSim.getCosineSimilarity();
										
										// This controle is here because of reasons above
										if(!Double.isNaN(similarity)){
											totalPercentage += similarity;
											totalComparedRecords++;
											valueForMedianStats.add(similarity); 
										} else
											badAbstracts++;
										
										/* This part of code is executed only when user enter some wikipedia title.  When engine find them, set queryFinished to true 
										   and reading XML is finished */
										if(queryOption){
											System.out.println("Abstract from XML: "+ abstractText);
											System.out.println("Abstract from article: "+articleText);
											System.out.println("Cosine similarity: "+ df.format(similarity*100)+" %");
											queryFinished = true;
										} else {
											if(develEnviroment)
												System.out.println(totalComparedRecords+".) Comparing: "+ allAbstracts.get(title).getTitleTag() + " and "+newPage.getTitleTag()+", Similarity: "+df.format(similarity*100)+" %");
											else{
												if(!Double.isNaN(similarity)){
													System.out.println(similarity+";"+totalComparedRecords+";");
													develFileWithSimilarity.write(similarity+";"+totalComparedRecords+";"+"\n");
													valueForMedianStats.add(similarity); 
												}
											}
												
										}
											
									} 
								} catch (IOException e) {
									System.out.println(e.getLocalizedMessage());
									e.printStackTrace();
								}
							}
						} else
							unComparedArticles++;				
					}
				}
				break;
			default:
				break;
		}			
	}
	
	/**
	 * Method which test if text of article entity is original wiki page or redirect
	 * @param text Text from text tag in XML
	 * @return Return true if article entity is only redirect, not original page
	 */
	private Boolean isRedirect(String text){
		return text.toLowerCase().matches("^#redirect.*");
	}
	
	/**
	 * This method clean wikipedia text from template, category file marks and so on. Also
	 * provide conversion from Media wiki italic, bold marks to normal readable text
	 * @param text Input text is text of text tag from XML of article entity
	 * @return Returned string is cleaned from marks and also can by cutted by setting 
	 * number of sentences in code.
	 */
	private String cleanWikiPageText(String text){
		String result = "";
		
		result = text;
		
		//Remove comments
		result = result.replaceAll("<!--[^>]+>", "");
		
		//Remove {{#...}}
		result = result.replaceAll("\\{\\{#.+?\\}{2}", "");
		
		//Remove text and curly brackets inline
		//result = result.replaceAll("\\{\\{.+?\\}{2}", "");
		result = result.replaceAll("\\{{1,2}.+?\\}{1,2}", "");
		
		//Remove everything else in curly brackets and new lines
		//result = result.replaceAll("\\*? ?\\{\\{[^}]+\\}\\}","");
		result = result.replaceAll("\\*? ?\\{{1,2}[^}]+\\}{1,2}","");
		
		
		/*--------BONUS REMOVERS------*/
		//Remove images,categories,files... in categories
		result = result.replaceAll("\\[\\[.+:.*\\]\\]", "");
		
		//Remove bold and italic marks
		result = result.replaceAll("\\'{2,3}", "");
		
		//Remove headers
		result = result.replaceAll("={2,}.+={2,}", "");	
		
		//Remove new lines
		result = result.replaceAll("\\n", "");
		
		//result = result.replaceAll("\\[{2}(.+)?\\|(.+)?\\]{2}","$2");
		//result = result.replaceAll("\\[(.*?)\\]|\\[(.*?)\\|(.*?)\\]","$2");
		
		result = result.replaceAll("\\[{2}([^\\]^\\|]+)\\]{2}", "$1");
		result = result.replaceAll("\\[{2}(.*?)\\|(.*?)\\]{2}", "$2");
		
		//Number of lines in output string of article text comment code bellow to obtain fulltext.
		
		//System.out.println(result);
		
		//String[] sentences = result.split("(\\. )|[!?] {0,1}", 3);
		/*String[] sentences = result.split("(?i)(?<=[.?!])\\S+(?=[a-z])",3);
		
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sentences.length; i++) {
			sb.append(sentences[i]+". ");
		}
		result = sb.toString();*/
		
		//System.out.println(result);
		
		BreakIterator bi = BreakIterator.getSentenceInstance();
		bi.setText(result);
		int index = 0;
		int i = 0;
		
		StringBuilder sb = new StringBuilder();
		while (bi.next() != BreakIterator.DONE && i < numberOfParsedSentencesFromText) {
			sb.append(result.substring(index, bi.current()).trim());
		    index = bi.current();
		    i++;
		}

		return result = sb.toString();
	}
	
	
	// Getters and setters
	public ArrayList<ParsedWikiArticleEntity> getAllPages(){
		return allPages;
	}
	
	public HashMap<String,ParsedWikiAbstractEntity> getAllAbstracts(){
		return allAbstracts;
	}
	
	public void setIntersectionBetwenArticlesAndAbstracts(LinkedHashSet<String> inter){
		this.intersection = inter;
	}
	
	public Integer getUncomparedAbstracts(){
		return (intersection.size()-allAbstracts.size());
	}
	
	public void setQueryOption(Boolean state){
		this.queryOption = state;
	}
	
	public void resetQueryFinished(){
		this.queryFinished = false;
	}
	
	public Boolean getQueryFinised(){
		return queryFinished;
	}
	
	public void setTestEnvireoment(Boolean testEnviroment){
		this.testEnviroment = testEnviroment;
	}
	
	public void setDevelFile(Writer develFile){
		this.develFileWithSimilarity = develFile;
	}
	
	public ArrayList<Double> getValuesForMedians(){
		Collections.sort(valueForMedianStats);
		return valueForMedianStats;
	}
}