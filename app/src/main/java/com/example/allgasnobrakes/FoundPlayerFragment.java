package com.example.allgasnobrakes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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

public class FoundPlayerFragment extends DialogFragment {
    private String username1;
    private String email;
    

    private RecyclerView QRList;
    private RecyclerView.Adapter QrAdapter;

    public void main(String username, String email) {
        // Get information from the selected city to be edited
        this.username1  = username;
        this.email = email;



    }
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.foundplayerprofile, null);
        ArrayList<HashedQR> QRList2 = new ArrayList<HashedQR>();
        TextView username = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        username.setText(username1);
        email.setText(this.email);
        QRList = view.findViewById(R.id.codes_list1);
        QRList.setLayoutManager(new LinearLayoutManager(getActivity()));
        PlayerProfile user = new PlayerProfile(username1,this.email,"test");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users").document(username1).collection("QR");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d("test2",doc.getId() );
                    DocumentReference p = db.collection("QR").document(doc.getId());
                    p.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("test", "DocumentSnapshot data: " + document.getData().get("Score"));
                                    String QRHash = doc.getId();
                                    String QRName = (String) document.getData().get("Name");
                                    Number QRScore = (Number) document.getData().get("Score");
                                    QRList2.add(new HashedQR(QRHash, QRScore.intValue(), QRName));
                                    QrAdapter.notifyDataSetChanged();
                                } else {
                                    Log.d("test", "No such document");
                                }
                            } else {
                                Log.d("test", "get failed with ", task.getException());
                            }
                        }
                    });

                }

            }
        });
        QrAdapter = new QrArrayAdapter(QRList2, getActivity());
        QRList.setAdapter(QrAdapter);










        return builder
                .setView(view)
                .setNegativeButton("Exit", null)
                .create();
    }

}
