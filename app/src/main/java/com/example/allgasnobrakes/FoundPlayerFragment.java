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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * foundplayerfragment, displays searched player profile
 * @author zhaoyu5
 * @version 1.0
 */
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

        QrAdapter = new QrArrayAdapter(user.getQRList(), getActivity(), new QrArrayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(HashedQR hashedQR) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Users").document(username1).collection("QRRef").document(hashedQR.getHashedQR());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            final String comment = (String) task.getResult().get("Comment");
                            final String longitude = (String) task.getResult().get("Longitude");
                            final String latitude = (String) task.getResult().get("Latitude");
                            HashedQrFragment ADSF1 = new HashedQrFragment();
                            ADSF1.main(hashedQR,comment,longitude,latitude);
                            ADSF1.show(getActivity().getSupportFragmentManager(), "finding");
                            Log.d("test", "Cached document data: " + comment);
                        } else {
                            Log.d("test", "Cached get failed: ", task.getException());
                        }
                    }
                });
            }
        });
        QRList.setAdapter(QrAdapter);
        user.retrieveQR(QrAdapter, "Highest Score");

        return builder
                .setView(view)
                .setNegativeButton("Exit", null)
                .create();
    }

}
