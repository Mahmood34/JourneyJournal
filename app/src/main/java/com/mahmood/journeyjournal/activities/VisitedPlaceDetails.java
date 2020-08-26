package com.mahmood.journeyjournal.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.StorageReference;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.adapters.GalleryAdapter;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.TripPhoto;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class VisitedPlaceDetails extends AppCompatActivity implements OnMapReadyCallback {
    private static final int REQUEST_SELECT_IMAGE = 102;
    private int _resultCode = RESULT_OK;
    private VisitedPlace _visitedPlace;
    private EditText _name;
    private Button _dateButton;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private RecyclerView _recyclerView;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        RecyclerViewClickListener recyclerViewListener = this::recyclerViewClick;

        setContentView(R.layout.activity_visited_place_details);
        _visitedPlace = Objects.requireNonNull(getIntent().getExtras()).getParcelable("visitedPlace");
        _name = findViewById(R.id.visited_place_edit_text_name);
        _dateButton = findViewById(R.id.visited_place_button_date);
        EditText _notes = findViewById(R.id.visited_place_edit_text_notes);

        Button _addPhotoButton = findViewById(R.id.add_photo);
        _addPhotoButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.choose_photo_mode)
                    .setItems(R.array.visited_place_choose_photo_mode_array, (dialog, which) -> {
                        if (which == 0) {
                            Toast.makeText(VisitedPlaceDetails.this, "1", Toast.LENGTH_SHORT).show();

                        } else if (which == 1) {
                            choosePicture();
                        }
                    });
            builder.show();
        });

        _recyclerView = findViewById(R.id.recycler_view_gallery);
        _recyclerView.setNestedScrollingEnabled(false);
        GalleryAdapter galleryAdapter = new GalleryAdapter(_visitedPlace.getTripPhotoIds(), recyclerViewListener);
        _recyclerView.setAdapter(galleryAdapter);
        _recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        MapView mapView = findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        _name.setText(_visitedPlace.getName());
        _dateButton.setText(_formatter.format(_visitedPlace.getDateVisited()));
        _notes.setText(_visitedPlace.getTravellersNotes());

        _dateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            try {
                datePickerDialog.getDatePicker().setMinDate(Objects.requireNonNull(_formatter.parse(_dateButton.getText().toString())).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                _dateButton.setText(_formatter.format(calendar.getTime()));
            });
            datePickerDialog.show();
        });

    }

    private void recyclerViewClick(View view, int i) {
        Intent intent = new Intent(view.getContext(), TripPhotoDetailsActivity.class);
        intent.putExtra("photo", _visitedPlace.getTripPhotoId(i));
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_item:

                break;
            case R.id.share_button_item:
                String url = "https://www.google.com/maps/search/?api=1&query=" + _visitedPlace.getLat() + "," + _visitedPlace.getLng();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                break;
            case R.id.delete_trip_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete visited place");
                builder.setMessage("Are you sure you want to delete this visited place?");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    DatabaseConstant.getInstance().removeVisitedPlace(_visitedPlace);
                    VisitedPlaceDetails.super.onBackPressed();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                builder.show();
                break;
            default:
                finish();
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = Objects.requireNonNull(data).getData();

            storageRef = DatabaseConstant.getInstance().getStorageRef();

            String url = Calendar.getInstance().getTimeInMillis() + ".jpg";
            StorageReference imageRef = storageRef.child("images/" + url);
            TripPhoto tripPhoto = new TripPhoto(_name.getText().toString() + Calendar.getInstance().getTime().toString(), url, _visitedPlace.getTripId());


            imageRef.putFile(Objects.requireNonNull(imageUri))
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get a URL to the uploaded content
                        storageRef.child("images/" + tripPhoto.getImageUrl()).getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    // Got the download URL for 'users/me/profile.png'
                                    _visitedPlace.addTripPhotoId(tripPhoto.getImageUrl());
                                    tripPhoto.setVisitedPlace(_visitedPlace.getId());
                                    DatabaseConstant.getInstance().addVisitedPlace(_visitedPlace);
                                    DatabaseConstant.getInstance().addTripPhoto(tripPhoto);
                                    Toast.makeText(getApplication(), "Successfully uploaded photo", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(exception -> {
                            // Handle any errors
                        }).addOnCompleteListener(task -> Objects.requireNonNull(_recyclerView.getAdapter()).notifyDataSetChanged());

                    })
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                        // ...
                    }).addOnCompleteListener(task -> Objects.requireNonNull(_recyclerView.getAdapter()).notifyDataSetChanged());

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(_visitedPlace.getLat(), _visitedPlace.getLng()))
                .title(_visitedPlace.getName()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(_visitedPlace.getLat(),
                        _visitedPlace.getLng()), 10));
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_SELECT_IMAGE);
    }

}
