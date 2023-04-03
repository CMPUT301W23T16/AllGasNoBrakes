package com.example.allgasnobrakes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.rule.ActivityTestRule;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.allgasnobrakes.models.HashedQR;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class QRListFragmentTest {
    private static HashedQR dummyQR;
    private static String id;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,
            true, true);

    @Before
    public void setUp() throws InterruptedException {
        id = MainActivity.getId();
        dummyQR = new HashedQR("testQR", 100, "testName",
                "testFace", "testComment", "10.123", "-100.123");
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

        Thread.sleep(3000);
        onView(withText("ROLL OUT")).perform(click());
    }

    @Test
    public void testAddAndDeleteQrUiChanges() throws InterruptedException {
        MainActivity.getCurrentUser().addQR(0, dummyQR);
        Thread.sleep(2000);
        assertTrue(MainActivity.getCurrentUser().getQRList().contains(dummyQR));

        MainActivity.getCurrentUser().deleteQR(dummyQR);
        Thread.sleep(2000);
        assertFalse(MainActivity.getCurrentUser().getQRList().contains(dummyQR));
    }

    @Test
    public void testHomeScreenUiChanges() throws InterruptedException {
        MainActivity.getCurrentUser().addQR(0, dummyQR);
        Thread.sleep(2000);

        // test for player score matching with app displayed score
        String userScoretxt = String.valueOf(MainActivity.getCurrentUser().getScore());
        onView(withId(R.id.player_score)).check(matches(withText(userScoretxt)));
        Thread.sleep(2000);

        // test for scanned codes matching with app displayed scanned codes
        String userCounttxt = String.valueOf(MainActivity.getCurrentUser().getCount());
        onView(withId(R.id.total_codes)).check(matches(withText(userCounttxt)));

        // test for sort order button click changing displayed text
        Thread.sleep(2000);
        onView(withId(R.id.sort_order)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.sort_order)).check(matches(withText("Lowest Score")));
    }

    @AfterClass
    public static void deleteTestUser() throws InterruptedException {
        FirebaseFirestore.getInstance().collection("Users")
                .document("Test User").delete();
        FirebaseFirestore.getInstance().collection("DeviceID").document(id).delete();
        FirebaseFirestore.getInstance().collection("QR")
                .document("testQR").delete();
        Thread.sleep(3000);
    }
}
