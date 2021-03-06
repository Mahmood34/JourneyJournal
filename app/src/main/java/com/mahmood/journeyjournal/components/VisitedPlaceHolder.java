package com.mahmood.journeyjournal.components;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;

public class VisitedPlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView _nameTextView;
    private TextView _dateTextView;
    private TextView _notesTextView;
    private RecyclerViewClickListener _listener;

    public VisitedPlaceHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        _nameTextView = itemView.findViewById(R.id.visited_place_name);
        _dateTextView = itemView.findViewById(R.id.visited_place_date);
        _listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _listener.onItemClick(v, getAdapterPosition());
    }

    public TextView get_nameTextView() {
        return _nameTextView;
    }

    public void setNameTextView(TextView _nameTextView) {
        this._nameTextView = _nameTextView;
    }

    public TextView getDateTextView() {
        return _dateTextView;
    }

    public void setDateTextView(TextView _dateTextView) {
        this._dateTextView = _dateTextView;
    }

}
