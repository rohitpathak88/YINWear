package com.android.yinwear.core.network.model.response;

import com.google.gson.annotations.SerializedName;

public class LoginResp {
    @SerializedName("authentication_token")
    private String authToken;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("yin_account_id")
    private String yin_account_id;

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getYin_account_id() {
        return yin_account_id;
    }
}
