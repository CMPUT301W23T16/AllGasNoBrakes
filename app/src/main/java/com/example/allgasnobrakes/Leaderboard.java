package com.example.allgasnobrakes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * To communicate PlayerProfile between main activity and fragments
 * @author zhaoyu4
 * @version 1.0
 */
public class Leaderboard extends ViewModel {
    private final MutableLiveData<PlayerProfile> players = new MutableLiveData<>();

    public void selectPlayer(PlayerProfile player) {
        players.setValue(player);
    }

    public LiveData<PlayerProfile> getSelectedPlayer() {
        return players;
    }
}
