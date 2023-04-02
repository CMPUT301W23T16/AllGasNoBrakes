package com.example.allgasnobrakes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

/**
 * UI test cases for in-app paging
 * @author zhaoyu4
 * @version 2.0
 */
@RunWith(AndroidJUnit4.class)
public class PagingTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);
    private static String id;

    @Before
    public void setUp() throws InterruptedException {
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

        Thread.sleep(3000);
        onView(withText("ROLL OUT")).perform(click());
    }

    @Test
    public void testMenuPaging() {
        onView(withText("Map")).perform(click());
        onView(withText("Map")).check(matches(isSelected()));
        onView(withText("Search")).check(matches(isDisplayed()));

        onView(withText("Camera")).perform(click());
        onView(withText("Camera")).check(matches(isSelected()));
        onView(withId(R.id.scanner_view)).check(matches(isDisplayed()));

        onView(withText("Leaderboard")).perform(click());
        onView(withText("Leaderboard")).check(matches(isSelected()));
        onView(withText("The One and Only")).check(matches(isDisplayed()));

        onView(withText("Profile")).perform(click());
        onView(withText("Profile")).check(matches(isSelected()));
        onView(withId(R.id.player_profile)).check(matches(isDisplayed()));

        onView(withText("Home")).perform(click());
        onView(withText("Home")).check(matches(isSelected()));
        onView(withId(R.id.sort_order)).check(matches(isDisplayed()));
    }

    @Test
    public void testLeaderboardPaging() {
        onView(withText("Leaderboard")).perform(click());
        onView(withText("The One and Only")).check(matches(isDisplayed()));
        onView(withText("The One and Only")).check(matches(isSelected()));
        onView(withText("The Hardcore Collectors")).check(matches(isDisplayed()));
        onView(withText("The Hardcore Collectors")).perform(click());
        onView(withText("The Hardcore Collectors")).check(matches(isSelected()));
    }

    @AfterClass
    public static void deleteTestUser() throws InterruptedException {
        FirebaseFirestore.getInstance().collection("Users")
                .document("Test User").delete();
        FirebaseFirestore.getInstance().collection("DeviceID").document(id).delete();
        Thread.sleep(3000);
    }
}
