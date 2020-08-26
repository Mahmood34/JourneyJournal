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
    private Double _lat;
    private Double _lng;
    private String _travellersNotes;
    private String _tripId;
    private ArrayList<String> _tripPhotoIds = new ArrayList<>();

    public VisitedPlace() {
    }

    public VisitedPlace(String name, Date dateVisited, String location, Double lat, Double lng, String travellersNotes, String tripId) {
        _id = UUID.randomUUID();
        _name = name;
        _dateVisited = dateVisited;
        _location = location;
        _lat = lat;
        _lng = lng;
        _travellersNotes = travellersNotes;
        _tripId = tripId;
    }

    //region parcel
    protected VisitedPlace(Parcel in) {
        _id = UUID.fromString(in.readString());
        _name = in.readString();
        _dateVisited = (Date) in.readValue(Date.class.getClassLoader());
        _location = in.readString();
        _lat = in.readDouble();
        _lng = in.readDouble();
        _travellersNotes = in.readString();
        _tripId = in.readString();
        _tripPhotoIds = in.readArrayList(String.class.getClassLoader());
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
        dest.writeDouble(_lat);
        dest.writeDouble(_lng);
        dest.writeString(_travellersNotes);
        dest.writeString(_tripId);
        dest.writeList(_tripPhotoIds);
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

    public Double getLat() {
        return _lat;
    }

    public void setLat(Double _lat) {
        this._lat = _lat;
    }

    public Double getLng() {
        return _lng;
    }

    public void setLng(Double _lng) {
        this._lng = _lng;
    }

    public String getTravellersNotes() {
        return _travellersNotes;
    }

    public void setTravellersNotes(String travellersNotes) {
        this._travellersNotes = travellersNotes;
    }

    public String getTripId() {
        return _tripId;
    }

    public void setTripId(String tripId) {
        _tripId = tripId;
    }

    public String getTripPhotoId(int position) {
        return _tripPhotoIds.get(position);
    }

    public ArrayList<String> getTripPhotoIds() {
        return _tripPhotoIds;
    }

    public void setTripPhotoIds(ArrayList<String> photoIds) {
        _tripPhotoIds = photoIds;
    }
    //endregion

    public void addTripPhotoId(String tripPhotoId) {
        _tripPhotoIds.add(tripPhotoId);
    }

    public void addTripPhotoIds(ArrayList<String> tripPhotos) {
        _tripPhotoIds.addAll(tripPhotos);
    }

    public void removeTripPhotoId(String tripPhotoId) {
        _tripPhotoIds.remove(tripPhotoId);
    }

    public void removeTripPhotoIds(ArrayList<TripPhoto> tripPhotos) {
        _tripPhotoIds.removeAll(tripPhotos);
    }

    public void removeAllTripPhotoIds() {
        _tripPhotoIds = new ArrayList<>();
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String location) {
        this._location = location;
    }
}
