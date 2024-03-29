package com.example.allgasnobrakes.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allgasnobrakes.models.PlayerProfile;
import com.example.allgasnobrakes.R;
import com.example.allgasnobrakes.adapters.PlayerListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Displays and updates the leaderboard. Currently implements two types of leaderboard
 *  1. Highest scoring QR code that is unique
 *  2. Total QR code score
 * @author zhaoyu4
 * @version 1.0
 */
public class PlayerListFragment extends Fragment {
    private RecyclerView playerRecyclerView;
    private final ArrayList<PlayerProfile> playersList = new ArrayList<>();
    private PlayerListAdapter playerListAdapter;

    public PlayerListFragment() {
        super(R.layout.player_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        playerRecyclerView = view.findViewById(R.id.player_list);
        playerRecyclerView.setLayoutManager(new LinearLayoutManager(activity));

        playerListAdapter = new PlayerListAdapter(playersList, activity,
                new PlayerListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(PlayerProfile playerProfile) {
                FoundPlayerFragment ADSF1 = new FoundPlayerFragment();
                ADSF1.main(playerProfile.getUsername(), playerProfile.getEmail());
                ADSF1.show(getActivity().getSupportFragmentManager(), "Finding");
            }
        });

        playerRecyclerView.setAdapter(playerListAdapter);
    }

    /**
     * Determines which leaderboard we are getting
     * @param type The type of leaderboard. 0: unique highest-scoring QR code leaderboard; 1: total
     *             QR code score leaderboard
     */
    private void getPlayersList(int type) {
        if (type == 0) {
            getHighestUnique();
        } else if (type == 1) {
            getHighestTotal();
        }

    }

    /**
     * Gets the unique highest-scoring QR code leaderboard
     */
    private void getHighestUnique() {
        playersList.clear();

        FirebaseFirestore.getInstance().collection("QR")
                .whereEqualTo("PlayerCount", 1)
                .orderBy("Score", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()) {
                            String playerName = ((ArrayList<String>) doc.get("OwnedBy")).get(0);
                            PlayerProfile player = new PlayerProfile(playerName, "");
                            player.setDisplayMetric((Number) doc.get("Score"));
                            playersList.add(player);
                            playerListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * Gets the total QR code score leaderboard
     */
    private void getHighestTotal() {
        playersList.clear();
        FirebaseFirestore.getInstance().collection("Users")
                .whereGreaterThan("Total Score", 0)
                .orderBy("Total Score", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()) {
                            String playerName = doc.getId();
                            PlayerProfile player = new PlayerProfile(playerName, "");
                            player.setDisplayMetric((Number) doc.get("Total Score"));
                            playersList.add(player);
                            playerListAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlayersList(requireArguments().getInt("type"));
    }
}
