package com.example.allgasnobrakes;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;

/**
 * Handles operations with code scanner
 * @author zhaoyu4 zhaoyu5
 * @version 2.0
 */
public class ScannerFragment extends Fragment {
    private CodeScanner mCodeScanner;
    int total = 0;
    String sha256hex;
    String com;
    FusedLocationProviderClient client;

    public ScannerFragment() {
        super(R.layout.scanner);
    }

    /**
     * Overridden to add functionality for scanning a QR code, obtaining the sha256 hash, calling
     * other classes to build a QR code, and storing the QR appropriately to the cloud
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The created view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate(R.layout.scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.startPreview();
        TextView t = root.findViewById(R.id.tv_textView);
        PlayerProfile playerProfile = (PlayerProfile) requireArguments().getSerializable("User");
        FirebaseFirestore db;
        final String TAG = "Sample";
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        ToggleButton location = root.findViewById(R.id.location_button);
        db = FirebaseFirestore.getInstance();
        final CollectionReference playerReference = db.collection("Users").document(playerProfile.getUsername()).collection("QRRef");
        final CollectionReference collectionReference = db.collection("/QR");
        final DocumentReference playerAttributes = db.collection("Users").document(playerProfile.getUsername());
        Button confirm = root.findViewById(R.id.confirm_button);
        EditText comment = root.findViewById(R.id.comment);
        confirm.setOnClickListener(new View.OnClickListener() {
            HashMap<String, Object> QRData = new HashMap<>();
            @Override
            public void onClick(View v) {
                if (location.isChecked()) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // When permission is granted
                        // Call method
                        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();

                                // Check condition
                                if (location != null) {
                                    QRData.put("Longitude", String.valueOf(location.getLongitude()));
                                    QRData.put("Latitude", String.valueOf(location.getLatitude()));
                                } else {

                                    LocationRequest locationRequest = new LocationRequest()
                                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                            .setInterval(10000)
                                            .setFastestInterval(
                                                    1000)
                                            .setNumUpdates(1);

                                    // Initialize location call back
                                    LocationCallback locationCallback = new LocationCallback() {
                                        @Override
                                        public void
                                        onLocationResult(LocationResult locationResult) {
                                            // Initialize
                                            // location
                                            Location location1 = locationResult.getLastLocation();
                                            // Set latitude
                                            QRData.put("Longitude", String.valueOf(location1.getLongitude()));
                                            QRData.put("Latitude", String.valueOf(location1.getLatitude()));

                                        }

                                    };
                                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                                }

                            }
                        });
                    }


                }
                else {
                    // When location service is not enabled
                    // open location setting
                    QRData.put("Longitude", "");
                    QRData.put("Latitude", "");
                }

                if (sha256hex != null ){

                    QRData.put("QRReference", "/" + collectionReference.document(sha256hex).getPath());
                    QRData.put("Comment", comment.getText().toString());
                    NameGenerator name = new NameGenerator(sha256hex);
                    CarGenerator car = new CarGenerator(sha256hex);
                    if (total>=0 && sha256hex.length()>0) {
                        collectionReference
                                .document(sha256hex)
                                .set(new HashMap<String, Object>(){
                                    {put("Score", total);
                                        put("Name", name.Generate());
                                        put("Hash", sha256hex);
                                        put("Face",car.Generate());}})
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // These are a method which gets executed when the task is succeeded
                                        Log.d("collectionRef", "Data has been added successfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // These are a method which gets executed if there’s any problem
                                        Log.d("collectionRef", "Data could not be added!" + e.toString());
                                    }
                                });
                    }
                    playerReference.document(sha256hex).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "Document exists!");
                                    playerReference
                                            .document(sha256hex)
                                            .set(QRData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("playerRef", "Data has been added successfully!");

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("playerRef", "Data could not be added!" + e.toString());
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Document does not exist!");
                                    playerReference
                                            .document(sha256hex)
                                            .set(QRData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // These are a method which gets executed when the task is succeeded
                                                    Log.d("playerRef", "Data has been added successfully!");

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // These are a method which gets executed if there’s any problem
                                                    Log.d("playerRef", "Data could not be added!" + e.toString());
                                                }
                                            });
                                    playerAttributes.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                            int count = ((Number) task.getResult().get("QR Count")).intValue();
                                            int score = ((Number) task.getResult().get("Total Score")).intValue();
                                            playerAttributes.update("Total Score", score+total);
                                            playerAttributes.update("QR Count", count+1);

                                        }
                                    });
                                }
                            } else {
                                Log.d(TAG, "Failed with: ", task.getException());
                            }
                        }
                    });

                }

            }
        });
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sha256hex = DigestUtils.sha256Hex(result.getText());
                        char starting = sha256hex.charAt(0);
                        String current = "";
                        for (int i = 1; i < sha256hex.length(); i++){
                            if (starting != sha256hex.charAt(i)){
                                starting = sha256hex.charAt(i);
                                if (current.length() != 0){
                                    String hex = String.format("%c",current.charAt(0));
                                    int integer = Integer.parseInt(hex, 16);
                                    if (integer == 0){
                                        total += Math.pow(20,current.length());
                                    }else{
                                        total += Math.pow(integer,current.length());
                                    }
                                }
                                current = "";
                            }
                            else{
                                current = current + starting;
                            }
                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });


        return root;
    }
}