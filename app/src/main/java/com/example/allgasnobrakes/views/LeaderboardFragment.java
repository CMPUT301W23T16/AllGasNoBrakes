package com.example.allgasnobrakes.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.allgasnobrakes.R;
import com.example.allgasnobrakes.adapters.LeaderBoardAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * Hosts the leaderboards and provide the tabs for switching between different leaderboards
 * @author zhaoyu4
 * @version 1.0
 */
public class LeaderboardFragment extends Fragment {
    public LeaderboardFragment() {
        super(R.layout.leaderboard);
    }

    LeaderBoardAdapter leaderBoardAdapter;
    ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leaderboard, container, false);
    }

    /**
     * Sets up the tabs for leaderboard.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Android Developers Documentation
        // https://developer.android.com/guide/navigation/navigation-swipe-view-2
        leaderBoardAdapter = new LeaderBoardAdapter(this);
        viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(leaderBoardAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        if (position == 0) {
                            tab.setText("The One and Only");
                        } else if (position == 1) {
                            tab.setText("The Hardcore Collectors");
                        }
                    }
                }).attach();
    }
}
