package com.example.allgasnobrakes;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

/**
 * Contains player profile information
 * @author zhaoyu4 zhaoyu5
 * @version 2.0
 */
public class PlayerProfile extends Observable implements Serializable {
    private String username;
    private String email;
    private String password;
    private ArrayList<HashedQR> QRList = new ArrayList<>();
    private final QRCounter profileSummary = new QRCounter(0, 0);

    /**
     * Constructor without password, for searching for friends account
     * @param username The username of the account
     * @param email The email of the account
     */
    public PlayerProfile(String username, String email) {
        super();
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
        super();
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
        super();
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

    public QRCounter getProfileSummary() {
        return profileSummary;
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

    /**
     * Retrieves the collection of HashedQR that a player has collected and stores it locally. Also
     * notifies the view that displays this information to update itself with the latest data.
     * @param QrAdapter - the view to be updated
     */
    public void retrieveQR(RecyclerView.Adapter QrAdapter, String sortOrder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Users")
                .document(username).collection("QRRef");

        Query.Direction order;
        if (sortOrder.equals("Highest Score")) {
            order = Query.Direction.DESCENDING;
        } else {
            order = Query.Direction.ASCENDING;
        }


        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> QRS = new ArrayList<>();

                // We get the hashed value for each of QRs that the player has...
                for (QueryDocumentSnapshot QRRef : task.getResult()) {
                    DocumentReference hashedQR = db.document(QRRef.getString("QRReference"));
                    QRS.add(hashedQR.getId());
                }

                for (String i : QRS) {Log.d("Test", i);}

                // Then in the QR database, we retrieve the details of those QRs and store the data locally
                if (!QRS.isEmpty()) {
                    db.collection("/QR")
                            .whereIn("Hash", QRS)
                            .orderBy("Score", order)
                            .orderBy("Name", Query.Direction.ASCENDING)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QRList.clear();
                                        int score = 0;
                                        for (QueryDocumentSnapshot QR : task.getResult()) {
                                            Log.d("GetQR", QR.getId() + " => " + QR.getData());
                                            String QRHash = QR.getId();
                                            String QRName = (String) QR.get("Name");
                                            String QRFace = (String) QR.get("Face");
                                            Number QRScore = (Number) QR.get("Score");
                                            score += QRScore.intValue();
                                            QRList.add(new HashedQR(QRHash, QRScore.intValue(), QRName,QRFace));
                                            // QRList.sort(new HashedQR().reversed());
                                            QrAdapter.notifyDataSetChanged(); // Notify the view to update
                                        }
                                        profileSummary.assign(QRS.size(), score);
                                        setChanged();
                                        notifyObservers();
                                    } else {
                                        Log.d("GetQR", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        });
        Log.d("Test", "Called");
    }

    /**
     * Removed the QR code from the user's account
     * @param QR The QR code to be deleted
     */
    public void deleteQR(HashedQR QR) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(username).collection("QRRef")
                .document(QR.getHashedQR()).delete();

        profileSummary.update(-1, -QR.getScore());
        setChanged();
        notifyObservers();
    }

    /**
     * Implementation of the UNDO function in case the user deleted a QR code by accident
     * @param QR The QR code to be re-added
     */
    public void addQR(HashedQR QR) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(username).collection("QRRef")
                .document(QR.getHashedQR())
                .set(new HashMap<String, Object>(){
                    {put("QRReference", "QR/" + QR.getHashedQR());}
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("Add QR", "Data has been added successfully!");
                        profileSummary.update(1, QR.getScore());
                        setChanged();
                        notifyObservers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d("Add QR", "Data could not be added!" + e.toString());
                    }
                });
    }
}