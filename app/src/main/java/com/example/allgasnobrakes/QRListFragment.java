package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Handles operations with QR code list
 * @author zhaoyu4 zhaoyu5
 * @version 2.0
 */

public class QRListFragment extends Fragment  {

    private RecyclerView QRList;
    private RecyclerView.Adapter QrAdapter;
    protected ArrayList<HashedQR> player_Qr;
    final String TAG = "Sample";

    public QRListFragment() {
        super(R.layout.homepage);
    }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.homepage, container, false);
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        final String username = requireArguments().getString("Username");
        Log.d("user", username);
        final Activity activity = getActivity();
        final CollectionReference collectionReference = db.collection("Users").document(username).collection("QR");


        player_Qr = new ArrayList<>();
        QRList = root.findViewById(R.id.codes_list);
        QRList.setLayoutManager(new LinearLayoutManager(activity));
        QrAdapter = new QrArrayAdapter(player_Qr, activity);
        QRList.setAdapter(QrAdapter);


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                player_Qr.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    // Log.d(TAG, String.valueOf(doc.getData().get("Score")));
                    String QRReference = (String) doc.get("QRReference");
                    if (QRReference != null) {
                        DocumentReference documentReference = db.document(QRReference);
                        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();

                                    if (document.exists()) {
                                        Log.d("QR", "DocumentSnapshot data: " + document.getData());
                                        String hash = document.getId();
                                        Number score = (Number) document.get("Score");
                                        player_Qr.add(new HashedQR(hash, score.intValue()));
                                        QrAdapter.notifyDataSetChanged();
                                    } else {
                                        Log.d("QR", "No such document");
                                    }

                                } else {
                                    Log.d("QR", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });
        return root;
    }

}
