package com.eriksuta.search;

import com.eriksuta.data.search.InfoboxPropertyType;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author Erik Suta
 * */
public class SearchUtil {

    public static List<String> processStringListResult(ScoreDoc[] hits, IndexSearcher indexSearcher, String contentName) throws IOException{
        List<String> stringResultList = new ArrayList<String>();

        for(ScoreDoc hit: hits){
            int docId = hit.doc;
            Document d = indexSearcher.doc(docId);

            stringResultList.add(d.get(contentName));
        }

        return stringResultList;
    }

    public static List<InfoboxPropertyType> processInfoboxProperties(ScoreDoc[] hits, IndexSearcher indexSearcher, String contentName) throws IOException{
        List<InfoboxPropertyType> infoboxPropertyList = new ArrayList<InfoboxPropertyType>();

        for(ScoreDoc hit: hits){
            int docId = hit.doc;
            Document d = indexSearcher.doc(docId);

            String retrievedValue = d.get(contentName);
            String[] properties = retrievedValue.split("\t");

            InfoboxPropertyType infoboxProperty;
            for(String property: properties){
                if(!property.isEmpty()){
                    String[] propertyFields = property.split(":");

                    if(propertyFields.length == 2){
                        infoboxProperty = new InfoboxPropertyType();
                        infoboxProperty.setName(propertyFields[0]);
                        infoboxProperty.setValue(propertyFields[1]);

                        infoboxPropertyList.add(infoboxProperty);
                    }
                }
            }
        }

        return infoboxPropertyList;
    }

    public static String processStringProperty(ScoreDoc[] hits, IndexSearcher indexSearcher, String contentName) throws IOException {
        String property = null;

        if(hits.length != 0){
            int docId = hits[0].doc;
            Document d = indexSearcher.doc(docId);

            property = d.get(contentName);
        }

        return property;
    }

    public static String processIntegerProperty(ScoreDoc[] hits, IndexSearcher indexSearcher, String contentName) throws IOException {
        StringBuilder sb = new StringBuilder();

        if(hits.length != 0){
            for(ScoreDoc hit: hits){
                int docId = hit.doc;
                Document d = indexSearcher.doc(docId);

                String actValue = d.get(contentName);
                String afterTrim = actValue.replaceAll("\t", "");

                sb.append(afterTrim);
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
