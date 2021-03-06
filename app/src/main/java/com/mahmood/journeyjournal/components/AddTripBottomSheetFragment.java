package com.mahmood.journeyjournal.components;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.AddTripClickListener;
import com.mahmood.journeyjournal.models.Trip;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddTripBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private AddTripClickListener _listener;
    private EditText _titleEditText;
    private EditText _notesEditText;
    private Button _startDateButton;
    private Button _endDateButton;
    private Button _confirmButton;

    private Trip _trip;

    public AddTripBottomSheetFragment(AddTripClickListener listener) {
        _listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_add_trip, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date today = Calendar.getInstance().getTime();
        _titleEditText = view.findViewById(R.id.add_trip_edit_text_title);
        _notesEditText = view.findViewById(R.id.add_trip_edit_text_notes);
        _startDateButton = view.findViewById(R.id.add_trip_button_start_date);
        _endDateButton = view.findViewById(R.id.add_trip_button_end_date);
        _confirmButton = view.findViewById(R.id.add_trip_button);
        _confirmButton.setOnClickListener(this);
        _startDateButton.setText(_formatter.format(today));
        _endDateButton.setText(_formatter.format(today));
        _startDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            try {
                datePickerDialog.getDatePicker().setMaxDate(Objects.requireNonNull(_formatter.parse(_endDateButton.getText().toString())).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
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
            datePickerDialog.setOnDateSetListener((view12, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                _endDateButton.setText(_formatter.format(calendar.getTime()));
            });
            datePickerDialog.show();
        });

    }

    @Override
    public void onClick(View v) {
        try {
            Date startDate = _formatter.parse(_startDateButton.getText().toString());
            Date endDate = _formatter.parse(_endDateButton.getText().toString());
            if (_titleEditText.getText().toString().trim().equals("")) {
                _titleEditText.setText(_formatter.format(Objects.requireNonNull(startDate)));
            }
            _trip = new Trip(_titleEditText.getText().toString(), startDate, endDate, _notesEditText.getText().toString());
            _listener.onClick(_trip);

            DatabaseConstant.getInstance().addTrip(_trip);
            Toast.makeText(v.getContext(), "New trip added", Toast.LENGTH_SHORT).show();
            dismiss();
        } catch (ParseException e) {
            Toast.makeText(v.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

}
