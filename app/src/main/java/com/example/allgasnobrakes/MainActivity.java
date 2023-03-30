
package com.example.allgasnobrakes;

import android.Manifest;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private PlayerProfile currentUser;
    private final FragmentManager fm = getSupportFragmentManager();
//    private final int CAMERA_PERMISSION_CODE = 101;

    private PPFViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PPFViewModel.class);
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA }, 101);


        setContentView(R.layout.activity_main);

        /*
        We check if there has been a previous log in on the device. If the is, we log in the last
        user on the device, and ask for confirmation. If there isn't one, we prompt the new user
        to register
         */
        if (savedInstanceState == null) {
//            String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//            String id = "DAJ101";
            String id = "MH415";

            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            db.useEmulator("10.0.2.2", 8080);
            DocumentReference cloudID = db.collection("DeviceID").document(id);

            //https://firebase.google.com/docs/firestore/query-data/get-data
            cloudID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Bundle bundle = new Bundle();

                        if (document.exists()) {
                            Log.d("Log In", "DocumentSnapshot data: " + document.getData());
                            bundle.putString("LastUser", document.get("LastUser").toString()); // TODO: add error checking
                            fm.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container, SignInFragment.class, bundle)
                                    .commit();
                        } else {
                            Log.d("Log In", "No such document");
                            bundle.putString("deviceID", id);
                            fm.beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.fragment_container, RegisterFragment.class, bundle)
                                    .commit();
                        }

                    } else {
                        Log.d("Log In", "get failed with ", task.getException());
                    }
                }
            });
        }

        /*
        We have received player information, load into the homepage
         */
        viewModel.getSelectedPlayer().observe(this, item -> {
            setContentView(R.layout.split_fragment);
            currentUser = item;

            Bundle bundle = new Bundle();
            bundle.putSerializable("User", currentUser);
            bundle.putString("SortOrder", "Highest Score");

            fm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.split_container, QRListFragment.class, bundle)
                    .replace(R.id.menu_bar_container, MenuBarFragment.class, bundle)
                    .commit();
        });

    }

}