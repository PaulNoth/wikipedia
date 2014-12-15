package com.eriksuta.data.index;

import com.eriksuta.data.search.InfoboxPropertyType;
import com.eriksuta.data.types.InfoboxObject;
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
public class PropertyIndexAlgorithm implements IndexAlgorithm{

    private String labelName;
    private String contentName;

    public PropertyIndexAlgorithm(String labelName, String contentName){
        this.labelName = labelName;
        this.contentName = contentName;
    }

    @Override
    public void createSimpleIndex(String line, IndexWriter indexWriter) throws IOException {
        Gson gson = new Gson();
        InfoboxObject object = gson.fromJson(line, InfoboxObject.class);

        if(object == null){
            return;
        }

        StringBuilder sb = new StringBuilder();
        for(InfoboxPropertyType i: object.getInfoboxProperties()){
            sb.append(i.getName());
            sb.append(":");
            sb.append(i.getValue());
            sb.append("\t");
        }
        String content = sb.toString();

        Document document = new Document();
        document.add(new TextField(labelName, object.getLabel(), Field.Store.YES));

        /*
        *   This is a dirty fix for issue from https://issues.apache.org/jira/browse/LUCENE-5472
        *   If a term is longer than 2^15, IllegalArgumentException is thrown and the process
        *   of indexing is stopped. We don't want that, so we just won't index fields longer
        *   than 2^15 bytes.
        * */
        if(content.getBytes().length < 32766){
            document.add(new StringField(contentName, sb.toString(), Field.Store.YES));
        }
        indexWriter.addDocument(document);
    }
}
