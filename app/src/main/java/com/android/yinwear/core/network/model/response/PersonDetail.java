package com.android.yinwear.core.network.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PersonDetail implements Parcelable {
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("inactive")
    private boolean inactive;
    @SerializedName("person_id")
    private String personId;
    @SerializedName("pin")
    private String pin;

    protected PersonDetail(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        inactive = in.readByte() != 0;
        personId = in.readString();
        pin = in.readString();
    }

    public static final Creator<PersonDetail> CREATOR = new Creator<PersonDetail>() {
        @Override
        public PersonDetail createFromParcel(Parcel in) {
            return new PersonDetail(in);
        }

        @Override
        public PersonDetail[] newArray(int size) {
            return new PersonDetail[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isInactive() {
        return inactive;
    }

    public String getPersonId() {
        return personId;
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
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeByte((byte) (inactive ? 1 : 0));
        dest.writeString(personId);
        dest.writeString(pin);
    }
}
