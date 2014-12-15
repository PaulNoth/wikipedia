package com.eriksuta.search;

import com.eriksuta.data.search.SearchResultType;
import com.eriksuta.page.SearchOptions;

/**
 *  This is an interface for SearchService. This interface contains a signature
 *  for main supported search methods. Feel free to add new types of search
 *  options and capabilities.
 *
 *  @author Erik Suta
 * */
public interface SearchService {

    /**
     *  This search method takes another parameter, search options.
     *  Search options contain options for search algorithm, e.g. if
     *  provided search term should be processed as regular expression,
     *  or if we need to match case during search etc.
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @param searchOptions SearchOptions
     *          An object containing a list of special options for
     *          search operation
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType search(String searchTerm, SearchOptions searchOptions);

    /**
     *  This method performs search on a subset of indexed dump of DBPedia.
     *  Link subset is defined by all kinds of links found in DBPedia,
     *  specifically wikipedia links, freebase links, inter-language links,
     *  page links, redirects and transitive redirects.
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType searchInLinks(String searchTerm);

    /**
     *  This method performs search on a subset of indexed dump of DBPedia.
     *  Link subset is defined by all kinds of links found in CZ DBPedia,
     *  specifically wikipedia links, freebase links, inter-language links,
     *  page links, redirects and transitive redirects.
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType searchInCzLinks(String searchTerm);

    /**
     *  This method performs search on a subset of indexed dump of DBPedia.
     *  Abstract subset is defined by short and long abstracts.
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType searchInAbstracts(String searchTerm);

    /**
     *  This method performs search on a subset of indexed dump of DBPedia.
     *  Categories subset is defined by article categories and
     *  skos categories.
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType searchInCategories(String searchTerm);

    /**
     *  This method performs search on a subset of indexed dump of DBPedia.
     *  Infobox Properties subset is defined by, well, infobox properties...
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType searchInInfoboxProperties(String searchTerm);

    /**
     *  This method performs search on a subset of indexed dump of DBPedia.
     *  Basic information subset is defined by:
     *     - labels
     *     - outDegree
     *     - page ID
     *     - page length
     *     - revision ID
     *     - revision uri
     *
     *  @param searchTerm String
     *          A String representation of search term, e.g. term,
     *          upon which the search operation is performed.
     *
     *  @return SearchResultType
     *          A special object containing all data retrieved
     *          from indexed dump
     *
     * */
    public SearchResultType searchInBasicInformation(String searchTerm);
}
