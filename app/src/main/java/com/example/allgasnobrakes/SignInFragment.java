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

/**
 * Handles returning user (sign in) operations
 * @author zhaoyu4
 * @version 1.0
 */
public class SignInFragment extends Fragment {
    private FirebaseFirestore db;
    private PPFViewModel viewModel;
    private TextView username;
    private Button rollOutButton;

    public SignInFragment() {
        super(R.layout.sign_in);
    }

    /**
     * Overridden to allow return user to confirm sign in with device ID
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The created view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(PPFViewModel.class);
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
                                Number score = (Number) document.getData().get("Total Score");
                                Number QRCount = (Number) document.getData().get("QR Count");
                                PlayerProfile playerProfile = new PlayerProfile(playerName, email, password, score.intValue(), QRCount.intValue());
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
