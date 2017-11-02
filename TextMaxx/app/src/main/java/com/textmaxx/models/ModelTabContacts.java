package com.textmaxx.models;

import io.realm.RealmObject;

public class ModelTabContacts extends RealmObject {

    private String title;
    private String varified;

    public ModelTabContacts(String title, String varified) {
        this.title = title;

        this.varified = varified;
    }

    public ModelTabContacts() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVarified() {
        return varified;
    }

    public void setVarified(String varified) {
        this.varified = varified;
    }
}