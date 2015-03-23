package enwikiPagerank.pagerankComputation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import enwikiPagerank.mapreduce.PagelinksParser;

public class SampleCreator {
	
	public static void createSampleCleanFile(String input, String output){
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			String key;
			String value;
			int c = 0;
			String line;
			ArrayList<String> list = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			ArrayList<String> write = generateRandom(list);
			for(String l : write){
				System.out.println(l);
			}
			PagelinksParser.writeFileList(output, write);
		}catch(Exception e){
			
		}
		
	}
	
	public static int getRandomNumberFrom(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt((max + 1) - min) + min;

        return randomNumber;

    }
	
	public static ArrayList<String> generateRandom(ArrayList<String> list){
		System.out.println(list.size());
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i< list.size(); i++){
			String line = list.get(i);
			int a = getRandomNumberFrom(0, 30);
			String ids = "";
			String all = "";
			for(int c = 0; c < a; c++){
				int b = getRandomNumberFrom(0, list.size()-1);
				while(b==i){
					b = getRandomNumberFrom(0, list.size()-1);
				}
				String lineb = list.get(b);
				ids += " "+ lineb ; 
			}
			all = line + ids;
			l.add(all);
			//System.out.println(all);
			
		}
		
		return(l);
	}

}
