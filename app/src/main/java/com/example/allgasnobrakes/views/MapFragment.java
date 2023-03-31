package com.example.allgasnobrakes.views;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.allgasnobrakes.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ClosingFuture;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.SphericalUtil;


public class MapFragment extends Fragment {
    private GoogleMap mMap;
    private SupportMapFragment supportMapFragment;
    private Button search;
    private EditText lat;
    private EditText lon;
    public MapFragment() {
        super(R.layout.scanner);
    }
    FusedLocationProviderClient client;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.map, container, false);
        search = root.findViewById(R.id.button);
        lat = root.findViewById(R.id.lat);
        lon = root.findViewById(R.id.lon);
        return root;
    }

    @Override
    public void onPause() {
        mMap.clear();
        super.onPause();
    }
    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onResume() {
        super.onResume();
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LatLng location = new LatLng(Double.parseDouble(lat.getText().toString()),Double.parseDouble(lon.getText().toString()));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(location).visible(true).icon(getMarkerIcon("#2243ff"));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                FirebaseFirestore.getInstance().collection("Geo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot QR : task.getResult()) {
                            LatLng allLatLang = new LatLng(Double.parseDouble(QR.get("Lat").toString()),Double.parseDouble(QR.get("Lon").toString()));
                            MarkerOptions markerOptions = new MarkerOptions();
                            mMap.addMarker(markerOptions.position(allLatLang).visible(false));
                            if (SphericalUtil.computeDistanceBetween(location, mMap.addMarker(markerOptions).getPosition()) < 1200) {
                                mMap.addMarker(markerOptions).setVisible(true);
                            }
                        }
                    }
                });
            }
        });
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            LatLng yourLatLang = new LatLng(location.getLatitude(),location.getLongitude());
                            FirebaseFirestore.getInstance().collection("Geo").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot QR : task.getResult()) {
                                        Log.d("test1231231",  QR.get("Lon").toString());
                                        LatLng allLatLang = new LatLng(Double.parseDouble(QR.get("Lat").toString()),Double.parseDouble(QR.get("Lon").toString()));
                                        MarkerOptions markerOptions = new MarkerOptions();
                                        mMap.addMarker(markerOptions.position(allLatLang).visible(false));
                                        if (SphericalUtil.computeDistanceBetween(yourLatLang, mMap.addMarker(markerOptions).getPosition()) < 1200) {
                                            mMap.addMarker(markerOptions).setVisible(true);
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
                googleMap.setMyLocationEnabled(true);

            }
        });
    }
}
