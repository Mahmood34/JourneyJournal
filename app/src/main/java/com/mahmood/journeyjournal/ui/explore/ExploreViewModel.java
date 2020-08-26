package com.mahmood.journeyjournal.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mahmood.journeyjournal.models.VisitedPlace;

import java.util.ArrayList;

public class ExploreViewModel extends ViewModel {

    private MutableLiveData<ArrayList<VisitedPlace>> _visitedPlaces;

    public ExploreViewModel() {
        _visitedPlaces = new MutableLiveData<>();
    }

    public LiveData<ArrayList<VisitedPlace>> getVisitedPlaces() {
        return _visitedPlaces;


    }
}
