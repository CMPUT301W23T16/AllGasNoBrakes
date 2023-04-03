package com.example.allgasnobrakes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.allgasnobrakes.models.HashedQR;
import com.example.allgasnobrakes.models.PlayerProfile;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.chrono.MinguoChronology;
import java.util.HashMap;

/**
 * Test user sign in procedures
 */
@RunWith(AndroidJUnit4.class)
public class SignInFragUITest {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private Solo solo; //used to call the interface component
    private static String id;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo (InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        id = MainActivity.getId();
        FirebaseFirestore.getInstance().collection("DeviceID").document(id)
                .set(new HashMap<String, Object>() {{
                    put("LastUser", "/Users/Test User");
                }});

        FirebaseFirestore.getInstance().collection("Users")
                .document("Test User")
                .set(new HashMap<String, Object>() {{
                    put("Email", "test");
                    put("Password", "test");
                    put("Total Score", 0);
                    put("QR Count", 0);
                }});
    }

    /**
     * Sign in to the homepage (QR list). Assumes existing user.
     */
    @Test
    public void signInTest() {
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.sign_in);
        assertTrue("button not shown", (solo.getView(R.id.roll_out_button)).isShown());
        solo.clickOnButton("ROLL OUT");

        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.homepage);
        assertTrue("button not shown", (solo.getView(R.id.sort_order)).isShown());
    }

    @Test
    public void sortButtonTest() {
        signInTest();
        solo.clickOnButton("Highest Score");
        solo.waitForText("Lowest Score", 1, 2);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.homepage);
        assertEquals("Lowest Score",
                ((Button) solo.getView(R.id.sort_order)).getText().toString());
    }

    /**
     * Runs after all the tests
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();  //will cycle through to close down the activities
    }

    @AfterClass
    public static void deleteTestUser() throws InterruptedException {
        firestore.collection("Users").document("Test User").delete();
        firestore.collection("DeviceID").document(id).delete();
        Thread.sleep(3000);
    }
}
