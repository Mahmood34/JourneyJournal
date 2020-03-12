package com.mahmood.journeyjournal.models;

import java.util.ArrayList;
import java.util.Date;

public class VisitedPlace {
    private String name;
    private Date dateVisited;
    private String location;
    private String travellersNotes;
    private ArrayList<TripPhoto> _tripPhotos = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateVisited() {
        return dateVisited;
    }

    public void setDateVisited(Date dateVisited) {
        this.dateVisited = dateVisited;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTravellersNotes() {
        return travellersNotes;
    }

    public void setTravellersNotes(String travellersNotes) {
        this.travellersNotes = travellersNotes;
    }

    public ArrayList<TripPhoto> get_tripPhotos() {
        return _tripPhotos;
    }

    public void set_tripPhotos(ArrayList<TripPhoto> _tripPhotos) {
        this._tripPhotos = _tripPhotos;
    }


}
