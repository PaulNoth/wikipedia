package dewiki_categories.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserSQL {
	
	//singleton instance
	private static ParserSQL instance = null;

  	//
  	// SQL categories init region below
  	//
	
	//HashMap containing result from SQL categories dump
    private HashMap<String, Integer> sqlHashmap = new HashMap<String, Integer>();    
    public HashMap<String, Integer> getSqlHashmap() {
		return sqlHashmap;
	}

	private FileInputStream reader1 = null;
    private Scanner scanner1 = null;

    //default relative path to SQL categories file
  	//+ setter
  	private String pathToCategoriesSQL = ".\\data\\input-dewiki-SQLcategories.sql";
  	public void setPathToCategoriesSQL(String pathToSQL) {
  		this.pathToCategoriesSQL = pathToSQL;
  		try {
			reader1 = new FileInputStream(pathToSQL);
			scanner1 = new Scanner(reader1, "UTF-8");	
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}		
  	}
  	
  	//Regex string
  	private String regexString1 = "\\((\\d+),'(\\w+)',(\\d+),(\\d+),(\\d+)\\)";
  	private Pattern p1 = Pattern.compile(regexString1);
  	
  	//
  	// SQL categorylink init region below
  	//
  	
    private FileInputStream reader2 = null;
    private Scanner scanner2 = null;
  	
    //default relative path to SQL link categories file
  	//+ setter
  	private String pathToLinksSQL = ".\\data\\input-dewiki-SQLcategorylinks.sql";
  	public void setPathToLinksSQL(String pathToSQL) {
  		this.pathToLinksSQL = pathToSQL;
  		try {
			reader2 = new FileInputStream(pathToSQL);
			scanner2 = new Scanner(reader2, "UTF-8");	
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}		
  	}
    
  	//Regex string
  	private String regexString2 = "\\((\\d+),'(\\w+)','(.*?)','(.*?)','(.*?)','(\\w+)','(\\w+)'\\)";
  	private Pattern p2 = Pattern.compile(regexString2);
  	
	//getter for Instance
	public static ParserSQL getInstance()
	{
		if(instance == null)
			return new ParserSQL();
		else
			return instance;
	}
	
	//
	//CONSTRUCTOR
	//
	private ParserSQL() {
		try {
			reader1 = new FileInputStream(pathToCategoriesSQL);
			scanner1 = new Scanner(reader1, "UTF-8");	
			
			reader2 = new FileInputStream(pathToLinksSQL);
			scanner2 = new Scanner(reader2, "UTF-8");	
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean parseCategFile() throws IOException
	{	
		if (scanner1 == null)
		{
			System.out.println("No scanner.");
			return false;
		}
		
		while(scanner1.hasNextLine())
		{
			String line = scanner1.nextLine();
			//check for errors, because Scanner suppresses them
			if (scanner1.ioException() != null) {
		        throw scanner1.ioException();
		    }
			
			//do parsing with the line
			Matcher matcher = p1.matcher(line);
			
			String category = "";
			try
			{
				while(matcher.find())
				{
					category = matcher.group(2);
					//every space is replaced by '_'
					category = category.replace('_', ' ');
					
					int noOfOccurances = -1;
					try
					{
						noOfOccurances = Integer.parseInt(matcher.group(3));
						
						System.out.println("Inserting SQL category: " + category + ", " + noOfOccurances);
						
						insertIntoMap(category, noOfOccurances);
					}
					catch(NumberFormatException e) { System.out.println(e.getMessage()); }			
				}
			}
			catch(IllegalStateException e) { 
				System.out.println(e.getMessage()); 
			}
		}
		
		return true;
	}
	
	public boolean parseLinksFile() throws IOException
	{
		if (scanner2 == null)
		{
			System.out.println("No scanner.");
			return false;
		}
		
		File file = new File(".\\data\\output-dewiki-SQLlinks.txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bf = new BufferedWriter(new FileWriter(file));
		
		while(scanner2.hasNextLine())
		{
			String line = scanner2.nextLine();
			
			//check for errors, because Scanner suppresses them
			if (scanner2.ioException() != null) {
				bf.close();
		        throw scanner2.ioException();
		    }
			
			//do parsing with the line
			Matcher matcher = p2.matcher(line);
			
			int articleId = -1;
			String category = "";
			String type = "";
			try
			{
				while(matcher.find())
				{
					try
					{
						articleId = Integer.parseInt( matcher.group(1) );
					}
					catch(NumberFormatException nfe) { System.out.println(nfe.getMessage()); continue; }
					
					category = matcher.group(2);
					type = matcher.group(7);
					
					//just for check
					if(!"page".equals(type))
						continue;
					
					//every space is replaced by '_'
					category = category.replace('_', ' ');
										
					System.out.println("Inserting into LINKS: " + category + ", " + articleId);
						
					bf.write(category + " | " + articleId);
					bf.newLine();
					bf.flush();
				}
			}
			catch(IllegalStateException e) { 
				System.out.println(e.getMessage()); 
			}
		}
		
		bf.close();
		return true;
	}
	
	private void insertIntoMap(String str, int i)
	{
		sqlHashmap.put(str, i);
	}
}
