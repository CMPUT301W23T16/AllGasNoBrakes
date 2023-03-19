package com.example.allgasnobrakes;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchTest {
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

}
