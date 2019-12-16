package com.mahmood.journeyjournal.components;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;

public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView _name;
    private TextView _startDate;
    private TextView _endDate;
    private TextView _notes;
    private TextView _companions;
    private RecyclerViewClickListener _listener;

    public TripViewHolder(View itemView, RecyclerViewClickListener listener){
        super(itemView);
        _name = itemView.findViewById(R.id.trip_name);
        _startDate = itemView.findViewById(R.id.trip_start_date);
        _endDate = itemView.findViewById(R.id.trip_end_date);
        _notes = itemView.findViewById(R.id.trip_notes);
        _companions = itemView.findViewById(R.id.trip_companions);
        _listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _listener.onItemClick(v, getAdapterPosition());
    }

    public TextView get_name() {
        return _name;
    }

    public void set_name(TextView _name) {
        this._name = _name;
    }

    public TextView get_startDate() {
        return _startDate;
    }

    public void set_startDate(TextView _startDate) {
        this._startDate = _startDate;
    }

    public TextView get_endDate() {
        return _endDate;
    }

    public void set_endDate(TextView _endDate) {
        this._endDate = _endDate;
    }

    public TextView get_notes() {
        return _notes;
    }

    public void set_notes(TextView _notes) {
        this._notes = _notes;
    }

    public TextView get_companions() {
        return _companions;
    }

    public void set_companions(TextView _companions) {
        this._companions = _companions;
    }

}
