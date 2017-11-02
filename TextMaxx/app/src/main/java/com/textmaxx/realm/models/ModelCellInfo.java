package com.textmaxx.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ModelCellInfo extends RealmObject {
    @Required
    private String cellNo;
    private String name;
    private String acceptsAccount;
    private String acceptMarket;
    private String Language;
    private String tags;



    public ModelCellInfo(String cellNo, String name, String acceptsAccount, String acceptMarket, String Language, String tags) {
        this.cellNo = cellNo;
        this.name = name;
        this.acceptsAccount = acceptsAccount;

        this.acceptMarket = acceptMarket;
        this.Language = Language;
        this.tags = tags;
    }

    public ModelCellInfo() {

    }

    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcceptsAccount() {
        return acceptsAccount;
    }

    public void setAcceptsAccount(String acceptsAccount) {
        this.acceptsAccount = acceptsAccount;
    }

    public String getAcceptMarket() {
        return acceptMarket;
    }

    public void setAcceptMarket(String acceptMarket) {
        this.acceptMarket = acceptMarket;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String Language) {
        this.Language = Language;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


}