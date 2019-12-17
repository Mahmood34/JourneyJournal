package com.mahmood.journeyjournal.models;

import java.util.UUID;

public class Person {
    private UUID _id;
    private String _name;

    public Person(String name){
        _id = UUID.randomUUID();
        _name = name;

    }

    public UUID getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

}
