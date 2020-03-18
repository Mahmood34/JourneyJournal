package com.mahmood.journeyjournal.ui.gallery;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Trip>> _trips;
    private DatabaseReference _database;

    public Trip getTrip(UUID id) {
        if (_trips == null) {
            return null;
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
            return null;
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
        _trips.setValue(trips);
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

    public ArrayList<TripPhoto> getAllTripPhotos() {
        if (_trips == null) {
            loadTrips();
        }
        ArrayList<Trip> trips = getTrips().getValue();
        ArrayList<TripPhoto> tripPhotos = new ArrayList<>();
        for (Trip trip :
                trips) {
            tripPhotos.addAll(trip.getTripPhotos());
        }
        return tripPhotos;
    }

    /**
     * Loads list of trips.
     */
    private void loadTrips() {
        _trips = new MutableLiveData<>();
        _database = FirebaseDatabase.getInstance().getReference();

        ValueEventListener tripListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Trip> trips = new ArrayList<>();
                for (DataSnapshot ds :
                        dataSnapshot.child(DatabaseConstant.TRIP_REPOSITORY).getChildren()) {
                    Trip trip = ds.getValue(Trip.class);
                    trips.add(trip);
                }
                setTrips(trips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        _database.addValueEventListener(tripListener);
    }
}