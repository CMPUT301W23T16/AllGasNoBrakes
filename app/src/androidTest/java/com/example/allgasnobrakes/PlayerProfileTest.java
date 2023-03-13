package com.example.allgasnobrakes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;


@RunWith(AndroidJUnit4.class)
public class PlayerProfileTest {
    private static final PlayerProfile playerProfile = new PlayerProfile("Breaker",
            "test@gmail.com", "1234", 0, 0);

    private final HashedQR dummyQR = new HashedQR();
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference collectionReference;

    /**
     * Use an emulated Firestore instead of a real one. Initialize with a test user.
     */
    @BeforeClass
    public static void setUp() {
        firestore.collection("Users").document(playerProfile.getUsername())
                .set(new HashMap<String, Object>(){{
                    put("Email", playerProfile.getEmail());
                    put("Password", playerProfile.getPassword());
                    put("QR Count", 0);
                    put("Total Score", 0);
                }});

        collectionReference = firestore
                .collection("Users").document(playerProfile.getUsername())
                .collection("QRRef");
    }

    @Test
    public void testAddQR() throws InterruptedException {
        playerProfile.addQR(dummyQR);
        Thread.sleep(3000);

        firestore.collection("Users").document(playerProfile.getUsername())
                .collection("QRRef")
                .document(dummyQR.getHashedQR()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            assertTrue(task.getResult().exists());
                        } else {
                            fail("Document does not exist");
                        }
                    }
                });
    }

    @Test
    public void testDeleteQR() throws InterruptedException {
        playerProfile.addQR(dummyQR);
        playerProfile.deleteQR(dummyQR);

        firestore.collection("Users").document(playerProfile.getUsername())
                .collection("QRRef")
                .document(dummyQR.getHashedQR()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        assertFalse(task.isSuccessful());
                    }
                });
    }

    @AfterClass
    public static void deleteTestUser() throws InterruptedException {
        firestore.collection("Users").document(playerProfile.getUsername()).delete();
        Thread.sleep(3000);
    }
}
