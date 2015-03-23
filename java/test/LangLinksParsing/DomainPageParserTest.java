package LangLinksParsing;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DomainPageParserTest {
	
	private Map<Integer, String> map = new HashMap<Integer, String>(420000);
	private String correct_file_path = "dumps/skwiki-latest-page.sql";
	private String incorrect_file_path = "invalid_path";
	

	@Test
	public void scanFile() throws UnsupportedEncodingException,
			FileNotFoundException {
		InputStreamReader filePage = new InputStreamReader(new FileInputStream(
				correct_file_path), "UTF-8");
		Scanner scanner = new Scanner(filePage);
		scanner.useDelimiter("\\);");		
	}

	@Test(expected = FileNotFoundException.class)
	public void scanIncorrectFile() throws UnsupportedEncodingException,
			FileNotFoundException {
		InputStreamReader filePage;

		filePage = new InputStreamReader(new FileInputStream(
				incorrect_file_path), "UTF-8");

		Scanner scanner = new Scanner(filePage);
		scanner.useDelimiter("\\);");
	}
}
