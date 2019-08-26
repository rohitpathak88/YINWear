package com.android.yinwear.core.network.model.response;

import com.google.gson.annotations.SerializedName;

public class PairingStatusCheckResp {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("pairing_status")
    private String pairingStatus;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getPairingStatus() {
        return pairingStatus;
    }

}
