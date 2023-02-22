package com.example.allgasnobrakes;

/**
 * Describes the hashed QR code
 * @author zhaoyu4
 * @version 1.0
 */
public class HashedQR {
    private String hashedQR;
    private int score;
    private String name;
    private String[][] face;
    private final double[] geolocation= new double[2];

    public HashedQR(String hashedQR, int score, String name, String[][] face, int lat, int lon) {
        this.hashedQR = hashedQR;
        this.score = score;
        this.name = name;
        this.face = face;
        geolocation[0] = lat;
        geolocation[1] = lon;
    }

    public String getHashedQR() {
        return hashedQR;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String[][] getFace() {
        return face;
    }

    public double[] getGeolocation() {
        return geolocation;
    }

    public void setHashedQR(String hashedQR) {
        this.hashedQR = hashedQR;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFace(String[][] face) {
        this.face = face;
    }

    public void setGeolocation(double lat, double lon) {
        this.geolocation[0] = lat;
        this.geolocation[1] = lon;
    }
}
