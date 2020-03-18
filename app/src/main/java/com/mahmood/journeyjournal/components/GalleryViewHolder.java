package com.mahmood.journeyjournal.components;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.interfaces.RecyclerViewClickListener;

public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView _imageView;
    private RecyclerViewClickListener _listener;

    public GalleryViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
        super(itemView);
        _imageView = itemView.findViewById(R.id.image_view_gallery);
        _listener = listener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (_listener != null) {
            _listener.onItemClick(v, getAdapterPosition());
        }
    }

    public ImageView getImage() {
        return _imageView;
    }

    public void setImage(ImageView imageView) {
        _imageView = imageView;
    }
}
