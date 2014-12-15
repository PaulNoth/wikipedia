package com.eriksuta.data.types;

import com.eriksuta.data.search.InfoboxPropertyType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author Erik Suta
 * */
public class InfoboxObject implements Serializable{

    private String label;
    private List<InfoboxPropertyType> infoboxProperties;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<InfoboxPropertyType> getInfoboxProperties() {
        if(infoboxProperties == null){
            infoboxProperties = new ArrayList<InfoboxPropertyType>();
        }

        return infoboxProperties;
    }

    public void setInfoboxProperties(List<InfoboxPropertyType> infoboxProperties) {
        this.infoboxProperties = infoboxProperties;
    }
}
