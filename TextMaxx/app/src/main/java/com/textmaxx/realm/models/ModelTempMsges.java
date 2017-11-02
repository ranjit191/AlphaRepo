package com.textmaxx.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ModelTempMsges extends RealmObject {
    @PrimaryKey
    private String id;
    @Required
    private String message;

    public ModelTempMsges(String id, String message) {
        this.id = id;
        this.message = message;
    }

    public ModelTempMsges() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}