package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Handles operations with QR code list
 * @author zhaoyu4 zhaoyu5
 * @version 2.0
 */

public class QRListFragment extends Fragment  {

    private RecyclerView QRList;
    private RecyclerView.Adapter QrAdapter;
    final String TAG = "Sample";

    public QRListFragment() {
        super(R.layout.homepage);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final PlayerProfile user = (PlayerProfile) requireArguments().getSerializable("User");
        Log.d("Current User", user.getUsername());
        final Activity activity = getActivity();

        QRList = view.findViewById(R.id.codes_list);
        QRList.setLayoutManager(new LinearLayoutManager(activity));
        QrAdapter = new QrArrayAdapter(user.getQRList(), activity);
        QRList.setAdapter(QrAdapter);

        if (user.getQRList().size() == 0) {
            user.retrieveQR(QrAdapter);
        }
    }
}
