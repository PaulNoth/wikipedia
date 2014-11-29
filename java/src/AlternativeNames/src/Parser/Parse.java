package Parser;

import edu.jhu.nlp.wikipedia.InfoBox;
import edu.jhu.nlp.wikipedia.PageCallbackHandler;
import edu.jhu.nlp.wikipedia.WikiPage;
import edu.jhu.nlp.wikipedia.WikiXMLParser;
import edu.jhu.nlp.wikipedia.WikiXMLParserFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextArea;

/**
 * Trieda ktor· parsuje jeden vybrat˝ dokument
 * @author Samuel Benkovic
 *
 */
public class Parse implements ActionListener{

	JComboBox<String> File;
	JTextArea parseredData;
	String space = "	";
	 Writer w;
	/**
	 * Seter Pre Triedu GUI
	 * @param Combo - Subor ktory chceme parsovat , naraz sa da len 1
	 * @param parseredData - V pripade ak by sme chceli zobraziù vystup do GUI
	 */
	public void set(JComboBox<String> Combo, JTextArea parseredData) {
		this.File= Combo;
		this.parseredData = parseredData;
	}
	
	/**
	 * Metoda ktor· sa vykon· po stlacenÌ tlacidla  PARSE!
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		final Date oldDate = new Date();
		String workingDir = System.getProperty("user.dir");
		try{
		File statText = new File(workingDir+"/parsered/"+File.getSelectedItem().toString() );
         FileOutputStream is = new FileOutputStream(statText);
         OutputStreamWriter osw = new OutputStreamWriter(is, "UTF-8");    
         w = new BufferedWriter(osw);
        
		 w.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		 w.write("<Document>\n");
		 WikiXMLParser wxsp = WikiXMLParserFactory.getSAXParser(workingDir+"/input/"+File.getSelectedItem().toString());
		 try {
			
	            wxsp.setPageCallback(new PageCallbackHandler() { 
	                           public void process(WikiPage page) {
	                        	   InfoBox infoBox = page.getInfoBox();
                                   if (infoBox != null) {
                                	  List<String> infoBoxAka = getInfoboxAtrr(page.getInfoBox().dumpRaw(),"Aka");
                                	 
	                                  List<alsoCalled> textAkaText = getAkaText(page.getWikiText());
	                                  try
	                                  {	
	                                	  	if(!infoBoxAka.isEmpty() || !textAkaText.isEmpty() )
	                                	  	{
	                                	  		w.write(Replace("<article>\n"+space+"<title>\n"+space+space+page.getTitle()+space+"</title>\n"));
	                                	  		w.write(Replace(addAtrXML("aka",infoBoxAka)));
	                                		  for(alsoCalled aka: textAkaText){
	                                			w.write(Replace(addAlsoXML(aka.Title,aka.AlsoCalled)));
	                                		  }
	                                		  w.write("</article>\n");
	                                	  	}
	                                      
	                                  }catch(Exception e) 
	                                  {
	                                	  System.err.println("Problem writing to the file " +File.getSelectedItem().toString());
	                                  }
                                   }
	                           }

	            });
	           
	                
	           wxsp.parse();
	        }catch(Exception e) {
	                e.printStackTrace();
	        }
		 w.write("\n</Document>");
		 w.close();
		}
	 catch(Exception e) {
		 System.err.println("Problem writing to the file " +File.getSelectedItem().toString());
	 }
		 
		 double time = System.currentTimeMillis() - oldDate.getTime();
		 System.out.print("Sek˙nd: " +time/1000);
		
	}
	
	/**
	 * Metoda Ktora ma v sebe regExy potrebnÈ pre vyparsovanie Atributu z Infoboxu, V mojom pripade aka.
	 * @param infoboxText - Text Infoboxu
	 * @param Attribute - Hodnotu ktor˙ chceme, v mojom pripade aka
	 * @return - hodnoty ktorÈ atrib˙t obsahuje vo forme List<String>
	 */
	private List<String> getInfoboxAtrr(String infoboxText,String Attribute) {
		// Regex for parsing infobox attributes
				String re1="(\\|)";	// Single Character |
			    String re2="( |)";	// White Space 1 or no space
			    String re3="("+Attribute+")";	// Attribute eg. Aka
			    String re4 = "(.*=)"; // any char plus =
			    String re5="( |)";	// White Space 2 or no space
			    String re6="(.*\n)";	// rest of the line
			    Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6,Pattern.CASE_INSENSITIVE);
			    List<String> attributes =  new ArrayList<String>();
			    infoboxText = infoboxText.trim().replaceAll(" +", " ");
			    Matcher m = p.matcher(infoboxText);
			    while(m.find()) {
			    	if(!m.group(6).trim().isEmpty())
			    	attributes.add(m.group(6).trim());	
			    }
			    return attributes;
			    
	}
	/**
	 * Metoda Ktora ma v sebe regExy potrebnÈ pre vyparsovanie Atributu textu , AkaTextu. 
	 * @param text - cel˝ WikiText
	 * @return - vyparsovany vo forme List<alsoCalled>
	 * @see alsoColled Class
	 */
		private List<alsoCalled> getAkaText(String text) {
			// Regex for parsing infobox attributes
			String re1="(\\[\\[)";	// Single Character |
			String re2="(\\s*[a-z ]*)"; // title
			String re3= "(\\]\\])";
		    String re4="(\\(aka| \\(aka| \\( aka|\\( aka)";	// Aka
		    String re5="(\\.|)";
		    String re6="(\\'|\\'\\'|)";	
		    String re7="(.*?)";	 // rest of 
		    String re8="(\\))";
		    Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8,Pattern.CASE_INSENSITIVE);
		    List<alsoCalled> attributes =  new ArrayList<alsoCalled>();
		    text = text.trim().replaceAll(" +", " ");
		    Matcher m = p.matcher(text);
		    while(m.find()) {
		    	if(!m.group(3).trim().isEmpty())
		    	{
		    	alsoCalled ac= new alsoCalled(m.group(2).trim(),m.group(7).replaceAll("\\[","").replaceAll("\\]", ""));
		    	attributes.add(ac);
		    	}
		    }
		    return attributes;
		}
	/**
	 * Metoda ktora len Formatuje vystup, dava medzery atd... Urcen· najme pre udaje z infoboxu
	 * @param atr - Meno Atrib˙tu
	 * @param value - Hodnota
	 * @return- Naformatovany atrib˙t
	 */
	private String addAtrXML(String atr,List<String> value){
		if(!value.isEmpty() && value != null){
			String Values="";
			for (String s : value){
				Values += space+space+s+"\n";
			}
		return space+"<"+atr+">\n"+Values + "\n"+space+"</"+atr+">\n";
		}
		return "";
	}

	/**
	 * Metoda ktora len Formatuje vystup, dava medzery atd... Urcen· najme pre akaText
	 * @param Title - Hodnota Title v tagu AkaText
	 * @param Also - Hodnota Aka v tagu AkaText
	 * @return - Naformatovany atrib˙t
	 */
	private String addAlsoXML(String Title,String Also) {
		String also=space+space+"<aka>\n"+space+space+space+Also+"\n"+space+space+"</aka>\n";
		String title=space+space+"<title>\n"+space+space+space+Title+"\n"+space+space+"</title>\n";
		if(!Title.isEmpty() && Title != null){
			
		return space+"<AkaText>\n"+title+also+space+"</AkaText>\n";
		}
		return "";
	}
	

	/**
	 * Odstranenie Neziaducich znakov v mojom pripade LUCENE nevedel parsovat znak & , ktor˝ som musel nahradiù &amp; 
	 * @param ReplaceString
	 * @return
	 */
	private String Replace(String ReplaceString) {
		ReplaceString = ReplaceString.replace("&", "&amp;");
		return ReplaceString;
	}



}
