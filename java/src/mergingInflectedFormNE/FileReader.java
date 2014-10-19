package mergingInflectedFormNE;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// TODO: Auto-generated Javadoc
/**
 * The Class FileReader.
 */
public class FileReader {

	/** The file. */
	private String file;
	
	/** The page list. */
	private LinkedList<Page> pageList;
	
	/** The link_anchor. */
	private LinkedList<LinkAnchor> link_anchor;
	
	/**
	 * Instantiates a new file reader.
	 *
	 * @param file the file
	 * @param pageList the page list
	 */
	public FileReader(String file, LinkedList<Page> pageList){
		link_anchor = new LinkedList<LinkAnchor>();
		this.pageList = pageList;
		this.file=file;
		
		try {
			readFile();
		} catch (IOException e) {
			System.out.println("Error: " + "could not open file" + "  " + file);
			e.printStackTrace();
		}
	}
	
	/**
	 * Read file.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void readFile() throws IOException{
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
		
		//BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("pageTitles3.txt"),"UTF-8"));
		
		String strLine;
					
		Pattern pTitle = Pattern.compile("<title>(\\p{Lu}\\p{L}+[^:]+)</title>");	//regex for page title detection
		Matcher mTitle;
		
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
		
		int i = 0;
		String pomText = "";
		String pomTitle = "";
		while ((strLine = br.readLine()) != null){
			
			if(!inPageElement){	// line not in wiki page
				mPageStart = pPageStart.matcher(strLine);
				if(mPageStart.find()){
					//System.out.println("PAGE");
					inPageElement = true;
					continue;
				}
				
			}
			else{
				mPageEnd = pPageEnd.matcher(strLine);
				if(mPageEnd.find()){
					pageList.add(new Page(pomTitle, pomText, link_anchor));
					pomText = "";
					pomTitle = "";
					inPageElement = false;
					//System.out.println("/PAGE");
					continue;
				}
							
				if(!inTextElement){
					mTextStart = pTextStart.matcher(strLine);
					if(mTextStart.find()){
						//System.out.println("TEXT");
						inTextElement = true;
						pomText = pomText + strLine + "\n";
					}
					
					mTitle = pTitle.matcher(strLine);
					if(mTitle.find()){
						/*try {
							out.write(mTitle.group(1).trim()  + "\n");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						//System.out.println("TITLE " + mTitle.group(1).trim() + " /TITLE");
						//System.out.println( mTitle.group(1).trim());
						pomTitle = mTitle.group(1).trim();
						
					}
				}
				else{
					mTextEnd = pTextEnd.matcher(strLine);
					if(mTextEnd.find()){
						//System.out.println("/TEXT");
						pomText = pomText + strLine + "\n";
						inTextElement = false;
					}
					
					if(inPageElement && inTextElement){	
						pomText = pomText + strLine + "\n";
					}
				}
			}
			//System.out.println(strLine);
			//processLine(strLine, out);
			i++;		
		}
		//System.out.println("Pocet spracovanych riadkov suboru: " + i);
		
		//out.close();
				
		br.close();
		in.close();
		fstream.close();
		
	}
	
	//  \p{L}	- letter				\pL
	//	\p{Lu}	- uppercase letter		\pLu
	/**
	 * Process line.
	 *
	 * @param strLine the str line
	 * @param out the out
	 */
	private void processLine(String strLine, BufferedWriter out){
		strLine = strLine.replaceAll("\\]\\].*\\[\\[", "]]\n[[");

		Pattern pLink = Pattern.compile(".*\\[\\[\\p{L}+\\|\\p{L}+\\]\\].*");
		Matcher mLink;
		String[] sSplit = strLine.split("\n");
		
		String pom;
		for(String s : sSplit){
			mLink = pLink.matcher(s);
			if(mLink.find()){
					pom = s.replaceAll("\\]\\].*", "]]").replaceAll(".*\\[\\[", "[[");
					this.link_anchor.add(new LinkAnchor(pom));
					
					try {
						out.write(pom + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		}
	}
	
	/**
	 * To string link anchor list.
	 *
	 * @return the string
	 */
	public String ToStringLinkAnchorList(){
		String s="";
		for(LinkAnchor la : link_anchor){
			s = s + la.getLink() + "|" + la.getAnchor() + "\n";
			
			
		}
		return s;
	}
	
	/**
	 * Gets the link anchor.
	 *
	 * @return the link anchor
	 */
	public LinkedList<LinkAnchor> getLinkAnchor(){
		return link_anchor;
	}
	
	
}
