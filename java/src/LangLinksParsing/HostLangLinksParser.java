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
 * Instance of this class read langlinks.sql dump where are stored all language
 * links
 * 
 * @author Peter
 *
 */
public class HostLangLinksParser {

	private Writer writer;
	private InputStreamReader filePage;
	private static String LANGUAGE;
	private Map<String, Integer> map = new HashMap<String, Integer>(420000); 
	
	public HostLangLinksParser(String file_path, String lang1) {
		LANGUAGE = lang1;
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
		scanner.useDelimiter("\\),");
		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("cslinks.txt"), "utf-8"));
		tokenizeFile(scanner);
	}

	private void tokenizeFile(Scanner scanner) {
		// int counter = 0;
		// long start = System.currentTimeMillis();
		while (scanner.hasNext()) {
			String regex_tokenize = "(\\d+),'("+LANGUAGE+")','(.+)'";
			Pattern pattern = Pattern.compile(regex_tokenize);
			Matcher matcher = pattern.matcher(scanner.next());
			if (matcher.find()) {

				// counter++;
				try {
					map.put(matcher.group(3), Integer.valueOf(matcher.group(1)));
					writer.write(matcher.group(3) + " " + matcher.group(1) + "\n");
				} catch (NumberFormatException | IOException e) {
					
					e.printStackTrace();
				}
			}

		}
		// long end = System.currentTimeMillis();
		// System.out.println(counter + "records, operation took "+(end-start));
	}

	protected Map<String, Integer> getPageMap() {
		return map;
	}
}
