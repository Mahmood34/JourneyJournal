package com.mahmood.journeyjournal.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mahmood.journeyjournal.models.Trip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Trip>> _trips;


    /**
     * Retrieves list of trips.
     * @return list of trips.
     */
    public LiveData<ArrayList<Trip>> getTrips() {
        if(_trips == null){
            _trips = new MutableLiveData<>();
            loadTrips();
        }
        return _trips;
    }

    /**
     * Set a new list of trips.
     * @param trips
     */
    public void setTrips(ArrayList<Trip> trips){
        _trips = new MutableLiveData<>(trips);
    }

    /**
     * Add a trip to the list.
     * @param trip
     */
    public void addTrip(@NonNull Trip trip){
        ArrayList<Trip> trips = _trips.getValue();
        trips.add(0, trip);
        _trips.setValue(trips);
    }


    /**
     * Loads list of trips.
     */
    private void loadTrips() {

        ArrayList<Trip> trips = new ArrayList<>();
        Date today = Calendar.getInstance().getTime();

        trips.add(new Trip("Barcelona", today, today, "" , "This is a note"));
        trips.add(new Trip("London", today, today, "" , "This is a note"));
        trips.add(new Trip("Brazil", today, today, "" , "This is a note"));
        trips.add(new Trip("New York", today, today, "" , "This is a note"));
        trips.add(new Trip("Madrid", today, today, "" , "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "" , "This is a note"));
        trips.add(new Trip("London", today, today, "" , "This is a note"));
        trips.add(new Trip("Brazil", today, today, "" , "This is a note"));
        trips.add(new Trip("New York", today, today, "" , "This is a note"));
        trips.add(new Trip("Madrid", today, today, "" , "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "" , "This is a note"));
        trips.add(new Trip("London", today, today, "" , "This is a note"));
        trips.add(new Trip("Brazil", today, today, "" , "This is a note"));
        trips.add(new Trip("New York", today, today, "" , "This is a note"));
        trips.add(new Trip("Madrid", today, today, "" , "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "" , "This is a note"));
        trips.add(new Trip("London", today, today, "" , "This is a note"));
        trips.add(new Trip("Brazil", today, today, "" , "This is a note"));
        trips.add(new Trip("New York", today, today, "" , "This is a note"));
        trips.add(new Trip("Madrid", today, today, "" , "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "" , "This is a note"));
        trips.add(new Trip("London", today, today, "" , "This is a note"));
        trips.add(new Trip("Brazil", today, today, "" , "This is a note"));
        trips.add(new Trip("New York", today, today, "" , "This is a note"));
        trips.add(new Trip("Madrid", today, today, "" , "This is a note"));
        _trips = new MutableLiveData<>(trips);

    }
}