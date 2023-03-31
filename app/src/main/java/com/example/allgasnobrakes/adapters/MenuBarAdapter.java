package com.example.allgasnobrakes.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.allgasnobrakes.views.BlankFragment;
import com.example.allgasnobrakes.views.ProfileFragment;
import com.example.allgasnobrakes.views.QRListFragment;
import com.example.allgasnobrakes.views.ScannerFragment;
import com.example.allgasnobrakes.views.LeaderboardFragment;

// https://developer.android.com/guide/navigation/navigation-swipe-view-2
public class MenuBarAdapter extends FragmentStateAdapter {
    private Bundle bundle;

    public MenuBarAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        if (position == 0) {
            QRListFragment qrListFragment = new QRListFragment();
            qrListFragment.setArguments(bundle);
            return qrListFragment;

        } else if (position == 1) {
            QRListFragment qrListFragment = new QRListFragment();
            qrListFragment.setArguments(bundle);
            return qrListFragment;

        } else if (position == 2) {
            ScannerFragment scannerFragment = new ScannerFragment();
            scannerFragment.setArguments(bundle);
            return scannerFragment;

        } else if (position == 3) {
            LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
            leaderboardFragment.setArguments(bundle);
            return leaderboardFragment;

        } else if (position == 4) {
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(bundle);
            return profileFragment;

        } else {
            return new BlankFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
