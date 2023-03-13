package com.example.allgasnobrakes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.widget.Button;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class SignInFragUITest {
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private static final PlayerProfile testUser =
            new PlayerProfile("Test User", "test@gmail.com", "1234",
                    0, 0);

    private static String id;

    private Solo solo; //used to call the interface component

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs once before testing to inject the testing environment
     */
    @BeforeClass
    public static void registerUser() {
        id = "ec7df04b91ff8c69";

        firestore.collection("Users").document(testUser.getUsername())
                .set(new HashMap<String, Object>(){{
                    put("Email", testUser.getEmail());
                    put("Password", testUser.getPassword());
                    put("QR Count", 0);
                    put("Total Score", 0);
                }});

        firestore.collection("DeviceID").document(id)
                .set(new HashMap<String, String>(){{
                    put("LastUser", "/Users/" + testUser.getUsername());
                }});
    }

    @Before
    public void setUp() {
        solo = new Solo (InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     */
    @Test
    public void start() {
        Activity activity = rule.getActivity();
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

    /**
     * Test for switching to different fragments using the menu bar
     */
    @Test
    public void menuBarTest() {
        signInTest();
        solo.clickOnButton("Camera");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.scanner);
        assertTrue("button not shown", (solo.getView(R.id.confirm_button)).isShown());

        solo.clickOnButton("Profile");
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.profile);
        assertTrue("button not shown", (solo.getView(R.id.search_btn)).isShown());
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
    public static void deleteTestUser() {
        firestore.collection("Users").document(testUser.getUsername()).delete();

        firestore.collection("DeviceID").document(id).delete();
    }
}
