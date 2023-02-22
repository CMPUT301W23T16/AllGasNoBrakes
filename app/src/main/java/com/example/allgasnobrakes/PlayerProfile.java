package com.example.allgasnobrakes;

import java.util.ArrayList;

/**
 * Contains player profile information
 * @author zhaoyu4
 * @version 1.0
 */
public class PlayerProfile {
    private String username;
    private String email;
    private ArrayList<HashedQR> QRList;

    public PlayerProfile(String username, String email, ArrayList<HashedQR> QRList) {
        this.username = username;
        this.email = email;
        this.QRList = QRList;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<HashedQR> getQRList() {
        return QRList;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setQRList(ArrayList<HashedQR> QRList) {
        this.QRList = QRList;
    }
}
