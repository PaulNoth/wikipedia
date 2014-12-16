package wikical.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author Martin Polak
 *
 */
public class FileSave {

	BufferedWriter bufferedWriter;
	boolean firstRun = true;

	/**
	 * Create file to selected pathToFile
	 */
	public FileSave(String pathToFile) {
		File fout = new File(pathToFile);
		FileOutputStream fos;

		firstRun = true;

		try {
			fos = new FileOutputStream(fout);
			try {
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Add line to created file
	 * @param addingLine
	 * @param addNewLine true - before line is added end of line
	 */
	public void addLineToFile(String addingLine, boolean addNewLine) {
		try {
			if (addNewLine && !firstRun) {
				bufferedWriter.newLine();
			}
			firstRun = false;
			bufferedWriter.write(addingLine);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopWritingToFile() {
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
