package com.android.yinwear.core.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.yinwear.core.db.entity.UserDetail;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UsersResp implements Parcelable {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;

    public UsersResp(String message, boolean success, ArrayList<UserDetail> userList) {
        this.message = message;
        this.success = success;
        this.userList = userList;
    }

    protected UsersResp(Parcel in) {
        message = in.readString();
        success = in.readByte() != 0;
        userList = in.createTypedArrayList(UserDetail.CREATOR);
    }

    public static final Creator<UsersResp> CREATOR = new Creator<UsersResp>() {
        @Override
        public UsersResp createFromParcel(Parcel in) {
            return new UsersResp(in);
        }

        @Override
        public UsersResp[] newArray(int size) {
            return new UsersResp[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<UserDetail> getUserList() {
        return userList;
    }

    @SerializedName("users")
    private ArrayList<UserDetail> userList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(userList);
    }
}
