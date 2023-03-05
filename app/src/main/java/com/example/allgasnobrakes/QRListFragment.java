package com.example.allgasnobrakes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

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

    /**
     * Overridden to display a list of QR codes sorted by score in descending order. Also allows to
     * resort in ascending order
     */
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

        // https://www.geeksforgeeks.org/swipe-to-delete-and-undo-in-android-recyclerview/
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                HashedQR deletedQR = user.getQRList().get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                user.getQRList().remove(viewHolder.getAdapterPosition());

                // Then we remove it from the cloud database
                user.deleteQR(deletedQR.getHashedQR());

                // below line is to notify our item is removed from adapter.
                QrAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(QRList, deletedQR.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        user.getQRList().add(position, deletedQR);

                        // Add it back to cloud database
                        user.addQR(deletedQR.getHashedQR());

                        // below line is to notify item is
                        // added to our adapter class.
                        QrAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
        }).attachToRecyclerView(QRList);
    }

    /**
     * Overridden to keep sorting order persistent across fragment transactions
     */
    @Override
    public void onPause() {
        super.onPause();
        requireArguments().putString("SortOrder", currentSortOrder.getText().toString());
    }
}
