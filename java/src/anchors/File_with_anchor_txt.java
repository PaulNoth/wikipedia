package anchors;

import java.io.*;

public class File_with_anchor_txt 
{
	private String file_name;
	private BufferedWriter bw;
	private BufferedReader br;
	
	//constructor
	public File_with_anchor_txt(String file_name, BufferedWriter bw, BufferedReader br) {
		this.file_name = file_name;
		this.bw = bw;
		this.br = br;
	}	
	
	//method add page title to file and separate it with tab \t 
	public void add_page_title(String page_title)
	{		
		try {
			bw.write(page_title + "\t");
		}
		catch (IOException e) 
		{	System.out.println(e.getMessage());	}
	}
	
	//method add anchor text and page link and separate them with tab \t
	public void add_page_anchor(String page_link, String page_anchor)
	{
		try {
			bw.write(page_link + "\t" + page_anchor + "\t");
		}
		catch (IOException e) 
		{	System.out.println(e.getMessage());	}	
	}

	//method load file content to Records list structure
	public void load_file_to_record_list()
	{
		try {
			String str;
			
			while((str = br.readLine()) != null)
			{	System.out.println(str);	}
			
			//only pages with at least one anchor link
    		//record_list.add_record(page_title, page_link, page_anchor);
		}
		catch (UnsupportedEncodingException e) 
		{	System.out.println(e.getMessage());	} 
		catch (IOException e) 
		{	System.out.println(e.getMessage());	}
		catch (Exception e)
		{	System.out.println(e.getMessage());	}
	}
	
	//method erase file content
	public void erase_file()
	{	try {
			PrintWriter	wr = new PrintWriter(file_name);
			wr.print("");
			wr.close();
		} 
		catch (FileNotFoundException e) 
		{	e.printStackTrace();	}		
	}
	
	//method break line in file
	public void file_new_line()
	{	try {
			bw.newLine();
		}
		catch (IOException e) 
		{	System.out.println(e.getMessage());	}
	}
}
