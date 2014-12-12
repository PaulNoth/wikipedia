package com.eriksuta.data.index;

import com.eriksuta.data.types.SimpleMultiValueObject;
import com.google.gson.Gson;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;

/**
 *  @author Erik Suta
 * */
public class SimpleIndexAlgorithm implements IndexAlgorithm{

    private String labelName;
    private String contentName;

    public SimpleIndexAlgorithm(String labelName, String contentName){
        this.labelName = labelName;
        this.contentName = contentName;
    }

    @Override
    public void createSimpleIndex(String line, IndexWriter indexWriter) throws IOException {
        Gson gson = new Gson();
        SimpleMultiValueObject object = gson.fromJson(line, SimpleMultiValueObject.class);

        if(object == null){
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(String s: object.getValues()){
            sb.append(s);
            sb.append("\t");
        }

        Document document = new Document();
        document.add(new TextField(labelName, object.getLabel(), Field.Store.YES));
        document.add(new StringField(contentName, sb.toString(), Field.Store.YES));
        indexWriter.addDocument(document);
    }
}
