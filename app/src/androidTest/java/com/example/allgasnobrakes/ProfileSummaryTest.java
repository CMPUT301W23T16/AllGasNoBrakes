package com.example.allgasnobrakes;

import static org.junit.Assert.assertEquals;

import com.example.allgasnobrakes.models.ProfileSummary;

import org.junit.Test;

public class ProfileSummaryTest {

    public ProfileSummary testQRCounter() {
        return new ProfileSummary(2, 10);
    }

    @Test
    public void testAssign() {
        ProfileSummary profileSummary = testQRCounter();
        profileSummary.assign(5, 50);
        assertEquals(5, profileSummary.getTotalQR());
        assertEquals(50, profileSummary.getTotalScore());
    }

    @Test
    public void testUpdate() {
        ProfileSummary profileSummary = testQRCounter();
        int oldCount = profileSummary.getTotalQR();
        int oldScore = profileSummary.getTotalScore();
        profileSummary.update("", 3, 40);
        assertEquals(profileSummary.getTotalQR(), oldCount + 3);
        assertEquals(profileSummary.getTotalScore(), oldScore + 40);
    }
}
