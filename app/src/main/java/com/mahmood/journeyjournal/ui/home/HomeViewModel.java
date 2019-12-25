package com.mahmood.journeyjournal.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mahmood.journeyjournal.models.Trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Trip>> _trips;

    public Trip getTrip(UUID id) {
        if (_trips == null) {
            _trips = new MutableLiveData<>();
            loadTrips();
        }
        for (Trip trip : _trips.getValue()) {
            if (trip.getId().equals(id)) {
                return trip;
            }
        }
        return null;
    }

    public Trip getTrip(int position) {
        if (_trips == null) {
            _trips = new MutableLiveData<>();
            loadTrips();
        }
        return _trips.getValue().get(position);
    }


    /**
     * Retrieves list of trips.
     *
     * @return list of trips.
     */
    public LiveData<ArrayList<Trip>> getTrips() {
        if (_trips == null) {
            _trips = new MutableLiveData<>();
            loadTrips();
        }
        return _trips;
    }

    /**
     * Set a new list of trips.
     *
     * @param trips
     */
    public void setTrips(ArrayList<Trip> trips) {
        _trips = new MutableLiveData<>(trips);
    }

    /**
     * Add a trip to the list.
     *
     * @param trip
     */
    public void addTrip(@NonNull Trip trip) {
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

        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));
        trips.add(new Trip("Barcelona", today, today, "This is a note"));
        trips.add(new Trip("London", today, today, "This is a note"));

        _trips = new MutableLiveData<>(trips);

    }
}