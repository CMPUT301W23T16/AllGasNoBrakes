package com.example.allgasnobrakes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allgasnobrakes.PlayerProfile;
import com.example.allgasnobrakes.R;

import java.util.ArrayList;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.ViewHolder> {
    private final ArrayList<PlayerProfile> players;
    private final Context context;
    private final PlayerListAdapter.ItemClickListener item;

    public PlayerListAdapter(ArrayList<PlayerProfile> players, Context context,
                             PlayerListAdapter.ItemClickListener item) {
        this.players = players;
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public PlayerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content, parent, false);
        return new PlayerListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter.ViewHolder holder, int position) {
        PlayerProfile player = players.get(position);

        holder.username.setText(player.getUsername());
        holder.totalScore.setText(Integer.toString(player.getDisplayMetric()));

        holder.itemView.setOnClickListener(view -> {
            item.onItemClick(players.get(position));
        });
    }

    public interface ItemClickListener{
        void onItemClick(PlayerProfile playerProfile);
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView username;
        private final TextView totalScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.Hash);
            totalScore = itemView.findViewById(R.id.Score);
        }
    }
}
