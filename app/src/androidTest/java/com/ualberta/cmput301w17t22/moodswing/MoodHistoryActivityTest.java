package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;


/**
 * Created by PeterWeckend on 2017-03-12.
 * Run from Login because a user needs to log in for app to work
 */

public class MoodHistoryActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public MoodHistoryActivityTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Try viewing mood history.
     * A bit long (especially to navitage to the history after making an event),
     * and because accounts save their history, it'll be searching through a pile
     * of already existing moodevents with the same descriptions, which is messy,
     * but for now it's the best I can think of.
     */
    public void testViewMoodHistory(){
        // creating a new mood event
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "Lana");
        solo.clickOnButton("login");
        assertTrue(solo.waitForText("Welcome user \"Lana\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // might be a way for this to work, but can't find out how yet
        //solo.clickOnActionBarItem(R.id.newMoodEventToolBarButton);
        // sloppier but works fine
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Happiness
        solo.pressSpinnerItem(0,5);
        // should press Crowd
        solo.pressSpinnerItem(1,2);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "danger zone");
        solo.clickOnButton("Post");
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to mood history
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        solo.scrollToBottom();
        assertTrue(solo.waitForText("danger zone"));

    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
