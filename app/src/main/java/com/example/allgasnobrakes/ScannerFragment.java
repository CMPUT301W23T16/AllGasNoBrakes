package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
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
        PlayerProfile playerProfile = (PlayerProfile) requireArguments().getSerializable("User");
        FirebaseFirestore db;
        final String TAG = "Sample";
        db = FirebaseFirestore.getInstance();
        final CollectionReference playerReference = db.collection("Users").document(playerProfile.getUsername()).collection("QRRef");
        final CollectionReference collectionReference = db.collection("/QR");
        final DocumentReference playerAttributes = db.collection("Users").document(playerProfile.getUsername());
        Button confirm = root.findViewById(R.id.confirm_button);
        EditText comment = root.findViewById(R.id.comment);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sha256hex != null){
                    HashMap<String, Object> QRData = new HashMap<>();
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

            }
        });
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
