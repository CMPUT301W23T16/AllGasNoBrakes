package com.example.allgasnobrakes;

import android.util.Log;

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

    public void assign(int QR, int score) {
        totalQR = QR;
        totalScore = score;
        Log.d("QRC", String.format(Locale.CANADA, "%d", QR));
        Log.d("QRC", String.format(Locale.CANADA, "%d", score));
    }

    public void update(int QR, int score) {
        totalQR += QR;
        totalScore += score;
        Log.d("QRC", String.format(Locale.CANADA, "%d", QR));
        Log.d("QRC", String.format(Locale.CANADA, "%d", score));
    }
}
