package com.mahmood.journeyjournal.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.models.Trip;

import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Trip>> _trips;

    public Trip getTrip(String id) {
        if (_trips == null) {
            return null;
        }
        for (Trip trip : Objects.requireNonNull(_trips.getValue())) {
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
        return Objects.requireNonNull(_trips.getValue()).get(position);
    }

    public LiveData<ArrayList<Trip>> getTrips() {
        if (_trips == null) {
            loadTrips();
        }
        return _trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        _trips.setValue(trips);
    }

    public void addTrip(@NonNull Trip trip) {
        ArrayList<Trip> trips = _trips.getValue();
        Objects.requireNonNull(trips).add(0, trip);
        setTrips(trips);
    }

    private void loadTrips() {
        _trips = new MutableLiveData<>();
        ValueEventListener tripListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Trip> trips = new ArrayList<>();
                for (DataSnapshot ds :
                        dataSnapshot.child(DatabaseConstant.TRIP_REPOSITORY)
                                .getChildren()) {
                    Trip trip = ds.getValue(Trip.class);
                    trips.add(trip);
                }
                setTrips(trips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        DatabaseConstant.getInstance().getDatabaseRef().addValueEventListener(tripListener);
    }
}