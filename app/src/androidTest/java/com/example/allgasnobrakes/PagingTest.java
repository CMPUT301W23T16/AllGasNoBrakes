package com.example.allgasnobrakes;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Before
    public void setUp() throws InterruptedException {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
        Thread.sleep(2000);
        try {
            Log.d("test", "displayed");
            onView(withId(R.id.username_edittext)).perform(typeText("Test User"));
            onView(withId(R.id.email_edittext)).perform(typeText("user@test.com"));
            onView(withId(R.id.password_edittext)).perform(typeText("test"));
            Espresso.closeSoftKeyboard();
            onView(withId(R.id.registerbutton)).perform(click());
        } catch (NoMatchingViewException e) {
            Log.d("test", "not displayed");
            onView(withId(R.id.roll_out_button)).perform(click());
        }
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
}
