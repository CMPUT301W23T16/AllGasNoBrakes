package com.example.allgasnobrakes;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private final int CAMERA_PERMISSION_CODE = 101;
    private Button homeButton;
    private Button cameraButton;
    final String TAG = "Sample";
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},CAMERA_PERMISSION_CODE);

        homeButton = findViewById(R.id.home_button);
        cameraButton = findViewById(R.id.camera_button);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, QRListFragment.class, null)
                    .commit();
        }

        homeButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, QRListFragment.class, null)
                    .commit();
        });

        cameraButton.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, ScannerFragment.class, null)
                    .commit();

        });


    }
}