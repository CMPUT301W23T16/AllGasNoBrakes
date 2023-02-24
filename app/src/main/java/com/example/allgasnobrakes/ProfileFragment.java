package com.example.allgasnobrakes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    private TextView username;
    private TextView email;

    public ProfileFragment() {
        super(R.layout.player_profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_profile, container, false);

        username = view.findViewById(R.id.username_textview);
        email = view.findViewById(R.id.email_textview);

        username.setText(requireArguments().getString("Username"));
        email.setText(requireArguments().getString("Email"));

        return view;
    }
}
