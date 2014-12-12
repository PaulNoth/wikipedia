package enwikiPagerank.parseToText;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by Jozef on 20.10.2014.
 * this class parse file with pagelinks into new file, which holds only the necessary 
 * attributes in single line per instance
 */
public class PagelinksFileRW {

	public static long counter = 0;

	static String corrupted = "";

	public final void readGzWriteTxt(String inputFilename, String outputFilename) {
		GZIPInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new GZIPInputStream((new FileInputStream(
					inputFilename)));
			sc = new Scanner(inputStream, "UTF-8");
			int c = 0; // "UnicodeLittle"

			boolean controll = true;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				List<String> list = new ArrayList<String>();
				while (true && controll) {
					String currLine = sc.nextLine();
					if (!currLine.contains("INSERT INTO `pagelinks` VALUES")
							&& controll) {
						System.out.println("mam");
						// sc.nextLine();
						continue;
					} else {
						controll = false;
						line = currLine;
						break;
					}
				}
				// System.out.println("Parsing line>" + c + "veta" + line);
				list.add(line); // here I have list of lines in string, now i
								// need to parse it
				List<String> parsedInstances = new ArrayList<String>();
				// writeFile("a", list);
				List<String> instancesInSingleLine = new ArrayList<String>();
				instancesInSingleLine = instanceIntoNewLine(list); 
				List<String> cr = new ArrayList<String>();

				cr = createPagesModelStringList(instancesInSingleLine);

				// make all instance into single line
				// parse it and store to parsedInstances
				// write

				writeFile(outputFilename, cr);

				list.clear();
				list = null;
				list = new ArrayList<String>();
				instancesInSingleLine.clear();
				instancesInSingleLine = null;

				cr.clear();
				cr = null;
				c++;

			}

			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (sc != null) {
				sc.close();
			}
		}

	}
	
	public final void readTxtWriteTxt(String inputFilename, String outputFilename) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(inputFilename);
			sc = new Scanner(inputStream, "UnicodeLittle");
			int c = 0; // "UnicodeLittle"

			boolean controll = true;
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				List<String> list = new ArrayList<String>();
				while (true && controll) {
					String currLine = sc.nextLine();
					if (!currLine.contains("INSERT INTO `pagelinks` VALUES")
							&& controll) {
						System.out.println("mam");
						// sc.nextLine();
						continue;
					} else {
						controll = false;
						line = currLine;
						break;
					}
				}
				// System.out.println("Parsing line>" + c + "veta" + line);
				list.add(line); // here I have list of lines in string, now i
								// need to parse it
				List<String> parsedInstances = new ArrayList<String>();
				// writeFile("a", list);
				List<String> instancesInSingleLine = new ArrayList<String>();
				instancesInSingleLine = instanceIntoNewLine(list); 
				List<String> cr = new ArrayList<String>();

				cr = createPagesModelStringList(instancesInSingleLine);

				// make all instance into single line
				// parse it and store to parsedInstances
				// write

				writeFile(outputFilename, cr);

				list.clear();
				list = null;
				list = new ArrayList<String>();
				instancesInSingleLine.clear();
				instancesInSingleLine = null;

				cr.clear();
				cr = null;
				c++;

			}

			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (sc != null) {
				sc.close();
			}
		}

	}

	public static void existCheck(String fileName) {
		String dirName = "\\.";

		File dir = new File(dirName);
		File outputFile = new File(dir, fileName);
		if (outputFile.exists()) {
			System.out.println("Existuje");
			outputFile.delete();

		} else {
			System.out.println("Neexistuje,vytvaram");
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void writeFile(String fileName, List<String> lines) {

		try {
			String filename = fileName;
			FileWriter fw = new FileWriter(filename, true); // the true will
															// append the new
															// data
			int i = 0;
			for (String l : lines) {
				fw.write(l + "\n");// appends the string to the file
				i++;
				counter++;
			}
			fw.close();
			
			if (counter % 10000 == 0) {
				System.out.println("Added lines:" + i + "  all lines>"
						+ counter);
			}
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static void printStringList(List<String> l) {
		for (String a : l) {
			System.out.println(a);
		}
	}

	public static List<String> instanceIntoNewLine(List<String> list) {
		/*
		 * this method parse each instance which start and end with () and then
		 * return the list of lines, which each line represents one instance
		 */

		List<String> entities = new ArrayList<String>();

		for (String el : list) {
			String[] pom = el.split("INSERT INTO `pagelinks` VALUES");
			// System.out.println(el);

			Pattern pattern = Pattern
					.compile("\\(.*?[^\\\\]\\'\\(*.*?\\)*[^\\\\]\\'.*?\\)");
			Matcher matcher = pattern.matcher(el);
			String title = null;
			while (matcher.find()) {
				title = matcher.group(0);
				entities.add(title);
				// System.out.println("en " + title);
				corrupted = title;
			}

			// System.out.println(arr[0]);
		}
		// entities.remove(0);
		return entities;
	}

	public List<String> createPagesModelStringList(List<String> list) { // 
		/*
		 * create data model for pages - each object is one entity builded
		 * by necessary attributes
		 */

		List<String> listP = new ArrayList<String>();
		// 1,2,3,6
		// printStringList(list);

		for (int i = 0; i < list.size(); i++) {

			String el = new String();
			el = list.get(i);
			// // System.out.println(el);
			String title = null;
			Pattern patternWords = Pattern.compile("('.*?')");
			Matcher matcherWords = patternWords.matcher(el);
			PageLinksModel p = new PageLinksModel();
			if (matcherWords.find()) {
				title = matcherWords.group(1);
				title = title.replace("'", "");
				// System.out.println(title + " title");
			}
			Pattern patternNums = Pattern.compile("([0-9]\\d*),");
			Matcher matcherNums = patternNums.matcher(el);
			String plFrom = null;
			String namespace = null;
			String redir = null;
			int c = 0;
			while (matcherNums.find()) {
				if (c == 0) {
					plFrom = matcherNums.group(1);
				} else {
					namespace = matcherNums.group(1);
					// System.out.println(namespace);
				}
				c++;

			}

			// System.out.println(el + " --->" + title);
			// String[] split =
			// el.split("([0-9]*),(\\w),(\\'(.*)\\'),(\\'\\'),([0-9]*),([0-9]*),([0-9]*),([0.0-9]*),(\\'(\\w+)\\'),(.*),([0-9]*),([0-9]*)");

			// System.out.println(pid + " " + namespace + " " + title + " " +
			// redir);

			try {
				p.setPlFrom(Integer.parseInt(plFrom));
				p.setPlNamespace(Long.parseLong(namespace));
				p.setPlTitle(title);

				String st = null;

				st = p.getPlFrom() + " " + p.getPlNamespace() + " "
						+ p.getPlTitle();
				listP.add(st);
				// System.out.println(st);

			} catch (Exception e) {
				e.printStackTrace();

				// System.out.println("title:" + title + " corrupted" + "\n" +
				// corrupted);
				// System.exit(1);

				// System.out.println(split.length);
				continue;

			} finally {

			}
			// System.out.println("Current etity>" + i);

		}

		return listP;
	}

}
