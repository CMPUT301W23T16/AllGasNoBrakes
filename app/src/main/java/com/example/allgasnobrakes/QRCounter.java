package com.example.allgasnobrakes;

import android.util.Log;

import java.io.Serializable;
import java.util.Locale;
/**
 * Displays total QR codes and total score
 * @author fartar zhaoyu4
 * @version 2.0
 */
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
    }

    public void update(int QR, int score) {
        totalQR += QR;
        totalScore += score;
    }
}
