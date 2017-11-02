package com.textmaxx.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class StaticModelChat extends RealmObject {

    @Required
    private String cellno;
    private String message;

    public String getCellno() {
        return cellno;
    }

    public void setCellno(final String cellno) {
        this.cellno = cellno;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }


}