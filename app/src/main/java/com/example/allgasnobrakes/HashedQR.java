package com.example.allgasnobrakes;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Describes the hashed QR code
 * @author zhaoyu4
 * @version 2.0
 */
public class HashedQR implements Comparator<HashedQR>, Serializable {
    private final String hashedQR;
    private final int score;
    private String name;
//    private String[][] face;
//    private final double[] geolocation= new double[2];
//    String name, String[][] face, int lat, int lon


    @Override
    public int compare(HashedQR o1, HashedQR o2) {
        if (o1.score == o2.getScore()) {
            return o1.name.compareTo(o2.getName());
        } else {
            return o1.score - o2.getScore();
        }
    }

    public HashedQR() {
        this.hashedQR = "hashedQR";
        this.score = 0;
        this.name = "name";
    }

    public HashedQR(String hashedQR, int score, String name) {
        this.hashedQR = hashedQR;
        this.score = score;
        this.name = name;
//        this.face = face;
//        geolocation[0] = lat;
//        geolocation[1] = lon;
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
//
//    public String[][] getFace() {
//        return face;
//    }
//
//    public double[] getGeolocation() {
//        return geolocation;
//    }
//
//    public void setHashedQR(String hashedQR) {
//        this.hashedQR = hashedQR;
//    }
//
//    public void setScore(int score) {
//        this.score = score;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setFace(String[][] face) {
//        this.face = face;
//    }
//
//    public void setGeolocation(double lat, double lon) {
//        this.geolocation[0] = lat;
//        this.geolocation[1] = lon;
//    }
}
