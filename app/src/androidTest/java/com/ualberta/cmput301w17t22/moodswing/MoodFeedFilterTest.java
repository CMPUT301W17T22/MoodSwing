package com.ualberta.cmput301w17t22.moodswing;

/**
 * Tests filters in Mood Event Feed. Currently just tests Toast text.
 * Created by PeterWeckend on 2017-03-28.
 */

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;


public class MoodFeedFilterTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public MoodFeedFilterTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Try logging in.
     * Does not follow assignment specifications for log in yet,
     * but works with our current app build.
     */
    public void testFilterWeek(){

        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intentPoster");

        // click on button
        solo.clickOnView(solo.getView(R.id.loginButton));

        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intentPoster\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // Poster creates new activity
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Anger
        solo.pressSpinnerItem(0,1);
        solo.clickOnButton("Post");


    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
