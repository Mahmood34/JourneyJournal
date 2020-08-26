package com.mahmood.journeyjournal;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.util.ArrayList;

public class DatabaseConstant {

    public static final String TRIP_REPOSITORY = "Trips";
    public static final String PHOTO_REPOSITORY = "Photos";
    public static final String VISITED_PLACE_REPOSITORY = "VisitedPlaces";
    public static DatabaseConstant INSTANCE = new DatabaseConstant();
    private String _userId;
    private ArrayList<Trip> _trips = new ArrayList<>();
    private ArrayList<TripPhoto> _tripPhotos = new ArrayList<>();
    private ArrayList<VisitedPlace> _visitedPlaces = new ArrayList<>();

    public static synchronized DatabaseConstant getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseConstant();
        }
        return (INSTANCE);
    }

    private void loadTrips() {
        getDatabaseRef().child(TRIP_REPOSITORY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _trips = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Trip trip = ds.getValue(Trip.class);
                    _trips.add(trip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void loadTripPhotos() {

        getDatabaseRef().child(PHOTO_REPOSITORY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _tripPhotos = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TripPhoto tripPhoto = ds.getValue(TripPhoto.class);
                    _tripPhotos.add(tripPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadVisitedPlaces() {
        getDatabaseRef().child(VISITED_PLACE_REPOSITORY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _visitedPlaces = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    VisitedPlace visitedPlace = ds.getValue(VisitedPlace.class);
                    _visitedPlaces.add(visitedPlace);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setUserId(String userId) {
        _userId = userId;
        loadTrips();
        loadTripPhotos();
        loadVisitedPlaces();
    }

    public String getUserAuth() {
        return _userId;
    }

    public StorageReference getStorageRef() {
        return FirebaseStorage.getInstance().getReference();
    }

    public DatabaseReference getDatabaseRef() {
        return FirebaseDatabase.getInstance().getReference().child(_userId);
    }

    public FirebaseAuth getAuthentication() {
        return FirebaseAuth.getInstance();
    }

    public ArrayList<Trip> getTrips() {
        return _trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        _trips = trips;
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(trips);
    }

    public ArrayList<TripPhoto> getTripPhotos() {
        return _tripPhotos;
    }

    public void setTripPhotos(ArrayList<TripPhoto> tripPhotos) {
        _tripPhotos = tripPhotos;
        getDatabaseRef().child(PHOTO_REPOSITORY).setValue(tripPhotos);
    }

    public ArrayList<VisitedPlace> getVisitedPlaces() {
        return _visitedPlaces;
    }

    public void setVisitedPlaces(ArrayList<VisitedPlace> visitedPlaces) {
        _visitedPlaces = visitedPlaces;
        getDatabaseRef().child(VISITED_PLACE_REPOSITORY).setValue(visitedPlaces);
    }

    public void addTrip(Trip trip) {
        for (Trip t : _trips) {
            if (t.getId().equals(trip.getId())) {
                return;
            }
        }
        _trips.add(trip);
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(_trips);
    }

    public void addTripPhoto(TripPhoto tripPhoto) {
        if (_tripPhotos.contains(tripPhoto)) {
            return;
        }
        for (Trip trip : _trips) {
            if (tripPhoto.getTripId().equals(trip.getId())) {
                trip.addTripPhotoId(tripPhoto.getImageUrl());
            }
        }
        if (tripPhoto.getVisitedPlace() != null) {
            for (VisitedPlace visitedPlace : _visitedPlaces) {
                if (tripPhoto.getVisitedPlace().equals(visitedPlace.getId())) {
                    visitedPlace.addTripPhotoId(tripPhoto.getImageUrl());
                }
            }
            getDatabaseRef().child(VISITED_PLACE_REPOSITORY).setValue(_visitedPlaces);
        }
        _tripPhotos.add(tripPhoto);
        getDatabaseRef().child(PHOTO_REPOSITORY).setValue(_tripPhotos);
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(_trips);
    }

    public void addVisitedPlace(VisitedPlace visitedPlace) {
        if (_visitedPlaces.contains(visitedPlace)) {
            return;
        }
        for (VisitedPlace vp : _visitedPlaces) {
            if (vp.getId().equals(visitedPlace.getId())) {
                return;
            }
        }
        for (Trip trip : _trips) {
            if (visitedPlace.getTripId().equals(trip.getId())) {
                trip.addVisitedPlaceId(visitedPlace.getId());
            }
        }
        _visitedPlaces.add(visitedPlace);
        getDatabaseRef().child(VISITED_PLACE_REPOSITORY).setValue(_visitedPlaces);
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(_trips);
    }

    public void removeTrip(Trip trip) {
        for (TripPhoto tripPhoto : _tripPhotos) {
            if (tripPhoto.getTripId().equals(trip.getId())) {
                _tripPhotos.remove(tripPhoto);
            }
            _tripPhotos.removeIf(tp -> tp.getTripId().equals(trip.getId()));
        }
        for (VisitedPlace visitedPlace : _visitedPlaces) {
            if (visitedPlace.getTripId().equals(trip.getId())) {
                _visitedPlaces.remove(visitedPlace);
            }
            _visitedPlaces.removeIf(vp -> vp.getTripId().equals(trip.getId()));
        }
        _trips.removeIf(t -> t.getId().equals(trip.getId()));
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(_trips);
        getDatabaseRef().child(VISITED_PLACE_REPOSITORY).setValue(_visitedPlaces);
        getDatabaseRef().child(PHOTO_REPOSITORY).setValue(_visitedPlaces);
    }

    public void removeTripPhoto(String tripPhotoId) {
        TripPhoto tripPhoto = new TripPhoto();
        for (TripPhoto tp : _tripPhotos) {
            if (tp.getImageUrl().equals(tripPhotoId)) {
                tripPhoto = tp;
            }
        }

        for (Trip trip : _trips) {
            if (tripPhoto.getTripId().equals(trip.getId())) {
                trip.removeTripPhotoId(tripPhoto.getImageUrl());
            }
        }
        if (tripPhoto.getVisitedPlace() != null) {
            for (VisitedPlace visitedPlace : _visitedPlaces) {
                if (tripPhoto.getVisitedPlace().equals(visitedPlace.getLocation())) {
                    visitedPlace.removeTripPhotoId(tripPhoto.getImageUrl());
                }
            }
            getDatabaseRef().child(VISITED_PLACE_REPOSITORY).setValue(_visitedPlaces);
        }
        _tripPhotos.remove(tripPhoto);
        getDatabaseRef().child(PHOTO_REPOSITORY).setValue(_tripPhotos);
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(_trips);
    }

    public void removeVisitedPlace(VisitedPlace visitedPlace) {
        for (Trip trip : _trips) {
            if (visitedPlace.getTripId().equals(trip.getId())) {
                trip.removeVisitedPlaceId(visitedPlace.getId());
            }
        }
        for (TripPhoto tripPhoto : _tripPhotos) {
            if (tripPhoto.getVisitedPlace() != null && tripPhoto.getVisitedPlace().equals(visitedPlace.getLocation())) {
                tripPhoto.setVisitedPlace(null);
            }
        }
        _visitedPlaces.removeIf(vp -> vp.getId().equals(visitedPlace.getId()));
        getDatabaseRef().child(VISITED_PLACE_REPOSITORY).setValue(_visitedPlaces);
        getDatabaseRef().child(TRIP_REPOSITORY).setValue(_trips);
        getDatabaseRef().child(PHOTO_REPOSITORY).setValue(_tripPhotos);
    }

    public boolean tripHasVisitedPlace(String visitedPlaceId) {
        for (Trip trip : _trips) {
            if (trip.getVisitedPlaceIds().contains(visitedPlaceId)) {
                return true;
            }

        }
        return false;
    }

    public boolean tripHasTripPhoto(String tripPhotoId) {
        for (Trip trip : _trips) {
            if (trip.getTripPhotoIds().contains(tripPhotoId)) {
                return true;
            }

        }
        return false;
    }

    public boolean visitedPlaceHasTripPhoto(String tripPhotoId) {
        for (VisitedPlace visitedPlace : _visitedPlaces) {
            if (visitedPlace.getTripPhotoIds().contains(tripPhotoId)) {
                return true;
            }
        }
        return false;
    }
}
