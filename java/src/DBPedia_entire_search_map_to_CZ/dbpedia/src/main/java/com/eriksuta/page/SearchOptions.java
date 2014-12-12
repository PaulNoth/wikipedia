package com.eriksuta.page;

import java.io.Serializable;

/**
 *  @author Erik Suta
 * */
public class SearchOptions implements Serializable{

    public static final String F_MATCH_CASE = "matchCase";
    public static final String F_WHOLE_WORD = "wholeWord";
    public static final String F_REGEX = "asRegex";
    public static final String F_LINKS = "links";
    public static final String F_ABSTRACTS = "abstracts";
    public static final String F_CATEGORIES = "categories";
    public static final String F_INFO_PROPERTIES = "infoboxProperties";
    public static final String F_LINK_MAPPING_CZ = "linkMappingCz";

    private boolean matchCase;
    private boolean wholeWord;
    private boolean asRegex;

    private boolean links = true;
    private boolean abstracts = true;
    private boolean categories = true;
    private boolean infoboxProperties = true;

    private boolean linkMappingCz;

    public SearchOptions(){}

    public boolean isMatchCase() {
        return matchCase;
    }

    public void setMatchCase(boolean matchCase) {
        this.matchCase = matchCase;
    }

    public boolean isWholeWord() {
        return wholeWord;
    }

    public void setWholeWord(boolean wholeWord) {
        this.wholeWord = wholeWord;
    }

    public boolean isAsRegex() {
        return asRegex;
    }

    public void setAsRegex(boolean asRegex) {
        this.asRegex = asRegex;
    }

    public boolean isLinks() {
        return links;
    }

    public void setLinks(boolean links) {
        this.links = links;
    }

    public boolean isAbstracts() {
        return abstracts;
    }

    public void setAbstracts(boolean abstracts) {
        this.abstracts = abstracts;
    }

    public boolean isCategories() {
        return categories;
    }

    public void setCategories(boolean categories) {
        this.categories = categories;
    }

    public boolean isInfoboxProperties() {
        return infoboxProperties;
    }

    public void setInfoboxProperties(boolean infoboxProperties) {
        this.infoboxProperties = infoboxProperties;
    }

    public boolean isLinkMappingCz() {
        return linkMappingCz;
    }

    public void setLinkMappingCz(boolean linkMappingCz) {
        this.linkMappingCz = linkMappingCz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchOptions)) return false;

        SearchOptions options = (SearchOptions) o;

        if (abstracts != options.abstracts) return false;
        if (asRegex != options.asRegex) return false;
        if (categories != options.categories) return false;
        if (infoboxProperties != options.infoboxProperties) return false;
        if (linkMappingCz != options.linkMappingCz) return false;
        if (links != options.links) return false;
        if (matchCase != options.matchCase) return false;
        if (wholeWord != options.wholeWord) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (matchCase ? 1 : 0);
        result = 31 * result + (wholeWord ? 1 : 0);
        result = 31 * result + (asRegex ? 1 : 0);
        result = 31 * result + (links ? 1 : 0);
        result = 31 * result + (abstracts ? 1 : 0);
        result = 31 * result + (categories ? 1 : 0);
        result = 31 * result + (infoboxProperties ? 1 : 0);
        result = 31 * result + (linkMappingCz ? 1 : 0);
        return result;
    }
}
