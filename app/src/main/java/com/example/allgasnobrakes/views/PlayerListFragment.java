package com.example.allgasnobrakes.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allgasnobrakes.PlayerProfile;
import com.example.allgasnobrakes.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlayerListFragment extends Fragment {
    private RecyclerView playerRecyclerView;
    private ArrayList<PlayerProfile> playersList;

    public PlayerListFragment() {
        super(R.layout.player_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        playerRecyclerView = view.findViewById(R.id.player_list);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        getPlayersList(requireArguments().getInt("type"));
    }

    private void getPlayersList(int type) {
        if (type == 0) {
            playersList = getHighestUnique();
        } else if (type == 1){
            playersList = getHighestTotal();
        }

    }

    private ArrayList<PlayerProfile> getHighestUnique() {
        ArrayList<PlayerProfile> players = new ArrayList<>();
        FirebaseFirestore.getInstance();
        return players;
    }

    private ArrayList<PlayerProfile> getHighestTotal() {
        ArrayList<PlayerProfile> players = new ArrayList<>();
        FirebaseFirestore.getInstance();
        return players;
    }

}
