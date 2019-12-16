package com.mahmood.journeyjournal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.adapters.TripAdapter;
import com.mahmood.journeyjournal.components.AddTripFragment;
import com.mahmood.journeyjournal.activities.TripDetailsActivity;
import com.mahmood.journeyjournal.interfaces.AddTripClickListener;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Trip;

import java.util.ArrayList;

public class HomeFragment extends Fragment{

    private HomeViewModel _homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        AddTripClickListener _addTripListener = (trip -> {
            _homeViewModel.addTrip(trip);
        });
        RecyclerViewClickListener recyclerViewListener = (view , position) -> {
            Intent intent= new Intent(getActivity(), TripDetailsActivity.class);
            startActivity(intent);
        };

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView _recyclerView = root.findViewById(R.id.recycler_view_home);

        _homeViewModel.getTrips().observe(this, new Observer<ArrayList<Trip>>() {
            @Override
            public void onChanged(ArrayList<Trip> trips) {
                TripAdapter tripAdapter = new TripAdapter(trips, recyclerViewListener);
                _recyclerView.setAdapter(tripAdapter);
            }
        });

        FloatingActionButton button = getActivity().findViewById(R.id.floating_action_button);
        button.setOnClickListener(v -> {
            final AddTripFragment sheetDialog = new AddTripFragment(_addTripListener);
            sheetDialog.show(getFragmentManager(), sheetDialog.getTag());
        });
        _recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));



        return root;
    }

}