package com.android.yinwear.core.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DeviceResp implements Parcelable {
    @SerializedName("message")
    private String message;
    @SerializedName("success")
    private boolean success;
    @SerializedName("devices")
    private ArrayList<DeviceRespObj> deviceList;

    public DeviceResp(String message, boolean success, ArrayList<DeviceRespObj> deviceList) {
        this.message = message;
        this.success = success;
        this.deviceList = deviceList;
    }

    public ArrayList<DeviceRespObj> getDeviceList() {
        return deviceList;
    }

    protected DeviceResp(Parcel in) {
        message = in.readString();
        success = in.readByte() != 0;
        deviceList = in.createTypedArrayList(DeviceRespObj.CREATOR);
    }

    public static final Creator<DeviceResp> CREATOR = new Creator<DeviceResp>() {
        @Override
        public DeviceResp createFromParcel(Parcel in) {
            return new DeviceResp(in);
        }

        @Override
        public DeviceResp[] newArray(int size) {
            return new DeviceResp[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeTypedList(deviceList);
    }
}
