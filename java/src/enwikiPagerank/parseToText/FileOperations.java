package enwikiPagerank.parseToText;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Jozef on 29.10.2014.
 */
public class FileOperations {

    public long linesInFile(String inputFile){
        long count = 0;
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(inputFile);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                count++;
                if(count % 1000000 == 0){
                    System.out.println(count);
                }
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return count;
    }

    public long countLines(String input){

            LineNumberReader reader = null;
            try {
                reader = new LineNumberReader(new FileReader(input));
                while ((reader.readLine()) != null){
                    if(reader.getLineNumber() % 1000000 == 0){
                        System.out.println("number"+reader.getLineNumber());
                    }
                }
                return reader.getLineNumber();
            } catch (Exception ex) {
                return -1;
            } finally {
                if(reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }

}
