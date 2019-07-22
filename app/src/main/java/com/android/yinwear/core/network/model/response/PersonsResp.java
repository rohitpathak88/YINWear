package com.android.yinwear.core.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.yinwear.core.db.entity.PersonDetail;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PersonsResp implements Parcelable {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public PersonsResp(String message, boolean success, ArrayList<PersonDetail> personList) {
        this.message = message;
        this.success = success;
        this.personList = personList;
    }

    protected PersonsResp(Parcel in) {
        message = in.readString();
        success = in.readByte() != 0;
        personList = in.createTypedArrayList(PersonDetail.CREATOR);
    }

    public static final Creator<PersonsResp> CREATOR = new Creator<PersonsResp>() {
        @Override
        public PersonsResp createFromParcel(Parcel in) {
            return new PersonsResp(in);
        }

        @Override
        public PersonsResp[] newArray(int size) {
            return new PersonsResp[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(personList);
    }
}
