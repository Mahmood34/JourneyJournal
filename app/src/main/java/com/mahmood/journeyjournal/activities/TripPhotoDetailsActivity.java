package com.mahmood.journeyjournal.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.StorageReference;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;

import java.util.Objects;

public class TripPhotoDetailsActivity extends AppCompatActivity {

    private String _tripPhotoId;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_photo_details);

        _tripPhotoId = Objects.requireNonNull(getIntent().getExtras()).getString("photoId");
        ImageView imageView = findViewById(R.id.trip_photo_details_photo);

        StorageReference imageRef = DatabaseConstant.getInstance().getStorageRef().child("images/").child(_tripPhotoId);


        final long TEN_MEGABYTES = 2048 * 2048;
        imageRef.getBytes(TEN_MEGABYTES).addOnSuccessListener(bytes -> {
            // Data for "images/island.jpg" is returns, use this as needed
            bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(Bitmap.createBitmap(bmp));
        }).addOnFailureListener(exception -> {
            // Handle any errors
        }).addOnCompleteListener(task -> {

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        menu.findItem(R.id.save_menu_item).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_menu_item:

                //TODO save to storage
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();

                break;
            case R.id.share_button_item:
                StorageReference storageRef = DatabaseConstant.getInstance().getStorageRef();

                storageRef.child("images/" + _tripPhotoId)
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Hey, this is one of the photos I took on my trip " + "\n" +
                                            uri);
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, "Share photo");
                            startActivity(shareIntent);

                        }).addOnFailureListener(exception -> {
                    // Handle any errors
                }).addOnCompleteListener(task -> {
                });


                break;
            case R.id.delete_trip_item:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Photo");
                builder.setMessage("Are you sure you want to remove this Photo?");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", (dialog, which) -> {
                    DatabaseConstant.getInstance().removeTripPhoto(_tripPhotoId);
                    TripPhotoDetailsActivity.super.onBackPressed();
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show());
                builder.show();
                break;
            default:
                finish();
        }
        return true;
    }

}
