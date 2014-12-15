/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * For xml compare is used tutorial from "http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/"
 */
package dbpedia_parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for unit test. Test whether output is correctly generated
 * @author Zuzka
 */
public class UnitTest {

    //Check whether output for entity Autism is as we expect. If no then there is a failure in program
    public static boolean unitTest() throws IOException {

        List<String> categories_expected = new ArrayList<>();
        List<String> redirects_expected = new ArrayList<>();
        List<String> short_abstract_expected = new ArrayList<>();

        xmlreader("expected.xml", categories_expected, short_abstract_expected, redirects_expected);
        
        List<String> categories_actual = new ArrayList<>();
        List<String> redirects_actual = new ArrayList<>();
        List<String> short_abstract_actual = new ArrayList<>();

        xmlreader("output.xml", categories_actual, short_abstract_actual, redirects_actual);

      
        //Check whether first 20 characters of actual short abstract and expected short abstract are the same
        if (short_abstract_actual.get(0).substring(0, 20).equals(short_abstract_expected.get(0).substring(0, 20)) == false) {
            return false;
        }

        //Check whether actual categories are the same or there is more categories
        if (categories_expected.size() <= categories_actual.size()) {
            for (String l1 : categories_expected) {
                if (categories_actual.contains(l1) == false) {
                    return false;
                }
            }
        } else {
            return false;
        }

        //Check whether actual redirects are the same or there is more redirects
        if (redirects_expected.size() <= redirects_actual.size()) {
            for (String l1 : redirects_expected) {
                if (redirects_actual.contains(l1) == false) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;

    }

    public static void xmlreader(String filepath, List<String> categories, List<String> short_abstract, List<String> redirects) throws IOException {

        try {

            File xmlfile = new File(filepath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlfile);

            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("article");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
//
//                    //System.out.println("Staff id : " + eElement.getElementsByTagName("short_abstract").item(0).getTextContent());
//                    System.out.println("First Name : " + eElement.getElementsByTagName("categories").item(0).getTextContent());
////                    System.out.println("Last Name : " + eElement.getElementsByTagName("redirects").item(0).getTextContent());
//                    System.out.println("Nick Name : " + eElement.getElementsByTagName("cat").item(0).getTextContent());

                    short_abstract.add(eElement.getElementsByTagName("short_abstract").item(0).getTextContent());

                    //categories = new ArrayList<>();
                    int categories_count = Integer.parseInt(eElement.getElementsByTagName("nmbcat").item(0).getTextContent());
                    if (categories_count > 0) {
                        for (int i = 0; i < categories_count; i++) {
                            categories.add(eElement.getElementsByTagName("cat").item(i).getTextContent());
                        }
                    }

                    //redirects = new ArrayList<>();
                    int redirects_count = Integer.parseInt(eElement.getElementsByTagName("nmbred").item(0).getTextContent());
                    if (redirects_count > 0) {
                        for (int i = 1; i < redirects_count; i++) {
                            redirects.add(eElement.getElementsByTagName("redirects").item(i).getTextContent());
                        }
                    }

                }
            }
        } catch (Exception e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }
}
