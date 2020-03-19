package com.mahmood.journeyjournal.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class TripVisitedPlaceActivity extends AppCompatActivity implements OnMapReadyCallback {

    Trip _trip;
    AddVisitedPlaceListener addVisitedPlaceListener = this::addVisitedPlaceClick;
    MutableLiveData<ArrayList<VisitedPlace>> _visitedPlaces;
    Location location;
    private int _resultCode = RESULT_CANCELED;
    private Button _addVisitedPlaceButton;
    private RecyclerView _recyclerView;
    private MapView mapView;
    private GoogleMap map;
    private DatabaseReference _database;
    private String _userid;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited_places);
        _trip = getIntent().getExtras().getParcelable("trip");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });

        mapView = findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        RecyclerViewClickListener recyclerViewListener = (view, position) -> recyclerViewClick(view, position);

        _addVisitedPlaceButton = findViewById(R.id.add_visited_place_button);


        _recyclerView = findViewById(R.id.visited_place_recycler_view);

//        ArrayList<VisitedPlace> visitedPlaces = _trip.getVisitedPlaces();


        if (_trip.getVisitedPlaces() != null) {
            VisitedPlaceAdapter visitedPlaceAdapter = new VisitedPlaceAdapter(_trip.getVisitedPlaces(), recyclerViewListener);
            _recyclerView.setAdapter(visitedPlaceAdapter);
        }

        _addVisitedPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddVisitedPlaceBottomSheetFragment sheetDialog = new AddVisitedPlaceBottomSheetFragment(addVisitedPlaceListener);
                Bundle bundle = new Bundle();
                bundle.putParcelable("trip", _trip);
                sheetDialog.setArguments(bundle);
                sheetDialog.show(getSupportFragmentManager(), sheetDialog.getTag());
            }
        });

        loadVisitedPlaces();
        _recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void addVisitedPlaceClick(VisitedPlace visitedPlace) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        createIntent();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        createIntent();
        super.onBackPressed();
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
        //TODO Create VisitedPlaceDetailsActivity
//        Intent intent = new Intent(view.getContext(), TripPhotoDetailsActivity.class);
//        intent.putExtra("photo", _trip.getVisitedPlaces(position));
//        startActivityForResult(intent, 1);
    }

    private void createIntent() {
        Intent intent = getIntent();
        //intent.putExtra("updatedTrip", visitedPlace);
        setResult(_resultCode, intent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        map = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(0, 0);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void loadVisitedPlaces() {
        _database = FirebaseDatabase.getInstance().getReference();
        _visitedPlaces = new MutableLiveData<>();

        ValueEventListener visitedPlaceListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<VisitedPlace> visitedPlaces = new ArrayList<>();

                _userid = FirebaseAuth.getInstance().getUid();
                for (DataSnapshot ds :
                        dataSnapshot.child(DatabaseConstant.TRIP_REPOSITORY).child(_userid).child(_trip.getId()).child("visitedPlaces").getChildren()) {

                    VisitedPlace visitedPlace = ds.getValue(VisitedPlace.class);
                    visitedPlaces.add(visitedPlace);
                }
                _visitedPlaces.setValue(visitedPlaces);
                _recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        _database.addValueEventListener(visitedPlaceListener);
    }
}



