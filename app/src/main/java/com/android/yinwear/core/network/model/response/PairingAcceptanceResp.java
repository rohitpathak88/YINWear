package com.android.yinwear.core.network.model.response;

import com.google.gson.annotations.SerializedName;

public class PairingAcceptanceResp {
    @SerializedName("confirmation_code")
    private String confirmationCode;
    @SerializedName("device_id")
    private String deviceId;
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
