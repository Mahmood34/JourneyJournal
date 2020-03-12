package com.mahmood.journeyjournal.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.components.GalleryViewHolder;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;
import com.mahmood.journeyjournal.models.TripPhoto;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TripPhoto> _tripPhotos;
    private RecyclerViewClickListener _listener;

    public GalleryAdapter(ArrayList<TripPhoto> tripPhotos, RecyclerViewClickListener listener) {
        _tripPhotos = tripPhotos;
        _listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View galleryView = layoutInflater.inflate(R.layout.recyclerview_gallery, parent, false);
        return new GalleryViewHolder(galleryView, _listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TripPhoto photo = _tripPhotos.get(position);
        if (holder instanceof GalleryViewHolder) {
            GalleryViewHolder galleryViewHolder = (GalleryViewHolder) holder;
            ImageView imageView = galleryViewHolder.getImage();
            imageView.setImageURI(Uri.parse(photo.getImage()));
        }
    }

    @Override
    public int getItemCount() {
        return _tripPhotos.size();
    }
}
