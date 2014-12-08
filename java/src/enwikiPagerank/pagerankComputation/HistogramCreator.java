/**
 * 
 */
package enwikiPagerank.pagerankComputation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import enwikiPagerank.mapreduce.PagelinksParser;

/**
 * @author Jozef
 * class which holds methods to create histogram of defined intervals and store it into file
 */
public class HistogramCreator {
	
	public static long from0to1 = 0;
	public static long from1to10 = 0;
	public static long from10to100= 0;
	public static long from100to1000= 0;
	public static long from1000to10000= 0;
	public static long from10000to100000= 0;
	public static long from100000toMore= 0;

	public static void createHistogram(String input, String output){
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
			while ((line = br.readLine()) != null) { //read final file
				if(line.contains("and")) continue;
				
				String[] p = line.split(" ");
				double rank = Double.parseDouble(p[1]);
				counter(rank); //compute number of occurrences 
				
			}
			String names = "0to1" + "\t" + "1to10" + "\t" + "10to100" + "\t" + "100to1000" + "\t" + "1000to10000" + "\t" + "10000to100000" + "\t" + "moreThan100000";
			String values = from0to1 + "\t" + from1to10 + "\t" + from10to100 + "\t" + from100to1000 + "\t" + from1000to10000 + "\t" + from10000to100000 + "\t" + from100000toMore;
			
			ArrayList<String> outputList = new ArrayList<String>();
			outputList.add(names);
			outputList.add(values);
			PagelinksParser.writeFileList(output, outputList); //write histogram
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void counter(double rank){

		if(rank >=0 && rank < 1){
			from0to1++;
		}
		if(rank >=1 && rank < 10){
			from1to10++;
		}
		if(rank >=10 && rank < 100){
			from10to100++;
		}
		if(rank >=100 && rank < 1000){
			from100to1000++;
		}
		if(rank >=1000 && rank < 10000){
			from1000to10000++;
		}
		if(rank >=10000 && rank < 100000){
			from10000to100000++;
		}
		if(rank >=100000){
			from100000toMore++;
		}
		
	}
}
