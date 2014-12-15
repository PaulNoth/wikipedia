package enwikiPagerank.mapreduce;

/**
 * Created by Jozef on 20.10.2014.

 */

/*
 * this class joins the pagelinks and pages by namespace#nameOfPage and creates the file
 * which holds most important data for pagerank computation
 * - idOfPage and set of ids of pages that pointing to idOfPage
 * example:
 * 20185 122 5548 6637 4421 
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import enwikiPagerank.parseToText.Main;

public class PagelinksAndPagesJoiner {

	public static HashMap<String, String> map = new HashMap<String, String>();
	public static ArrayList<DataObject> mapp = new ArrayList<DataObject>();

	// /media/jozef/Misc/School/VI/parsingAndPageranking/pagesControll.txt

	public static void readPagesFileToMap(String input, String output,
			String currpath) {
		/*
		 * this method create hashmap of namespace#nameOfPage and ID, where 
		 * namespace#nameOfPage is key and ID is value
		 */
		FileInputStream inputStream = null;
		Scanner sc = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
			DataObject dob;
			while (sc.hasNextLine() /* && counter < 1000000 */) {
				counter++;
				line = sc.nextLine();
				p = line.split(" ");
				title = p[0];
				id = p[1];
				map.put(title, id); // put into map necessary data
				if (counter % 100000 == 0) {

					System.out.println(counter + " zize " + map.size());
				}
				if (counter % 100000 == 0) {
					System.gc();
				}
				titleP = null;
				namespace = null;
				id = null;
				title = null;
				p = null;
				line = null;

			}

			
		} catch (Exception e) {
			e.printStackTrace();
			// note that Scanner suppresses exceptions

		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sc.close();
		}
		// printHashMap(map, 600000);
		System.gc();
		// start joining job - read file pagelinksReduced.txt and for each namespace#nameOfPage in it replace the namespace#nameOfPage 
		// by ID founded in hashmap by namespace#nameOfPage
		reducePagelinksByHashMap(currpath + "/pagelinksReduced.txt",
				currpath + "/pagelinksReducedSubdomain.txt");
		// reduce the pagelinks - delete entries that are not in current
		// hashtable
		map.clear();
		System.gc();

	}

	public static void serializeHashMap(HashMap map, String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, String> deserializeHashMap(String path) {

		try {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			HashMap<String, String> hmap = (HashMap) ois.readObject();
			ois.close();
			fis.close();
			return hmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void printHashMap(HashMap<String, String> map, long toIndex) {

		long c = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			map.get("0#Benjamin_and_James_U.S._Navy_admirals_Sands");
			System.out.println("Key: " + key + " Value: " + value);
			c++;
			if (c == toIndex) {
				break;
			}

		}
	}

	public static void showMemoryUsage() {
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
		System.out.println("Used memory is bytes: " + memory);
		System.out.println("Used memory is megabytes: "
				+ bytesToMegabytes(memory));

	}

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}

	private static final long MEGABYTE = 1024L * 1024L;

	public static void reducePagelinksByHashMap(String input, String output) {
		/*
		 * this is the main method of joining/reducing job
		 *  by the hashmap we replace the namespace#nameOfPage in pagelinks and write it into new file
		 */
		System.out.println("Starting reduce job");
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(input));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			String line = null;
			String[] p = null;
			String titleAlfa = null;
			String titleNumeric = null;
			String ids = null;
			String all = null;
			int c = 0;
			System.out.println("opened input stream");
			showMemoryUsage();
			Pattern pattern = Pattern.compile("^[^\\s]+");
			long counter = 0;
			Matcher matcher;
			long start;
			long readLine = 0;
			long matcherTime = 0;
			long mapContainsTime = 0;
			long mapGetTime = 0;
			long splitTime = 0;
			
			long end;
			while ((line = br.readLine()) != null) {
				
				start = Main.startTime(); //time computing - only for benchmarking
				//line = sc.nextLine();
				end = Main.endTime(start);
				readLine+=end;
				//System.out.println("sc.next> " + end);
				
				
				start = Main.startTime();
				matcher = pattern.matcher(line); // match the pattern

				if (matcher.find()) {
					titleAlfa = matcher.group(0); // get namespace#nameOfPage
				}
				end = Main.endTime(start);
				matcherTime+= end;
				//System.out.println("matcher> " + end);
				counter++;
				// System.out.println(titleAlfa);
				
				start = Main.startTime();
				if (!map.containsKey(titleAlfa)) { // if does not contain the key continue
					/*line = null;
					p = null;
					titleNumeric = null;
					titleAlfa = null;*/
					
				//	System.out.println("map.contains> " + end);
					continue;
				}
				end = Main.endTime(start);
				mapContainsTime+= end;
				start = Main.startTime();
				titleNumeric = map.get(titleAlfa); //get ID from map by namespace#nameOfPage represented by titleAlfa
				end = Main.endTime(start);
				mapGetTime += end;
				//System.out.println("map.get> " + end);
				
				start = Main.startTime();
				p = line.split("^[^\\s]+");
				ids = p[1]; 
				all = titleNumeric + ids; // join ID of page and IDs that pointing to it
				end = Main.endTime(start);
				splitTime += end;
				
				list.add(all);
			
				line = null;
				p = null;
				titleNumeric = null;
				titleAlfa = null;
				ids = null;
				all = null;
				if (list.size() > 10000) { //write into file
					// showMemoryUsage();
					/*System.out.println("readline " + readLine);
					System.out.println("matcherTime " + matcherTime);
					System.out.println("mapContainsTime " + mapContainsTime);
					System.out.println("mapGetTime " +mapGetTime);
					System.out.println("splitTime " +splitTime);*/
					//this comments above - uncoment if you wanna know
					// hom much time operations take
					readLine = 0;
					matcherTime = 0;
					mapContainsTime = 0;
					mapGetTime = 0;
					splitTime = 0;
					c++;
					System.out.println("save" + " " + c + " line" + counter);
					start = Main.startTime();
					PagelinksParser.writeFileList(output, list);
					end = Main.endTime(start);
					System.out.println("Write> " + end);
					
					start = Main.startTime();
					list.clear();
					end = Main.endTime(start);
					System.out.println("Clear " + end);
					//list = null;
					//list = new ArrayList<String>();
					// System.gc();
					// list.trimToSize();
				}
				if (counter % 500000 == 0) {
					//System.gc();
				}

			}
			if(list.size() > 0){
				PagelinksParser.writeFileList(output, list);
			}
				

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// sc.close();

		}

	}

	public static void reducePagelinksByArrayList(String input, String output,
			ArrayList<DataObject> map) {

		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(input);
			sc = new Scanner(inputStream, "UTF-8");
			long counter = 0;
			String line = null;
			String id = null;
			String[] p = null;
			String title = null;
			String ids = null;
			String element = null;
			String allIds = null;
			long founded = 0;
			List<String> instance = new ArrayList<String>();
			DataObject dob;
			while (sc.hasNextLine() /* && counter < 1000000 */) {
				counter++;
				if (counter % 100000 == 0) {
					System.out.println("at line " + counter);
				}
				line = sc.nextLine();
				p = line.split(" ");
				title = p[0];
				dob = getDataObject(title, map);
				if (dob == null) {
					line = null;
					ids = null;
					element = null;
					p = null;
					continue;
				} else {
					founded++;
					if (founded % 1000 == 0) {
						System.out.println("founded: ");
					}
					String k = dob.getTitle();
					// ids = createStringFromIDs(p, allIds);
					element = k + "" + ids; // replace title to id
					instance.add(element);
					line = null;
					ids = null;
					allIds = null;
					element = null;
					k = null;
					dob = null;
					title = null;
					p = null;
				}
				if (instance.size() > 100000) {
					System.out.println("Writing");
					PagelinksParser.writeFileList(output, instance);
					// instance.clear();
					instance = null;
					instance = new ArrayList<String>();

					System.out.println("founded: " + founded + " line: "
							+ counter + " mapsize " + map.size());
				}
				if (founded % 30000 == 0) {
					System.out.println("Deleting system");
					System.gc();
				}

			}
			if (instance.size() > 0) {
				PagelinksParser.writeFileList(output, instance);
				instance.clear();
				System.gc();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String createStringFromIDs(String[] p) {
		String all = "";
		for (int i = 1; i < p.length; i++) {
			all += " " + p[i];
		}

		return all;
	}

	public static void consume() {
			//benchmark of RAM comsuption 
		List<DataObject> a = new ArrayList<DataObject>();
		String title = null;
		String ids = null;
		DataObject dob = null;
		new DataObject();

		for (int i = 0; i < 600000; i++) {
			dob = new DataObject();
			title = "4#Alfa_omega";
			ids = "8445" + "44444" + "8445" + "44444" + "8445" + "44444"
					+ "8445" + "44444" + "8445" + "44444" + "8445" + "44444"
					+ "8445" + "44444" + "8445" + "44444";

			dob.setTitle(title);
			dob.setId(ids);
			// System.out.println(i);
			a.add(dob);
			dob = null;
			title = null;
			ids = null;
		}

		for (int i = 0; i < a.size(); i++) {
			if (i == 580000) {
				System.out.println(a.get((int) i).getId() + " "
						+ a.get((int) i).getTitle());
			}

		}

	}

	public static DataObject getDataObject(String title,
			ArrayList<DataObject> list) {
		for (DataObject e : list) {
			if (e.getTitle().contentEquals(title)) {
				list.remove(e);
				list.trimToSize();
				return e;

			} else {
				return null;
			}
		}

		return null;
	}

	
}
