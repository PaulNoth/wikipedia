package testAndTools;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
/**
 * The Class BasicFileIO.
 */
public class BasicFileIO {
	/**
	 * Basic file read.
	 *
	 * @param inFileName the in file name
	 * @return the string
	 */
	public String basicFileRead(String inFileName){
		StringBuilder s = new StringBuilder();
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(inFileName);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
			
			String strLine;
			while ((strLine = br.readLine()) != null){
				s.append(strLine.trim() + "\n");
				//s = s + strLine + "\n";
			}
			br.close();
			in.close();
			fstream.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s.toString();
	}
}
