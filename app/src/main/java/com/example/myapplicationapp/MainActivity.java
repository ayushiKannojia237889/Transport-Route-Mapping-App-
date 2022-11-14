package com.example.myapplicationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSION_FINE_LOCATION = 99;
    LocationCallback locationCallback;
    private String lat_value;
    GoogleMap mMap;
    Switch location_updates;
    EditText latlong,geom;

    private Polyline line;
    List<LatLng> latLngList = new ArrayList<>();
    private LatLng point;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    boolean isPermissionGranter;
    private final int REQUEST_CHECK_CODE = 8989;
    private LocationSettingsRequest.Builder builder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("               Transport App");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        location_updates = findViewById(R.id.location_updates);
        latlong = findViewById(R.id.latlong);
        //geom = findViewById(R.id.geom);

        initviews();
        if (isPermissionGranter) {
            if (checkGooglePlayServices()) {
                Toast.makeText(this, "Google play Services  Available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Google play Services not Available", Toast.LENGTH_SHORT).show();
            }
        }
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationCallback = new LocationCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                LatLng latLng = new LatLng(location.getLongitude(),location.getLatitude());
                latLngList.add(latLng);
                PolylineOptions polylineOptions = new PolylineOptions()
                        .addAll(latLngList)
                        .width(5)
                        .color(Color.GREEN)
                        .geodesic(true);
                for (int i = 0; i < latLngList.size(); i++) {
                    point = latLngList.get(i);
                }
                polylineOptions.add(point);
                line = mMap.addPolyline(polylineOptions);
                latLngList.add(latLng);
                //resource


                // string formatted code
                StringBuilder stringBuilder = new StringBuilder();
                latLngList.forEach(s -> stringBuilder.append(s).append(":"));
                String s = stringBuilder.toString();
                String k = s.replace("lat/lng:", "").
                        replace("[", "")
                        .toString().replace("]", "").toString()
                        .replaceAll("[(){}]","").toString()
                        .replace(","," ").toString()
                        .replace(":",",").toString();
                String h = "LINESTRING(" +k ;
                // removing the last comma from a string
                h= h.substring(0,h.length()-1) + ")";
                latlong.setText(h);
//               String m = "ST_GeomFromText(' "  + h +  " ')";
//               geom.setText(m);
                Log.i("my tag" ,"result:"+ h);
                Toast.makeText(MainActivity.this, "location:" + h, Toast.LENGTH_SHORT).show();

      }
        };

        location_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (location_updates.isChecked()) {
                    startLocationUpdates();

                } else {
                    stopLocationUpdates();

                }
            }
        });
        updateGPS();

    }
    private void startLocationUpdates() {
        Toast.makeText(this, "service start", Toast.LENGTH_SHORT).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();

    }

    private void stopLocationUpdates() {
        Toast.makeText(this, "service off", Toast.LENGTH_SHORT).show();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        String lat_value = latlong.getText().toString().trim();
        //String geom_value = geom.getText().toString().trim();
        Intent i = new Intent(MainActivity.this,UserForm.class);
        i.putExtra("lat_value",lat_value);
       // i.putExtra("geom_value",geom_value);
        startActivity(i);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }


    }


    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                MainActivity.this
        );
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permission
                    // Toast.makeText(MainActivity.this, "location:" +location.getLatitude()+":"+location.getLongitude(), Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }


    private boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(result)) {

            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(MainActivity.this, "User Canceled Dialog", Toast.LENGTH_SHORT).show();

                }
            });
            dialog.show();
        }


        return false;

    }

    private void initviews() {
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_map, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
        LocationRequest locationRequest = new LocationRequest()
                .setFastestInterval(1500)
                .setInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this)
                        .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity.this, REQUEST_CHECK_CODE);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            } catch (ClassCastException ex) {

                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE: {
                            break;
                        }

                    }
                }
            }
        });


    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }
}