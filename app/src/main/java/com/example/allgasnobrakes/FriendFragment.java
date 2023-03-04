package com.example.allgasnobrakes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Handles display of other player profiles
 * @author theresag
 * @version 1.0
 */
public class FriendFragment extends Fragment {
    private TextView friend_user;
    private TextView friend_email;

    public FriendFragment() {
        super(R.layout.player_profile);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_profile, container, false);

        friend_user = view.findViewById(R.id.username_textview);
        friend_email = view.findViewById(R.id.email_textview);

        friend_user.setText(requireArguments().getString("Username"));
        friend_email.setText(requireArguments().getString("Email"));

        return view;
    }
}
