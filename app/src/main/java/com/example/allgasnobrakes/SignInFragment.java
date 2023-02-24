package com.example.allgasnobrakes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
                String playerName = cloudID.getId();
                String email = cloudID.get().getResult().getString("Email");
                String password = cloudID.get().getResult().getString("Password");
                PlayerProfile playerProfile = new PlayerProfile(playerName, email, password);
                viewModel.selectPlayer(playerProfile);
            }
        });
        return view;
    }
}
