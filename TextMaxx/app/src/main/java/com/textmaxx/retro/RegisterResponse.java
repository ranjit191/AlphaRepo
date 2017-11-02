package com.textmaxx.retro;

import com.google.gson.annotations.SerializedName;

/**
 * Created by manbir on 10/19/2016.
 */

public class RegisterResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Object data;


}
