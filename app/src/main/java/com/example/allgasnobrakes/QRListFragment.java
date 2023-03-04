package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;

/**
 * Handles operations with QR code list
 * @author zhaoyu4 zhaoyu5
 * @version 3.0
 */

public class QRListFragment extends Fragment  {

    private Button currentSortOrder;
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

        currentSortOrder = view.findViewById(R.id.sort_order);
        currentSortOrder.setText(requireArguments().getString("SortOrder"));

        if (currentSortOrder.getText().toString().equals("Highest Score")) {
            user.getQRList().sort(new HashedQR().reversed());
        } else {
            user.getQRList().sort(new HashedQR());
        }

        currentSortOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSortOrder.getText().toString().equals("Highest Score")) {
                    user.getQRList().sort(new HashedQR());
                    QrAdapter.notifyDataSetChanged();
                    currentSortOrder.setText(R.string.lowest_score_text);
                } else {
                    user.getQRList().sort(new HashedQR().reversed());
                    currentSortOrder.setText(R.string.highest_score_text);
                    QrAdapter.notifyDataSetChanged();
                }
            }
        });
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        requireArguments().putString("SortOrder", currentSortOrder.getText().toString());
//    }
}
