package com.mahmood.journeyjournal.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TripPhoto implements Parcelable {

    private String _name;
    private String _image;

    public TripPhoto(String name, String image) {
        _name = name;
        _image = image;

    }

    private TripPhoto(Parcel in) {
        _name = in.readString();
        _image = in.readString();
    }

    public static final Creator<TripPhoto> CREATOR = new Creator<TripPhoto>() {
        @Override
        public TripPhoto createFromParcel(Parcel in) {
            return new TripPhoto(in);
        }

        @Override
        public TripPhoto[] newArray(int size) {
            return new TripPhoto[size];
        }
    };

    public String getName() {
        return _name;
    }

    public String getImage() {
        return _image;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_name);
        dest.writeString(_image);
    }
}
