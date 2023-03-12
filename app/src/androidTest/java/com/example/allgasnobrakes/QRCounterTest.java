package com.example.allgasnobrakes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QRCounterTest {

    public QRCounter testQRCounter() {
        return new QRCounter(2, 10);
    }

    @Test
    public void testAssign() {
        QRCounter qrCounter = testQRCounter();
        qrCounter.assign(5, 50);
        assertEquals(5, qrCounter.getTotalQR());
        assertEquals(50, qrCounter.getTotalScore());
    }

    @Test
    public void testUpdate() {
        QRCounter qrCounter = testQRCounter();
        int oldCount = qrCounter.getTotalQR();
        int oldScore = qrCounter.getTotalScore();
        qrCounter.update("", 3, 40);
        assertEquals(qrCounter.getTotalQR(), oldCount + 3);
        assertEquals(qrCounter.getTotalScore(), oldScore + 40);
    }
}
