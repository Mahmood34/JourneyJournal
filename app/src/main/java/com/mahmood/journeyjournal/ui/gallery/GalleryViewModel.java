package com.mahmood.journeyjournal.ui.gallery;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class GalleryViewModel extends ViewModel {


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

    public LiveData<ArrayList<TripPhoto>> getAllTripPhotos() {
        if (_trips == null) {
            _trips = new MutableLiveData<>();
            loadTrips();
        }

        ArrayList<TripPhoto> tripPhotos = new ArrayList<>();
        for (Trip trip :
                _trips.getValue()) {
            tripPhotos.addAll(trip.getTripPhotos());
        }
        return new MutableLiveData<>(tripPhotos);
    }

    /**
     * Loads list of trips.
     */
    private void loadTrips() {

        ArrayList<Trip> trips = new ArrayList<>();
        Date today = Calendar.getInstance().getTime();

//        trips.add(new Trip("Barcelona", today, today, "A Week in Barcelona"));
//        trips.add(new Trip("London", today, today, "With family"));
//        trips.add(new Trip("Madrid", today, today, "Adventure"));
//        trips.add(new Trip("Paris", today, today, "Date night with the wife"));


        _trips = new MutableLiveData<>(trips);

    }
}