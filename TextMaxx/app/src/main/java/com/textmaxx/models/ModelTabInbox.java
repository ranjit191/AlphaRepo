package com.textmaxx.models;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class ModelTabInbox extends RealmObject {
    private String time;
    private String name;
    private String message;
    private String count;
    //    @PrimaryKey


    public ModelTabInbox(String name, String message, String count, String time) {
        this.name = name;
        this.message = message;
        this.count = count;

        this.time = time;
    }

    public ModelTabInbox() {

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}