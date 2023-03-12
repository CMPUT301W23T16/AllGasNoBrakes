package com.example.allgasnobrakes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchPlayerUITest {

    private Solo solo;

    @Rule
    //initalTouchMode is set to true so that anything that touches the screen during the test isn't affected/side effects
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests
     */
    @Before
    public void setUp() {
        solo = new Solo (InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.clickOnButton("ROLL OUT");
        solo.clickOnButton("Profile");
    }

    /**
     * Gets the Activity
     */
    @Test
    public void start() {
        Activity activity = rule.getActivity();
    }

    /**
     * Runs after all the tests
     */
    @After
    public void tearDown() {
        solo.finishOpenedActivities();  //will cycle through to close down the activities
    }

    //Intent Tests
    /**
     * Search for the player "Algernon" - exists in database
     * and click on the name to make the QR codes appear
     */
    @Test
    public void playerExists() {
        //Asserts Main Activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.profile);

        //Searches for user named "Algernon" - exists in database
        solo.enterText((EditText) solo.getView(R.id.search_friends), "Algernon");
        solo.clickOnButton("Search");

        //searches from email connected to Algernon's account
        assertTrue(solo.searchText("bunbury@gmail.com"));
    }

    /**
     * Search for the player "Cecily" - username does not exist in database.
     * Then, will try to search for an email "cecily@gmail.com - does not exist in database,
     * so it will not appear on the screen
     */
    @Test
    public void playerNotFound() {
        //Asserts Main Activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.profile);

        //searches for non-existent player named "Cecily"
        solo.enterText((EditText) solo.getView(R.id.search_friends), "Cecily");
        solo.clickOnButton("Search");

        //searches for non-existent email connected to "Cecily"
        assertFalse(solo.searchText("cecily@gmail.com"));
    }
}
