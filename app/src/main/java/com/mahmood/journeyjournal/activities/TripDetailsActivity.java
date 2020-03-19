package com.mahmood.journeyjournal.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.adapters.GalleryAdapter;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Person;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TripDetailsActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_SELECT_IMAGE = 102;
    private int _resultCode = RESULT_CANCELED;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private Trip _trip;
    private boolean isEditing;
    private EditText _titleEditText;
    private EditText _notesEditText;
    private Button _startDateButton;
    private Button _endDateButton;
    private Button _companionsButton;
    private Button _addCompanionButton;
    private Button _visitedPlacesButton;
    private Button _addPhotoButton;
    private RecyclerView _recyclerView;
    private DatabaseReference _databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        RecyclerViewClickListener recyclerViewListener = (view, position) -> recyclerViewClick(view, position);
        _trip = getIntent().getExtras().getParcelable("trip");
        setTitle(_trip.getTitle());
        _titleEditText = findViewById(R.id.trip_details_edit_text_title);
        _notesEditText = findViewById(R.id.trip_details_edit_text_notes);
        _startDateButton = findViewById(R.id.trip_details_button_start_date);
        _endDateButton = findViewById(R.id.trip_details_button_end_date);
        _companionsButton = findViewById(R.id.trip_details_button_companions);
        _addCompanionButton = findViewById(R.id.trip_details_add_companion);
        _visitedPlacesButton = findViewById(R.id.visited_places_button);
        _addPhotoButton = findViewById(R.id.trip_details_add_photo);
        _recyclerView = findViewById(R.id.trip_details_recycler_view_gallery);
        GalleryAdapter galleryAdapter = new GalleryAdapter(_trip.getTripPhotos(), recyclerViewListener);
        _recyclerView.setAdapter(galleryAdapter);
        _recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        _titleEditText.setText(_trip.getTitle());
        _notesEditText.setText(_trip.getNotes());
        _startDateButton.setText(_formatter.format(_trip.getStartDate()));
        _endDateButton.setText(_formatter.format(_trip.getEndDate()));
        _companionsButton.setText("Companions: " + _trip.getCompanions().size());
        _databaseRef = FirebaseDatabase.getInstance().getReference();

        //region onClickListener
        _startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
                try {
                    datePickerDialog.getDatePicker().setMaxDate(_formatter.parse(_endDateButton.getText().toString()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        _startDateButton.setText(_formatter.format(calendar.getTime()));
                    }
                });
                datePickerDialog.show();
            }
        });

        _endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
                try {
                    datePickerDialog.getDatePicker().setMinDate(_formatter.parse(_startDateButton.getText().toString()).getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        _endDateButton.setText(_formatter.format(calendar.getTime()));
                    }
                });
                datePickerDialog.show();
            }
        });
        _companionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Companions");
                LayoutInflater layoutInflater = getLayoutInflater();
                View addCompanionView = layoutInflater.inflate(R.layout.alert_dialog_add_companion, null);
                builder.setView(addCompanionView);
                ArrayList<String> y = new ArrayList<>();
                for (Person person : _trip.getCompanions()) {
                    y.add(person.getName());
                }
                CharSequence[] charSequences = y.toArray(new CharSequence[y.size()]);
                builder.setItems(charSequences, null);

                builder.show();
            }
        });

        _addCompanionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Add Companion");
                LayoutInflater layoutInflater = getLayoutInflater();
                View addCompanionView = layoutInflater.inflate(R.layout.alert_dialog_add_companion, null);
                builder.setView(addCompanionView);

                builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = addCompanionView.findViewById(R.id.alert_dialog_companion_edit_text);
                        Person person = new Person(editText.getText().toString());
                        _trip.addCompanion(person);
                        _companionsButton.setText("Companions: " + _trip.getCompanions().size());
                    }
                });
                builder.show();
            }
        });

        _visitedPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TripVisitedPlaceActivity.class);
                intent.putExtra("trip", _trip);
                startActivityForResult(intent, 1);
            }
        });

        _addPhotoButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle(R.string.choose_photo_mode)
                    .setItems(R.array.choose_photo_mode_array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                takePicture();
                            }
                            else if (which == 1){
                                choosePicture();
                            }
                        }
                    });
            builder.show();
        });
        //endregion
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trip_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.trip_detail_save_menu_item:
                if (_trip.getNotes() != _notesEditText.getText().toString() || _trip.getTitle() != _titleEditText.getText().toString()) {
                    _resultCode = RESULT_OK;
                    _trip.setTitle(_titleEditText.getText().toString());
                    _trip.setNotes(_notesEditText.getText().toString());
                    try {
                        Date startDate = _formatter.parse(_startDateButton.getText().toString());
                        Date endDate = _formatter.parse(_endDateButton.getText().toString());
                        _trip.setStartDate(startDate.getTime());
                        _trip.setEndDate(endDate.getTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    _databaseRef.child(DatabaseConstant.TRIP_REPOSITORY).child(_trip.getId()).setValue(_trip);
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.trip_detail_share_button_item:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String companionList = "";
                for (int i = 0; i < _trip.getCompanions().size(); i++) {
                    companionList += _trip.getCompanion(i).getName() + "\n";
                }
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey, this is my trip, " + "\n" + _trip.getTitle() + "\n" +
                                "these are my notes I took: " + "\n" + _trip.getNotes() + "\n" +
                                "And these are the people I went with:" + "\n" + companionList);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            case R.id.trip_detail_delete_trip_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete trip");
                builder.setMessage("You are about to delete this trip. To confirm this delete, press the back button?");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _databaseRef.child(DatabaseConstant.TRIP_REPOSITORY).child(_trip.getId()).removeValue();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
                break;
            default:
                createIntent();
                finish();
        }
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
            if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
                Uri imageUri = data.getData();
                TripPhoto tripPhoto = new TripPhoto(_trip.getTitle() + _trip.getTripPhotos().size(), imageUri.toString());
                _trip.addTripPhoto(tripPhoto);
                _recyclerView.getAdapter().notifyDataSetChanged();
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
            }
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void recyclerViewClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), TripPhotoDetailsActivity.class);
        intent.putExtra("photo", _trip.getTripPhoto(position));
        startActivityForResult(intent, 1);
    }

    private void createIntent() {
        Intent intent = getIntent();
        intent.putExtra("updatedTrip", _trip);
        setResult(_resultCode, intent);
    }

    private void takePicture(){
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (imageTakeIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_SELECT_IMAGE);
    }

}

