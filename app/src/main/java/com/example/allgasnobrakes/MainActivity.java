package com.example.allgasnobrakes;

import android.Manifest;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
            String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference cloudID = db.collection("DeviceID").document(id);

            //https://firebase.google.com/docs/firestore/query-data/get-data
            cloudID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Bundle bundle = new Bundle();

                        if (document.exists()) {
                            Log.d("User", "DocumentSnapshot data: " + document.getData());
                            bundle.putString("LastUser", document.get("LastUser").toString()); // TODO: add error checking
                            fm.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container, SignInFragment.class, bundle)
                                    .commit();
                        } else {
                            Log.d("User", "No such document");
                            bundle.putString("deviceID", id);
                            fm.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container, RegisterFragment.class, bundle)
                                    .commit();
                        }

                    } else {
                        Log.d("User", "get failed with ", task.getException());
                    }
                }
            });
        }

        viewModel.getSelectedPlayer().observe(this, item -> {
            setContentView(R.layout.split_fragment);
            currentUser = item;

            Bundle bundle = new Bundle();
            bundle.putString("Username", currentUser.getUsername());
            bundle.putString("Email", currentUser.getEmail());

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.split_container, QRListFragment.class, bundle)
                    .replace(R.id.menu_bar_container, MenuBarFragment.class, bundle)
                    .commit();
        });
    }
}