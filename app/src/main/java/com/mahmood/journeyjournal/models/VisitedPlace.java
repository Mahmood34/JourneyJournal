package com.mahmood.journeyjournal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class VisitedPlace implements Parcelable {

    public static final Creator<VisitedPlace> CREATOR = new Creator<VisitedPlace>() {
        @Override
        public VisitedPlace createFromParcel(Parcel in) {
            return new VisitedPlace(in);
        }

        @Override
        public VisitedPlace[] newArray(int size) {
            return new VisitedPlace[size];
        }
    };
    private UUID _id;
    private String _name;
    private Date _dateVisited;
    private String _location;
    private String _travellersNotes;
    private ArrayList<TripPhoto> _tripPhotos = new ArrayList<>();

    public VisitedPlace() {
    }

    public VisitedPlace(String name, Date dateVisited, String location, String travellersNotes, ArrayList<TripPhoto> tripPhotos) {
        _id = UUID.randomUUID();
        _name = name;
        _dateVisited = dateVisited;
        _location = location;
        _travellersNotes = travellersNotes;
        _tripPhotos = tripPhotos;
    }

    //region parcel
    protected VisitedPlace(Parcel in) {
        _id = UUID.fromString(in.readString());
        _name = in.readString();
        _dateVisited = (Date) in.readValue(Date.class.getClassLoader());
        _location = in.readString();
        _travellersNotes = in.readString();
        _tripPhotos = in.createTypedArrayList(TripPhoto.CREATOR);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id.toString());
        dest.writeString(_name);
        dest.writeValue(_dateVisited);
        dest.writeString(_location);
        dest.writeString(_travellersNotes);
        dest.writeTypedList(_tripPhotos);
    }
    //endregion

    //region getterSetter
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
        this._name = name;
    }

    public long getDateVisited() {
        return _dateVisited.getTime();
    }

    public void setDateVisited(long dateVisited) {
        this._dateVisited = new Date(dateVisited);
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }

    public String getTravellersNotes() {
        return _travellersNotes;
    }

    public void setTravellersNotes(String travellersNotes) {
        this._travellersNotes = travellersNotes;
    }

    public TripPhoto getTripPhoto(int position) {
        return _tripPhotos.get(position);
    }

    public ArrayList<TripPhoto> getTripPhotos() {
        return _tripPhotos;
    }

    public void setTripPhotos(ArrayList<TripPhoto> photos) {
        _tripPhotos = photos;
    }

    //endregion

    public void addTripPhoto(TripPhoto tripPhoto) {
        _tripPhotos.add(tripPhoto);
    }

    public void addTripPhotos(ArrayList<TripPhoto> tripPhotos) {
        _tripPhotos.addAll(tripPhotos);
    }

    public void removeTripPhoto(TripPhoto tripPhoto) {
        _tripPhotos.remove(tripPhoto);
    }

    public void removeTripPhotos(ArrayList<TripPhoto> tripPhotos) {
        _tripPhotos.removeAll(tripPhotos);
    }

    public void removeAllTripPhotos() {
        _tripPhotos = new ArrayList<>();
    }


}
