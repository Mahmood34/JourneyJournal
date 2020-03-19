package com.mahmood.journeyjournal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.components.VisitedPlaceHolder;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VisitedPlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DateFormat _formatter = SimpleDateFormat.getDateInstance();
    private ArrayList<VisitedPlace> _visitedPlace;
    private RecyclerViewClickListener _listener;

    public VisitedPlaceAdapter(ArrayList<VisitedPlace> visitedPlaces, RecyclerViewClickListener listener) {
        _visitedPlace = visitedPlaces;
        _listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View visitedPlaceView = layoutInflater.inflate(R.layout.recyclerview_visted_place, parent, false);
        return new VisitedPlaceHolder(visitedPlaceView, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VisitedPlace visitedPlace = _visitedPlace.get(position);

        if (holder instanceof VisitedPlaceHolder) {
            VisitedPlaceHolder visitedPlaceHolder = (VisitedPlaceHolder) holder;
            TextView titleTextView = visitedPlaceHolder.get_nameTextView();
            TextView startDateTextView = visitedPlaceHolder.get_dateTextView();
            TextView notesTextView = visitedPlaceHolder.get_notesTextView();
            titleTextView.setText(visitedPlace.getName());
            startDateTextView.setText(_formatter.format(new Date(visitedPlace.getDateVisited())));
            notesTextView.setText(visitedPlace.getTravellersNotes());
        }
    }

    @Override
    public int getItemCount() {
        return _visitedPlace.size();
    }

}
