package com.example.allgasnobrakes.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allgasnobrakes.R;

public class PlayerListFragment extends Fragment {
    private RecyclerView playerList;

    public PlayerListFragment() {
        super(R.layout.player_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();

        playerList = view.findViewById(R.id.player_list);
        playerList.setLayoutManager(new LinearLayoutManager(activity));
    }
}
