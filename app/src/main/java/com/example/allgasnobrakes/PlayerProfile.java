package com.example.allgasnobrakes;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * Contains player profile information
 * @author zhaoyu4
 * @version 1.1
 */
public class PlayerProfile {
    private String username;
    private String email;
    private String password;
    private ArrayList<HashedQR> QRList;

    public PlayerProfile() {
        this.username = "username";
        this.email = "email";
        this.password = "password";
    }

    public PlayerProfile(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setQRList(ArrayList<HashedQR> QRList) {
        this.QRList = QRList;
    }
}
