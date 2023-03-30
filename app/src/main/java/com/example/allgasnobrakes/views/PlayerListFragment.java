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
import com.example.allgasnobrakes.adapters.PlayerListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PlayerListFragment extends Fragment {
    private RecyclerView playerRecyclerView;
    private ArrayList<PlayerProfile> playersList;
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
        getPlayersList(requireArguments().getInt("type"));

        playerListAdapter = new PlayerListAdapter(playersList, activity,
                new PlayerListAdapter.ItemClickListener() {
            @Override
            public void onItemClick(PlayerProfile playerProfile) {

            }
        });

        playerRecyclerView.setAdapter(playerListAdapter);
    }

    private void getPlayersList(int type) {
        if (type == 0) {
            playersList = getHighestUnique();
        } else if (type == 1) {
            playersList = getHighestTotal();
        }

    }

    private ArrayList<PlayerProfile> getHighestUnique() {
        ArrayList<PlayerProfile> players = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("QR")
                .whereEqualTo("PlayerCount", 1)
                .orderBy("Score", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult()) {
                            String playerName = ((ArrayList<String>) doc.get("OwnedBy")).get(0);
                            PlayerProfile player = new PlayerProfile(playerName, "");
                            player.setDisplayMetric((Number) doc.get("Score"));
                            players.add(player);
                            playerListAdapter.notifyItemInserted(players.size() - 1);
                        }
                    }
                });
        return players;
    }

    private ArrayList<PlayerProfile> getHighestTotal() {
        ArrayList<PlayerProfile> players = new ArrayList<>();
        FirebaseFirestore.getInstance();
        return players;
    }

}
