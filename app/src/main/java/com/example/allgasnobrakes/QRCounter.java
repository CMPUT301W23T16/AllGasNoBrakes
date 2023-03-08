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

import java.io.Serializable;
import java.util.Locale;

public class QRCounter implements Serializable {
    private int totalQR;
    private int totalScore;

    public QRCounter(int score, int  count) {
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

    public void update(int QR, int score) {
        totalQR = QR;
        totalScore = score;
        Log.d("QRC", String.format(Locale.CANADA, "%d", QR));
        Log.d("QRC", String.format(Locale.CANADA, "%d", score));
    }
}
