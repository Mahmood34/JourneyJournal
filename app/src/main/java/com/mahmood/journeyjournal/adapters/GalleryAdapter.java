package com.mahmood.journeyjournal.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.components.GalleryViewHolder;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> _tripPhotos;
    private RecyclerViewClickListener _listener;

    public GalleryAdapter(ArrayList<String> tripPhotos, RecyclerViewClickListener listener) {
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
        String photo = _tripPhotos.get(position);
        if (holder instanceof GalleryViewHolder) {
            GalleryViewHolder galleryViewHolder = (GalleryViewHolder) holder;
            ImageView imageView = galleryViewHolder.getImage();
            StorageReference ref = DatabaseConstant.getInstance().getStorageRef().child("images/" + photo);

            final long TEN_MEGABYTES = 2048 * 2048;
            ref.getBytes(TEN_MEGABYTES).addOnSuccessListener(bytes -> {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 2, bmp.getHeight() / 2, false));
            }).addOnFailureListener(exception -> {
                // Handle any errors
            }).addOnCompleteListener(task -> {

            });
        }

    }

    @Override
    public int getItemCount() {
        return _tripPhotos.size();
    }
}