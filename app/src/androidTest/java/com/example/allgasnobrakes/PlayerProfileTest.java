package com.example.allgasnobrakes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class PlayerProfileTest {
    private final PlayerProfile playerProfile = new PlayerProfile("Test user",
            "test@gmail.com", "1234", 0, 0);

    private final HashedQR dummyQR = new HashedQR();

    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private final CollectionReference collectionReference = firestore
            .collection("Users").document(playerProfile.getUsername())
            .collection("QRRef");

    @BeforeClass
    public static void useEmulator() {
        firestore.useEmulator("10.0.2.2", 8080);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firestore.setFirestoreSettings(settings);
    }

    @Test
    public void testDeleteQR() {
        testAddQR();
        playerProfile.deleteQR(dummyQR);
        collectionReference.document(dummyQR.getHashedQR()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        assertTrue(task.isSuccessful());
                        assertFalse(task.getResult().exists());
                    }
                });
    }

    @Test
    public void testAddQR() {
        playerProfile.addQR(dummyQR);
        collectionReference.document(dummyQR.getHashedQR()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        assertTrue(task.isSuccessful());
                        assertTrue(task.getResult().exists());
                    }
                });
    }

    @Test
    public void testRetrieveQR() {

    }
}
