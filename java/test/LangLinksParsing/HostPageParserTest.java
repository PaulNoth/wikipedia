package LangLinksParsing;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;

import org.junit.Test;

public class HostPageParserTest {
	
	private String correct_file_path = "dumps/skwiki-latest-page.sql";
	private String incorrect_file_path = "invalid_path";

	@Test
	public void scanFile()
			throws UnsupportedEncodingException, FileNotFoundException {
		InputStreamReader filePage = new InputStreamReader(new FileInputStream(correct_file_path),
				"UTF-8");
		Scanner scanner = new Scanner(filePage);
		scanner.useDelimiter("\\),");
		Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("cslinks.txt"), "utf-8"));

	}

}
