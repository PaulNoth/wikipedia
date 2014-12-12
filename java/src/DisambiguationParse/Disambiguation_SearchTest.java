package DisambiguationParse;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class Disambiguation_SearchTest {
/* this test has dependency on test wikipediaparsetest 
 * wikipediaparsetest create an index from first wiki file
 * this test work with this index and check if index works good
 * 
 *  OR
 *  you create own index AND edit assertEquals function in the bottom of this code into your values
 *  if every test is true, search class works
 * */	

@Test
public void generate() throws IOException{
	List<String[]> myList = new ArrayList<String[]>();
	 String userdir = System.getProperty("user.dir"); 	
	 File path = new File(userdir+"/WikiParse");
     
     File [] files = path.listFiles();
     System.out.println("Ukladam index do hashmapy");
     for (int i = 0; i < files.length; i++){
         if (files[i].isFile()){ 
             
             String spracovanysubor = files[i].toString();
             System.out.println("INDEX: "+spracovanysubor);
            
             
             
             assertEquals(userdir+"\\WikiParse\\index.txt",spracovanysubor);
           
            
            
             String cesta= files[i].toString();
             final BufferedReader reader = new BufferedReader(new FileReader(cesta));
             final StringBuilder contents = new StringBuilder();
             int a =0;
             while(reader.ready()) {
                // contents.append(reader.readLine());
            	 
            	 
            	 
            	 
            	 String precitane = reader.readLine().toString();
            	 
            	if(a==0){
            		String[] rozdel = precitane.split("\\|");
            		assertEquals(rozdel[0],"T:AccessibleComputing");
            		
            	}
            	
            	if(a==1){
            		String[] rozdel = precitane.split("\\|");
            		assertEquals(rozdel[0],"T:Anarchism");
            	}

            	if(a==2){
            		String[] rozdel = precitane.split("\\|");
            		assertEquals(rozdel[0],"T:AfghanistanHistory");
            	}
            	 
            	 
            	// System.out.println(precitane);
                 if(precitane.contains("T:")){
                 String[] cely = precitane.split("\\|");
                
             //   hashMap2.put(cely[0], cely[2]);}
                
               a++;  
             }
            
         
         } reader.close();
     }
}
	
}
}
