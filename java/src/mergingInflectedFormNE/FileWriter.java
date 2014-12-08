package mergingInflectedFormNE;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

// TODO: Auto-generated Javadoc
/**
 * The Class FileWriter.
 */
public class FileWriter {

/**
 * Write file.
 *
 * @param filePath the file path
 * @param outputText the output text
 */
public void writeFile(String filePath, String outputText){
	BufferedWriter out;
	try {
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8"));
		out.write(outputText);
		out.close();
	} catch (IOException e) {
		System.out.println("Error: " + "could not write to file" + "  " + filePath);
		e.printStackTrace();
	}	
}

/**
 * Named entity list to string.
 *
 * @param llne the llne
 * @return the string
 */
public String namedEntityListToString(LinkedList<NamedEntity> llne){
	int i = 1;
	String str = "";
	for(NamedEntity ne : llne){
		//str=str+ "============================================================\n" + ne.getNE();
		str=str+ i + "  ============================================================";
		for(String infe : ne.getInflectedForms()){
			str=str+ "\n" + infe;
		}
		str=str + "\n";
		i++;
	}
	str=str.substring(0, str.length()-1);
	//System.out.println(str);
	return str;
}

}
