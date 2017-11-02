package com.textmaxx.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ModelFindContacts extends RealmObject {
    @Required
    private String title;
    private String varified;
    private String cellNo;

    public ModelFindContacts(String title, String varified, String cellNo) {
        this.title = title;
        this.varified = varified;
        this.cellNo = cellNo;
    }

    public ModelFindContacts() {

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


    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }
}