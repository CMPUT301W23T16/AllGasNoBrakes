package com.example.allgasnobrakes;

import static org.junit.Assert.*;

import com.example.allgasnobrakes.models.QRCounter;

import org.junit.Test;
/**
 * Test cases for QRCounter
 * @author fartar
 */
// will not work with current version of the app
public class QRCounterTest {

    @Test
    public void getTotalQR() {
        QRCounter qrCounter = new QRCounter(0, 7);
        assertEquals(7, qrCounter.getTotalQR());
    }

    @Test
    public void getTotalScore() {
        QRCounter qrCounter = new QRCounter(100, 7);
        assertEquals(100, qrCounter.getTotalScore());
    }

    @Test
    public void setTotalQR() {
        QRCounter qrCounter = new QRCounter(0, 0);
        qrCounter.setTotalQR(3);
        assertEquals(3, qrCounter.getTotalQR());
    }

    @Test
    public void setTotalScore() {
        QRCounter qrCounter = new QRCounter(0, 0);
        qrCounter.setTotalScore(777);
        assertEquals(777, qrCounter.getTotalScore());
    }

    @Test
    public void assign() {
        QRCounter qrCounter = new QRCounter(0, 0);
        qrCounter.assign(80, 3);
        assertEquals(80, qrCounter.getTotalQR());
        assertEquals(3, qrCounter.getTotalScore());
    }

    @Test
    public void update() {
        QRCounter qrCounter = new QRCounter(45, 3);
        qrCounter.update("", 2, 55);
        assertEquals(5, qrCounter.getTotalQR());
        assertEquals(100, qrCounter.getTotalScore());
    }
}