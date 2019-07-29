package com.android.yinwear.core.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class DeviceDetail implements Parcelable {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("device_id")
    @ColumnInfo(name = "device_id")
    private String deviceId;

    @SerializedName("device_type")
    @ColumnInfo(name = "device_type")
    private String deviceType;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("service_provider")
    @ColumnInfo(name = "service_provider")
    private String service_provider;

    public DeviceDetail() {
    }

    public DeviceDetail(String deviceId, String deviceType, String name, String service_provider) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.name = name;
        this.service_provider = service_provider;
    }

    private DeviceDetail(Parcel in) {
        id = in.readInt();
        deviceId = in.readString();
        deviceType = in.readString();
        name = in.readString();
        service_provider = in.readString();
    }

    public static final Creator<DeviceDetail> CREATOR = new Creator<DeviceDetail>() {
        @Override
        public DeviceDetail createFromParcel(Parcel in) {
            return new DeviceDetail(in);
        }

        @Override
        public DeviceDetail[] newArray(int size) {
            return new DeviceDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(deviceId);
        dest.writeString(deviceType);
        dest.writeString(name);
        dest.writeString(service_provider);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService_provider() {
        return service_provider;
    }

    public void setService_provider(String service_provider) {
        this.service_provider = service_provider;
    }
}