package com.example.allgasnobrakes;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.google.common.hash.Hashing;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.Result;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

/**
 * Handles operations with code scanner
 * @author zhaoyu4 zhaoyu5
 * @version 3.0
 */
public class ScannerFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private int total = 0;
    private String sha256hex;
    private String name;
    private String car;
    private String lastPlace;
    FusedLocationProviderClient client;

    private ImageView imgCamera;
    private ProgressDialog progressDialog;
    private Bitmap img;

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

        TextView scannedView = root.findViewById(R.id.scannedView);

        PlayerProfile playerProfile = (PlayerProfile) requireArguments().getSerializable("User");
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        ToggleButton location = root.findViewById(R.id.location_button);
        Button confirm = root.findViewById(R.id.confirm_button);
        EditText comment = root.findViewById(R.id.comment);

        //The button is for photo of surrounding area
        Button btnCamera = root.findViewById(R.id.photo_taking_btn);
        imgCamera = root.findViewById(R.id.imgSurround);

        progressDialog = new ProgressDialog(getActivity());

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sha256hex == null) {
                    Toast.makeText(activity, "Scan QR First", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    someActivityResultLauncher.launch(iCamera);
                }
            }
        });

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sha256hex = Hashing.sha256()
                                .hashString(result.getText(), StandardCharsets.UTF_8)
                                .toString();
                        name = NameGenerator.Generate(sha256hex);
                        car = CarGenerator.Generate(sha256hex);

                        FirebaseFirestore.getInstance().document("/QR/" + sha256hex).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot ds = task.getResult();

                                        if (ds.exists()) {
                                            lastPlace = String.format(Locale.CANADA, "%d", (((ArrayList<String>) (ds.get("OwnedBy"))).size()));
                                            Log.d("lastPlace", String.format(Locale.CANADA, "%d", ((ArrayList<String>) (ds.get("OwnedBy"))).size()));
                                        } else {
                                            lastPlace = "0";
                                        }

                                        String scannedContent = String.format(Locale.CANADA, "%s\n%s\n%s other players own this car.", name, car, lastPlace);
                                        scannedView.setText(scannedContent);
                                    }
                                });

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

                scannerView.animate()
                        .alpha(0f)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                scannerView.setVisibility(View.GONE);
                            }
                        });

                scannedView.setVisibility(View.VISIBLE);
                scannedView.setAlpha(0f);
                scannedView.animate()
                        .alpha(1f)
                        .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .setListener(null);

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

                        if (sha256hex != null ) {
                            HashedQR newQR = new HashedQR(sha256hex, total, name, car,
                                    comment.getText().toString(),
                                    QRData.get("Latitude"), QRData.get("Longitude"));

                            playerProfile.addQR(newQR);
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

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        img = (Bitmap)(data.getExtras().get("data"));
                        imgCamera.setImageBitmap(img);
                        compressImages();
                    }
                }
            });

    private void compressImages() {
        progressDialog.setMessage("Images Uploading...");
        progressDialog.show();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        byte[] imgByte = stream.toByteArray();
        uploadImages(imgByte);
    }

    private void uploadImages(byte[] imgByte){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child("images"+sha256hex+"jpg");
        storageReference.putBytes(imgByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}