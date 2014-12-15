package enwikiPagerank.parseToText;

import java.io.File;

import enwikiPagerank.mapreduce.FileCleaner;
import enwikiPagerank.mapreduce.PagelinksAndPagesJoiner;
import enwikiPagerank.mapreduce.PagelinksParser;
import enwikiPagerank.mapreduce.SubDomainCreator;
import enwikiPagerank.pagerankComputation.PagerankComputation;
import enwikiPagerank.pagerankComputation.SampleCreator;
import enwikiPagerank.test.PagelinksTest;
import enwikiPagerank.test.PagesTest;

/**
 * Created by Jozef on 20.10.2014.
 * 
 * THE MOST IMPORTANT - read before running... make sure you checked this page
 * http://vi.ikt.ui.sav.sk/User:Jozef.Marcin?view=home
 * 
 * contains information about unit tests, running and implementation
 */
public class Main implements Runnable {

	private static final long MEGABYTE = 1024L * 1024L;
	public static int in;
    public static void main(String[] args){
    	in = Integer.parseInt(args[0]);
      
        String currPath = null; // get working dir
        String currPathParrent = null; // get parent directory for data
        try{
            File currentDirectory = new File(new File(".").getAbsolutePath());
            System.out.println(currentDirectory.getCanonicalPath());
            System.out.println(currentDirectory.getAbsolutePath());
            currPath = currentDirectory.getCanonicalPath();
            currPathParrent = currentDirectory.getParentFile().getParent();
            System.out.println(currPathParrent + " parent");
            }catch(Exception e){
            	e.printStackTrace();
            }
 
        long startTime = System.currentTimeMillis();

		switch (in) {
		case 1:
			/* 
			 * time of parsing 145mins
			 */
			FileRW frw = new FileRW();
		    frw.readGzWriteTxt(currPath+"/enwiki-latest-page.sql.gz",currPath+"/pagesControll.txt");
		    PagelinksFileRW pgrw = new PagelinksFileRW();
		    pgrw.readGzWriteTxt(currPath+"/enwiki-latest-pagelinks.sql.gz", currPath+"/pageLinksControll.txt");
		    break;
		    
		case 2:
			/*
			 * before this step there is need for archive the file pageLinksControl.txt to pageLinksControl.txt.gz
			 * this act eliminates the bottleneck of the computation
			 * 51mins
			 */
			PagelinksParser.parseFile(currPath+"/pageLinksControll.txt.gz", currPath+"/pagelinksReduced.txt");
			break;
			
		case 3:
			/* create subdomain from whole file pages - becouse 
			 * 1 mins
			 */
			SubDomainCreator.createSubDomain(currPath+"/pagesControll.txt", currPath+"/pagesSubdomain.txt", 5000000);	
			break;
			
		case 4:
			/*create hashmap with pages names and ids, create file pagelinksReducedSubdomain.txt, which stores
			*the id and ids of pages that point to page with id
			*34mins
			*/
			PagelinksAndPagesJoiner.readPagesFileToMap(currPath+"/pagesSubdomain.txt", null, currPath);
			break;
			
		case 5:	
			/*
			 *create the last files - onlyIDs.txt - holds only the IDs of pages
			 *and cleanFile.txt - contains only IDs and ID pointings that are in pages subdomain
			 * 212 mins
			 */
			FileCleaner.writeIdsToFile(currPath+"/pagelinksReducedSubdomain.txt", currPath+"/onlyIDs.txt");
	        FileCleaner.createHashMap(currPath+"/onlyIDs.txt");
	        FileCleaner.cleanFile(currPath+"/pagelinksReducedSubdomain.txt", currPath+"/onlyIDs.txt", currPath+"/cleanFile.txt");
	        PagerankComputation.computeAndWriteOutgoingLinks(currPath+"/cleanFile.txt", currPath+"/outgoing.txt", currPath+"/onlyIDs.txt");
	        break;
			
		case 6:
			/*
			 * compute pagerank for pages and store it to file finalPagerank.txt
			 * one epoch takes approximately 4mins
			 *  for convergent results we need approximately 120 - 150 epochs
			 *   
			 */
			PagerankComputation.createPagerankHashMap(currPath
					+ "/eonlyIDs.txt");
			PagerankComputation.createOutgoingHashMap(currPath
					+ "/outgoing.txt");
			PagerankComputation.computePagerank(currPath
					+ "/cleanFile.txt", null, currPath, 220);
			break;
			
		case 7:
			/*
			 * if we want to generate the file which is important if we want to test the pagerank
			 * need to generate your own sampleOnlyIDs.txt file before
			 */
			SampleCreator.createSampleCleanFile(currPath+"/sampleOnlyIDs.txt", currPath+"/sampleCleanFile.txt");
			break;
			
		case 8:
			/*
			 * runs unit tests for pages and pagelinks
			 * running unit test is simple - we need to set the input parameter to number 8
			 * then just run the program in any IDE
			 * if the project directories structure is OK, test will pass trough - if no exception is prompted in console
			 * you know that tests passed well
			 * only one restriction - there is need to check data folder if contains two files
			 * test-pages-output.txt and test-pagelinks-output.txt. If yes, we need to delete them before we run the test
			 */
			PagelinksTest plT = new PagelinksTest();
			PagelinksFileRW plrw = new PagelinksFileRW();
			plrw.readTxtWriteTxt(currPathParrent+"/data/sample-input-enwiki-latest-pagelinks.txt", currPathParrent+"/data/test-pagelinks-output.txt");
			plT.populateParameters(currPathParrent+"/data/sample-input-enwiki-latest-pagelinks.txt", currPathParrent+"/data/test-pagelinks-output.txt", currPathParrent+"/data/sample-output-enwiki-latest-pagelinks.txt");
			plT.testForEqualOutput();
			
			PagesTest pT = new PagesTest();
			FileRW rw = new FileRW();
			rw.readTxtWriteTxt(currPathParrent+"/data/sample-input-enwiki-latest-page.txt", currPathParrent+"/data/test-pages-output.txt");
			pT.populateParameters(currPathParrent+"/data/sample-input-enwiki-latest-page.txt", currPathParrent+"/data/test-pages-output.txt", currPathParrent+"/data/sample-output-enwiki-latest-page.txt");
			pT.testForEqualOutput();
			break;
			
			
			case 9:
			/*
			 * creates histogram for final pagerank file
			 */
			HistogramCreator.createHistogram(currPath+"/finalPgrnk.txt", currPath+"/histogram.txt");
			break;
			
		default: 
			System.out.println("Wrong parameters - system exit");
			System.exit(1);	
		}
		
		

        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: "
            + bytesToMegabytes(memory));
        

        long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total parsing time> " + totalTime/1000/60 + "mins");
       
    }
    
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
      }


    @Override
    public void run() {
        
    }
    
    public static long startTime(){
       long startTime = System.currentTimeMillis();
       return startTime;

    }
    
    public static long endTime(long startTime){
    	long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		return totalTime;
		//System.out.println("Total parsing time> " + totalTime/1000/60 + "mins");
    	
    }
}
