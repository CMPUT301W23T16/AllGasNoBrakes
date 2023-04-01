package com.example.allgasnobrakes.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * A class that stores player profile summary information. Displays total QR codes and total score
 * @author fartar zhaoyu4
 * @version 4.0
 */
public class ProfileSummary implements Serializable {
    public static final String TOTAL_QR = "totalQR";
    public static final String TOTAL_SCORE = "totalScore";
    private int totalQR;
    private int totalScore;
    private boolean test = false;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ProfileSummary() {
        totalQR = 1;
        totalScore = 10;
        test = true;
    }

    /**
     *
     * @param score The total QR score of the player account
     * @param count The total number of the QR codes that account has
     */
    public ProfileSummary(int score, int  count) {
        totalQR = count;
        totalScore = score;
    }

    public void addPropertyChangeListener(String field, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(field, listener);
    }

    public int getTotalQR() {
        return totalQR;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public PropertyChangeSupport getPcs() {
        return pcs;
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
        int oldTotalQR = totalQR;
        int oldTotalScore = totalScore;
        totalQR += QR;
        totalScore += score;

        if (! test) {
            pcs.firePropertyChange(TOTAL_QR, oldTotalQR, totalQR);
            pcs.firePropertyChange(TOTAL_SCORE, oldTotalScore, totalScore);

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
}
