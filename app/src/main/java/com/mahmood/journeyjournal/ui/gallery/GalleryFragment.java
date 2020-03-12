package com.mahmood.journeyjournal.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.activities.TripDetailsActivity;
import com.mahmood.journeyjournal.adapters.GalleryAdapter;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private GalleryViewModel _galleryViewModel;
    private RecyclerView _recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerViewClickListener recyclerViewListener = (view, position) -> recyclerViewClick(view, position);
        _recyclerView = root.findViewById(R.id.trip_details_recycler_view_gallery);
        _galleryViewModel.getAllTripPhotos().observe(this, new Observer<ArrayList<TripPhoto>>() {
            @Override
            public void onChanged(ArrayList<TripPhoto> trips) {
                GalleryAdapter tripAdapter = new GalleryAdapter(trips, recyclerViewListener);
                _recyclerView.setAdapter(tripAdapter);
            }
        });
//        _recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        return root;
    }

    private void recyclerViewClick(View view, int position) {
        Intent intent = new Intent(getActivity(), TripDetailsActivity.class);
        intent.putExtra("trip", _galleryViewModel.getTrip(position));
        startActivityForResult(intent, 1);
    }
}