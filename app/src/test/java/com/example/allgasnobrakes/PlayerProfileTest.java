package com.example.allgasnobrakes;

import org.junit.Test;

public class PlayerProfileTest {
    private PlayerProfile playerProfile;

    public PlayerProfile testUser() {
        playerProfile = new PlayerProfile("Test user",
                "test@gmail.com", "1234", 0, 0);
        return playerProfile;
    }

    @Test
    public void testDeleteQR() {

    }

    @Test
    public void testAddQR() {

    }
}
