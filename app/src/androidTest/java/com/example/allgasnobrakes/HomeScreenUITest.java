package com.example.allgasnobrakes;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.allgasnobrakes.models.HashedQR;
import com.example.allgasnobrakes.models.PlayerProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class HomeScreenUITest {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    private static final PlayerProfile playerProfile = new PlayerProfile("test00", "test00@gmail.com", "1234", 0, 0);
    //private final HashedQR dummyQR = new HashedQR();

    private static HashedQR dummyQR = new HashedQR("testQR", 1, "testName", "testFace");
    private static CollectionReference collectionReference;
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }



    @Test
    public void testAddQR() {


        dummyQR.setLat(Double.toString(10.123));
        dummyQR.setLon(Double.toString(-100.123));

        playerProfile.addQR(0, dummyQR);

        assertTrue(playerProfile.getQRList().contains(dummyQR));
    }

    @Test
    public void deleteQR() {


        //HashedQR dummyQR = new HashedQR("testQR", 12, "testName", "testFace");
        dummyQR.setLat(Double.toString(10.123));
        dummyQR.setLon(Double.toString(-100.123));
        playerProfile.addQR(0, dummyQR);

        playerProfile.deleteQR(dummyQR);
    }


    @Test
    public void start() {
        Activity activity = rule.getActivity();
    }

    @Test
    public void signInTest() {
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.sign_in);
        assertTrue("button not shown", (solo.getView(R.id.roll_out_button)).isShown());
        solo.clickOnButton("ROLL OUT");

        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.homepage);
        assertTrue("button not shown", (solo.getView(R.id.sort_order)).isShown());
    }

    @Test
    public void HomeQRTest() {
        signInTest();


        solo.clickOnMenuItem("Home");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.homepage);

        TextView totalScore = (TextView) solo.getView(R.id.player_score);
        int initialTotalScore = Integer.parseInt(totalScore.getText().toString());
        //assertEquals(53, initialTotalScore);

        TextView totalTextView = (TextView) solo.getView(R.id.total_codes);
        int initialTotalCount = Integer.parseInt(totalTextView.getText().toString());
        //assertEquals(1, initialTotalCount);


    }




}
