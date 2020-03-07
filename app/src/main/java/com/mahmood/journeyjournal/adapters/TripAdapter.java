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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
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

        if (holder instanceof TripViewHolder) {
            TripViewHolder tripViewHolder = (TripViewHolder) holder;
            TextView titleTextView = tripViewHolder.getTitle();
            TextView startDateTextView = tripViewHolder.getStartDate();
            TextView endDateTextView = tripViewHolder.getEndDate();
            TextView notesTextView = tripViewHolder.getNotes();
            TextView companionTextView = tripViewHolder.getCompanions();

            titleTextView.setText(trip.getTitle());
            startDateTextView.setText(_formatter.format(new Date(trip.getStartDate())));
            endDateTextView.setText(_formatter.format(new Date(trip.getEndDate())));
            notesTextView.setText(trip.getNotes());
            companionTextView.setText("Companions : " + trip.getCompanions().size());
        }
    }

    @Override
    public int getItemCount() {
        return _trips.size();
    }

}
