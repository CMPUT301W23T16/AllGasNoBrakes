package com.example.allgasnobrakes.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.allgasnobrakes.views.PlayerListFragment;

// Android Developers Documentation
// https://developer.android.com/guide/navigation/navigation-swipe-view-2

/**
 * Instantiate the different leaderboards based on the selected leaderboard option
 * @author zhaoyu4
 * @version 1.0
 */
public class LeaderBoardAdapter extends FragmentStateAdapter {
    public LeaderBoardAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new PlayerListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
