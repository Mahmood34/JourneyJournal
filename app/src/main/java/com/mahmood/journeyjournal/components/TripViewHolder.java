package com.mahmood.journeyjournal.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.TripRecyclerViewClickListener;

import java.text.SimpleDateFormat;

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView _titleTextView;
    private TextView _startDateTextView;
    private TextView _endDateTextView;
    private TextView _notesTextView;
    private TextView _companionsTextView;
    private TripRecyclerViewClickListener _listener;

    public TripViewHolder(View itemView, TripRecyclerViewClickListener listener) {
        super(itemView);
        _titleTextView = itemView.findViewById(R.id.trip_name);
        _startDateTextView = itemView.findViewById(R.id.trip_start_date);
        _endDateTextView = itemView.findViewById(R.id.trip_end_date);
        _notesTextView = itemView.findViewById(R.id.trip_notes);
        _companionsTextView = itemView.findViewById(R.id.trip_companions);
        _listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _listener.onItemClick(v, getAdapterPosition());
    }

    public TextView getName() {
        return _titleTextView;
    }

    public void setName(TextView name) {
        _titleTextView = name;
    }

    public TextView getStartDate() {
        return _startDateTextView;
    }

    public void setStartDate(TextView startDate) {
        _startDateTextView = startDate;
    }

    public TextView getEndDate() {
        return _endDateTextView;
    }

    public void setEndDate(TextView endDate) {
        _endDateTextView = endDate;
    }

    public TextView getNotes() {
        return _notesTextView;
    }

    public void setNotes(TextView notes) {
        _notesTextView = notes;
    }

    public TextView getCompanions() {
        return _companionsTextView;
    }

    public void setCompanions(TextView companions) {
        _companionsTextView = companions;
    }

}
