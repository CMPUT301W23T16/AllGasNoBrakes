package com.example.allgasnobrakes;

import static junit.framework.TestCase.assertEquals;
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

/**
 * Test class for RegisterFragment. All the UI tests for the register fragment are written here
 * - Robotium is used for the tests
 */

//https://www.vogella.com/tutorials/Robotium/article.html
    //Lars Vogel, last updated 01.09.2016 - article contains a table of Solo test methods and their descriptions
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
     * Tests that the username entered is unique
     * - Test will try to register with a username that already exists
     */
    @Test
    public void aUniqueUsernameTest() {
        //Asserts activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Avi Cherry on Stackoverflow answered how to check if a fragment exists in the app
        //https://stackoverflow.com/questions/7763906/check-existence-of-a-fragment-using-robotium-android#:~:text=first%20call%20getCurrentActivity%20%28%29%20on%20Solo%20and%20then,call%20getFragmentManager%20%28%29.findFragmentById%20%28%29%20or%20getSupportFragmentManager%20%28%29.findFragmentById%20%28%29
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.register);

        //This user already exists in the database
        solo.enterText((EditText) solo.getView(R.id.username_edittext), "Alger");
        //Other info to fill input fields
        solo.enterText((EditText) solo.getView(R.id.email_edittext), "bunbury@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password_edittext), "Earnest");

        //Attempts to register this user
        solo.clickOnButton("Register");

        //Karim on Stackoverflow how to search for a Toast on a robotium test (Aug. 11, 2014)
        //https://stackoverflow.com/questions/11135105/toast-is-not-shown-android-robotium-test
        //assertTrue(solo.waitForText("Username already exists"));
        //Toast appears during test, but is not read????

        //gionnut on Stackoverflow on September 30, 2014. Seeing this post was what made me try using .isShown() in these tests
        //https://stackoverflow.com/questions/26020839/how-to-check-from-robotium-that-my-png-is-present-on-the-screen#:~:text=try%20using.isShown%20%28%29%20solo.getCurrentActivity%20%28%29.getResources%20%28%29.getDrawable%20%28R.drawable.action_drw%29.isShown%20%28%29%3B,is%20displayed%3A%20assertEquals%20%28true%2C%20solo.getCurrentActivity%20%28%29.findViewById%20%28R.id.getting_started_image_1%29.isShown%20%28%29%29%3B
        //Checks that the register button is still shown (indicating the register fragment is shown)
        //If sign-up was successful, the QRList fragment would have been shown instead
        assertTrue("button not shown", (solo.getView(R.id.registerbutton)).isShown());
    }
    @Test
    public void search(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.profile);
        if (solo.waitForText("ROLL OUT")){
            solo.clickOnButton("ROLL OUT");
        }else{
            solo.enterText((EditText) solo.getView(R.id.username_edittext), "Alger");
            //Other info to fill input fields
            solo.enterText((EditText) solo.getView(R.id.email_edittext), "bunbury@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.password_edittext), "Earnest");
            solo.clickOnButton("Register");
        }
        //This user already exists in the database

        solo.clickOnButton("Profile");
        solo.enterText((EditText) solo.getView(R.id.search_friends), "Blitz");
        solo.clickOnButton("Search");
        solo.clickOnText("blitz@gmail.vom");
        assertEquals(true, solo.waitForText("Blitz"));
    }

       //This test creates an account (through robotium). This test is dependent, it has to run last of the tests in this file
       // Uncomment these section if you want to test this (account creating)
//     @Test
//     public void createAccount() {
//         //Asserts activity
//         solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//         solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.register);

//         //This user already exists in the database
//         solo.enterText((EditText) solo.getView(R.id.username_edittext), "Gwendolen");
//         //Other info to fill input fields
//         solo.enterText((EditText) solo.getView(R.id.email_edittext), "fairfax@gmail.com");
//         solo.enterText((EditText) solo.getView(R.id.password_edittext), "earnest");

//         //Attempts to register this user
//         solo.clickOnButton("Register");

//         //Searches for "Profile" button in the bottom menu bar
//         assertTrue(solo.searchButton("Profile"));
//     }

}
