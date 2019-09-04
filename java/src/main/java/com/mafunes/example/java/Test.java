package com.mafunes.example.java;

import android.os.Parcel;
import android.os.Parcelable;

public class Test implements Parcelable {

    public String aString;
    public OtherTest aTest;

    public Test() {

    }

    protected Test(Parcel in) {
        aString = in.readString();
        aTest = in.readParcelable(OtherTest.class.getClassLoader());
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(aString);
        parcel.writeParcelable(aTest, i);
    }
}
