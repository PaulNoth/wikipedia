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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to read langlinks in slovak language and so on
 * @author Peter
 *
 */
public class Linker {

	private InputStreamReader fileLangLinks;
	private Writer writer;
	private Scanner scanner_langlinks;
	
	private static String LANGUAGE_1;
	private static String LANGUAGE_2;
	
	private DomainPageParser dpp;
	private HostPageParser hpp;
	private HostLangLinksParser hllp;

	public Linker(String file_path, DomainPageParser dPP, String lang1, String lang2) {
		
		LANGUAGE_1 = lang1;
		LANGUAGE_2 = lang2;
		dpp = dPP;
		try {
			scanFile(file_path, false);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Linker(String file_path, DomainPageParser dPP,
			 HostLangLinksParser hLLP, HostPageParser hPP, String lang1, String lang2) {

		
		LANGUAGE_1 = lang1;
		LANGUAGE_2 = lang2;
		dpp = dPP;
		hpp = hPP;
		hllp = hLLP;
	
		try {
			scanFile(file_path, true);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void scanFile(String file_path, boolean backtrack)
			throws IOException {
		fileLangLinks = new InputStreamReader(new FileInputStream(file_path),
				"UTF-8");

		scanner_langlinks = new Scanner(fileLangLinks);
		scanner_langlinks.useDelimiter("\\),"); // tokenize

		writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("solution.txt"), "utf-8"));

		if (backtrack) {
			link_backtrack();
		} else {
			link();
		}
	}

	private void link() {
		// long start = System.currentTimeMillis();
		while (scanner_langlinks.hasNext()) {
			String one_insert = scanner_langlinks.next() + ")";
			String regex_tokenize = "(\\d+),'(" + LANGUAGE_2 + ")','(.+)'";
			Pattern pattern = Pattern.compile(regex_tokenize);
			Matcher matcher = pattern.matcher(one_insert);
			while (matcher.find()) {

				String s = new String(dpp.getPageMap().get(
						Integer.valueOf(matcher.group(1)))
						+ " ("
						+ LANGUAGE_1
						+ ")\t"
						+ matcher.group(3) + " ("+LANGUAGE_2+")\n");
				try {
					writer.write(s);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		// long end = System.currentTimeMillis();
		System.out.println("FINISHED! Find result in solution.txt");

	}

	private void link_backtrack() throws IOException {
		// long start = System.currentTimeMillis();
		while (scanner_langlinks.hasNext()) {
			String one_insert = scanner_langlinks.next() + ")";
			String regex_tokenize = "(\\d+),'(" + LANGUAGE_2 + ")','(.+)'";
			Pattern pattern = Pattern.compile(regex_tokenize);
			Matcher matcher = pattern.matcher(one_insert);
			while (matcher.find()) {

		
				Integer backtrack_title_id = hllp.getPageMap().get(dpp.getPageMap().get(Integer.valueOf(matcher.group(1))));
				String s;
				if(backtrack_title_id != null){
				s = new String(dpp.getPageMap().get(
						Integer.valueOf(matcher.group(1)))
						+ " ("
						+ LANGUAGE_1
						+ ")\t"
						+ matcher.group(3) + " ("+LANGUAGE_2+") | [BACKTRACKED:] " +hpp.getPageMap().get(backtrack_title_id)+" ("+LANGUAGE_2+")\t"+ dpp.getPageMap().get(Integer.valueOf(matcher.group(1)))+""
								+ " ("+LANGUAGE_1+")\n");
				}
				else{
					s = new String(dpp.getPageMap().get(
							Integer.valueOf(matcher.group(1)))
							+ " ("
							+ LANGUAGE_1
							+ ")\t"
							+ matcher.group(3) + " ("+LANGUAGE_2+") | [BACKTRACKED:] null \n");
				}
						writer.write(s);
				
			}
		}
		// long end = System.currentTimeMillis();
		System.out.println("FINISHED! Find result in solution.txt");

	}
}
