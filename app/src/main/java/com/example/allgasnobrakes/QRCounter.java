package com.example.allgasnobrakes;

import static android.content.ContentValues.TAG;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class QRCounter {
    TextView totalCount;
    private int counter = 0;
    public void updateCounter(String username, TextView totalCount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users").document(username).collection("QR")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                counter++;
                            }
                            Log.d(TAG, "Counter: " + counter);
                            totalCount.setText(String.valueOf(counter));
                        }
                        else {
                            Log.e("QRCounter", "Error", task.getException());
                        }
                    }
                });
    }

    private int totalScore = 0;
    TextView playerScore;
    public void scoreCounter(String username, TextView playerScore) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(username).collection("QR")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                String playerScoreString = documentSnapshot.getString("Score");
                                totalScore += Integer.parseInt(playerScoreString);
                            }
                            playerScore.setText("Score: "+String.valueOf(totalScore));
                            Log.d(TAG, "TotalPlayerScore: " + totalScore);
                        }
                        else {
                            Log.e("scoreCounter", "Error", task.getException());
                        }

                    }
                });

    }

//    public int getCounter() {
//        return counter;
//    }
}



//    /**
//     * Testing, fatih
//     */
//    QRCounter qrCounter = new QRCounter();
//        qrCounter.updateCounter("hi");
//
//                TextView totalCount = root.findViewById(R.id.total_codes);
//                totalCount.setText(String.valueOf(qrCounter.getCounter()));
