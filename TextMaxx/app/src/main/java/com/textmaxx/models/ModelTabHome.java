package com.textmaxx.models;

import io.realm.RealmObject;

public class ModelTabHome extends RealmObject {
    private String name;
    private String message;
    private String sentTime;
    private String count;

    public ModelTabHome(String name, String message, String sentTime, String count) {
        this.name = name;
        this.message = message;
        this.count = count;
        this.sentTime = sentTime;
    }

    public ModelTabHome() {

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

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }


}