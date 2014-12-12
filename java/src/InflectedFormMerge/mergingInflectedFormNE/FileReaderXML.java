package mergingInflectedFormNE;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import testAndTools.StringTools;

// TODO: Auto-generated Javadoc
/**
 * The Class FileReaderXML.
 */
public class FileReaderXML {
	
	/** The hm link anchor. */
	private HashMap<String, LinkAnchor> hmLinkAnchor;
	
	/** The input file. */
	private String inputFile;
	
	/**
	 * Instantiates a new file reader xml.
	 *
	 * @param file the file
	 * @param hmLinkAnchor the hm link anchor
	 */
	public FileReaderXML(String file, HashMap<String, LinkAnchor> hmLinkAnchor){
		this.hmLinkAnchor = hmLinkAnchor;
		inputFile = file;
		
		try {
			readFile();
		} catch (IOException e) {
			System.out.println("Error: " + "could not open file" + "  " + inputFile);
			e.printStackTrace();
		}
	}
	
	/**
	 * Read file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readFile() throws IOException{		
		FileInputStream fstream = new FileInputStream(inputFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
		
		
		Pattern pPageStart = Pattern.compile("<page.*?>");		//regex for page start detection
		Matcher mPageStart;
		Pattern pPageEnd = Pattern.compile("</page>");			//regex for page end detection
		Matcher mPageEnd;
		
		Pattern pTextStart = Pattern.compile("<text.*?>");		//regex for page text start detection
		Matcher mTextStart;
		Pattern pTextEnd = Pattern.compile("</text>");			//regex for page text end detection
		Matcher mTextEnd;
		
		boolean inTextElement = false;
		boolean inPageElement = false;
		String strLine;
			
		while ((strLine = br.readLine()) != null){
			if(!inPageElement){	// line not in wiki page
				mPageStart = pPageStart.matcher(strLine);
				if(mPageStart.find()){
					//System.out.println("PAGE");
					inPageElement = true;
					continue;
				}				
			}
			else{		//line in wiki page element
				mPageEnd = pPageEnd.matcher(strLine);
				if(mPageEnd.find()){	//end of wiki page
					inPageElement = false;
					continue;
				}							
				if(!inTextElement){	//line in wiki page text
					mTextStart = pTextStart.matcher(strLine);
					if(mTextStart.find()){
						//System.out.println("TEXT");
						inTextElement = true;
						
						handleLine(strLine);
					}					
				}
				else{
					mTextEnd = pTextEnd.matcher(strLine);
					if(mTextEnd.find()){	//end of page text
						inTextElement = false;
					}
					
					if(inPageElement && inTextElement){	
						handleLine(strLine);
					}
				}
			}
		}
		br.close();
		in.close();
		fstream.close();
	}
	
	/**
	 * Handle line.
	 *
	 * @param str the str
	 */
	private void handleLine(String str){
		
		StringTools ST = new StringTools();
		//System.out.println(str);
		
		Pattern pLink = Pattern.compile("(\\[\\[([^]]+?)\\]\\]\\p{L}*)");
		Matcher mLink;
		
		Pattern pOtherLink = Pattern.compile("\\[\\[[^\\|\\]]+:[^\\]]+\\]\\]");
		Matcher mOtherLink;
		
		String pom = "";
		mLink = pLink.matcher(str);
		while(mLink.find()){
			mOtherLink = pOtherLink.matcher(str); 
			if(!mOtherLink.find()){
				pom = ST.removeHTMLCharactersFromString(mLink.group(1));
				//System.out.println(pom);
				LinkAnchor pomLA = new LinkAnchor(pom);
				
				if(!pomLA.getLink().equalsIgnoreCase("")){
					if(hmLinkAnchor.containsKey(pomLA.getLink()+pomLA.getAnchor())){
						hmLinkAnchor.get(pomLA.getLink()+pomLA.getAnchor()).addOccurrence();
					}
					else{
						hmLinkAnchor.put(pomLA.getLink()+pomLA.getAnchor(), pomLA);
					}
				}
			}
		}
	}
	
	/**
	 * Gets the link anchor.
	 *
	 * @return the link anchor
	 */
	public HashMap<String, LinkAnchor> getLinkAnchor(){
		return hmLinkAnchor;
	}
	
	/**
	 * Prints the hash map link anchor.
	 */
	public void printHashMapLinkAnchor(){
		Iterator<String> keySetIterator = hmLinkAnchor.keySet().iterator();
		int count = 0;
		int totalCount = 0;
		while(keySetIterator.hasNext()){
			String key = keySetIterator.next();
			System.out.println("link: " + hmLinkAnchor.get(key).getLink() + "	anchor: " + hmLinkAnchor.get(key).getAnchor() + "	occurance: " + hmLinkAnchor.get(key).getOccurrences());
			count++;
			totalCount = totalCount + hmLinkAnchor.get(key).getOccurrences();
		}
		System.out.println("==============================================================\nNumber of individual links: "+count+"\nNumber of all links: "+totalCount);
		System.out.println("==============================================================\n");
	}
}
