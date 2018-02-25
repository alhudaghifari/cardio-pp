package com.urbannightdev.cardiopp.activity.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.urbannightdev.cardiopp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String accessToken=null;
    private boolean mLocationPermissionGranted;

    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private final LatLng mDefaultLocation = new LatLng(107.611967, -6.900387);
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int DEFAULT_ZOOM = 15;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapslong);
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private String token;

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-6.915, 107.61);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        Log.d("MASPACTIVITY", "on map ready");

//        getLocationPermission();
////
////        updateLocationUI();
////
////        getDeviceLocation();
//
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        new LandmarkMapping().execute(location);
    }


    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("mapsactivity", "Current location is null. Using defaults.");
                            Log.e("mapsactivity", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    class LandmarkMapping extends AsyncTask<Location, Integer, Void> {

        @Override
        protected Void doInBackground(Location... params) {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
            Request request = new Request.Builder()
                    .url("https://api.mainapi.net/token")
                    .post(body)
                    .addHeader("Authorization", "Basic RWQwTDZCOVJPcHdTU242NnZWblVzZGo5MGFRYTpfVEx2TmpMRVBORVQwVjRyeWR4VHlFQm5ZNXNh") // get token
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "0803bf9d-0182-42a6-8a9b-c26113190a91")
                    .build();


//                    .addHeader("Authorization", "Basic RWQwTDZCOVJPcHdTU242NnZWblVzZGo5MGFRYTpfVEx2TmpMRVBORVQwVjRyeWR4VHlFQm5ZNXNh")
//                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                    .addHeader("Cache-Control", "no-cache")
//                    .addHeader("Postman-Token", "817de5c4-a88f-4c75-8154-93aa7315f9f9")
//                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                accessToken = jsonObject.getString("access_token");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Location location = new Location("");//params[0];
            location.setLatitude(mDefaultLocation.latitude);
            location.setLongitude(mDefaultLocation.longitude);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();

            client = new OkHttpClient();

            request = new Request.Builder()
                    .url("https://api.mainapi.net/arcgis/0.0.2/35/query/1000?geometry=" + location.getLongitude() + "," + location.getLatitude())
                    .get()
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "79aa27b1-6518-45ec-9b73-442729276add")
                    .build();

            String jsonRes = null;

            try {
                Response response = client.newCall(request).execute();
                jsonRes = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(jsonRes);
                JSONArray jsonArray = jsonObject.getJSONArray("features");
                List<LatLng> latLngList = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject geo = jsonArray.getJSONObject(i).getJSONObject("geometry");
                    latLngList.add(new LatLng(geo.getDouble("y"),geo.getDouble("x")));
                }
                for(final LatLng latLng:latLngList){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMap.addMarker(new MarkerOptions().position(latLng));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}