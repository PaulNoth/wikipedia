package LangLinksParsing;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Instance of this class read latest_page.sql dump where are stored all titles
 * in a particular language
 * 
 * @author Peter
 *
 */
public class HostPageParser {

	private Writer writer;
	private InputStreamReader filePage;
	private Map<Integer, String> map = new HashMap<Integer, String>(420000);

	public HostPageParser(String file_path) {
		try {
			scanFile(file_path);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.print("File " + e + " not found");
			e.printStackTrace();
		}
	}

	private void scanFile(String file_path)
			throws UnsupportedEncodingException, FileNotFoundException {
		filePage = new InputStreamReader(new FileInputStream(file_path),
				"UTF-8");
		Scanner scanner = new Scanner(filePage);
		scanner.useDelimiter("\\);");
		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("maping.txt"), "utf-8"));
		tokenizeFile(scanner);
	}

	private void tokenizeFile(Scanner scanner) {
		// int counter = 0;
		// long start = System.currentTimeMillis();
		while (scanner.hasNext()) {
			String regex_tokenize = "(\\d+),(\\d+),'(((?!',).)+)";
			Pattern pattern = Pattern.compile(regex_tokenize);
			Matcher matcher = pattern.matcher(scanner.next());
			while (matcher.find()) {
				try {
					// counter++;
					String title = matcher.group(3).replaceAll("_", " ");
					// System.out.println(matcher.group(1)+" "+matcher.group(3));
					map.put(Integer.valueOf(matcher.group(1)),title);
					try {
						writer.write(Integer.valueOf(matcher.group(1))+ " "
								+title+ "\n");
					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IllegalStateException e) {
					System.err.println("Skipping non-compliant record");
				}
			}
		}
		// long end = System.currentTimeMillis();
		// System.out.println(counter + "zaznamov, trvalo to "+(end-start));
	}

	protected Map<Integer, String> getPageMap() {
		return map;
	}
}
