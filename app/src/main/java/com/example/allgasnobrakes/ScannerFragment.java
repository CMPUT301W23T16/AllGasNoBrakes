package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
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
    public ScannerFragment() {
        super(R.layout.scanner);
    }
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

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sha256hex = DigestUtils.sha256Hex(result.getText());
                        t.setText(sha256hex);
                        char starting = sha256hex.charAt(0);
                        String current = "";
                        for (int i = 1; i < sha256hex.length(); i++){
                            if (starting != sha256hex.charAt(i)){
                                starting = sha256hex.charAt(i);
                                if (current.length() != 0){
                                    String hex = String.format("%c",current.charAt(0));
                                    int integer = Integer.parseInt(hex, 16);
                                    total += Math.pow(integer,current.length());
                                }
                                current = "";
                            }
                            else{
                                current = current + starting;
                            }
                        }
                        String totalstring = Integer.toString(total);

                        FirebaseFirestore db;
                        final String TAG = "Sample";
                        db = FirebaseFirestore.getInstance();
                        final CollectionReference playerReference = db.collection("Users").document(requireArguments().getString("Username")).collection("QR");
                        final CollectionReference collectionReference = db.collection("QR");
                        HashMap<String, Number> QRData = new HashMap<>();

                        if (total>0 && sha256hex.length()>0) {
                            QRData.put("Score", total);

                            collectionReference
                                    .document(sha256hex)
                                    .set(QRData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // These are a method which gets executed when the task is succeeded
                                            Log.d(TAG, "Data has been added successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // These are a method which gets executed if there’s any problem
                                            Log.d(TAG, "Data could not be added!" + e.toString());
                                        }
                                    });

                            playerReference
                                    .document(sha256hex)
                                    .set(new HashMap<String, String>(){{put("QRReference", collectionReference.document(sha256hex).getPath());}})
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // These are a method which gets executed when the task is succeeded
                                            Log.d(TAG, "Data has been added successfully!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // These are a method which gets executed if there’s any problem
                                            Log.d(TAG, "Data could not be added!" + e.toString());
                                        }
                                    });
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
