package com.mahmood.journeyjournal.models;

import java.util.ArrayList;
import java.util.Date;

public class Trip {

    private String _name;
    private Date _startDate;
    private Date _endDate;
    private ArrayList<TripPhoto> _tripPhotos;
    private String _notes;
    private ArrayList<Person> _companions;

    public Trip(String name, Date startDate, Date endDate, ArrayList<TripPhoto> tripPhotos, String notes ,  ArrayList<Person> companions){
        _name = name;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = tripPhotos;
        _notes = notes;
        _companions = companions;

    }

    public Trip (String name, Date startDate, Date endDate, ArrayList<TripPhoto> tripPhotos, String notes){
        _name = name;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = tripPhotos;
        _notes = notes;
        _companions = new ArrayList<>();

    }

    public Trip(String name, Date startDate, Date endDate, String notes, ArrayList<Person> companions){
        _name = name;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = new ArrayList<>();
        _notes = notes;
        _companions = companions;

    }

    public Trip(String name, Date startDate, Date endDate, String notes){
        _name = name;
        _startDate = startDate;
        _endDate = endDate;
        _tripPhotos = new ArrayList<>();
        _notes = notes;
        _companions = new ArrayList<>();

    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public Date getStartDate() {
        return _startDate;
    }

    public void setStartDate(Date startDate) {
        _startDate = _startDate;
    }

    public Date getEndDate() {
        return _endDate;
    }

    public void setEndDate(Date endDate) {
        _endDate = _endDate;
    }

    public String getNotes() {
        return _notes;
    }

    public void setNotes(String notes) {
        _notes = _notes;
    }

    public ArrayList<Person> getCompanions() {
        return _companions;
    }

    public void addCompainion(Person person){
        _companions.add(person);
    }

    public void addCompanions(ArrayList<Person> people){
        _companions.addAll(people);
    }

    public void removeCompainion(Person person){
        _companions.remove(person);
    }

    public void removeCompanions(ArrayList<Person> people){
        _companions.removeAll(people);
    }

    public void removeAllCompanions(){
        _companions = new ArrayList<Person>();
    }

    public ArrayList<TripPhoto> getTripPhotos() {
        return _tripPhotos;
    }

    public void addTripPhoto(TripPhoto tripPhoto){
        _tripPhotos.add(tripPhoto);
    }

    public void addTripPhotos(ArrayList<TripPhoto> tripPhotos){
        _tripPhotos.addAll(tripPhotos);
    }

    public void removeTripPhoto(TripPhoto tripPhoto){
        _tripPhotos.remove(tripPhoto);
    }

    public void removeTripPhotos(ArrayList<TripPhoto> tripPhotos){
        _tripPhotos.removeAll(tripPhotos);
    }

    public void removeAllTripPhotos(){
        _tripPhotos = new ArrayList<>();
    }
}
