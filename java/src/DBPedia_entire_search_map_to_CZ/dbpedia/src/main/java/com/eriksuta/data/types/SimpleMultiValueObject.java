package com.eriksuta.data.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author Erik Suta
 * */
public class SimpleMultiValueObject implements Serializable{

    private String label;
    private List<String> values;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getValues() {
        if(values == null){
            values = new ArrayList<String>();
        }

        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }


}
