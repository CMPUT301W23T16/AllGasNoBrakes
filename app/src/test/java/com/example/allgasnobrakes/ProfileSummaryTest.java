package com.example.allgasnobrakes;

import static org.junit.Assert.*;

import com.example.allgasnobrakes.models.ProfileSummary;

import org.junit.Test;
/**
 * Test cases for ProfileSummary
 * @author fartar
 */
// will not work with current version of the app
public class ProfileSummaryTest {

    @Test
    public void getTotalQR() {
        ProfileSummary profileSummary = new ProfileSummary(0, 7);
        assertEquals(7, profileSummary.getTotalQR());
    }

    @Test
    public void getTotalScore() {
        ProfileSummary profileSummary = new ProfileSummary(100, 7);
        assertEquals(100, profileSummary.getTotalScore());
    }

    @Test
    public void setTotalQR() {
        ProfileSummary profileSummary = new ProfileSummary(0, 0);
        profileSummary.setTotalQR(3);
        assertEquals(3, profileSummary.getTotalQR());
    }

    @Test
    public void setTotalScore() {
        ProfileSummary profileSummary = new ProfileSummary(0, 0);
        profileSummary.setTotalScore(777);
        assertEquals(777, profileSummary.getTotalScore());
    }

    @Test
    public void assign() {
        ProfileSummary profileSummary = new ProfileSummary(0, 0);
        profileSummary.assign(80, 3);
        assertEquals(80, profileSummary.getTotalQR());
        assertEquals(3, profileSummary.getTotalScore());
    }

    @Test
    public void update() {
        ProfileSummary profileSummary = new ProfileSummary(45, 3);
        profileSummary.update("", 2, 55);
        assertEquals(5, profileSummary.getTotalQR());
        assertEquals(100, profileSummary.getTotalScore());
    }
}