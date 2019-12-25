package com.mahmood.journeyjournal.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.mahmood.journeyjournal.MainActivity;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.models.Trip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TripDetailsActivity extends AppCompatActivity {
    private int _resultCode = RESULT_CANCELED;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private Trip _trip;
    private boolean isEditing;
    private EditText _titleEditText;
    private EditText _notesEditText;
    private Button _startDateButton;
    private Button _endDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        _trip = getIntent().getExtras().getParcelable("trip");
        _titleEditText = findViewById(R.id.trip_details_edit_text_title);
        _notesEditText = findViewById(R.id.trip_details_edit_text_notes);
        _startDateButton = findViewById(R.id.trip_details_button_start_date);
        _endDateButton = findViewById(R.id.trip_details_button_end_date);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditing = !isEditing;
                Snackbar.make(view, isEditing?"Now editing":"Confirmed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(isEditing){

                }else{
                    if(_trip.getNotes() != _notesEditText.getText().toString() || _trip.getTitle() != _titleEditText.getText().toString() ){
                        _resultCode = RESULT_OK;
                        _trip.setTitle(_titleEditText.getText().toString());
                        _trip.setNotes(_notesEditText.getText().toString());
                        try {
                            Date startDate = _formatter.parse(_startDateButton.getText().toString());
                            Date endDate = _formatter.parse(_endDateButton.getText().toString());
                            _trip.setStartDate(startDate);
                            _trip.setEndDate(endDate);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }
                _titleEditText.setEnabled(isEditing);
                _notesEditText.setEnabled(isEditing);
                _startDateButton.setEnabled(isEditing);
                _endDateButton.setEnabled(isEditing);

            }
        });

        _titleEditText.setText(_trip.getTitle());
        _notesEditText.setText(_trip.getNotes());
        _startDateButton.setText(_formatter.format(_trip.getStartDate()));
        _endDateButton.setText(_formatter.format(_trip.getEndDate()));
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

    private void createIntent(){
        Intent intent = getIntent();
        intent.putExtra("updatedTrip", _trip);
        setResult(_resultCode, intent);
    }

}
