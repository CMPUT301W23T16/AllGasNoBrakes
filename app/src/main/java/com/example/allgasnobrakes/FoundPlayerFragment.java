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
        TextView username = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        username.setText(username1);
        email.setText(this.email);
        QRList = view.findViewById(R.id.codes_list1);
        QRList.setLayoutManager(new LinearLayoutManager(getActivity()));
        PlayerProfile user = new PlayerProfile(username1, this.email);

        QrAdapter = new QrArrayAdapter(user.getQRList(), getActivity());
        QRList.setAdapter(QrAdapter);
        user.retrieveQR(QrAdapter);









        return builder
                .setView(view)
                .setNegativeButton("Exit", null)
                .create();
    }

}
