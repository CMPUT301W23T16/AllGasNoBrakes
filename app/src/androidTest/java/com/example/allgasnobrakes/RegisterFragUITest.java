package com.example.allgasnobrakes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.view.View;
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


/**
 * Test class for RegisterFragment. All the UI tests for the fragment are written here
 * - Robotium is used for the tests
 */
@RunWith(AndroidJUnit4.class)
public class RegisterFragUITest {

    private Solo solo;  //used to call the interface component

    @Rule
    //initalTouchMode is set to true so that anything that touches the screen during the test isn't affected/side effects
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests
     */
    @Before
    public void setUp() {
        solo = new Solo (InstrumentationRegistry.getInstrumentation(),rule.getActivity());
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

    /**
     * Checks that RegisterFragment is shown
     */
    @Test
    public void shownRegisterFrag() {
        //Asserts activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Avi Cherry on Stackoverflow answered how to check if a fragment exists in the app
        //https://stackoverflow.com/questions/7763906/check-existence-of-a-fragment-using-robotium-android#:~:text=first%20call%20getCurrentActivity%20%28%29%20on%20Solo%20and%20then,call%20getFragmentManager%20%28%29.findFragmentById%20%28%29%20or%20getSupportFragmentManager%20%28%29.findFragmentById%20%28%29
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.register);

        //Checks that the register button is still shown (indicating the register fragment is shown
        assertTrue("button not shown", (solo.getView(R.id.registerbutton)).isShown());


    }

    /**
     * Tests that the username entered is unique
     * - Test will try to register with a username that already exists
     */
    @Test
    public void uniqueUsernameTest() {
        //Asserts activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.register);

        //This user already exists in the database
        solo.enterText((EditText) solo.getView(R.id.username_edittext), "Algernon");
        //Other info to fill input fields
        solo.enterText((EditText) solo.getView(R.id.email_edittext), "bunbury@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password_edittext), "Earnest");

        //Attempts to register this user
        solo.clickOnButton("Register");

        //Karim on Stackoverflow how to search for a Toast on a robotium test (Aug. 11, 2014)
        //https://stackoverflow.com/questions/11135105/toast-is-not-shown-android-robotium-test
        //Will search for the string in the toast
        //assertTrue(solo.waitForText("Username already exists"));

        //Checks that the register button is still shown (indicating the register fragment is shown)
        //If sign-up was successful, the QRList fragment would have been shown instead
        assertTrue("button not shown", (solo.getView(R.id.registerbutton)).isShown());
    }

}
