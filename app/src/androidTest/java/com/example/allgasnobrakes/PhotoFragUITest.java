package com.example.allgasnobrakes;

import static org.junit.Assert.assertFalse;

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
public class PhotoFragUITest {
    private Solo solo;

    @Rule
    //initalTouchMode is set to true so that anything that touches the screen during the test isn't affected/side effects
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Gets the Activity
     */
    @Test
    public void start() {
        Activity activity = rule.getActivity();
    }

    /**
     * Runs before all tests
     * Signs in or registers for the app
     */
    @Before
    public void setUp() {
        solo = new Solo (InstrumentationRegistry.getInstrumentation(),rule.getActivity());

        if (solo.searchButton("Register")) {
            //This user will be created in the database
            solo.enterText((EditText) solo.getView(R.id.username_edittext), "Hamlet");

            //Other info to fill input fields
            solo.enterText((EditText) solo.getView(R.id.email_edittext), "denmark@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.password_edittext), "mousetrap");

            //Register this user
            solo.clickOnButton("Register");
        } else {
            //Signs into the app
            solo.clickOnButton("ROLL OUT");
        }
        solo.clickOnButton("Camera");
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
     * Attempt to take a photo without scanning a qr code
     * The app will show a toast and NOT move to the photo taking fragment
     */
    @Test
    public void noQRCode() {
        //Asserts Main Activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.getCurrentActivity().getFragmentManager().findFragmentById(R.layout.scanner);

        //Tries to take photo without scanning QR code
        solo.clickOnButton("Take Photo");
        assertFalse(solo.searchButton("Photo Button"));
    }
}
