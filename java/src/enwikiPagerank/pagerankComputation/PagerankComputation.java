/**
 * 
 */
package enwikiPagerank.pagerankComputation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enwikiPagerank.mapreduce.PagelinksParser;

/**
 * @author Jozef 
 * this class is used for final pagerank computation
 */
public class PagerankComputation {
	public static HashMap<String, Double> pagerankMap = new HashMap<String, Double>(); //stores ID of page and current pagerank
	public static HashMap<String, Double> outgoingMap = new HashMap<String, Double>(); // stores ID of page nad count of outgoing links
	public static ArrayList<PagerankDataObject> pgoList = new ArrayList<PagerankDataObject>();

	public static Pattern pattern = Pattern.compile("^[^\\s]+");
	public static Matcher matcher;
	public static String idOrName;
	public static String restOfLine;

	public static String getNameOrId(String line, Pattern pattern) {
		matcher = pattern.matcher(line);

		if (matcher.find()) {
			idOrName = matcher.group(0);
		}

		return idOrName;
	}

	public static String getRestOfLine(String line) {
		String[] p = line.split("^[^\\s]+");
		int a = 0;
		a = a;
		restOfLine = p[1];
		p = null;
		return restOfLine;

	}

	public static void createPagerankHashMap(String input) {
		/*
		 * this method creates hash map which stores pair id of page - current
		 * pagerank in first iteration pagerank is set to 1.
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
			while ((line = br.readLine()) != null) {
				pagerankMap.put(line, 1.00);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		br = null;
		System.gc();
	}

	public static void createOutgoingHashMap(String input) {
		/*
		 * in this method we create hashMap for outgoing links per page - {id
		 * countOfOutgoingLinks} from input file
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
			while ((line = br.readLine()) != null) {
				String[] p = line.split(" ");
				outgoingMap.put(p[0], Double.parseDouble(p[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		br = null;
		System.gc();
	}

	public static void benchMarkHashMap(long toIndex) {
		/*
		 * method which was used only for benchmarking of hashmap and ram space
		 */

		long c = 0;
		for (Map.Entry<String, Double> entry : pagerankMap.entrySet()) {

			String key = entry.getKey();
			Double value = entry.getValue();
			int size = pagerankMap.size();
			pagerankMap.put(key, 2.0);
			// map.get("0#Benjamin_and_James_U.S._Navy_admirals_Sands");
			System.out.println("1 Key: " + key + " Value: " + value + " size: "
					+ size);
			value = pagerankMap.get(key);
			size = pagerankMap.size();
			System.out.println("2 Key: " + key + " Value: " + value + " size: "
					+ size);
			System.out.println(pagerankMap.get("5878274"));
			pagerankMap.put("5878274", 7.114);
			System.out.println(pagerankMap.get("5878274"));
			c++;
			if (c == toIndex) {
				break;
			}

		}
	}

	public static void computePagerank(String input, String output,
			String currpath, int count) {
		// printHashMap(outgoingMap, "Outgoing");
		// printHashMap(pagerankMap, "pagerank");

		/*
		 * in this method we read the file which stores data - id of page and
		 * ids of pages that are pointing to page with id. here was used
		 * buffered reader for more speed of reading, but we had to reset the
		 * buffer sometimes because of lack of the heap space
		 * 
		 * attributes: String input - which data file we are reading 
		 * String output - to with data file we want to store final pagerank 
		 * int count
		 * - number of algorithm repetition/epoch for obtaining better results
		 * 
		 */
		System.out.println("mapsCreated");
		int counter = 0;
		double currPagerank = 0;
		double prevPagerank = 0;
		while (counter < count) {
	        long startTime = System.currentTimeMillis();

			System.out.println("epoch " + counter);
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
				while ((line = br.readLine()) != null) {
					linePagerank(line, 0.85);

					if (c % 100000 == 0) {
						System.out.println(c);
						br.mark(0);
						br.reset();
						System.gc();
					}

					c++;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			br = null;
			System.gc();
			
			counter++;
			 
		}

		System.out.println("Pagerank computation is **** done ");
		String addStatistics = getStatistics();
		outgoingMap.clear();
		outgoingMap = null;
		System.gc();
		printPagerankToFile(currpath + "/finalPgrnk.txt", addStatistics);
	//	printHashMap(pagerankMap, "pagerankDone");
	}

	public static String getStatistics() {
		/*
		 * method to generate statistic as: 1. id of page with max outgoing
		 * links 2. average numbers of outgoing links 3. average pagerank
		 */
		double max = 0;
		double sum = 0;
		String id = null;
		for (Map.Entry<String, Double> entry : outgoingMap.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			if (value > max) {
				max = value;
				id = key;
			}
			sum += value;

		}
		sum = sum / outgoingMap.size();

		double pagerank = 0;
		for (Map.Entry<String, Double> entry : pagerankMap.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			pagerank += value;
		}
		pagerank = pagerank / pagerankMap.size();

		String all = "maxOutgoingLinks has " + id + " and value is: " + max
				+ " average pagerank: " + pagerank + " average outgoingLinks: "
				+ sum;

		return all;

	}
	
	public static double getAveragePagerank(){
		double pagerank = 0;
		for (Map.Entry<String, Double> entry : pagerankMap.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			pagerank += value;
		}
		pagerank = pagerank / pagerankMap.size();

		

		return pagerank;
		
	}

	public static void printPagerankToFile(String output, String statistics) {
		/*
		 * this method stores computed pagerank into file
		 */
		String maxKey = findMaxPagerank();
		double maxValue = pagerankMap.get(maxKey);
		String maxString = "max pagerank has " + maxKey + " and value is "
				+ maxValue;

		ArrayList<String> list = new ArrayList<String>();
		list.add(maxString);
		list.add(statistics);
		int c = 0;
		for (Map.Entry<String, Double> entry : pagerankMap.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			String line = key + " " + value.toString();
			list.add(line);
			key = null;
			value = null;
			line = null;
			c++;
			if (list.size() % 100000 == 0) {
				PagelinksParser.writeFileList(output, list);
				System.out.println("writed pagerang into file" + c);
				list.clear();
				System.gc();
			}
		}
		if (list.size() > 0) {
			PagelinksParser.writeFileList(output, list);

		}
		list.clear();
		list = null;
		System.gc();

	}

	public static void linePagerank(String line, double df) {
		/*
		 * method which compute pagerank for each page by finding current
		 * pagerank of pages in hashmap pagerankMap and finding num of outgoing
		 * links in hashmap outgoingMap 1 split the line by whitespace 2
		 * retrieve current pagerank and num of outgoing links 3 compute
		 * pagerank for page
		 */
		String[] p = line.split(" ");
		String id = p[0];
		double sum = 0;
		for (int i = 1; i < p.length; i++) {
			//if(outgoingMap.get(p[i]) == 0.0){
				//double p1 = pagerankMap.get(p[i]) / 2;
				//sum +=p1;
			//}else{
				
			double p1 = pagerankMap.get(p[i]) / outgoingMap.get(p[i]);
			sum += p1;
			//}

		}
		double prnk = (1 - df) + (df * sum);
		pagerankMap.put(id, prnk);

	}

	public static String findMaxPagerank() {
		/*
		 * method which finds the highes pagerank and return string as a key for
		 * retrieve pagerank and id of site
		 */
		double max = 0.0;
		String keyOfMax = null;
		for (Map.Entry<String, Double> entry : pagerankMap.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue() - 1;
			if (value > max) {
				max = value;
				keyOfMax = key;
			}

		}
		return keyOfMax;
	}

	public static void initializeOutgoingHashMap(String input){
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
			while ((line = br.readLine()) != null) {
				outgoingMap.put(line, 0.0);
				c++;
				if(c % 100000 == 0){
					System.out.println("map " +c);
					br.mark(0);
					br.reset();
					System.gc();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		
	}
	public static void computeAndWriteOutgoingLinks(String input, String output, String inputInitialize) {
		/*
		 * in this method we create a file which holds num of outgoing links it
		 * is better to create file, which is smaller(contains only pair id -
		 * num), becouse of lack of memory
		 */

		initializeOutgoingHashMap(inputInitialize);
		
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
			while ((line = br.readLine()) != null) {
				updateMap(line);

			}
			ArrayList<String> list = new ArrayList<String>();

			for (Map.Entry<String, Double> entry : outgoingMap.entrySet()) {
				String key = entry.getKey();
				Double value = entry.getValue();
				String full = key + " " + value;
				list.add(full);
				full = null;
				key = null;
				value = null;
				if (list.size() % 10000 == 0) {
					c++;
					System.out.println("write  " + c);
					PagelinksParser.writeFileList(output, list);
					list.clear();
				}

			}
			if (list.size() > 0) {
				PagelinksParser.writeFileList(output, list);
			}
			System.out.println(outgoingMap.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		br = null;
		System.gc();

	}

	public static void updateMap(String pp) {
		String[] p = pp.split("\\s");
		// System.out.println(pp);
		for (int i = 1; i < p.length; i++) {
			// System.out.println(p[i]);
			outgoingMap.put(p[i], outgoingMap.get(p[i]) + 1);
		}
		p = null;
	}

	public static void printHashMap(HashMap<String, Double> map, String type) {
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			System.out
					.println(type + " " + " key> " + key + " value> " + value);
		}

	}
}
