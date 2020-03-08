package com.mahmood.journeyjournal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class TripPhoto implements Parcelable {

    private UUID _id;
    private String _name;
    private String _image;

    public  TripPhoto(){

    }

    public TripPhoto(String name, String image) {
        _id = UUID.randomUUID();
        _name = name;
        _image = image;
    }

    private TripPhoto(Parcel in) {
        _id = UUID.fromString(in.readString());
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

    public String getId() {
        return _id.toString();
    }

    public void setId(String id){ _id = UUID.fromString(id); }

    public String getName() {
        return _name;
    }

    public void setName(String name){ _name = name; }

    public String getImage() {
        return _image;
    }

    public void setImage(String image){ _image = image; }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id.toString());
        dest.writeString(_name);
        dest.writeString(_image);
    }
}
