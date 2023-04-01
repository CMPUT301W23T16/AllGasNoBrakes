package com.example.allgasnobrakes.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Contains player profile information
 * @author zhaoyu4 zhaoyu5
 * @version 5.0
 */
public class PlayerProfile implements Serializable {
    public static final String UNIQUE_HIGHEST_RANK = "uniqueHighestRank";
    public static final String COLLECTOR_RANK = "collectorRank";
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String username;
    private String email;
    private String password;
    private int uniqueHighestRank = 0;
    private int collectorRank = 0;
    private int displayMetric;
    private ArrayList<HashedQR> QRList = new ArrayList<>();
    private final ProfileSummary profileSummary = new ProfileSummary(0, 0);
    private boolean test = false;

    public PlayerProfile() {
        username = "testUser";
        test = true;
    }

    /**
     * Constructor without password, for searching for friends account
     * @param username The username of the account
     * @param email The email of the account
     */
    public PlayerProfile(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Constructor with password, for the user's account
     * @param username The username of the account
     * @param email The username of the account
     * @param password The password of the account
     */
    public PlayerProfile(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor with password, with initialization of player profile summary, for the user's
     * account
     * @param username The username of the account
     * @param email The username of the account
     * @param password The password of the account
     * @param score The total QR score of the player account
     * @param count The total number of the QR codes that account has
     */
    public PlayerProfile(String username, String email, String password, int score, int count) {
        this.username = username;
        this.email = email;
        this.password = password;
        profileSummary.assign(count, score);
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

    public ProfileSummary getProfileSummary() {
        return profileSummary;
    }

    public HashedQR getQR(int position) {
        return QRList.get(position);
    }

    public int getDisplayMetric() {
        return displayMetric;
    }

    public int getUniqueHighestRank() {
        return uniqueHighestRank;
    }

    public int getCollectorRank() {
        return collectorRank;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayMetric(Number displayMetric) {
        this.displayMetric = displayMetric.intValue();
    }

    public void addPropertyChangeListener(String field, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(field, listener);
    }

    public void addScorePropertyChangeListener(String field1, String field2,
                                               PropertyChangeListener listener1,
                                               PropertyChangeListener listener2) {
        profileSummary.addPropertyChangeListener(field1, listener1);
        profileSummary.addPropertyChangeListener(field2, listener2);
    }

    /**
     * Retrieves the collection of HashedQR that a player has collected and stores it locally. Also
     * notifies the view that displays this information to update itself with the latest data.
     * @param QrAdapter - the view to be updated
     * @param sortOrder - the order by which to sort the QR code
     */
    public void retrieveQR(RecyclerView.Adapter QrAdapter, String sortOrder) {
        // Notify the views to update
        setRank();

        Query.Direction order;

        if (sortOrder.equals("Highest Score")) {
            order = Query.Direction.DESCENDING;
        } else {
            order = Query.Direction.ASCENDING;
        }

        FirebaseFirestore.getInstance().collection("QR")
                .whereArrayContains("OwnedBy", username)
                .orderBy("Score", order)
                .orderBy("Name", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QRList.clear();

                        // We get the hashed value for each of QRs that the player has...
                        for (QueryDocumentSnapshot QR : task.getResult()) {
                            HashedQR newQR = new HashedQR(QR.getId(), QR.get("Score", int.class),
                                    QR.get("Name", String.class), QR.get("Face", String.class));

                            FirebaseFirestore.getInstance().document("/QR/" + QR.getId() + "/Players/" + username).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot meta = task.getResult();
                                            newQR.setComment(meta.get("Comment", String.class));
                                            newQR.setLat(meta.getString("Lat"));
                                            newQR.setLon(meta.getString("Lon"));
                                        }
                                    });

                            QRList.add(newQR);
                            QrAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * Removed the QR code from the user's account
     * @param QR The QR code to be deleted
     */
    public void deleteQR(HashedQR QR) {
        QRList.remove(QR);

        FirebaseFirestore.getInstance().document("/QR/" + QR.getHashedQR() + "/Players/" + username).delete();
        FirebaseFirestore.getInstance().document("/QR/" + QR.getHashedQR())
                .update("OwnedBy", FieldValue.arrayRemove(username))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updatePlayerCount(QR.getHashedQR());
                    }
                });

        profileSummary.update(getUsername(), -1, -QR.getScore());
    }

    /**
     * Implementation of the UNDO function in case the user deleted a QR code by accident
     * @param QR The QR code to be re-added
     */
    public void addQR(int position, HashedQR QR) {
        QRList.add(position, QR);
        profileSummary.update(getUsername(), 1, QR.getScore());

        HashMap<String, Object> meta = new HashMap<>();

        meta.put("Comment", QR.getComment());
        meta.put("Lat", QR.getLat().toString());
        meta.put("Lon", QR.getLon().toString());
        FirebaseFirestore.getInstance().collection("QR").document(QR.getHashedQR())
                .collection("Players").document(username)
                .set(meta);

        FirebaseFirestore.getInstance().collection("QR").document(QR.getHashedQR())
                .update("OwnedBy", FieldValue.arrayUnion(username))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updatePlayerCount(QR.getHashedQR());
                    }
                });
        Log.d("update", "2");
    }

    private void updatePlayerCount(String hash) {
        FirebaseFirestore.getInstance().collection("QR").document(hash)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot qr = task.getResult();
                        int arraySize = ((ArrayList<String>) qr.get("OwnedBy")).size();
                        FirebaseFirestore.getInstance().collection("QR").document(hash)
                                .update("PlayerCount", arraySize)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("arr size", String.format(Locale.CANADA, "%d", arraySize));
                                        setRank();
                                    }
                                });
                    }
                });
    }

    public void setRank() {
        setUniqueHighestRank();
        setCollectorRank();
    }

    private void setUniqueHighestRank() {
        FirebaseFirestore.getInstance().collection("QR")
                .whereEqualTo("PlayerCount", 1)
                .orderBy("Score", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int rank = 1;
                        boolean found = false;

                        for (DocumentSnapshot doc: task.getResult()) {
                            String playerName = ((ArrayList<String>) doc.get("OwnedBy")).get(0);
                            if (username.equals(playerName)) {
                                found = true;
                                break;
                            }
                            rank++;
                        }

                        if (found) {
                            int oldRank = uniqueHighestRank;
                            uniqueHighestRank = rank;
                            pcs.firePropertyChange(UNIQUE_HIGHEST_RANK, oldRank, rank);
                        } else {
                            uniqueHighestRank = -1;
                            pcs.firePropertyChange(UNIQUE_HIGHEST_RANK, 0, -1);
                        }
                    }
                });
    }

    private void setCollectorRank() {
        FirebaseFirestore.getInstance().collection("Users")
                .orderBy("Total Score", Query.Direction.DESCENDING)
                .limit(100)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        int rank = 1;
                        boolean found = false;

                        for (DocumentSnapshot doc: task.getResult()) {
                            String playerName = doc.getId();
                            if(username.equals(playerName)) {
                                found = true;
                                break;
                            }
                            rank++;
                        }

                        if (found) {
                            int oldRank = collectorRank;
                            collectorRank = rank;
                            pcs.firePropertyChange(COLLECTOR_RANK, oldRank, rank);
                        } else {
                            uniqueHighestRank = -1;
                            pcs.firePropertyChange(COLLECTOR_RANK, 0, -1);
                        }
                    }
                });
    }
}