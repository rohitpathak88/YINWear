package com.android.yinwear.core.network.model.response;

import com.android.yinwear.core.network.model.request.NetRequest;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("response_code")
    private int responseCode;
    @SerializedName("request")
    private NetRequest request;
    @SerializedName("response")
    private Object response;

    public BaseResponse() {
    }

    public BaseResponse(int responseCode, NetRequest request, Object response) {
        this.responseCode = responseCode;
        this.request = request;
        this.response = response;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setRequest(NetRequest request) {
        this.request = request;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public NetRequest getRequest() {
        return request;
    }

    public Object getResponse() {
        return response;
    }
}
