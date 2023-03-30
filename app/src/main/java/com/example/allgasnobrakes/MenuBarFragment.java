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

import com.example.allgasnobrakes.views.LeaderboardFragment;

/**
 * Handles menu bar operations
 * @author zhaoyu4
 * @version 1.0
 */
public class MenuBarFragment extends Fragment {
    private Button homeButton;
    private Button cameraButton;
    private Button profileButton;
    private Button leaderboardButton;

    public MenuBarFragment() {
        super(R.layout.menu_bar);
    }

    /**
     * Provides three button for switching between the QR list, the QR code scanner, and the player
     * profile page. Also passes account information for those pages to use
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The menu bar view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_bar, container, false);
        FragmentManager parent = getParentFragmentManager();

        homeButton = view.findViewById(R.id.home_button);
        cameraButton = view.findViewById(R.id.camera_button);
        profileButton = view.findViewById(R.id.profile_button);
        leaderboardButton = view.findViewById(R.id.leaderboard_button);

        // Switch to homepage
        homeButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, QRListFragment.class, requireArguments())
                        .commit()
        );

        // Switch to code scanner
        cameraButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, ScannerFragment.class, requireArguments())
                        .commit()
        );

        // Switch to player profile page
        profileButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, ProfileFragment.class, requireArguments())
                        .commit()
        );

        // Switch to player profile page
        leaderboardButton.setOnClickListener(v ->
                parent.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.split_container, LeaderboardFragment.class, requireArguments())
                        .commit()
        );
        return view;
    }
}
