package com.example.allgasnobrakes;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapActivity extends AppCompatActivity {

    private TextView latitude,longitude;
    private LocationManager locationManager;
    private ToggleButton location_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        latitude = findViewById(R.id.latitude_text);
        longitude = findViewById(R.id.longitude_text);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location_button = findViewById(R.id.location_button);

        location_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 1, new LocationListener() {
                        @Override
                        public void onLocationChanged(@NonNull Location location) {
                            longitude.setText(String.valueOf(location.getLongitude()));
                            latitude.setText(String.valueOf(location.getLatitude()));
                        }
                    });}
                else {
                    longitude.setText("Longitude");
                    latitude.setText("Latitude");
                    Toast.makeText(getApplicationContext(),"Location is turned off",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}