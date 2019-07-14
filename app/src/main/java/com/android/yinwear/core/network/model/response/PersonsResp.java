package com.android.yinwear.core.network.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PersonsResp {
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

    public ArrayList<PersonDetail> getPersonList() {
        return personList;
    }

    @SerializedName("persons")
    private ArrayList<PersonDetail> personList;
}
