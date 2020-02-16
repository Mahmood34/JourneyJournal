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
    private ArrayList<TripPhoto> _tripPhotos;
    private String _notes;
    private ArrayList<Person> _companions;

    public Trip(String title, Date startDate, Date endDate, ArrayList<TripPhoto> tripPhotos, String notes, ArrayList<Person> companions) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = tripPhotos;
        _notes = notes;
        _companions = companions;
    }

    public Trip(String title, Date startDate, Date endDate, ArrayList<TripPhoto> tripPhotos, String notes) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = tripPhotos;
        _notes = notes;
        _companions = new ArrayList<>();
    }

    public Trip(String title, Date startDate, Date endDate, String notes, ArrayList<Person> companions) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = new ArrayList<>();
        _notes = notes;
        _companions = companions;
    }

    public Trip(String title, Date startDate, Date endDate, String notes) {
        _id = UUID.randomUUID();
        _title = title;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = new ArrayList<>();
        _notes = notes;
        _companions = new ArrayList<>();
    }

    protected Trip(Parcel in) {
        _id = UUID.fromString(in.readString());
        _title = in.readString();
        _startDate = (Date) in.readValue(Date.class.getClassLoader());
        _endDate = (Date) in.readValue(Date.class.getClassLoader());
        _tripPhotos = in.readArrayList(TripPhoto.class.getClassLoader());
        _notes = in.readString();
        _companions = in.readArrayList(Person.class.getClassLoader());
    }

    public UUID getId() {
        return _id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public Date getStartDate() {
        return _startDate;
    }

    public void setStartDate(Date startDate) {
        _startDate = startDate;
    }

    public Date getEndDate() {
        return _endDate;
    }

    public void setEndDate(Date endDate) {
        _endDate = endDate;
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

    public void addCompainion(Person person) {
        _companions.add(person);
    }

    public void addCompanions(ArrayList<Person> people) {
        _companions.addAll(people);
    }

    public void removeCompainion(Person person) {
        _companions.remove(person);
    }

    public void removeCompanions(ArrayList<Person> people) {
        _companions.removeAll(people);
    }

    public void removeAllCompanions() {
        _companions = new ArrayList<>();
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
        dest.writeList(_tripPhotos);
        dest.writeString(_notes);
        dest.writeList(_companions);

    }
}
