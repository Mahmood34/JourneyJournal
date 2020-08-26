package com.mahmood.journeyjournal.ui.explore;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mahmood.journeyjournal.DatabaseConstant;
import com.mahmood.journeyjournal.R;
import com.mahmood.journeyjournal.activities.VisitedPlaceDetails;
import com.mahmood.journeyjournal.components.SearchPlacesBottomSheetFragment;
import com.mahmood.journeyjournal.models.VisitedPlace;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExploreFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final int DEFAULT_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 20;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    CameraUpdate cu;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationClient;
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private PlacesClient mPlacesClient;
    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private List[] mLikelyPlaceTypes;
    private HashMap<Marker, VisitedPlace> markerOptionsVisitedPlaceHashMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        // check permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int locationRequestCode = 1000;
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
        } else {
            // already permission granted
        }

        // Construct a PlacesClient
        Places.initialize(this.getContext(), getString(R.string.map_key));
        mPlacesClient = Places.createClient(this.requireContext());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    mLastKnownLocation = location;
                    if (location != null) {
                        // Logic to handle location object
                    }
                });

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExploreViewModel exploreViewModel = ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        FloatingActionButton button = requireActivity().findViewById(R.id.floating_action_button);
        button.setImageResource(R.drawable.ic_search_black_24dp);
        button.show();
        button.setOnClickListener(v -> {
            final SearchPlacesBottomSheetFragment sheetDialog = new SearchPlacesBottomSheetFragment();
            assert getFragmentManager() != null;
            sheetDialog.show(getFragmentManager(), sheetDialog.getTag());
        });

        Button _viewAllVisitedPlacesButton = root.findViewById(R.id.view_all_visited_places);

        _viewAllVisitedPlacesButton.setOnClickListener(v -> {


            DatabaseReference reference = DatabaseConstant.getInstance()
                    .getDatabaseRef()
                    .child(DatabaseConstant.VISITED_PLACE_REPOSITORY);
            markerOptionsVisitedPlaceHashMap = new HashMap<>();

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        VisitedPlace vp = ds.getValue(VisitedPlace.class);
                        assert vp != null;
                        MarkerOptions mo = new MarkerOptions()
                                .position(new LatLng(vp.getLat(), vp.getLng()))
                                .title(vp.getName())
                                .snippet("Click here for more details");
                        Marker marker = map.addMarker(mo);
                        builder.include(marker.getPosition());

                        markerOptionsVisitedPlaceHashMap.put(marker, vp);
                    }
                    if (!markerOptionsVisitedPlaceHashMap.isEmpty()) {
                        LatLngBounds bounds = builder.build();
                        int padding = 200; // offset from edges of the map in pixels
                        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        map.animateCamera(cu);
                    } else {
                        Toast.makeText(getActivity(), "You have no visited places saved", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };
            reference.addValueEventListener(valueEventListener);

        });

        MapView mapView = root.findViewById(R.id.explore_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);

        }

        exploreViewModel.getVisitedPlaces().observe(getViewLifecycleOwner(), visitedPlaces -> {
        });

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(requireContext());
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(true);
        map.setOnInfoWindowClickListener(this);

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
        updateLocationUI();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this.requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 15));
                        }
                    } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
                        map.animateCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void showCurrentPlace() {
        if (map == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG, Place.Field.TYPES);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    mPlacesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();

                    // Set the count, handling cases where less than 5 entries are returned.
                    int count = Math.min(likelyPlaces.getPlaceLikelihoods().size(), M_MAX_ENTRIES);

                    int i = 0;

                    mLikelyPlaceNames = new String[count];
                    mLikelyPlaceAddresses = new String[count];
                    mLikelyPlaceAttributions = new List[count];
                    mLikelyPlaceLatLngs = new LatLng[count];
                    mLikelyPlaceTypes = new List[count];

                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        // Build a list of likely places to show the user.

                        mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                        mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                        mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                .getAttributions();
                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();
                        mLikelyPlaceTypes[i] = placeLikelihood.getPlace().getTypes();

                        i++;
                        if (i > (count - 1)) {
                            break;
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    ExploreFragment.this.openPlacesDialog();
                } else {
                    Log.e("TAG", "Exception: %s", task.getException());
                }
            });
        } else {
//            // The user has not granted permission.
//            Log.i("TAG", "The user did not grant location permission.");
//
//            // Add a default marker, because the user hasn't selected a place.
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // The "which" argument contains the position of the selected item.
            LatLng markerLatLng = mLikelyPlaceLatLngs[which];
            String markerSnippet = mLikelyPlaceAddresses[which];
            if (mLikelyPlaceAttributions[which] != null) {
                markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            map.addMarker(new MarkerOptions()
                    .title(mLikelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet));

            // Position the map's camera at the location of the marker.
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM));
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(this.requireContext())
                .setTitle("Pick Place")
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        VisitedPlace vp = markerOptionsVisitedPlaceHashMap.get(marker);
        Intent intent = new Intent(getContext(), VisitedPlaceDetails.class);
        intent.putExtra("visitedPlace", vp);
        startActivityForResult(intent, 1);
    }
}