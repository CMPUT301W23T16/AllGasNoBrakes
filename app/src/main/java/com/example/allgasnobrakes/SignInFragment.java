package com.example.allgasnobrakes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Handles returning user (sign in) operations
 * @author zhaoyu4
 * @version 1.0
 */
public class SignInFragment extends Fragment {
    private FirebaseFirestore db;
    private Leaderboard viewModel;
    private TextView username;
    private Button rollOutButton;

    public SignInFragment() {
        super(R.layout.sign_in);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(Leaderboard.class);
        View view = inflater.inflate(R.layout.sign_in, container, false);
        String lastUser = requireArguments().getString("LastUser");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cloudID = db.document(lastUser);

        username = view.findViewById(R.id.name_textview);
        username.setText(cloudID.getId());

        rollOutButton = view.findViewById(R.id.roll_out_button);
        rollOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://firebase.google.com/docs/firestore/query-data/get-data
                cloudID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                Log.d("User", "DocumentSnapshot data: " + document.getData());
                                String playerName = cloudID.getId();
                                String email = (String) document.getData().get("Email");
                                String password = (String) document.getData().get("Password");
                                PlayerProfile playerProfile = new PlayerProfile(playerName, email, password);
                                viewModel.selectPlayer(playerProfile);
                            } else {
                                Log.d("User", "No such document");
                            }

                        } else {
                            Log.d("User", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
        return view;
    }
}
