
package com.example.allgasnobrakes;

import android.Manifest;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.allgasnobrakes.adapters.MenuBarAdapter;
import com.example.allgasnobrakes.models.PPFViewModel;
import com.example.allgasnobrakes.models.PlayerProfile;
import com.example.allgasnobrakes.views.RegisterFragment;
import com.example.allgasnobrakes.views.SignInFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private PlayerProfile currentUser;
    private final FragmentManager fm = getSupportFragmentManager();
    private PPFViewModel viewModel;
    private MenuBarAdapter menuBarAdapter;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PPFViewModel.class);
        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA }, 101);
        setContentView(R.layout.activity_main);

        //Setting Day/Night Mode for the app (hopefully)
        //nirav kalola and Bruno Bieri on how your app can handle Day/Night mode for system default
        //https://stackoverflow.com/questions/18001551/day-night-theme-for-android-app#:~:text=For%20Setting%20Default%20Day,Mode%20use%20AppCompatDelegate.setDefaultNightMode%20%28AppCompatDelegate.MODE_NIGHT_NO%29%3B
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        /*
        We check if there has been a previous log in on the device. If the is, we log in the last
        user on the device, and ask for confirmation. If there isn't one, we prompt the new user
        to register
         */
        if (savedInstanceState == null) {
            String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//            String id = "DAJ101";
 //           String id = "MH415";

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
            setContentView(R.layout.menu_bar);
            currentUser = item;

            menuBarAdapter = new MenuBarAdapter(this);
            viewPager = findViewById(R.id.menu_pager);
            viewPager.setAdapter(menuBarAdapter);
            viewPager.setUserInputEnabled(false);
            viewPager.setOffscreenPageLimit(1);

            Bundle bundle = new Bundle();
            bundle.putSerializable("User", currentUser);
            bundle.putString("SortOrder", "Highest Score");
            menuBarAdapter.setBundle(bundle);

            TabLayout tabLayout = findViewById(R.id.menu_bar_tab_layout);
            new TabLayoutMediator(tabLayout, viewPager,
                    new TabLayoutMediator.TabConfigurationStrategy() {
                        @Override
                        public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                            if (position == 0) {
                                tab.setText("Home");
                            } else if (position == 1) {
                                tab.setText("Map");
                            } else if (position == 2) {
                                tab.setText("Camera");
                            } else if (position == 3) {
                                tab.setText("Leaderboard");
                            } else if (position == 4) {
                                tab.setText("Profile");
                            }
                        }
                    }).attach();
        });

    }

}