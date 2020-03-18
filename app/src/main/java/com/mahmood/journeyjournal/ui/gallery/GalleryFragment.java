package com.mahmood.journeyjournal.ui.gallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahmood.journeyjournal.MainActivity;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.activities.TripDetailsActivity;
import com.mahmood.journeyjournal.adapters.GalleryAdapter;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class GalleryFragment extends Fragment {

    private GalleryViewModel _galleryViewModel;
    private RecyclerView _recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        _galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerViewClickListener recyclerViewListener = (view, position) -> recyclerViewClick(view, position);
        _recyclerView = root.findViewById(R.id.recycler_view_gallery);
        _recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        _galleryViewModel.getTrips().observe(this, new Observer<ArrayList<Trip>>() {
            @Override
            public void onChanged(ArrayList<Trip> trips) {
                ArrayList<TripPhoto> tripPhotos = _galleryViewModel.getAllTripPhotos();
                GalleryAdapter tripAdapter = new GalleryAdapter(tripPhotos, null);
                _recyclerView.setAdapter(tripAdapter);
            }
        });

        FloatingActionButton button = getActivity().findViewById(R.id.floating_action_button);
        button.hide();

        return root;
    }

    private void recyclerViewClick(View view, int position) {
        Intent intent = new Intent(getActivity(), TripDetailsActivity.class);
        intent.putExtra("trip", _galleryViewModel.getTrip(position));
        startActivityForResult(intent, 1);
    }
}