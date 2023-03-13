package com.example.allgasnobrakes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;


@RunWith(AndroidJUnit4.class)
public class PlayerProfileTest {
    private static final PlayerProfile playerProfile = new PlayerProfile("Test user",
            "test@gmail.com", "1234", 0, 0);

    private final HashedQR dummyQR = new HashedQR();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static CollectionReference collectionReference;

    /**
     * Use an emulated Firestore instead of a real one. Initialize with a test user.
     */
    @BeforeClass
    public static void useEmulator() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("127.0.0.1", 8080);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firestore.setFirestoreSettings(settings);

        firestore.collection("Users").document(playerProfile.getUsername())
                .set(new HashMap<String, Object>(){{
                    put("Email", playerProfile.getEmail());
                    put("Password", playerProfile.getPassword());
                }});

        collectionReference = firestore
                .collection("Users").document(playerProfile.getUsername())
                .collection("QRRef");
    }

    @Test
    public void testDeleteQR() {
        testAddQR();
        playerProfile.deleteQR(dummyQR);
        assertFalse(collectionReference.document(dummyQR.getHashedQR()).get().isSuccessful());
    }

    @Test
    public void testAddQR() {
        playerProfile.addQR(dummyQR);
        assertTrue(collectionReference.document(dummyQR.getHashedQR()).get().isSuccessful());
    }

    @Test
    public void testRetrieveQR() {

    }
}
