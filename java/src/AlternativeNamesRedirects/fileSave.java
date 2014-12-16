package xonder_VINF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class fileSave {
	File output;
	FileOutputStream oFile;
	BufferedWriter buf;
	boolean firstLine;
	/**********creating a csv file to save */
	public fileSave(){
		firstLine = true;
		output = new File("Preprocessing.csv");
		if(output.exists()){
			output.delete();
		}
		try{
			output.createNewFile();
			oFile = new FileOutputStream(output);
			buf = new BufferedWriter(new OutputStreamWriter(oFile, "UTF-8"),100000);
		} catch(Exception ex){
			System.out.println("nana "+ex.getMessage());
		}
			
	}
	
	/**************adding a new line to the csv file ****************/
	public int addLine(String line){
		try {
			if(!firstLine)
				buf.newLine();
			else firstLine = false;
			buf.write(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Adding line error "+e.getMessage());
			return -1;
		}
		return 0;
	}
	
	/************closing csv file bufferedwriter when done*************/
	public void close(){
		try {
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
