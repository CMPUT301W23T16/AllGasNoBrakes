package com.example.allgasnobrakes.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.example.allgasnobrakes.R;

public class CameraFragment extends Fragment {
    public CameraFragment() {
        super(R.layout.split_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentManager fm = getParentFragmentManager();
        fm.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.split_container, ScannerFragment.class, requireArguments())
                .commit();
    }
}
