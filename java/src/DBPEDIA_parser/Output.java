/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpedia_parser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for making output, xml or console
 * @author Zuzka
 */
public class Output {
    
    public static String fileOut = "output.xml";
    
    //method to create xml output
    public static String create_output_xml(String looking, List<String> shortabstract, List<String> categories, List<String> redirects) throws IOException {
    
        String out = "";
        
        try {
            try (FileWriter fw = new FileWriter(fileOut); BufferedWriter textWriter = new BufferedWriter(fw)) {
                List<String> output = new ArrayList<>();
                
                output.add("<article>");
                
                output.add("<title>" + looking + "</title>");
                output.add("<short_abstract>" + shortabstract.get(0) + "</short_abstract>");
                
                output.add("<categories>");
                output.add("<nmbcat>" + categories.size() + "</nmbcat>");
                for (String l : categories) {
                    output.add("<cat>" + l + "</cat>");
                }
                output.add("</categories>");
                
                output.add("<redirects>");
                output.add("<nmbred>" + redirects.size() + "</nmbred>");
                for (String l : redirects) {
                    output.add("<redrct>" + l + "</redrct>");
                }
                output.add("</redirects>");
                
                output.add("</article>");
                
                for (String l : output) {
                    out = out + l + "\n";
                    textWriter.write(l);
                    textWriter.newLine();
                }
            }
            
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
        
        
        return out;
    }
    
    //method to create console output
    public static String create_output_console(String label, List<String> shortabstract_list, List<String> categories_list, List<String> redirects_list) throws IOException {
        
        String output = "";
        output = output + ("*****************" + label + "*****************") + "\n";
        
        output = output + ("Short abstract:") + "\n";
        if (shortabstract_list.isEmpty()) {
            output = output + ("There's no short abstract for title " + label) + "\n";
        } else {
            for (String s : shortabstract_list) {
                output = output + (s) + "\n";
            }
        }
        output = output + ("Categories:") + "\n";
        if (categories_list.isEmpty()) {
            output = output + ("There's no categories for title " + label) + "\n";
        } else {
            for (String c : categories_list) {
                output = output + (c) + "\n";
            }
        }
        
        output = output + ("Redirect:") + "\n";
        if (redirects_list.isEmpty()) {
        output = output + ("There's no redirect for title " + label) + "\n";
        } else {//            
            for (String c : redirects_list) {
                System.out.println(c);
            }
        }
        return output;
    }
}
