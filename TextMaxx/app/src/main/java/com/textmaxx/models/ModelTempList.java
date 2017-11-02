package com.textmaxx.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ModelTempList extends RealmObject {

    @Required
    private String temp_id;
    private String label;

    public String getTemp_id() {
        return temp_id;
    }

    public void setTemp_id(final String temp_id) {
        this.temp_id = temp_id;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }


}