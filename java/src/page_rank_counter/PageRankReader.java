import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;
/**
 * class for reading the final pagerank page.
 *
 */

public class PageRankReader {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String _path) {
		path = _path;
	}
	public String getPageRank(String odkaz)
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "\\output-pageRank.txt"),Charset.forName("UTF-8").newDecoder()));
			String sCurrentLine = null;
			String pieces[];
			while ((sCurrentLine = br.readLine()) != null) {
					pieces = sCurrentLine.split(";");
					if(pieces[1].equalsIgnoreCase("'" + odkaz + "'"))
					{
						br.close();
						return pieces[2];
					}
				
			}
			br.close();
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
}
