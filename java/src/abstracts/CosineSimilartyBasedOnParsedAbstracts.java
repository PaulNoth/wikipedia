package abstracts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Scanner;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

/**
 * 
 * @author Matej Toma
 *
 */
public class CosineSimilartyBasedOnParsedAbstracts {
	
	//Used for JUnit test as array of pages from test XML file
	public static ArrayList<ParsedWikiArticleEntity> allPages = new ArrayList<ParsedWikiArticleEntity>();
	
	//Common wiki titles from articles and abstracts XMLs
	public static LinkedHashSet<String> intersection;
	
	//Number of lines from index_ids.txt file to compute cosine similarity
	private static int numbOfRecordsToProccessFromList = 1000;
	
	//Instance of my parser
	private static WikiPagesAndAbstractsParser myParser;
	
	// Names of XMLs files which contains data, star at the end is important as regex matcher for any number
	private static final String filterNameOfAbstractFile = "sample_input_enwiki-latest-abstract1";
	private static final String	filterNameOfPageFile = "sample_input_enwiki-latest-pages-articles9";
	
	// Set true if you want to create new index_ids.txt file, see createListOfCommonIDs method
	private static Boolean fullExample = true;
	
	//Filepath to resource folder
	private static String pathToResource = ".\\data";
	
	public static void main(String[] args) throws Exception{
		
		System.out.println("Enter mark ALL or wiki title without \"Wikipedia: \" prefix here : ");
		 
   		// Read input from console
		String inputFromConsole;
   		Scanner scanInput = new Scanner(System.in);
   		inputFromConsole = scanInput.nextLine();
   		scanInput.close();        
   		
   		myParser = new WikiPagesAndAbstractsParser();
   		
   		// If input string is "ALL" then iterate over all common IDs
   		if(inputFromConsole.equals("ALL")){
   			//Used to capture time complexity
   			long startTimeInMiliseconds = new Date().getTime();
			java.util.Date date = new Date();
			System.out.println("Start: "+new Timestamp(date.getTime()));
			
			//Create index_ids.txt file which contain common IDs from XMLs
			if(fullExample)
				createListOfCommonIDs();
			
			readListOfCommonIDs(numbOfRecordsToProccessFromList, false);
			
			System.out.println("Number of processed records is set to: "+numbOfRecordsToProccessFromList);
			System.out.println("Number of common titles are: "+intersection.size());
			
			myParser.setIntersectionBetwenArticlesAndAbstracts(intersection);
			
			//Set false because we want iterate over all files
			myParser.setQueryOption(false);
			
			// Reading abstracts 
			iterateOverFiles(pathToResource, true, filterNameOfAbstractFile);
			
			// Reading files 
			iterateOverFiles(pathToResource, false, filterNameOfPageFile);
			System.out.println("---------------------");
			// Format output string in console to two decimal precise
			DecimalFormat df = new DecimalFormat("###.##");
			System.out.println("Average similarity: "+df.format((myParser.totalPercentage / myParser.totalComparedRecords) * 100)+"%");
			System.out.println("Uncompared abstracts: "+myParser.getUncomparedAbstracts());
			System.out.println("Uncompared articles: "+myParser.unComparedArticles);
			System.out.println("Compared but abstracts are corrupted: "+myParser.badAbstracts);
			
			Integer totalRecs = myParser.totalComparedRecords;

			Double median = 0.0;
			if((totalRecs % 2)==0)
				median = (myParser.getValuesForMedians().get((totalRecs/2)-1) + myParser.getValuesForMedians().get((totalRecs/2)))/2;
			else 
				median = myParser.getValuesForMedians().get((totalRecs/2));
			
			System.out.println("Median of values: "+ df.format(median*100)+"%");
			//Used to capture time complexity
			java.util.Date date2 = new java.util.Date();
			System.out.println("End: "+new Timestamp(date2.getTime()));
			
			long endTimeInMiliseconds = new Date().getTime();
			System.out.println("Program running: "+(new SimpleDateFormat("mm:ss:SSS")).format(new Date(endTimeInMiliseconds-startTimeInMiliseconds)));
   		} else { 
   			/* Search only input string as title */
   			
   			//Need to set true because we are finding only specific title in files
   			myParser.setQueryOption(true);
   			
   			LinkedHashSet<String> query = new LinkedHashSet<>();
   			query.add("Wikipedia: "+inputFromConsole);
   			myParser.setIntersectionBetwenArticlesAndAbstracts(query);
   			
   			/* Reading abstracts */
			iterateOverFiles(pathToResource, true, filterNameOfAbstractFile);
			
			/* Reading files */ 
			myParser.resetQueryFinished();
			iterateOverFiles(pathToResource, false, filterNameOfPageFile);
			
			System.out.println("Process of searching query "+inputFromConsole+" has finished.");
   		}
	}
	
	/**
	 * This method read common IDs list and added to class variable intersection
	 * @param numbOfRecordsToRead Default number is set by class, this number specific how
	 * many lines from file we want to read
	 * @param all Set to true if you want to process all abstracts from intersection set. If
	 * this option is set to false then variable numbOfRecordsToProccessFromList is used.
	 */
	public static void readListOfCommonIDs(int numbOfRecordsToRead, boolean all){
		try {
			String path = new File(pathToResource+"\\index_ids.txt").getCanonicalPath();
			File file = new File(path);
			
			FileReader fr = new FileReader(file.getAbsoluteFile());
			BufferedReader br = new BufferedReader(fr);
			
			intersection = new LinkedHashSet<String>();
			int counter = numbOfRecordsToRead;
	        while(counter > 0){
	        	String line = br.readLine();
	        	if(line != null){
		            intersection.add(line);
		            if(!all)
		            	counter--;
	        	} else
	        		break;
	        }
	        br.close();
	        fr.close();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	/**
	 * This method create common IDs text file from given XML resources
	 */
	public static void createListOfCommonIDs(){	
		// Contains IDs from articles XMLs
		LinkedHashSet<String> abstractsIds = new LinkedHashSet<String>();
		// Contains IDs from abstracts XMLs
		LinkedHashSet<String> pagesIds = new LinkedHashSet<String>();
		
		abstractsIds = getIds(pathToResource, true, filterNameOfAbstractFile);
		pagesIds = getIds(pathToResource, false, filterNameOfPageFile);
		
		System.out.println("Abstracts: "+abstractsIds.size());
		System.out.println("Articles: "+pagesIds.size());
		
		intersection = new LinkedHashSet<String>(abstractsIds);
		
		//Intersection of articles and abstracts IDs
		intersection.retainAll(pagesIds);
		
		System.out.println("Common: "+intersection.size());
		
		Iterator<String> itr = intersection.iterator();
		
		try {
			System.out.println("Writting IDs to file");
			String pathOfIndex = new File(pathToResource+"\\index_ids.txt").getCanonicalPath();
			File file = new File(pathOfIndex);
			
			if (!file.exists())
				file.createNewFile();
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
	        while(itr.hasNext()){
	            bw.append(itr.next());
	            bw.newLine();
	        }
	        bw.close();
	        fw.close();
			
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		} finally{
			System.out.println("Writting common IDs was finished.");
		}

	}
	
	/**
	 * This method returns IDs of given XML files (articles or abstracts)
	 * @param path Path to resource folder
	 * @param isAbstract Is set true if input files are abstracts 
	 * @param filterString Name of XML file with star symbol at the end, this works like regex
	 * @return Set of Wikipedia titles are returned
	 */
	public static LinkedHashSet<String> getIds(String path, boolean isAbstract, String filterString){
		File dir = new File(path);
		
		FileFilter fileFilter = new WildcardFileFilter(filterString+"*.xml");
		File[] directoryListing = dir.listFiles(fileFilter);
		int i = 1;
		LinkedHashSet<String> results = new LinkedHashSet<String>();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				try {
					InputStream in = new FileInputStream(child);
					XMLInputFactory2 inputFactory = (XMLInputFactory2)XMLInputFactory.newInstance();
					
					XMLStreamReader2 streamReader = (XMLStreamReader2)inputFactory.createXMLStreamReader(in);
					String content = "";
					while (streamReader.hasNext()) {
			        	int eventType = streamReader.next();
	        	           switch (eventType) {
			                	case XMLEvent.CDATA:
			                	case XMLEvent.SPACE:
			                	case XMLEvent.COMMENT:
				                case XMLEvent.CHARACTERS:
				                	content = streamReader.getText();
				                    break;
				                case XMLEvent.END_ELEMENT:
				                	if(streamReader.getLocalName().equals("title")){
				                		if(isAbstract == true)
				                			results.add(content);
				                		else
				                			results.add("Wikipedia: "+content);
				                		
				                		System.out.println(i+".) "+content);
				                		i++;
				                	}
				                    break;
			                }
			        }
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}			
			}
		}
		
		return results;
	}
		
	/**
	 * Method iterate over given files and read every XML in loop and do with him something
	 * @param path	Path to resource folder
	 * @param isAbstractFile	Is set true if input files are abstracts 
	 * @param filterString	Name of XML file with star symbol at the end, this works like regex
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public static void iterateOverFiles(String path, Boolean isAbstractFile, String filterString) throws XMLStreamException, IOException{
		XMLInputFactory2 inputFactory = (XMLInputFactory2)XMLInputFactory.newInstance();
		
		
		
		File dir = new File(path);
		FileFilter fileFilter = new WildcardFileFilter(filterString+"*.xml");
		File[] directoryListing = dir.listFiles(fileFilter);
		if (directoryListing != null) {
			for (File child : directoryListing) {
				System.out.println("File name: "+ child.getName());
				
				InputStream in = new FileInputStream(child);						
				XMLStreamReader2 streamReader = (XMLStreamReader2)inputFactory.createXMLStreamReader(in);
				
				//If reading Article file => set false
				myParser.readXML(streamReader, isAbstractFile);
				
				in.close();
				streamReader.close();
				
				if(myParser.getQueryFinised())
					break;
			}
		}
	}
	
	/* Used only for JUnit testing, works like iterateOverFiles, but only on single file. */
	public static void singleFileReader(String filename, Boolean isAbstractFile) throws IOException, XMLStreamException{
		XMLInputFactory2 inputFactory = (XMLInputFactory2)XMLInputFactory.newInstance();
		String path = new File(pathToResource+"\\"+filename+".xml").getCanonicalPath();
		File file = new File(path);
		InputStream in = new FileInputStream(file);
		XMLStreamReader2 streamReader = (XMLStreamReader2)inputFactory.createXMLStreamReader(in);
		
		myParser = new WikiPagesAndAbstractsParser();
		
		myParser.setQueryOption(false);
		
		//Set true if we are running JUnit, otherwise false
		myParser.setTestEnvireoment(true);
		myParser.readXML(streamReader, isAbstractFile);	
		
		//Add parsed pages to array for testing reason
		allPages.addAll(myParser.getAllPages());
	}
}