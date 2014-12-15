package com.eriksuta.data.handler;

import com.eriksuta.data.ParserImpl;
import com.eriksuta.data.search.InfoboxPropertyType;
import com.eriksuta.data.types.InfoboxObject;
import com.google.gson.Gson;
import org.openrdf.model.Statement;

/**
 *  @author Erik Suta
 * */
public class InfoboxPropertiesHandler extends BasicRdfHandler{

    InfoboxObject lastObject;

    @Override
    public void handleStatement(Statement statement){
        statement.getObject();

        String[] subject = statement.getSubject().stringValue().split("/");
        String subjectValue = subject[subject.length-1];

        String[] predicate = statement.getPredicate().stringValue().split("/");
        String predicateValue = predicate[predicate.length-1];

        String objectValue =  statement.getObject().stringValue();
        if(objectValue.contains("\n")){
            objectValue = objectValue.replaceAll("\n", " ");
        }

        if(lastObject != null){
            if(lastObject.getLabel().equals(subjectValue)){
                lastObject.getInfoboxProperties().add(new InfoboxPropertyType(predicateValue, objectValue));
            } else {
                Gson gson = new Gson();
                ParserImpl.getParserInstance().writeToFile(gson.toJson(lastObject) + "\n");

                lastObject = new InfoboxObject();
                lastObject.setLabel(subjectValue);
                lastObject.getInfoboxProperties().add(new InfoboxPropertyType(predicateValue, objectValue));
                numberOfStatementsAfter++;
            }
        } else {
            lastObject = new InfoboxObject();
            lastObject.setLabel(subjectValue);
            lastObject.getInfoboxProperties().add(new InfoboxPropertyType(predicateValue, objectValue));
        }

        numOfStatements++;
    }
}
