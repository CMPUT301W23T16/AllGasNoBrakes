package com.example.allgasnobrakes;

import static org.junit.jupiter.api.Assertions.*;

import com.example.allgasnobrakes.models.HashedQR;
import com.example.allgasnobrakes.models.PlayerProfile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerProfileTest {
    private PlayerProfile playerProfile;

    @BeforeEach
    public void getDummyPPF() {
        playerProfile = new PlayerProfile();
    }

    @Test
    public void testDeleteQR() {
        assertEquals(1, playerProfile.getNumberOfQR());
        assertEquals(1, playerProfile.getCount());
        assertEquals(10, playerProfile.getScore());

        HashedQR qr = playerProfile.getQR(0);
        playerProfile.deleteQR(qr);

        assertEquals(0, playerProfile.getNumberOfQR());
        assertEquals(0, playerProfile.getCount());
        assertEquals(0, playerProfile.getScore());
    }

    @Test
    public void testAddQR() {
        assertEquals(1, playerProfile.getNumberOfQR());
        assertEquals(1, playerProfile.getCount());
        assertEquals(10, playerProfile.getScore());

        HashedQR qr = new HashedQR();
        playerProfile.addQR(0, qr);

        assertEquals(qr, playerProfile.getQR(0));
        assertEquals(2, playerProfile.getCount());
        assertEquals(20, playerProfile.getScore());
    }
}
