package com.android.yinwear.core.db.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class UserDetail implements Parcelable {

    protected UserDetail(Parcel in) {
        id = in.readInt();
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        inactive = in.readByte() != 0;
        pin = in.readString();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    private String userId;

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    private String firstName;

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    private String lastName;

    @SerializedName("inactive")
    @ColumnInfo(name = "inactive")
    private boolean inactive;

    @SerializedName("pin")
    @ColumnInfo(name = "pin")
    private String pin;

    public UserDetail() {
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isInactive() {
        return inactive;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeByte((byte) (inactive ? 1 : 0));
        dest.writeString(pin);
    }
}
