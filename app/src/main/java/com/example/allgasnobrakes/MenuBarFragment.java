package com.example.allgasnobrakes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * Handles menu bar operations
 * @author zhaoyu4
 * @version 1.0
 */
public class MenuBarFragment extends Fragment {
    private Button homeButton;
    private Button cameraButton;
    private Button profileButton;

    public MenuBarFragment() {
        super(R.layout.menu_bar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_bar, container, false);
        FragmentManager parent = getParentFragmentManager();

        homeButton = view.findViewById(R.id.home_button);
        cameraButton = view.findViewById(R.id.camera_button);
        profileButton = view.findViewById(R.id.profile_button);

        homeButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, QRListFragment.class, null)
                        .commit()
        );

        cameraButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, ScannerFragment.class, null)
                        .commit()
        );

        profileButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, ProfileFragment.class, null)
                        .commit()
        );
        return view;
    }
}
