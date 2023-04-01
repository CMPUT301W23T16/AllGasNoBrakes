package com.example.allgasnobrakes;

import static org.junit.jupiter.api.Assertions.*;

import com.example.allgasnobrakes.models.ProfileSummary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test cases for ProfileSummary
 * @author fartar
 */
// will not work with current version of the app
public class ProfileSummaryTest {
    private ProfileSummary profileSummary;

    @BeforeEach
    public void getDummyPFS() {
        profileSummary = new ProfileSummary();
    }

    @Test
    public void testAssign() {
        assertEquals(1, profileSummary.getTotalQR());
        assertEquals(10, profileSummary.getTotalScore());

        profileSummary.assign(80, 3);

        assertEquals(80, profileSummary.getTotalQR());
        assertEquals(3, profileSummary.getTotalScore());
    }

    @Test
    public void testUpdate() {
        assertEquals(1, profileSummary.getTotalQR());
        assertEquals(10, profileSummary.getTotalScore());

        profileSummary.update("", 2, 55);

        assertEquals(3, profileSummary.getTotalQR());
        assertEquals(65, profileSummary.getTotalScore());
    }
}