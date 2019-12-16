package com.mahmood.journeyjournal.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.AddTripClickListener;
import com.mahmood.journeyjournal.models.Trip;

import java.util.Calendar;
import java.util.Date;

public class AddTripFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private AddTripClickListener _listener;
    private EditText _title;

    public AddTripFragment(AddTripClickListener listener){
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
        _title = view.findViewById(R.id.edit_text_title);
        Button button = view.findViewById(R.id.button_add_trip);
        button.setOnClickListener(this);
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof ItemClickListener) {
//            _listener = (ItemClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement ItemClickListener.");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        _listener = null;
//    }

    @Override
    public void onClick(View v) {
        Date today = Calendar.getInstance().getTime();
        _listener.onItemClick(new Trip(_title.getText().toString(), today, today, "" , "This is a note"));
        dismiss();
    }

}
