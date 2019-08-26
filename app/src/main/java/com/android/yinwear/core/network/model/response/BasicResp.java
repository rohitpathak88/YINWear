package com.android.yinwear.core.network.model.response;

import com.google.gson.annotations.SerializedName;

public class BasicResp {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
