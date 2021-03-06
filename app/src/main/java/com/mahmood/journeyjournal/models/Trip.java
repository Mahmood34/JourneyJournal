package com.mahmood.journeyjournal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Trip implements Parcelable {

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    private UUID _id;
    private String _title;
    private Date _startDate;
    private Date _endDate;
    private ArrayList<String> _tripPhotoIds = new ArrayList<>();
    private String _notes;
    private ArrayList<Person> _companions = new ArrayList<>();
    private ArrayList<String> _visitedPlaceIds = new ArrayList<>();

    public Trip() {

    }

    public Trip(String title, Date startDate, Date endDate, ArrayList<String> tripPhotos, String notes, ArrayList<Person> companions) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotoIds = tripPhotos;
        _notes = notes;
        _companions = companions;
    }

    public Trip(String title, Date startDate, Date endDate, ArrayList<String> tripPhotos, String notes) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotoIds = tripPhotos;
        _notes = notes;
    }

    public Trip(String title, Date startDate, Date endDate, String notes, ArrayList<Person> companions) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _notes = notes;
        _companions = companions;
    }

    public Trip(String title, Date startDate, Date endDate, String notes) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _notes = notes;
    }

    protected Trip(Parcel in) {
        _id = UUID.fromString(in.readString());
        _title = in.readString();
        _startDate = (Date) in.readValue(Date.class.getClassLoader());
        _endDate = (Date) in.readValue(Date.class.getClassLoader());
        _tripPhotoIds = in.readArrayList(TripPhoto.class.getClassLoader());
        _notes = in.readString();
        _companions = in.readArrayList(Person.class.getClassLoader());
        _visitedPlaceIds = in.readArrayList(VisitedPlace.class.getClassLoader());
    }

    public String getId() {
        return _id.toString();
    }

    public void setId(String id) {
        this._id = UUID.fromString(id);
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public long getStartDate() {
        return _startDate.getTime();
    }

    public void setStartDate(long startDate) {
        _startDate = new Date(startDate);
    }

    public long getEndDate() {
        return _endDate.getTime();
    }

    public void setEndDate(long endDate) {
        _endDate = new Date(endDate);
    }

    public String getNotes() {
        return _notes;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public Person getCompanion(int position) {
        return _companions.get(position);
    }

    public ArrayList<Person> getCompanions() {
        return _companions;
    }

    public void setCompanions(ArrayList<Person> people) {
        _companions = people;
    }

    public void addCompanion(Person person) {
        _companions.add(person);
    }

    public void addCompanions(ArrayList<Person> people) {
        _companions.addAll(people);
    }

    public void removeCompanion(Person person) {
        _companions.remove(person);
    }

    public void removeCompanions(ArrayList<Person> people) {
        _companions.removeAll(people);
    }

    public void removeAllCompanions() {
        _companions = new ArrayList<>();
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

    public void addTripPhotoId(String tripPhotoId) {
        _tripPhotoIds.add(tripPhotoId);
    }

    public void addTripPhotos(ArrayList<String> tripPhotoIds) {
        _tripPhotoIds.addAll(tripPhotoIds);
    }

    public void removeTripPhotoId(String tripPhotoId) {
        _tripPhotoIds.remove(tripPhotoId);
    }

    public void removeTripPhotoIds(ArrayList<String> tripPhotoIds) {
        _tripPhotoIds.removeAll(tripPhotoIds);
    }

    public void removeAllTripPhotoIds() {
        _tripPhotoIds = new ArrayList<>();
    }

    public String getVisitedPlaceId(int position) {
        return _visitedPlaceIds.get(position);
    }

    public ArrayList<String> getVisitedPlaceIds() {
        return _visitedPlaceIds;
    }

    public void setVisitedPlaceIds(ArrayList<String> _visitedPlaces) {
        this._visitedPlaceIds = _visitedPlaces;
    }

    public void addVisitedPlaceId(String visitedPlaceId) {
        _visitedPlaceIds.add(visitedPlaceId);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id.toString());
        dest.writeString(_title);
        dest.writeValue(_startDate);
        dest.writeValue(_endDate);
        dest.writeList(_tripPhotoIds);
        dest.writeString(_notes);
        dest.writeList(_companions);
        dest.writeList(_visitedPlaceIds);
    }

    public void removeVisitedPlaceId(String id) {
        _visitedPlaceIds.remove(id);
    }
}
