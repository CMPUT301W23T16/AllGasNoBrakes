package com.example.allgasnobrakes.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.allgasnobrakes.views.LeaderboardFragment;
import com.example.allgasnobrakes.views.PlayerListFragment;

// https://developer.android.com/guide/navigation/navigation-swipe-view-2
public class LeaderBoardAdapter extends FragmentStateAdapter {
    public LeaderBoardAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        return new PlayerListFragment();
    }

    @Override
    public int getItemCount() {
        return 100;
    }
}
