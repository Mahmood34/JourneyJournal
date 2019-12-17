package com.mahmood.journeyjournal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.components.TripViewHolder;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.Trip;
import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Trip> _trips;
    private RecyclerViewClickListener _listener;
    public TripAdapter(ArrayList<Trip> trips, RecyclerViewClickListener listener) {
        _trips = trips;
        _listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tripView = layoutInflater.inflate(R.layout.recyclerview_trips, parent, false);
        return new TripViewHolder(tripView, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Trip trip = _trips.get(position);

        if(holder instanceof TripViewHolder){
            TripViewHolder tripViewHolder = (TripViewHolder) holder;
            TextView name = tripViewHolder.get_name();
            TextView startDate = tripViewHolder.get_startDate();
            TextView endDate = tripViewHolder.get_endDate();
            TextView notes = tripViewHolder.get_notes();
            TextView companion = tripViewHolder.get_companions();

            name.setText(trip.getName());
            startDate.setText(trip.getStartDate().toString());
            endDate.setText(trip.getEndDate().toString());
            notes.setText(trip.getNotes());
            companion.setText(trip.getCompanions().toString());
        }
    }

    @Override
    public int getItemCount() {
        return _trips.size();
    }

}
