package com.example.allgasnobrakes;

import android.util.Log;

import java.io.Serializable;
import java.util.Locale;

/**
 * A class that stores player profile summary information
 * @author zhaoyu4
 * @version 1.0
 */
public class QRCounter implements Serializable {
    private int totalQR;
    private int totalScore;

    /**
     *
     * @param score The total QR score of the player account
     * @param count The total number of the QR codes that account has
     */
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
    public void update(int QR, int score) {
        totalQR += QR;
        totalScore += score;
    }
}
