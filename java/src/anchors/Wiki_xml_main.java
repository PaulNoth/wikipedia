package anchors;

import java.io.*;

public class Wiki_xml_main 
{
	public static void main (String[] args) throws Exception
	{
		//String file_path = "../data/enwiki-latest-pages-articles.xml";		
		String file_path = "../data/sample_input_enwiki-pages-articles-123.xml";
		String file_name = "../data/anchor.txt";
		
		Record_list record_list = 	new Record_list();
		BufferedWriter bw = 		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_name, true),"UTF-8"));
		BufferedReader br = 		new BufferedReader(new InputStreamReader(new FileInputStream(file_name), "UTF8"));
		File_with_anchor_txt file = new File_with_anchor_txt(file_name, bw, br);
		XML_wiki xmlw = 			new XML_wiki(file_path, file, record_list);
		
		file.erase_file();		
		
		xmlw.read_wiki();
		
		bw.close();	//close text file
		
		//file.load_file_to_record_list();
		br.close();
		
		//record_list.print_list();
	}
}
