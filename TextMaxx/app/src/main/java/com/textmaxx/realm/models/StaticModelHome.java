package com.textmaxx.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@RealmClass
public class StaticModelHome extends RealmObject {

    @Required
    private String title;
    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }


}