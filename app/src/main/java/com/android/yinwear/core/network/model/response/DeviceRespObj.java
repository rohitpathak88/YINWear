package com.android.yinwear.core.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity
public class DeviceRespObj implements Parcelable {

    public DeviceRespObj() {
    }

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("name")
    private String name;

    @SerializedName("user_ids")
    private ArrayList<String> userIds;

    @SerializedName("serviceProvider")
    private String serviceProvider;

    protected DeviceRespObj(Parcel in) {
        deviceId = in.readString();
        deviceType = in.readString();
        name = in.readString();
        userIds = in.createStringArrayList();
        serviceProvider = in.readString();
    }

    public static final Creator<DeviceRespObj> CREATOR = new Creator<DeviceRespObj>() {
        @Override
        public DeviceRespObj createFromParcel(Parcel in) {
            return new DeviceRespObj(in);
        }

        @Override
        public DeviceRespObj[] newArray(int size) {
            return new DeviceRespObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceId);
        dest.writeString(deviceType);
        dest.writeString(name);
        dest.writeStringList(userIds);
        dest.writeString(serviceProvider);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserIds(ArrayList<String> userIds) {
        this.userIds = userIds;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
