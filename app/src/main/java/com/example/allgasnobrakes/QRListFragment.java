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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;


/**
 * Handles operations with QR code list
 * @author zhaoyu4 zhaoyu5
 * @version 3.0
 */

public class QRListFragment extends Fragment  {

    private Button currentSortOrder;
    private RecyclerView QRList;
    private RecyclerView.Adapter QrAdapter;
    private QRCountView totalCount;
    private ScoreView score;

    PlayerProfile user;

    public QRListFragment() {
        super(R.layout.homepage);
    }

    /**
     * Overridden to display a list of QR codes sorted by score in descending order. Also allows to
     * resort in ascending order. Displays player profile summary information (QRCounter). Allows
     * user to delete QR codes from their account
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = (PlayerProfile) requireArguments().getSerializable("User");
        Log.d("Current User", user.getUsername());
        final Activity activity = getActivity();

        QRList = view.findViewById(R.id.codes_list);
        QRList.setLayoutManager(new LinearLayoutManager(activity));
        QrAdapter = new QrArrayAdapter(user.getQRList(), activity, new QrArrayAdapter.ItemClickListener() {
            @Override
            public void onItemClick(HashedQR hashedQR) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Users").document(user.getUsername()).collection("QRRef").document(hashedQR.getHashedQR());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            final String document = (String) task.getResult().get("Comment");
                            HashedQrFragment ADSF1 = new HashedQrFragment();
                            ADSF1.main(hashedQR,document);
                            ADSF1.show(getActivity().getSupportFragmentManager(), "finding");
                            Log.d("test", "Cached document data: " + document);
                        } else {
                            Log.d("test", "Cached get failed: ", task.getException());
                        }
                    }
                });

            }
        });
        QRList.setAdapter(QrAdapter);

        totalCount = view.findViewById(R.id.total_codes);
        totalCount.setText(String.format(Locale.CANADA, "%d", user.getProfileSummary().getTotalQR()));
        score = view.findViewById(R.id.player_score);
        score.setText(String.format(Locale.CANADA, "%d", user.getProfileSummary().getTotalScore()));

        user.addObserver(totalCount);
        user.addObserver(score);

        currentSortOrder = view.findViewById(R.id.sort_order);
        currentSortOrder.setText(requireArguments().getString("SortOrder"));

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
                user.deleteQR(deletedQR);

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
                        user.addQR(deletedQR);

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
        requireArguments().putSerializable("User", user);
        user.deleteObservers();
    }

    /**
     * Overridden to update the user's QR code list everytime we switch back to this page
     */
    @Override
    public void onResume() {
        super.onResume();
        user.retrieveQR(QrAdapter, requireArguments().getString("SortOrder"));
        Log.d("resume", String.format(Locale.CANADA, "%d", user.getProfileSummary().getTotalQR()));
        Log.d("resume", String.format(Locale.CANADA, "%d", user.getProfileSummary().getTotalScore()));
        Log.d("resume", requireArguments().getString("SortOrder"));
    }
}