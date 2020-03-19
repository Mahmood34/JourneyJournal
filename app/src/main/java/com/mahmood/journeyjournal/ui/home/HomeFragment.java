package com.mahmood.journeyjournal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.activities.TripDetailsActivity;
import com.mahmood.journeyjournal.adapters.TripAdapter;
import com.mahmood.journeyjournal.components.AddTripBottomSheetFragment;
import com.mahmood.journeyjournal.interfaces.AddTripClickListener;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Trip;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private HomeViewModel _homeViewModel;
    private RecyclerView _recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        AddTripClickListener addTripClickListener = (trip) -> addTripClick(trip);

        RecyclerViewClickListener recyclerViewListener = (view, position) -> recyclerViewClick(view, position);

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        _recyclerView = root.findViewById(R.id.recycler_view_home);

        _homeViewModel.getTrips().observe(this, trips -> {
            TripAdapter tripAdapter = new TripAdapter(trips, recyclerViewListener);
            _recyclerView.setAdapter(tripAdapter);
        });

        FloatingActionButton button = getActivity().findViewById(R.id.floating_action_button);
        button.show();
        button.setOnClickListener(v -> {
            final AddTripBottomSheetFragment sheetDialog = new AddTripBottomSheetFragment(addTripClickListener);
            sheetDialog.show(getFragmentManager(), sheetDialog.getTag());
        });
        _recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        return root;
    }

    private void addTripClick(Trip trip) {
        _homeViewModel.addTrip(trip);
    }

    private void recyclerViewClick(View view, int position) {
        Intent intent = new Intent(getActivity(), TripDetailsActivity.class);
        intent.putExtra("trip", _homeViewModel.getTrip(position));
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK) {
                Trip updatedTrip = data.getExtras().getParcelable("updatedTrip");
                Trip trip = _homeViewModel.getTrip(updatedTrip.getId());
                trip.setTitle(updatedTrip.getTitle());
                trip.setNotes(updatedTrip.getNotes());
                trip.setStartDate(updatedTrip.getStartDate());
                trip.setEndDate(updatedTrip.getEndDate());
                trip.setCompanions(updatedTrip.getCompanions());
                trip.setTripPhotos(updatedTrip.getTripPhotos());
                _recyclerView.getAdapter().notifyDataSetChanged();

            }
        } catch (Exception exception) {
            Toast.makeText(this.getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
