package com.mafunes.example.java;

import android.os.Parcel;
import android.os.Parcelable;

class OtherTest implements Parcelable {
    public String aString;

    public OtherTest() {

    }

    protected OtherTest(Parcel in) {
        aString = in.readString();
    }

    public static final Creator<OtherTest> CREATOR = new Creator<OtherTest>() {
        @Override
        public OtherTest createFromParcel(Parcel in) {
            return new OtherTest(in);
        }

        @Override
        public OtherTest[] newArray(int size) {
            return new OtherTest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(aString);
    }
}
