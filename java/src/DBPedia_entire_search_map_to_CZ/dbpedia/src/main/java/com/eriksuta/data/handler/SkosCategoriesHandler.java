package com.eriksuta.data.handler;

import com.eriksuta.data.ParserImpl;
import com.eriksuta.data.types.SimpleMultiValueObject;
import com.google.gson.Gson;
import org.openrdf.model.Statement;

/**
 *  @author Erik Suta
 * */
public class SkosCategoriesHandler extends BasicRdfHandler{

    private SimpleMultiValueObject lastObject;

    @Override
    public void handleStatement(Statement statement){
        String[] subject = statement.getSubject().stringValue().split("/");
        String subjectValue = subject[subject.length-1];

        if(subjectValue.startsWith("Kategória") || subjectValue.startsWith("Category")){
            String[] subjects = subjectValue.split(":");
            subjectValue = subjects[subjects.length-1];
        }

        String predicate = statement.getPredicate().stringValue();
        String objectValue;

        if(predicate.endsWith("prefLabel")){
            objectValue = statement.getObject().stringValue();
        } else if (predicate.endsWith("broader")){
            String objects[] = statement.getObject().stringValue().split(":");
            objectValue = objects[objects.length-1];
        } else {
            numOfStatements++;
            return;
        }

        if(lastObject != null){
            if(lastObject.getLabel().equals(subjectValue)){
                lastObject.getValues().add(objectValue);
            }else{
                Gson gson = new Gson();
                ParserImpl.getParserInstance().writeToFile(gson.toJson(lastObject) + "\n");

                lastObject = new SimpleMultiValueObject();
                lastObject.setLabel(subjectValue);
                lastObject.getValues().add(objectValue);
                numberOfStatementsAfter++;
            }
        } else {
            lastObject = new SimpleMultiValueObject();
            lastObject.setLabel(subjectValue);
            lastObject.getValues().add(objectValue);
        }

        numOfStatements++;
    }
}
