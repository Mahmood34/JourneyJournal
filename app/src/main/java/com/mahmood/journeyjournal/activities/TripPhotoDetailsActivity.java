package com.mahmood.journeyjournal.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.models.TripPhoto;

public class TripPhotoDetailsActivity extends AppCompatActivity {

    private TripPhoto _tripPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_photo_details);
        _tripPhoto = getIntent().getExtras().getParcelable("photo");
        ImageView imageView = findViewById(R.id.trip_photo_details_photo);
        imageView.setImageURI(Uri.parse(_tripPhoto.getImage()));
    }
}
