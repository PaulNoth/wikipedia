import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DisambiguationSet {
	public ArrayList<DisambiguationUnit> set;

	
	public void fillSet(String fileName) throws IOException
	{
		BufferedReader scanner = new BufferedReader(new FileReader(new File(fileName)));
		String line;
		String delims = "[\t]";
		String[] tokens;
		
		String lastDisambTitle = "";
		
		this.set = new ArrayList<DisambiguationUnit>();
		
		while ((line = scanner.readLine()) != null) {
			DisambiguationUnit tmp = new DisambiguationUnit();
			tokens = line.split(delims);
			
			tmp.title = tokens[0];
			tmp.titleDisamb = lastDisambTitle;
			
			if(tokens.length > 1) {
				tmp.isDisamb = true;
				lastDisambTitle = tokens[0];
			}
			else {
				tmp.isDisamb = false;
			}
			
			set.add(tmp);
			
		}
	}
	
}

