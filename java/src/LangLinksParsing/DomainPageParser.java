package LangLinksParsing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
public class DomainPageParser {

	private InputStreamReader filePage;
	private Map<Integer, String> map = new HashMap<Integer, String>(420000);

	public DomainPageParser(String file_path) {
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
		tokenizeFile(scanner);
	}

	private void tokenizeFile(Scanner scanner) {
		// int counter = 0;
		// long start = System.currentTimeMillis();
		while (scanner.hasNext()) {
			String regex_tokenize = "(\\d+),(\\d+),'(((?!',).)+)";
			Pattern pattern = Pattern.compile(regex_tokenize);
			String scanner_next = scanner.next();
			Matcher matcher = pattern.matcher(scanner_next);
			while (matcher.find()) {

				try {
					// counter++;
					String sk_title = matcher.group(3).replaceAll("_", " ");
					// System.out.println(matcher.group(1)+" "+matcher.group(3));
					map.put(Integer.valueOf(matcher.group(1)), sk_title);
				} catch (IllegalStateException e) {
					System.err.println("Skipping " + scanner_next);
				}
			}
		}
		// long end = System.currentTimeMillis();
		// System.out.println(counter + "records, operation took "+(end-start));
	}

	protected Map<Integer, String> getPageMap() {
		return map;
	}
}
