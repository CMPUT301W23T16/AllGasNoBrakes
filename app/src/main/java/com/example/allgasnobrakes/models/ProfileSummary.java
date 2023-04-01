package com.example.allgasnobrakes.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

/**
 * A class that stores player profile summary information. Displays total QR codes and total score
 * @author fartar zhaoyu4
 * @version 3.0
 */
public class ProfileSummary implements Serializable {
    private int totalQR;
    private int totalScore;

    /**
     *
     * @param score The total QR score of the player account
     * @param count The total number of the QR codes that account has
     */
    public ProfileSummary(int score, int  count) {
        totalQR = count;
        totalScore = score;
    }

    public int getTotalQR() {
        return totalQR;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalQR(int totalQR) {
        this.totalQR = totalQR;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Directly assigns the the number of QR and the total score to this amount
     * @param QR Number of QR codes the user has
     * @param score The total score of those QR codes
     */
    public void assign(int QR, int score) {
        totalQR = QR;
        totalScore = score;
    }

    /**
     * Updates the QR count and total score by the specified amount
     * @param QR The number of QR codes will change by this amount
     * @param score The total score will change by this amount
     */
    public void update(String username, int QR, int score) {
        totalQR += QR;
        totalScore += score;

        DocumentReference documentReference =
                FirebaseFirestore.getInstance().collection("Users").document(username);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int totalCount = ((Number) task.getResult().get("QR Count")).intValue();
                int totalScore = ((Number) task.getResult().get("Total Score")).intValue();
                documentReference.update("Total Score", totalScore + score);
                documentReference.update("QR Count", totalCount + QR);
            }
        });
    }
}
