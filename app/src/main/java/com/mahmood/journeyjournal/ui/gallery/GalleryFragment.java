package com.mahmood.journeyjournal.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.activities.TripPhotoDetailsActivity;
import com.mahmood.journeyjournal.adapters.GalleryAdapter;
import com.mahmood.journeyjournal.components.SortFilterBottomSheetFragment;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.interfaces.SortFilterListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class GalleryFragment extends Fragment {

    private RecyclerView _recyclerView;
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private ArrayList<String> tripPhotoIds;
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                int random = new Random().nextInt(tripPhotoIds.size());
                Intent intent = new Intent(getActivity(), TripPhotoDetailsActivity.class);
                intent.putExtra("photoId", tripPhotoIds.get(random));
                startActivity(intent);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private String currentOrder;
    private SortFilterListener sortFilterListener = this::sortFilterClick;

    @Override
    public void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void sortFilterClick(String order, ArrayList<String> filters) {

        if (!currentOrder.equals(order)) {
            Collections.reverse(tripPhotoIds);
            currentOrder = order;
        }

        Objects.requireNonNull(_recyclerView.getAdapter()).notifyDataSetChanged();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        currentOrder = "Newest";
        GalleryViewModel _galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerViewClickListener recyclerViewListener = this::recyclerViewClick;
        _recyclerView = root.findViewById(R.id.recycler_view_gallery);
        SearchView searchView = root.findViewById(R.id.gallery_search);
        searchView.setQueryHint("Search photos");
        Button _sortFilterButton = root.findViewById(R.id.floating_action_button);

        FloatingActionButton button;
        button = requireActivity().findViewById(R.id.floating_action_button);
        button.setImageResource(R.drawable.ic_filter_list_black_24dp);
        button.show();
        button.setOnClickListener(v -> {

            SortFilterBottomSheetFragment sheetDialog = new SortFilterBottomSheetFragment(sortFilterListener);
            sheetDialog.show(getChildFragmentManager(), sheetDialog.getTag());

        });

        _recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        _galleryViewModel.getAllTripPhotoIds().observe(getViewLifecycleOwner(), ids -> {
            tripPhotoIds = ids;
            GalleryAdapter galleryAdapter = new GalleryAdapter(tripPhotoIds, recyclerViewListener);
            _recyclerView.setAdapter(galleryAdapter);
        });

        mSensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        return root;
    }

    private void recyclerViewClick(View view, int position) {
        Intent intent = new Intent(getActivity(), TripPhotoDetailsActivity.class);
        intent.putExtra("photoId", tripPhotoIds.get(position));
        startActivity(intent);
    }

}