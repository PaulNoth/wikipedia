package enwikiPagerank.mapreduce;
/*
 * this class cointains operations for reduce pagelinks
 * pagelinks file contains of ID, namespace and nameOfPage where ID represents the ID of page which points
 * to page with nameOfPage and namespace.
 * so we need to create file, which holds namespace#nameOfPage and all ID which points to page with namespace#nameOfPage
 *
 */
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class PagelinksParser {

	public static void parseFile(String inputFilename, String output) {
		List<String> stringList = null;
		GZIPInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new GZIPInputStream((new FileInputStream(
					inputFilename)));
			sc = new Scanner(inputStream, "UTF-8"); // read line of pagelinks file
			int c = 0;
			String skipedKey = "";
			String skipedValue = "";
			boolean controll = false;
			String line = "";
			String[] p = null;
			String matcher;
			stringList = new ArrayList<String>();
			while (sc.hasNextLine()) {
				PagelinksMap o; //data object which hold namespace#nameOfPage and list of ID pointing to namespace#nameOfPage
				/*
				 * pagelinks file is ordered by nameOfPage
				 * example :
				 * id	nameOfPage	namespace
				 * 12   fire		0
				 * 33	fire		0
				 * 44	fire		0
				 * 88	fire		2
				 * 
				 * we want to obtain this
				 * namespace#nameOfPage 	ids 
				 * 0#fire					12 33 44
				 * 2#fire					88
				 * 
				 * so we need to read the line, read the next line, compare for equality of namespace#nameOfPage 
				 * and then join ids per one page
				 */
				if (!controll) {
					line = sc.nextLine();
					p = line.split(" ");
					if (p.length < 3)
						continue;
					matcher = p[1] + "#" + p[2];
					o = new PagelinksMap();
					o.key = matcher;
					o.value.add(p[0]);
				} else {

					matcher = p[1] + "#" + p[2];
					o = new PagelinksMap();
					o.key = matcher;
					o.value.add(p[0]);
					controll = false;
				}

				while (true) {
					String line2 = " ";
					try {
						//for page - find the next equal pages
						line = sc.nextLine();
					} catch (Exception e) {
						// System.out.println("LastLine");
						String li = createLine(o);
						System.out.println(li);
						stringList.add(li);
						// writeFile(output, li);

						if (stringList.size() > 900) { //write into file by batch - faster method than by one line
							writeFileList(output, stringList);
							stringList = new ArrayList<String>();
						}

						break;
					}
					String[] p2 = line.split(" ");
					if (p2.length < 3)
						continue;
					String comparator = p2[1] + "#" + p2[2];
					if (comparator.contentEquals(matcher)) { //comparator stores next line
						//so here we check if comparator equals matcher
						//if yes, we append another ID
						// if not, we know that we need to start obtaining IDs for another
						// nameOfPage
						o.value.add(p2[0]);
					} else {
						// comparator != matcher
						// so we know, we have to jump to next line an find equal namespace#nameOfPage
						String li = createLine(o);
						// System.out.println(li);
						stringList.add(li);
						// writeFile(output, li);
						if (stringList.size() > 900) {
							if (li.length() > 200000) {
								System.out.println(li);
							}
							writeFileList(output, stringList);
							stringList = new ArrayList<String>();
						}
						p = line.split(" ");
						controll = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String createLine(PagelinksMap po) {
		String key = po.key;

		StringBuilder sb = new StringBuilder();
		sb.append(key);
		for (int i = 0; i < po.value.size(); i++) {
			// key += " " + po.value.get(i);
			sb.append(" ");
			sb.append(po.value.get(i));
		}
		return sb.toString();
	}

	public static void writeFileList(String fileName, List<String> lines) {

		/*
		 * this method writes all lines of list into specified file
		 * 
		 */

		try {
			String filename = fileName;
			FileWriter fw = new FileWriter(filename, true); // the true will
															// append the new
			//String write = new String();					// data
			int i = 0;
			for (String li : lines) {
				fw.write(li + "\n");
			}
			//fw.write(write);// appends the string to the file

			i++;
			// counter++;

			fw.flush();
			fw.close();
			fw = null;
			lines = null;
			// System.out.println("Added lines:" + i + "  all lines>" + 2);
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

	public static void writeFile(String fileName, String lines) {

		try {
			String filename = fileName;
			FileWriter fw = new FileWriter(filename, true); // the true will
															// append the new
															// data
			int i = 0;
			fw.write(lines + "\n");// appends the string to the file

			i++;
			// counter++;

			fw.close();
			// fw.flush();
			// fw = null;
			// System.out.println("Added lines:" + i + "  all lines>" + 2);
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}
	}

}
