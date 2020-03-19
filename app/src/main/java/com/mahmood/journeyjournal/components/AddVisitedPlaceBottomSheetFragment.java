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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.AddVisitedPlaceListener;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.TripPhoto;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddVisitedPlaceBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    VisitedPlace visitedPlace;
    private DatabaseReference _tripRef;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private AddVisitedPlaceListener _listener;
    private EditText _titleEditText;
    private EditText _notesEditText;
    private Button _dateButton;
    private Button _confirmButton;
    private String _userid;
    private Trip _trip;


    public AddVisitedPlaceBottomSheetFragment(AddVisitedPlaceListener listener) {
        _listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _trip = getArguments().getParcelable("trip");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_add_visited_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _titleEditText = view.findViewById(R.id.add_visited_place_edit_text_title);
        _notesEditText = view.findViewById(R.id.add_visited_place_edit_text_notes);
        _dateButton = view.findViewById(R.id.add_visited_place_button_start_date);
        _confirmButton = view.findViewById(R.id.submit_visited_place_button);
        Date today = Calendar.getInstance().getTime();
        _confirmButton.setOnClickListener(this);
        _dateButton.setText(_formatter.format(today));
        _dateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            try {
                datePickerDialog.getDatePicker().setMaxDate(_formatter.parse(_dateButton.getText().toString()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                _dateButton.setText(_formatter.format(calendar.getTime()));
            });
            datePickerDialog.show();
        });

        _tripRef = FirebaseDatabase.getInstance().getReference().child(DatabaseConstant.TRIP_REPOSITORY);
    }

    @Override
    public void onClick(View v) {
        try {
            Date date = _formatter.parse(_dateButton.getText().toString());
            _userid = FirebaseAuth.getInstance().getUid();
            visitedPlace = new VisitedPlace(_titleEditText.getText().toString(), date, "_", _notesEditText.getText().toString(), new ArrayList<TripPhoto>());
//            _listener.onClick(visitedPlace);
            _trip.addVisitedPlace(visitedPlace);
            _tripRef.child(_trip.getId()).setValue(_trip);
//            _tripRef.child(_userid).child(_trip.getId()).child("visitedPlaces").child(visitedPlace.getId()).setValue(visitedPlace);
            Toast.makeText(v.getContext(), "Success", Toast.LENGTH_SHORT).show();

            dismiss();
        } catch (ParseException e) {
            Toast.makeText(v.getContext(), "Invalid", Toast.LENGTH_SHORT).show();

        }
    }

}
