package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;


/**
 * Created by PeterWeckend on 2017-03-12.
 * Run from Login because a user needs to log in for app to work.
 * Intent testing viewing the Mood History a user and ensuring a newly
 * created Mood Event shows up.
 * All created Mood Events are deleted after testing.
 * Filters are handled in FilterTest
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
     * Begins from login (to navitage to the history after making an event),
     * and because accounts save their history.
     */
    public void testViewMoodHistory(){
        // creating a new mood event
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent102");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent102\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
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
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        solo.scrollToBottom();
        assertTrue(solo.waitForText("danger zone"));

        // cleanup
        // view mood details
        solo.scrollToBottom();
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);
        assertTrue(solo.waitForText("danger zone"));
        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");


    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
