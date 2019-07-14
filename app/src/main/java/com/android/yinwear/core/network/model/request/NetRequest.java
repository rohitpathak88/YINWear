package com.android.yinwear.core.network.model.request;

import com.google.gson.annotations.SerializedName;

public class NetRequest {
    @SerializedName("request_id")
    private int requestId;
    @SerializedName("request_type")
    private int requestType;
    @SerializedName("request_url")
    private String requestUrl;
    @SerializedName("request_param")
    private Object requestParam;

    public NetRequest(int requestId, int requestType, String requestUrl, Object requestParam) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.requestUrl = requestUrl;
        this.requestParam = requestParam;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getRequestType() {
        return requestType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public Object getRequestParam() {
        return requestParam;
    }
}
