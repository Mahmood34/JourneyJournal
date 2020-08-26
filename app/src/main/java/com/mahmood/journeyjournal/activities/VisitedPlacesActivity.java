package com.mahmood.journeyjournal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.adapters.VisitedPlaceAdapter;
import com.mahmood.journeyjournal.components.AddVisitedPlaceBottomSheetFragment;
import com.mahmood.journeyjournal.interfaces.AddVisitedPlaceListener;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VisitedPlacesActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    Trip _trip;
    AddVisitedPlaceListener addVisitedPlaceListener = this::addVisitedPlaceClick;
    ArrayList<VisitedPlace> _visitedPlaces;
    RecyclerViewClickListener recyclerViewListener;
    private int _resultCode = RESULT_OK;
    private FloatingActionButton _addVisitedPlaceButton;
    private RecyclerView _recyclerView;
    private MapView mapView;
    private GoogleMap map;
    private String _userid;
    private HashMap<Marker, VisitedPlace> markerOptionsVisitedPlaceHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_places);

        _trip = Objects.requireNonNull(getIntent().getExtras()).getParcelable("trip");


        mapView = findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        recyclerViewListener = this::recyclerViewClick;

        _addVisitedPlaceButton = findViewById(R.id.add_visited_place_button);

        _recyclerView = findViewById(R.id.visited_place_recycler_view);


        loadVisitedPlaces();

        if (_visitedPlaces != null) {
            VisitedPlaceAdapter visitedPlaceAdapter = new VisitedPlaceAdapter(_visitedPlaces, recyclerViewListener);
            _recyclerView.setAdapter(visitedPlaceAdapter);
        }

        _addVisitedPlaceButton.setOnClickListener(v -> {
            AddVisitedPlaceBottomSheetFragment sheetDialog = new AddVisitedPlaceBottomSheetFragment(addVisitedPlaceListener);
            Bundle bundle = new Bundle();
            bundle.putParcelable("trip", _trip);
            sheetDialog.setArguments(bundle);
            sheetDialog.show(getSupportFragmentManager(), sheetDialog.getTag());
        });

        _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addVisitedPlaceClick(VisitedPlace visitedPlace) {

        DatabaseConstant.getInstance().addVisitedPlace(visitedPlace);

//            Trip.addToFilter(visitedPlace.getLocation());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            default:
                createIntent();
                finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1 && resultCode == RESULT_OK) {
            }
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    private void recyclerViewClick(View view, int position) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(_visitedPlaces.get(position).getLat(),
                        _visitedPlaces.get(position).getLng()), 10));
    }

    private void createIntent() {
        Intent intent = getIntent();
        intent.putExtra("updatedTrip", _trip);
        setResult(_resultCode, intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        map = googleMap;
        loadVisitedPlaces();
        map.setOnInfoWindowClickListener(this);
    }

    private void loadVisitedPlaces() {

        DatabaseReference ref = DatabaseConstant.getInstance().getDatabaseRef();
        markerOptionsVisitedPlaceHashMap = new HashMap<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _visitedPlaces = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.child(DatabaseConstant.VISITED_PLACE_REPOSITORY).getChildren()) {
                    VisitedPlace vp = ds.getValue(VisitedPlace.class);
                    if (Objects.requireNonNull(vp).getTripId().equals(_trip.getId())) {
                        _visitedPlaces.add(vp);
                        MarkerOptions mo = new MarkerOptions()
                                .position(new LatLng(vp.getLat(), vp.getLng()))
                                .title(vp.getName())
                                .snippet("Click here for more details");
                        Marker marker = map.addMarker(mo);
                        markerOptionsVisitedPlaceHashMap.put(marker, vp);
                    }
                }
                VisitedPlaceAdapter visitedPlaceAdapter = new VisitedPlaceAdapter(_visitedPlaces, recyclerViewListener);
                _recyclerView.setAdapter(visitedPlaceAdapter);
                Objects.requireNonNull(_recyclerView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        VisitedPlace vp = markerOptionsVisitedPlaceHashMap.get(marker);
        Intent intent = new Intent(this, VisitedPlaceDetails.class);
        intent.putExtra("visitedPlace", vp);
        startActivityForResult(intent, 1);
    }
}