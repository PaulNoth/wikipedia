package com.eriksuta.data;

import com.eriksuta.data.handler.BasicRdfHandler;

import java.io.File;

/**
 *  @author Erik Suta
 *
 * */
public interface IParser {

    /**
     *  This method is responsible for parsing entire dump of Slovak DB-Pedia
     * */
    public void parseSlovakDBPedia();

    /**
     *  This method is responsible for parsing entire dump of Czech DB-Pedia
     * */
    public void parseCzechDBPedia();

    /**
     *  This method is called from other methods of this interface and should be
     *  an implementation of general method for parsing any RDF file. It may use
     *  different RDF parsing technologies (Sesame, Apache jena) as long as
     *  interface method signatures are met
     *
     *  @param input
     *      An input RDF file
     *
     *  @param output
     *      An output file in any format the implementation needs
     *
     *  @param handler
     *      An implementation of BasicRdfHandler that handles the
     *      parsing of single triple of semantic data
     *
     * */
    public void parseRdfFile(File input, File output, BasicRdfHandler handler);

    /**
     *  This method is responsible for sorting the statements in file. It should be
     *  used to ease the process of indexing and searching but it is not necessary
     *  to use it (although at least an empty implementation is requested). Sorting
     *  method and algorithm depends entirely on implementation.
     * */
    public void sortStatementsInFile(File fileToSort);
}