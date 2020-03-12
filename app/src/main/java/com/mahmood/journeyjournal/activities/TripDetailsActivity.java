package com.mahmood.journeyjournal.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private int _resultCode = RESULT_CANCELED;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private Trip _trip;
    private boolean isEditing;
    private EditText _titleEditText;
    private EditText _notesEditText;
    private Button _startDateButton;
    private Button _endDateButton;
    private Button _companionsButton;
    private ImageButton _addCompanionButton;
    private ImageButton _addPhotoButton;
    private RecyclerView _recyclerView;
    private DatabaseReference _databaseRef;
    private String _userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        RecyclerViewClickListener recyclerViewListener = (view, position) -> recyclerViewClick(view, position);
        _trip = getIntent().getExtras().getParcelable("trip");
        _titleEditText = findViewById(R.id.trip_details_edit_text_title);
        _notesEditText = findViewById(R.id.trip_details_edit_text_notes);
        _startDateButton = findViewById(R.id.trip_details_button_start_date);
        _endDateButton = findViewById(R.id.trip_details_button_end_date);
        _companionsButton = findViewById(R.id.trip_details_button_companions);
        _addCompanionButton = findViewById(R.id.trip_details_add_companion);
        _addPhotoButton = findViewById(R.id.trip_details_add_photo);
        _recyclerView = findViewById(R.id.trip_details_recycler_view_gallery);
        GalleryAdapter galleryAdapter = new GalleryAdapter(_trip.getTripPhotos(), recyclerViewListener);
        _recyclerView.setAdapter(galleryAdapter);
        _recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        FloatingActionButton editFab = findViewById(R.id.fab);
        _titleEditText.setText(_trip.getTitle());
        _notesEditText.setText(_trip.getNotes());
        _startDateButton.setText(_formatter.format(_trip.getStartDate()));
        _endDateButton.setText(_formatter.format(_trip.getEndDate()));
        _companionsButton.setText("Companions: " + _trip.getCompanions().size());
        _databaseRef = FirebaseDatabase.getInstance().getReference();


        //region onClickListener
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = !isEditing;
                Snackbar.make(view, isEditing ? "Now editing" : "Confirmed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (isEditing) {

                } else {
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
                        _userid = FirebaseAuth.getInstance().getUid();
                        _databaseRef.child("Trips").child(_userid).child(_trip.getId()).setValue(_trip);
                    }
                }
                _titleEditText.setEnabled(isEditing);
                _notesEditText.setEnabled(isEditing);
                _startDateButton.setEnabled(isEditing);
                _endDateButton.setEnabled(isEditing);

            }
        });

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

        _addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
            }
        });
        //endregion
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
                Uri imageUri = data.getData();
                TripPhoto tripPhoto = new TripPhoto(_trip.getTitle() + _trip.getTripPhotos().size(), imageUri.toString());
                _trip.addTripPhoto(tripPhoto);
                _recyclerView.getAdapter().notifyDataSetChanged();

            }
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_SHORT).show();
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

}

