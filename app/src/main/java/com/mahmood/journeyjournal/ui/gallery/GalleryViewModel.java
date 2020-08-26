package com.mahmood.journeyjournal.ui.gallery;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> _tripPhotoIds;
    private ArrayList<TripPhoto> _tripPhotos;
    private DatabaseReference _database;
    private ArrayList<String> filters;

    public void setFilters() {
        for (TripPhoto tripPhoto : _tripPhotos) {
            filters = new ArrayList<>();
            filters.add(tripPhoto.getVisitedPlace());
        }
    }

    public ArrayList<TripPhoto> getTripPhotos() {
        return _tripPhotos;
    }

    public void setTripPhotos(ArrayList<TripPhoto> tripPhotos) {
        _tripPhotos = tripPhotos;
    }


    public void search(String searchText) {


    }

//    public void sortFilter(String order, ArrayList<String> filters){
//        _order = order;
//
//    }

    public void setTripPhotoIds(ArrayList<String> trips) {
        _tripPhotoIds.setValue(trips);
    }


    public LiveData<ArrayList<String>> getAllTripPhotoIds() {
        if (_tripPhotoIds == null) {
            loadTrips();
        }

        return _tripPhotoIds;
    }

    private void loadTrips() {
        _tripPhotoIds = new MutableLiveData<>();
        _database = DatabaseConstant.getInstance().getDatabaseRef()
                .child(DatabaseConstant.PHOTO_REPOSITORY);

        Query query = _database.orderByChild("dateAdded");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> tripPhotoIds = new ArrayList<>();
                ArrayList<TripPhoto> tripPhotos = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TripPhoto tripPhoto = ds.getValue(TripPhoto.class);
                    tripPhotos.add(tripPhoto);
                    tripPhotoIds.add(Objects.requireNonNull(tripPhoto).getImageUrl());
                }

                Collections.reverse(tripPhotoIds);
                Collections.reverse(tripPhotos);


                setTripPhotoIds(tripPhotoIds);
                setTripPhotos(tripPhotos);
                setFilters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}