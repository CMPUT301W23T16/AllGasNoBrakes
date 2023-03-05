package com.example.allgasnobrakes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Contains player profile information
 * @author zhaoyu4
 * @version 2.0
 */
public class PlayerProfile implements Serializable {
    private String username;
    private String email;
    private String password;
    private ArrayList<HashedQR> QRList = new ArrayList<>();

    public PlayerProfile() {
        this.username = "username";
        this.email = "email";
        this.password = "password";
    }

    public PlayerProfile(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<HashedQR> getQRList() {
        return QRList;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setQRList(ArrayList<HashedQR> QRList) {
        this.QRList = QRList;
    }

    public void retrieveQR(RecyclerView.Adapter QrAdapter) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users")
                .document(username).collection("QR");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                QRList.clear();

                ArrayList<String> QRS = new ArrayList<>();
                for(QueryDocumentSnapshot QRRef: queryDocumentSnapshots) {
                    DocumentReference hashedQR = db.document(QRRef.getString("QRReference"));
                    QRS.add(hashedQR.getId());
                }

                for (String i: QRS) {Log.d("Test", i);}

                if (! QRS.isEmpty()) {
                    db.collection("QR")
                            .whereIn("Hash", QRS)
                            .orderBy("Score", Query.Direction.DESCENDING)
                            .orderBy("Name", Query.Direction.ASCENDING)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value != null) {
                                        for (QueryDocumentSnapshot QR: value) {
                                            String QRHash = QR.getId();
                                            String QRName = (String) QR.get("Name");
                                            Number QRScore = (Number) QR.get("Score");
                                            QRList.add(new HashedQR(QRHash, QRScore.intValue(), QRName));
                                            // QRList.sort(new HashedQR().reversed());
                                            QrAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                }
            }
        });
        Log.d("Test", "Called");
    }
}
