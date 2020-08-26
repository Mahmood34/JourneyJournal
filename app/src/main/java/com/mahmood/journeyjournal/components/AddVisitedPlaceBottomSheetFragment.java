package com.mahmood.journeyjournal.components;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.AddVisitedPlaceListener;
import com.mahmood.journeyjournal.models.Trip;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddVisitedPlaceBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    VisitedPlace visitedPlace;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private DatabaseReference _tripRef;
    private DatabaseReference _visitedPlacesRef;
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private AddVisitedPlaceListener _listener;
    private EditText _titleEditText;
    private EditText _notesEditText;
    private Button _dateButton;
    private Button _locationSearch;
    private Button _confirmButton;
    private Trip _trip;
    private PlacesClient placesClient;
    private Double _lat, _lng;
    private String _locationName;

    public AddVisitedPlaceBottomSheetFragment(AddVisitedPlaceListener listener) {
        _listener = listener;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                _locationName = place.getName();
                _locationSearch.setText(_locationName);
                _titleEditText.setText(_locationName);
                _lat = Objects.requireNonNull(place.getLatLng()).latitude;
                _lng = place.getLatLng().longitude;
                _confirmButton.setEnabled(true);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", Objects.requireNonNull(status.getStatusMessage()));
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.

            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _trip = requireArguments().getParcelable("trip");

        _lat = 0.0;
        _lng = 0.0;
        // Initialize the SDK
        Places.initialize(this.requireContext(), "AIzaSyCTHlPH8wyDuX7oe9mZ0OirwmvqBzgFaR0");

        // Create a new Places client instance
        placesClient = Places.createClient(this.getContext());

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                //.setLocationRestriction(bounds)
                .setSessionToken(token)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i("TAG", prediction.getPlaceId());
                Log.i("TAG", prediction.getPrimaryText(null).toString());

            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("TAG", "Place not found: " + apiException.getStatusCode());
            }
        });
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
        _locationSearch = view.findViewById(R.id.Location_search);
        Date today = Calendar.getInstance().getTime();
        _confirmButton.setOnClickListener(this);
        _dateButton.setText(_formatter.format(today));
        _dateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            try {
                datePickerDialog.getDatePicker().setMaxDate(Objects.requireNonNull(_formatter.parse(_dateButton.getText().toString())).getTime());
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
        _locationSearch.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .build(v.getContext());
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        });

        _tripRef = DatabaseConstant.getInstance()
                .getDatabaseRef()
                .child(DatabaseConstant.TRIP_REPOSITORY);
        _visitedPlacesRef = DatabaseConstant.getInstance()
                .getDatabaseRef()
                .child(DatabaseConstant.VISITED_PLACE_REPOSITORY);

    }

    @Override
    public void onClick(View v) {
        Date date = null;
        try {
            date = _formatter.parse(_dateButton.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        visitedPlace = new VisitedPlace(_titleEditText.getText().toString(), date, _locationName, _lat, _lng, _notesEditText.getText().toString(), _trip.getId());
        _listener.onClick(visitedPlace);


//        if (_titleEditText.getText().toString().trim().equals("")) {
//            _titleEditText.setText(_locationSearch.getText().toString());
//        }

        dismiss();

    }

}
