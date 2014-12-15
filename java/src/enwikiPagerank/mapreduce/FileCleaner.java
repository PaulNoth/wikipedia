/**
 * 
 */
package enwikiPagerank.mapreduce;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jozef
 * 
 * this class creates final files for pagerank computation
 * very importatn file is file onlyIDs.txt - this file holds the IDs.
 * from this file, we generate hashmaps for pagerank and hashmaps for outgoing links - without these maps
 * we can not compute pagerank
 *
 */
public class FileCleaner {

	public static HashMap<String, String> map = new HashMap<String, String>();
	public static Pattern pattern = Pattern.compile("^[^\\s]+");
	public static Matcher matcher;
	public static String idOrName = "";

	public static String getNameOrId(String line, Pattern pattern) {
		/*
		 * this methods returns the first element in line - which is ID of page for which we compute pagerank
		 */
		matcher = pattern.matcher(line);

		if (matcher.find()) {
			idOrName = matcher.group(0);
		}

		return idOrName;
	}
	
	public static String cleanLine(String[] p){
		/*
		 * this is heart of cleaning file
		 * this method search in hashmap that contains IDs
		 * it takes one line and check it
		 * if there is ID in line, which is not obtained in hashmap, the line is updated and
		 * the ID is removed.
		 * this will ensure that we will work only with IDs that are in our subdomain
		 */
		String all = "";
		for(int i = 1; i< p.length; i++){
			if(map.containsKey(p[i])){
				all += p[i] + " ";
			}
		}
		
		return all;
	}

	public static void cleanFile(String inputFileToClean, String fileToMap, String output) {
		createHashMap(fileToMap); // first we create hashmap from onlyIDs.txt
		
		BufferedReader br = null;
		String line = null;
		String[] p;
		String ids = null;
		String all = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			br = new BufferedReader(new FileReader(inputFileToClean));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			int c = 0;
			while ((line = br.readLine()) != null) { //then we read file
				p = line.split(" ");
				ids = cleanLine(p); // clean the line
				all = p[0] + " " + ids; 
				
				list.add(all);
				if (c % 10000 == 0) {

					System.out.println("write" + c);
					PagelinksParser.writeFileList(output, list); // write the clean lines into file
					list.clear();
					br.mark(0);
					br.reset();
					System.gc();
				}
				c++;

			}
			if (!list.isEmpty()) {
				PagelinksParser.writeFileList(output, list);

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
	}

	public static void  createHashMap(String input){
		/*
		 * method whic create hashMap from file
		 *  this hashmap holds ID as key and ID as value
		 */
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			int c = 0;
			String line;
			while((line = br.readLine()) != null){
				map.put(line, line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public static void writeIdsToFile(String input, String output) {
		/*
		 * this method write IDs into file
		 */
		FileInputStream inputStream = null;
		Scanner sc = null;
		String line = null;
		String id = null;
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			int c = 0;
			while ((line = br.readLine()) != null) { //read file
				id = getNameOrId(line, pattern); //get only IDs
				// System.out.println(id);
				// map.put(id, id);
				list.add(id); //add ids
				line = null;
				id = null;
				if (c % 10000 == 0) {

					System.out.println("write" + c);
					PagelinksParser.writeFileList(output, list); //write IDs to file onlyIDs.txt
					list.clear();
					br.mark(0);
					br.reset();
					System.gc();
				}
				c++;

			}
			if (!list.isEmpty()) {
				PagelinksParser.writeFileList(output, list);

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
