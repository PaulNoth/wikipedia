package anchors;

import java.io.*;
import java.util.regex.*;



public class XML_wiki 
{
	private InputStream ins = null; 		// raw byte-stream
	private Reader r = null; 				// cooked reader
	private BufferedReader br = null; 		// buffered for readLine()


	private String path;				//wikipedia xml dump path
	private File_with_anchor_txt file;	
	private Record_list record_list;
	
	//constructor
	public XML_wiki(String path, File_with_anchor_txt file, Record_list record_list)
	{
		this.path = path;
		this.file = file;
		this.record_list = record_list;
	}
	
	//Method read wikipedia dump and searching for page_title, page_linka and page_anchor
	public void read_wiki () throws Exception
	{
		int i = 0;
		int j = 0;
		int k = 0;

		Pattern p_title = Pattern.compile("(<title>)(.+)(</title>)");			//compile regular expression for finding page title
		Pattern p_anchor = Pattern.compile("\\[\\[([^:\\]]+?)\\|(.+?)\\]\\]"); 	//compile regular expression for anchor and link
		Matcher m_title = null;
		Matcher m_anchor = null;
		
		try 
		{   String s;
		    ins = new FileInputStream(path);
		    r = new InputStreamReader(ins, "UTF-8"); 
		    br = new BufferedReader(r);
		    String page_title = null;
		    
		    while ((s = br.readLine()) != null)// && i < 100) 
		    {	//System.out.println(s);	
		    	m_title = p_title.matcher(s); 
		    	while(m_title.find())	// if page title is found
		    	{
		    		if(j>0)	{ file.file_new_line();	}	// new page title at new line 
		    		page_title = m_title.group(2).trim();
		    		file.add_page_title(page_title);
		    		j++;
		    	}
		    	
	    		m_anchor = p_anchor.matcher(s);
		    	while(m_anchor.find())	//if page anchor and link is found
		    	{
	    			String page_link = m_anchor.group(1).trim();
		    		String page_anchor = m_anchor.group(2).trim();
		    		file.add_page_anchor(page_link, page_anchor);
		    		k++;
		    	}
		    	i++;
		    }
		    System.out.println("Po�et riadkov: "+ i + " |  Po�et str�nok: " + j + "| Po�et anchorov: " + k);
		    
		}
		catch (Exception e)
		{    System.err.println(e.getMessage());	}	 // handle exception
		finally 
		{   if (br != null) { try { br.close(); } catch(Throwable t) { /* ensure close happens */ } }
		    if (r != null) { try { r.close(); } catch(Throwable t) { /* ensure close happens */ } }
		    if (ins != null) { try { ins.close(); } catch(Throwable t) { /* ensure close happens */ } }
		}
	}
}

