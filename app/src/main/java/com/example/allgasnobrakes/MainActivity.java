package com.example.allgasnobrakes;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private PlayerProfile currentUser;
    private final FragmentManager fm = getSupportFragmentManager();
    private final int CAMERA_PERMISSION_CODE = 101;
    private Leaderboard viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(Leaderboard.class);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                CAMERA_PERMISSION_CODE);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, SignInFragment.class, null)
                    .commit();
        }

        viewModel.getSelectedPlayer().observe(this, item -> {
            setContentView(R.layout.split_fragment);
            currentUser = item;
        });
    }
}