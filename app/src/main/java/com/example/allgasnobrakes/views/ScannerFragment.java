package com.example.allgasnobrakes.views;

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

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.allgasnobrakes.MainActivity;
import com.example.allgasnobrakes.models.CarGenerator;
import com.example.allgasnobrakes.models.HashedQR;
import com.example.allgasnobrakes.models.NameGenerator;
import com.example.allgasnobrakes.models.PlayerProfile;
import com.example.allgasnobrakes.R;
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
import com.google.firebase.firestore.FieldValue;
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
 * Handles operations with code scanner. Also for taking and uploading photos
 * @author zhaoyu4 zhaoyu5 dek theresag
 * @version 4.0
 */
public class ScannerFragment extends Fragment {
    private Activity activity;
    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;
    private TextView scannedView;
    private ToggleButton location;
    private Button confirm;
    private EditText comment;
    private int total = 0;
    private String sha256hex;
    private String name;
    private String car;
    private String lastPlace;
    private boolean owned = false;
    FusedLocationProviderClient client;

    private ImageView imgCamera;
    private ProgressDialog progressDialog;
    private Bitmap img;
    private String downloadURL;

    public ScannerFragment() {
        super(R.layout.scanner);
    }

    /**
     *  Allow user to scan QR codes and take a picture of the surrounding areas
     *
     *  Overridden to add functionality for scanning a QR code, obtaining the sha256 hash,
     *  calling other classes to build QR code, taking a photo, and storing the QR and photograph
     *  appropriately to the cloud.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the created view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        View root = inflater.inflate(R.layout.scanner, container, false);

        scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);

        scannedView = root.findViewById(R.id.scannedView);
        client = LocationServices.getFusedLocationProviderClient(getActivity());

        location = root.findViewById(R.id.location_button);
        confirm = root.findViewById(R.id.confirm_button);
        comment = root.findViewById(R.id.comment);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnCamera = view.findViewById(R.id.photo_taking_btn);
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
                                        String playerName = currentUser().getUsername();

                                        if (ds.exists()) {
                                            ArrayList<String> ownedBy = (ArrayList<String>) (ds.get("OwnedBy"));

                                            if (ownedBy.contains(playerName)) {
                                                owned = true;
                                            } else {
                                                lastPlace = String.format(Locale.CANADA, "%d", (ownedBy.size()));
                                                Log.d("lastPlace", lastPlace);
                                            }
                                        } else {
                                            lastPlace = "0";
                                        }

                                        String scannedContent;
                                        if (! owned) {
                                            scannedContent = String.format(Locale.CANADA, "%s\n%s\n%s others own this car.", name, car, lastPlace);
                                            setConfBtn();
                                        } else {
                                            scannedContent = name + "\n" + car + "\nLooks like you own this car already.";
                                        }

                                        scannedView.setText(scannedContent);
                                        btnCamera.setVisibility(View.VISIBLE);
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
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        //The button will take the user to fragment to take photo of surrounding area
        imgCamera = view.findViewById(R.id.imgSurround);

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

    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        imgCamera.setImageResource(android.R.color.transparent);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        total = 0;
        owned = false;
        scannedView.setAlpha(0f);
        scannerView.setVisibility(View.VISIBLE);
        scannerView.setAlpha(1f);
        mCodeScanner.startPreview();
    }

    /**
     * Sets the on click listener for the confirm button
     */
    public void setConfBtn() {
        confirm.setOnClickListener(new View.OnClickListener() {
            HashMap<String, String> QRData = new HashMap<>();
            @Override
            public void onClick(View v) {
                if (location.isChecked()&& sha256hex != null) {
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
                                HashedQR newQR = new HashedQR(sha256hex, total, name, car,
                                        comment.getText().toString(),
                                        QRData.get("Latitude"), QRData.get("Longitude"));

                                currentUser().addQR(0, newQR);
                                HashMap<String, Object> meta = new HashMap<>();
                                meta.put("Lat",QRData.get("Latitude"));
                                meta.put("Lon",QRData.get("Longitude"));
                                FirebaseFirestore.getInstance().collection("Geo").document( QRData.get("Latitude").toString() + ","+  QRData.get("Longitude").toString())
                                        .set(meta);
                            }
                        });
                    }
                }
                else {
                    // When location service is not enabled
                    // open location setting
                    QRData.put("Longitude", "");
                    QRData.put("Latitude", "");
                    HashedQR newQR = new HashedQR(sha256hex, total, name, car,
                            comment.getText().toString(),
                            QRData.get("Latitude"), QRData.get("Longitude"));

                    currentUser().addQR(0, newQR);
                }
            }
        });
    }
    
    //https://www.youtube.com/watch?v=M-sIL8OL18o
    //https://www.youtube.com/watch?v=kIgpSokJgzY
    //How To Compress And Upload Images In Firebase Storage

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
                .child(currentUser().getUsername()
                        +sha256hex+".jpg");
        storageReference.putBytes(imgByte).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();

                downloadURL = storageReference.getDownloadUrl().toString();
                Log.d("Upload", "Download URL: "+downloadURL);
                linkToQRCode();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Add a download link of the photo to the QR code
    private void linkToQRCode() {
        //update the QR doc with the new information
        FirebaseFirestore.getInstance().collection("QR").document(sha256hex)
                .update("Images", FieldValue.arrayUnion(downloadURL));

        Log.d("Upload", "Photo URL added to QR code");
    }

    private PlayerProfile currentUser() {
        return MainActivity.getCurrentUser();
    }
}
