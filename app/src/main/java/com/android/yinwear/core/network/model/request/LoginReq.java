package com.android.yinwear.core.network.model.request;

import com.google.gson.annotations.SerializedName;

public class LoginReq {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;

    public LoginReq(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
