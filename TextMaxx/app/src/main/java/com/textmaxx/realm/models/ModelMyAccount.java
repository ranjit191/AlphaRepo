package com.textmaxx.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ModelMyAccount extends RealmObject {
    @Required
    private String UserId;
    private String firstName;
    private String username;
    private String email;
    private String lastName;
    private String cellNo;

    public ModelMyAccount(String UserId, String firstName, String lastName, String username, String email, String cellNo) {
        this.UserId = UserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.cellNo = cellNo;
    }

    public ModelMyAccount() {

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
//


    public String getCellNo() {
        return cellNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }
    //

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
//

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}