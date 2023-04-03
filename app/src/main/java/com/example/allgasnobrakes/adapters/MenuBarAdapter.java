package com.example.allgasnobrakes.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.allgasnobrakes.views.BlankFragment;
import com.example.allgasnobrakes.views.MapFragment;
import com.example.allgasnobrakes.views.ProfileFragment;
import com.example.allgasnobrakes.views.QRListFragment;
import com.example.allgasnobrakes.views.ScannerFragment;
import com.example.allgasnobrakes.views.LeaderboardFragment;

// Android Developers Documentation
// https://developer.android.com/guide/navigation/navigation-swipe-view-2

/**
 * Instantiate the different pages (fragments) based on the selected tab in the menu bar
 * @author zhaoyu4
 * @version 1.0
 */
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
            return new MapFragment();

        } else if (position == 2) {
            return new ScannerFragment();

        } else if (position == 3) {
            return new LeaderboardFragment();

        } else if (position == 4) {
            return new ProfileFragment();

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
