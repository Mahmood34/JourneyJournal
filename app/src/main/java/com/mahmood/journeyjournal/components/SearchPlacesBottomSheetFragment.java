package com.mahmood.journeyjournal.components;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mahmood.journeyjournal.R;

public class SearchPlacesBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.bottom_sheet_search_places, container, false);

        root.findViewById(R.id.restaurant).setOnClickListener(this);
        root.findViewById(R.id.Hotels).setOnClickListener(this);
        root.findViewById(R.id.Cafe).setOnClickListener(this);
        root.findViewById(R.id.Groceries).setOnClickListener(this);
        root.findViewById(R.id.Hospitals).setOnClickListener(this);
        root.findViewById(R.id.Attractions).setOnClickListener(this);

        return root;
    }

    public void searchPlace(String mapsFilter) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + mapsFilter);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.restaurant:
                searchPlace("Restaurants");
                break;
            case R.id.Hotels:
                searchPlace("Hotels");
                break;
            case R.id.Cafe:
                searchPlace("Cafe");
                break;
            case R.id.Groceries:
                searchPlace("Groceries");
                break;
            case R.id.Hospitals:
                searchPlace("Hospitals");
                break;
            case R.id.Attractions:
                searchPlace("Attractions");
                break;
            default:
                break;
        }

    }
}
