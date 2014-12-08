package com.eriksuta.data.handler;

import org.openrdf.model.Statement;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 *  @author Erik Suta
 * */
public class BasicRdfHandler extends RDFHandlerBase {

    /**
     *  Number of parses statements in RDF file, e.g. semantic triplets
     * */
    int numOfStatements = 0;

    /**
     *  Number of statements after parsing - how much have we managed to reduce parsed file
     *  (This number depends on handling algorithm as well as on source data)
     * */
    int numberOfStatementsAfter = 0;


    /**
     *  Override this to provide handler for statements in RDF file
     * */
    @Override
    public void handleStatement(Statement statement){}

    public int getNumberOfStatements(){
        return numOfStatements;
    }

    public int getNumberOfStatementsAfter(){
        return numberOfStatementsAfter;
    }
}
