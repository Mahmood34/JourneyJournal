package com.mahmood.journeyjournal.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.adapters.GalleryAdapter;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Person;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class TripDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_SELECT_IMAGE = 102;
    private static final int REQUEST_VISITED_TRIPS = 103;
    String currentPhotoPath;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private Trip _trip;
    private Button _startDateButton;
    private Button _endDateButton;
    private Button _companionsButton;
    private RecyclerView _recyclerView;
    private StorageReference storageRef;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        _trip = Objects.requireNonNull(getIntent().getExtras()).getParcelable("trip");
        setTitle(Objects.requireNonNull(_trip).getTitle());

        RecyclerViewClickListener recyclerViewListener = this::recyclerViewClick;

        EditText _titleEditText = findViewById(R.id.trip_details_edit_text_title);
        EditText _notesEditText = findViewById(R.id.trip_details_edit_text_notes);
        _startDateButton = findViewById(R.id.trip_details_button_start_date);
        _endDateButton = findViewById(R.id.trip_details_button_end_date);
        _companionsButton = findViewById(R.id.trip_details_button_companions);
        Button _addCompanionButton = findViewById(R.id.trip_details_add_companion);
        Button _visitedPlacesButton = findViewById(R.id.visited_places_button);
        Button _addPhotoButton = findViewById(R.id.add_photo);
        _recyclerView = findViewById(R.id.recycler_view_gallery);
        _recyclerView.setNestedScrollingEnabled(false);
        GalleryAdapter galleryAdapter = new GalleryAdapter(_trip.getTripPhotoIds(), recyclerViewListener);
        _recyclerView.setAdapter(galleryAdapter);
        _recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        _titleEditText.setText(_trip.getTitle());
        _notesEditText.setText(_trip.getNotes());
        _startDateButton.setText(_formatter.format(_trip.getStartDate()));
        _endDateButton.setText(_formatter.format(_trip.getEndDate()));
        _companionsButton.setText("Companions: " + _trip.getCompanions().size());

        //region onClickListener
        _startDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            try {
                datePickerDialog.getDatePicker().setMaxDate(Objects.requireNonNull(_formatter.parse(_endDateButton.getText().toString())).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                _startDateButton.setText(_formatter.format(calendar.getTime()));
            });
            datePickerDialog.show();
        });

        _endDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            try {
                datePickerDialog.getDatePicker().setMinDate(Objects.requireNonNull(_formatter.parse(_startDateButton.getText().toString())).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                _endDateButton.setText(_formatter.format(calendar.getTime()));
            });
            datePickerDialog.show();
        });

        _companionsButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Companions");
            LayoutInflater layoutInflater = getLayoutInflater();
            View addCompanionView = layoutInflater.inflate(R.layout.alert_dialog_add_companion, null);
            builder.setView(addCompanionView);
            ArrayList<String> y = new ArrayList<>();
            for (Person person : _trip.getCompanions()) {
                y.add(person.getName());
            }
            CharSequence[] charSequences = y.toArray(new CharSequence[0]);
            builder.setItems(charSequences, null);

            builder.show();
        });

        _addCompanionButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Add Companion");
            LayoutInflater layoutInflater = getLayoutInflater();
            View addCompanionView = layoutInflater.inflate(R.layout.alert_dialog_add_companion, null);
            builder.setView(addCompanionView);

            builder.setPositiveButton(R.string.add, (dialog, which) -> {
                EditText editText = addCompanionView.findViewById(R.id.alert_dialog_companion_edit_text);
                Person person = new Person(editText.getText().toString());
                _trip.addCompanion(person);
                _companionsButton.setText("Companions: " + _trip.getCompanions().size());
            });
            builder.show();
        });

        _visitedPlacesButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VisitedPlacesActivity.class);
            intent.putExtra("trip", _trip);
            startActivityForResult(intent, REQUEST_VISITED_TRIPS);
        });

        _addPhotoButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        103);
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.choose_photo_mode)
                        .setItems(R.array.choose_photo_mode_array, (dialog, which) -> {
                            if (which == 0) {
                                takePicture();
                            } else if (which == 1) {
                                choosePicture();

                            }
                        });
                builder.show();
            }
        });
        //endregion
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
                DatabaseConstant.getInstance().addTrip(_trip);
                break;
            case R.id.share_button_item:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                StringBuilder companionList = new StringBuilder();
                for (int i = 0; i < _trip.getCompanions().size(); i++) {
                    companionList.append(_trip.getCompanion(i).getName()).append("\n");
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, this is my trip, " + "\n" + _trip.getTitle() + "\n" +
                                "these are my notes I took: " + "\n" + _trip.getNotes() + "\n" +
                                "And these are the people I went with:" + "\n" + companionList);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            case R.id.delete_trip_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete trip");
                builder.setMessage("Are you sure you want to delete this trip?");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", (dialog, which) -> {

                    DatabaseConstant.getInstance().removeTrip(_trip);

                    TripDetailsActivity.super.onBackPressed();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                builder.show();
                break;
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
            if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
                Uri imageUri = Objects.requireNonNull(data).getData();

                storageRef = DatabaseConstant.getInstance().getStorageRef();

                String url = Calendar.getInstance().getTimeInMillis() + ".jpg";
                StorageReference imageRef = storageRef.child("images/" + url);
                TripPhoto tripPhoto = new TripPhoto(_trip.getTitle() + _trip.getTripPhotoIds().size(), url, _trip.getId());

                imageRef.putFile(Objects.requireNonNull(imageUri))
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get a URL to the uploaded content
                            storageRef.child("images/" + tripPhoto.getImageUrl())
                                    .getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        // Got the download URL for 'users/me/profile.png'
                                        _trip.addTripPhotoId(tripPhoto.getImageUrl());
                                        DatabaseConstant.getInstance().addTripPhoto(tripPhoto);
                                        Toast.makeText(TripDetailsActivity.this, "Successfully uploaded photo", Toast.LENGTH_SHORT).show();
                                    }).addOnFailureListener(exception -> {
                                // Handle any errors
                            }).addOnCompleteListener(task -> Objects.requireNonNull(_recyclerView.getAdapter()).notifyDataSetChanged());

                        })
                        .addOnFailureListener(exception -> {
                            // Handle unsuccessful uploads
                            // ...
                        }).addOnCompleteListener(task -> Objects.requireNonNull(_recyclerView.getAdapter()).notifyDataSetChanged());
            } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = Objects.requireNonNull(data).getExtras();
                Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                storeImage(Objects.requireNonNull(imageBitmap));
            } else if (requestCode == REQUEST_VISITED_TRIPS && resultCode == RESULT_OK) {
                _trip = Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).getParcelable("updatedTrip");
            }
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void storeImage(Bitmap image) throws IOException {
        File pictureFile = createImageFile();

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 99, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("TAG", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("TAG", "Error accessing file: " + e.getMessage());
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void recyclerViewClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), TripPhotoDetailsActivity.class);
        intent.putExtra("photoId", _trip.getTripPhotoId(position));
        startActivityForResult(intent, 1);
    }

    private void createIntent() {
        Intent intent = getIntent();
        intent.putExtra("updatedTrip", _trip);
        setResult(RESULT_CANCELED, intent);
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_SELECT_IMAGE);
    }

}

