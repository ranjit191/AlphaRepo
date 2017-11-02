package com.textmaxx.models;

import io.realm.RealmObject;

public class ModelChat extends RealmObject {
    private String time;
    private String message;
    private String direction;
    private String comment;
    private String cellNO;
    private String msgSentTIme = "";

    public ModelChat(String time, String message, String direction, String comment, String cellNO, String msgSentTIme) {
        this.time = time;
        this.message = message;
        this.direction = direction;
        this.comment = comment;
        this.cellNO = cellNO;
        this.msgSentTIme = msgSentTIme;
    }

    public ModelChat() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String name) {
        this.time = time;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCellNO() {
        return cellNO;
    }

    public void setCellNO(String cellNO) {
        this.cellNO = cellNO;
    }

    public String getMsgSentTIme() {
        return msgSentTIme;
    }

    public void setMsgSentTIme(String msgSentTIme) {
        this.msgSentTIme = msgSentTIme;
    }

}