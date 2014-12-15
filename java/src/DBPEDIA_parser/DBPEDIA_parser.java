package dbpedia_parser;

import java.io.*;

/**
 * PROGRAM FOR PARSING AND INDEXING DBPEDIA DUMP FILES
 *
 * @author Zuzana Grešlíková
 */
public class DBPEDIA_parser {

    public static void main(String[] args) throws IOException {

        try { 
        
            //*IF YOU WOULD LIKE TO INDEX NEW DUMP FILE, UNCOMMENT "Indexer.parser();" IF NEW DUMP FILES ARE ADDED
            //*NAME DUMP FILES CONVENCES
                //*labels_en.ttl - DUMP with labels
                //*short_abstracts_en.ttl - DUMP with short abstract
                //*article_categories_en.ttl - DUMP with categories
                //*redirects_en.ttl - DUMP with redirect
            //*All the dump files should be placed to folder "input" and old files should be removed
            //Indexer.parser();
            
            ///Test whether program gives correct output
            if (UnitTest.unitTest() == false) {
                System.out.println("Unit test failed. Sorry but something went wrong. ");
                return;
            } else {
                System.out.println("Unit test successuful");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
    
            while (true) {

                //If you would like to try some input to search, you could fo example try: Autism, Autistic Disorder, Auti, Z, Auties  
                String search;
                System.out.println("What are you searching for ?   ");
                search = br.readLine();

                System.out.println("What kind of output do you prefer ? Press 1 for output to console, Press 2 for output to xml document ");

                boolean correct_input = false;
                int input_int = 0;

                while (correct_input == false) {
                    input_int = Integer.parseInt(br.readLine());
                    if (input_int != 1 && input_int != 2) {
                        System.out.println("Please insert correct input. Press 1 for output to console, Press 2 for output to xml document ");
                    } else {
                        correct_input = true;
                    }
                }

                System.out.println("Start searching for : " + search + "\n");

                if (input_int == 1) {
                    System.out.print(Searching.search((search).replace(" ", "_"), false));
                } else {
                    Searching.search((search).replace(" ", "_"), true);
                    System.out.println("Output file was created " + Output.fileOut);
                }
                
                System.out.println("Would you like to continue searching ? In case that yes press Y, in case that no press N");
                while (true) {
                input = br.readLine();
                    if ((input.equals("Y") ==false) && (input.equals("N") ==false)) {
                        System.out.println("Please insert correct input. Press Y if you want to continue, Press N if you dont want to");
                    } else {
                        break;
                    }
                }                
                if (input.equals("N")) {
                      break;
                } 
            }

        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }
}