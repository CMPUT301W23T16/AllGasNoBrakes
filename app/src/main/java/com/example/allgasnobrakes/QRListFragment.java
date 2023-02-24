package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Handles operations with QR code list
 * @author zhaoyu4
 * @version 1.0
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
                    Log.d(TAG, String.valueOf(doc.getData().get("Score")));
                    String hash = doc.getId();
                    String Score = (String) doc.getData().get("Score");
                    player_Qr.add(new HashedQR(hash, Integer.parseInt(Score))); // Adding the cities and provinces from FireStore
                }
                QrAdapter.notifyDataSetChanged();
            }
        });


        return root;
    }

}
