package other;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mergingInflectedFormNE.LinkAnchor;
import mergingInflectedFormNE.Page;

// TODO: Auto-generated Javadoc
/**
 * The Class Skuska.
 */
public class TitleExtraction {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		TitleExtraction s = new TitleExtraction();
		try {
			String inFile = "../data/sample_input_skwiki-latest-pages-articles_3-pages.xml";
			String outFile = "skwiki_page_titles_cleaned";
			LinkedList<Page> pageList = new LinkedList<Page>();
			LinkedList<LinkAnchor> link_anchor = new LinkedList<LinkAnchor>();
			
			s.createTestFileForPageTitles(inFile, outFile, pageList, link_anchor);
			//s.createTestFileForLinkAnchor("../data/skwiki-latest-pages-articles.xml", "../data/skwiki_link_anchor_cleaned.txt");
			//s.createTestFileForLinkAnchor("../data/sample_input_skwiki-latest-pages-articles_3-pages.xml", "../data/skwiki_link_anchor_cleaned.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*public static void main(String[] args) {
		Skuska s = new Skuska();
		LinkedList<Page> pageList = new LinkedList<Page>();
		LinkedList<LinkAnchor> link_anchor = new LinkedList<LinkAnchor>();
		try {
			//s.readFile("../data/sample_input_skwiki-latest-pages-articles_3-pages.xml", pageList, link_anchor);
			s.createTestFileForPageTitles("../data/skwiki-latest-pages-articles.xml", "../data/skwiki_page_titles_cleaned.txt", pageList, link_anchor);
			
			int i=1;
			for(Page p : pageList){
				if(p.getPageTitle().equalsIgnoreCase("")){
					
				}
				else{
					System.out.println(i++ + ": "+ p.getPageTitle());
				}

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
*/	
	/**
	 * Extract page titles.
	 *
	 * @param inFile the in file
	 * @return the string
	 */
	public String extractPageTitles(String inFile){
		StringBuilder s = new StringBuilder();
		
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(inFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;
			
			Pattern pTitle = Pattern.compile("<title>(.+)</title>");	//regex for page title detection
			Matcher mTitle;
			
			Pattern pOtherTitle = Pattern.compile("(Kategória:|Portál:|WP:|Wikipédia:|Šablóna:|MediaWiki:|Pomoc:|Súbor:)");
			Matcher mOtherTitle;
			
			//int i = 0;
			while ((strLine = br.readLine()) != null){
				
				//System.out.println(strLine);
				mTitle = pTitle.matcher(strLine);
				if(mTitle.find()){
					mOtherTitle = pOtherTitle.matcher(strLine); 
					if(!mOtherTitle.find()){
						//System.out.println(mTitle.group(1).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\""));
						//s = s.concat(mTitle.group(1).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"") + "\n");
						
						s.append( mTitle.group(1).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ").replaceAll("''", "") + "\n");
						//s = s + mTitle.group(1).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"") + "\n";
						//System.out.println(i++);
					}
				}
			}
		
			br.close();
			in.close();
			fstream.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s.toString();
	}
	
	/**
	 * createTestFileForPageTitles.
	 *
	 * @param inFile the in file
	 * @param outFile the out file
	 * @param pageList the page list
	 * @param link_anchor the link_anchor
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createTestFileForPageTitles(String inFile, String outFile, LinkedList<Page> pageList, LinkedList<LinkAnchor> link_anchor) throws IOException{
		FileInputStream fstream = new FileInputStream(inFile);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),"UTF-8"));
		
		String strLine;
					
		Pattern pTitle = Pattern.compile("<title>(.+)</title>");	//regex for page title detection
		Matcher mTitle;
		
		Pattern pOtherTitle = Pattern.compile("(Kategória:|Portál:|WP:|Wikipédia:|Šablóna:|MediaWiki:|Pomoc:|Súbor:)");
		Matcher mOtherTitle;
		
		//int i = 0;
		while ((strLine = br.readLine()) != null){
			
			//System.out.println(strLine);
			mTitle = pTitle.matcher(strLine);
			if(mTitle.find()){
				mOtherTitle = pOtherTitle.matcher(strLine); 
				if(!mOtherTitle.find()){
					out.write(mTitle.group(1).trim().replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&nbsp;", " ").replaceAll("''", ""));
					out.newLine();
					//i++;
				}
				//System.out.println(strLine);

			}
			

		}
		//System.out.println("Pocet spracovanych riadkov suboru: " + i);
		
		out.close();
				
		br.close();
		in.close();
		fstream.close();
		
	}
}
