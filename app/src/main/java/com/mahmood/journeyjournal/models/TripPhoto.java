package com.mahmood.journeyjournal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TripPhoto implements Parcelable {

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

    private UUID _id;
    private String _name;
    private String _imageUrl;
    private String _visitedPlace;
    private String _tripId;
    private Date _dateAdded;

    public TripPhoto() {

    }

    public TripPhoto(String name, String image, String tripId) {
        _id = UUID.randomUUID();
        _name = name;
        _imageUrl = image;
        _tripId = tripId;
        _dateAdded = Calendar.getInstance().getTime();
    }

    public TripPhoto(String name, String image, String visitedPlace, String tripId) {
        _id = UUID.randomUUID();
        _name = name;
        _imageUrl = image;
        _visitedPlace = visitedPlace;
        _tripId = tripId;
        _dateAdded = Calendar.getInstance().getTime();
    }

    private TripPhoto(Parcel in) {
        _id = UUID.fromString(in.readString());
        _name = in.readString();
        _imageUrl = in.readString();
        _visitedPlace = in.readString();
        _tripId = in.readString();
        _dateAdded = (Date) in.readValue(Date.class.getClassLoader());
    }

    public String getId() {
        return _id.toString();
    }

    public void setId(String id) {
        this._id = UUID.fromString(id);
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getImageUrl() {
        return _imageUrl;
    }

    public void setImageUrl(String image) {
        _imageUrl = image;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id.toString());
        dest.writeString(_name);
        dest.writeString(_imageUrl);
        dest.writeString(_visitedPlace);
        dest.writeString(_tripId);
        dest.writeValue(_dateAdded);
    }

    public String getVisitedPlace() {
        return _visitedPlace;
    }

    public void setVisitedPlace(String visitedPlace) {
        this._visitedPlace = visitedPlace;
    }

    public String getTripId() {
        return _tripId;
    }

    public void setTripId(String tripId) {
        _tripId = tripId;
    }

    public long getDateAdded() {
        return _dateAdded.getTime();
    }

    public void setDateAdded(long dateAdded) {
        _dateAdded = new Date(dateAdded);
    }
}
