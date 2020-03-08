package com.mahmood.journeyjournal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Person implements Parcelable {
    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    private UUID _id;
    private String _name;

    public Person(){

    }

    public Person(String name) {
        _id = UUID.randomUUID();
        _name = name;

    }

    protected Person(Parcel in) {
        _id = UUID.fromString(in.readString());
        _name = in.readString();
    }

    public String getId() {
        return _id.toString();
    }

    public String getName() {
        return _name;
    }

    public void setId(String id){
        _id = UUID.fromString(id);
    }

    public void setName(String name) {
        _name = name;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id.toString());
        dest.writeString(_name);
    }
}
