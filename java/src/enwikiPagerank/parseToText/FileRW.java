package enwikiPagerank.parseToText;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * Created by Jozef on 20.10.2014.
 * this class works almost exactly as class PagelinksFileRW.
 * the only difference is, that this class parse pages - so we need to contain different data
 */
public class FileRW {

    public static long counter = 0;

   static String corrupted = "";
    public final void readGzWriteTxt(String inputFilename, String outputFilename) {
    	/*
    	 * method which reads the dump file with pages and each instance parse to single line, 
    	 * and choose only attributes we want further
    	 */
        GZIPInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new GZIPInputStream((new FileInputStream(inputFilename)));
            sc = new Scanner(inputStream,"UTF-8"); // read file by line
            int c = 0; //"UnicodeLittle"
            
            boolean controll = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                List<String> list = new ArrayList<String>();
                while (true && controll) {
                    String currLine = sc.nextLine();
                    if (!currLine.contains("INSERT INTO `page` VALUES") && controll) { // skip file's lines until the data appears
                    	System.out.println("mam");
                    	sc.nextLine();
                        continue;
                    } else {
                        controll = false;
                        line = currLine;
                        break;
                    }
                }
                //System.out.println("Parsing line>" + c + "veta" /*+ line*/);
                list.add(line); // here I have list of lines in string, now i need to parse it
                List<String> parsedInstances = new ArrayList<String>();
                // writeFile("a", list);
                List<String> instancesInSingleLine = new ArrayList<String>();
                instancesInSingleLine = instanceIntoNewLine(list);
                List<String> cr = new ArrayList<String>();
                
                cr = createPagesModelStringList(instancesInSingleLine);
                
                //make all instance into single line
                //parse it and store to parsedInstances
                //write
                
                writeFile(outputFilename, cr);
                
                list.clear();
                list = null;
                list = new ArrayList<String>();
                instancesInSingleLine.clear();
                instancesInSingleLine = null;

                cr.clear();
                cr = null;
                c++;

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

    }
    
    public final void readTxtWriteTxt(String inputFilename, String outputFilename) {
    	/*
    	 * method which reads the dump file with pages and each instance parse to single line, 
    	 * and choose only attributes we want further
    	 */
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(inputFilename);
            sc = new Scanner(inputStream,"UnicodeLittle"); // read file by line
            int c = 0; //"UnicodeLittle"
            
            boolean controll = true;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                List<String> list = new ArrayList<String>();
                while (true && controll) {
                    String currLine = sc.nextLine();
                    if (!currLine.contains("INSERT INTO `page` VALUES") && controll) { // skip file's lines until the data appears
                    	System.out.println("mam");
                    	sc.nextLine();
                        continue;
                    } else {
                        controll = false;
                        line = currLine;
                        break;
                    }
                }
                //System.out.println("Parsing line>" + c + "veta" /*+ line*/);
                list.add(line); // here I have list of lines in string, now i need to parse it
                List<String> parsedInstances = new ArrayList<String>();
                // writeFile("a", list);
                List<String> instancesInSingleLine = new ArrayList<String>();
                instancesInSingleLine = instanceIntoNewLine(list);
                List<String> cr = new ArrayList<String>();
                
                cr = createPagesModelStringList(instancesInSingleLine);
                
                //make all instance into single line
                //parse it and store to parsedInstances
                //write
                
                writeFile(outputFilename, cr);
                
                list.clear();
                list = null;
                list = new ArrayList<String>();
                instancesInSingleLine.clear();
                instancesInSingleLine = null;

                cr.clear();
                cr = null;
                c++;

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

    }

    public static void existCheck(String fileName) {
        String dirName = "\\.";

        File dir = new File(dirName);
        File outputFile = new File(dir, fileName);
        if (outputFile.exists()) {
            System.out.println("Existuje");
            outputFile.delete();


        } else {
            System.out.println("Neexistuje,vytvaram");
            try {
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void writeFile(String fileName, List<String> lines) {

        try {
            String filename = fileName;
            FileWriter fw = new FileWriter(filename, true); //the true will append the new data
            int i = 0;
            for (String l : lines) {
                fw.write(l + "\n");//appends the string to the file
                i++;
                counter++;
            }
              fw.close();
            
              if(counter % 10000 == 0){
            	  System.out.println("Added lines:" + i + "  all lines>" + counter);
              }
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public static void printStringList(List<String> l ){
        for(String a : l){
            System.out.println(a);
        }
    }

    public static List<String> instanceIntoNewLine(List<String> list) {

        List<String> entities = new ArrayList<String>();

        for (String el : list) {
        	String[] pom = el.split("INSERT INTO `page` VALUES");
        	//System.out.println(el);
        	
        	Pattern pattern = Pattern.compile("\\(.*?[^\\\\]\\'\\(*.*?\\)*[^\\\\]\\'.*?\\)");
            Matcher matcher = pattern.matcher(el);
            String title = null; 
            while (matcher.find())
            {
               title = matcher.group(0);
               entities.add(title);
               //System.out.println("en " + title);
               corrupted = title;
            }
            
        	
        	
          /*  String[] arr = pom[1].split("\\((.*?)\\),");
            for (int i = 0; i < arr.length; i++) {
            	System.out.println("entyty" +arr[i]);
                //arr[i] = arr[i].replace("(", "");
                //arr[i] = arr[i].replace(")", "");
                entities.add(arr[i]);

            }*/
            // System.out.println(arr[0]);
        }
        // entities.remove(0);
        return entities;
    }

    public List<String> createPagesModelStringList(List<String> list) { // create data model for pages - each object is one entity build by necessary attributes

        List<String> listP = new ArrayList<String>();
        //1,2,3,6
        //printStringList(list);

        for (int i = 0; i < list.size(); i++) {

        	String el = new String();
        	el = list.get(i);
//          //  System.out.println(el);
            String title = null;
            Pattern patternWords = Pattern.compile("('.*?')");
            Matcher matcherWords = patternWords.matcher(el);
            PagesModel p = new PagesModel();
            if (matcherWords.find())
            {
                title = matcherWords.group(1);
                title = title.replace("'", "");
            }
            Pattern patternNums = Pattern.compile("([0-9]\\d*),");
            Matcher matcherNums = patternNums.matcher(el);
            String pid = null;
            String namespace = null;
            String redir = null;
            int c = 0;
            while(matcherNums.find()){
            	if(c == 0){
            		pid = matcherNums.group(1);
            	}
            	if(c == 1){
            		namespace = matcherNums.group(1);
            	}
            	if(c == 3){
            		redir = matcherNums.group(1);
            	}
            	if (c == 3)break;
            	c++;
            
            }
            
            
            //System.out.println(el + " --->" + title);
            //String[] split = el.split("([0-9]*),(\\w),(\\'(.*)\\'),(\\'\\'),([0-9]*),([0-9]*),([0-9]*),([0.0-9]*),(\\'(\\w+)\\'),(.*),([0-9]*),([0-9]*)");

            //System.out.println(pid + " " + namespace + " " + title + " " + redir);
            
            try{
                p.setPageId(Integer.parseInt(pid));
                p.setPageNameSpace(Long.parseLong(namespace));
                p.setPageTitle(title);
                p.setPageIsRedirect(Integer.parseInt(redir));
                String st = null;

               // p.setPageIsRedirect(Integer.parseInt(split[5]));
                st = p.getPageId() + " " + p.getPageNameSpace() + " " + p.getPageTitle() + " " + p.getPageIsRedirect();
                listP.add(st);

            } catch (Exception e) {
                e.printStackTrace();
               // p.setPageIsRedirect(0);
              //  System.out.println("IDE>" + p.getPageId() + "\n" + el);
               // System.out.println("el:" + el);
                System.out.println("title:" + title + " corrupted" + "\n" + corrupted);
                //System.exit(1);

              //  System.out.println(split.length);
               // System.exit(1);



            } finally {

            }
          //  System.out.println("Current etity>" + i);

        }

        return listP;
    }
}
