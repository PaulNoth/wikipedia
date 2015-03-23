package enwikiPagerank.mapreduce;
/*
 * this class creates subdomain of pages - quite simple
 */

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubDomainCreator {
	
	public static List<String> sentenceList = new ArrayList<String>();
	
	
	public static void createSubDomain(String input,String output, long index) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(input);
			sc = new Scanner(inputStream, "UTF-8");
			long counter = 0;
			String line = null;
			String titleP = null;
			String namespace = null;
			String id = null;
			String[] p = null;
			String title = null;
			String sentence = null;
			while (sc.hasNextLine() && counter < index) {
				counter++;
				line = sc.nextLine();
				p = line.split(" ");
				if(p.length < 4) continue;
				//System.out.println(line);
				titleP = p[2];
				namespace = p[1];
				id = p[0];
				
				title = namespace + "#" + titleP; // join namespace and title
				sentence = title + " " + id;
				sentenceList.add(sentence); // add sentence into list
				
				//System.out.println("adding " + title + " " + id);
				
				if(counter % 100000 == 0){ // if list has 10000 records, write the file
					PagelinksParser.writeFileList(output, sentenceList);
					sentenceList.clear();
					sentenceList = new ArrayList<String>();
					
				}
				if(counter % 200000 == 0){
					System.gc(); //need to release system resources because of lack of RAM
				}
				titleP = null;
				namespace = null;
				id = null;
				title = null;
				p=null;
				line =null;
				sentence = null;
				
				
			}
			if(!sentenceList.isEmpty()){ // write the rest of list
				PagelinksParser.writeFileList(output, sentenceList);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		

}
