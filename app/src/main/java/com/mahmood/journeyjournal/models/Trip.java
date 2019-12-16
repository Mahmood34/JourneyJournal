package com.mahmood.journeyjournal.models;

import java.util.ArrayList;
import java.util.Date;

public class Trip {

    private String _name;
    private Date _startDate;
    private Date _endDate;
    private String _notes;
    private ArrayList<Person> _companions;
    private String _image;

    public Trip(String name, Date startDate, Date endDate, String image, String notes ,ArrayList<Person> companions){
        _name = name;
        _startDate = startDate;
        _endDate = endDate;
        _image = image;
        _notes = notes;
        _companions = companions;

    }

    public Trip(String name, Date startDate, Date endDate, String image, String notes){
        _name = name;
        _startDate = startDate;
        _endDate = endDate;
        _image = image;
        _notes = notes;
        _companions = new ArrayList<Person>();

    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public Date get_startDate() {
        return _startDate;
    }

    public void set_startDate(Date _startDate) {
        this._startDate = _startDate;
    }

    public Date get_endDate() {
        return _endDate;
    }

    public void set_endDate(Date _endDate) {
        this._endDate = _endDate;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }

    public ArrayList<Person> get_companions() {
        return _companions;
    }

    public void set_companions(ArrayList<Person> _companions) {
        this._companions = _companions;
    }

    public String get_image() {
        return _image;
    }

    public void set_image(String _image) {
        this._image = _image;
    }
}
