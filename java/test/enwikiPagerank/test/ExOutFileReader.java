package enwikiPagerank.test;

/*
 * class which reads two kind of file - parsed file and file, which is not in UTF-8
 * this class is used in unit tests
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ExOutFileReader {
	
	public List<String> readFileToStringList(String inputFile){
        InputStream fis = null;
        BufferedReader br = null;
        String line = "";
        List<String> inputList = new ArrayList<String>();
        boolean pass = false;
        try {
            fis = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UnicodeLittle")));
        try {
            while ((line = br.readLine()) != null) {
                inputList.add(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

// Done with the file
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        br = null;
        fis = null;

       // inputList.remove(0);
        return inputList;
    }
	
	public List<String> readParsedFileToStringList(String inputFile){
        InputStream fis = null;
        BufferedReader br = null;
        String line = "";
        List<String> inputList = new ArrayList<String>();
        boolean pass = false;
        try {
            fis = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        try {
            while ((line = br.readLine()) != null) {
                inputList.add(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

// Done with the file
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        br = null;
        fis = null;

       // inputList.remove(0);
        return inputList;
    }

}
