package com.mahmood.journeyjournal.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.SortFilterListener;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;

public class SortFilterBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private SortFilterListener _listener;
    private Spinner spinner;
    private ChipGroup chipGroup;
    private ArrayList<TripPhoto> tripPhotos;
    private ArrayList<String> filters;
    private Button confirmSortFilterButton;

    public SortFilterBottomSheetFragment(SortFilterListener listener) {
        _listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.bottom_sheet_sort_filter, container, false);

        spinner = root.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.requireContext(),
                R.array.sorting_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        confirmSortFilterButton = root.findViewById(R.id.sort_filter_confirm);
        confirmSortFilterButton.setOnClickListener(this);

        chipGroup = root.findViewById(R.id.filter_group);

//        for (String filterItem : filters) {
//            Chip chip = new Chip(chipGroup.getContext());
//            chip.setText(filterItem);
//            chip.setClickable(true);
//            chipGroup.addView(chip);
//        }

        return root;
    }

    @Override
    public void onClick(View v) {

        ArrayList<String> selectedFilters = new ArrayList<>();
        String order = spinner.getSelectedItem().toString();

        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selectedFilters.add(chip.getText().toString());
            }
        }

        _listener.onClick(order, selectedFilters);
        dismiss();
    }

}
