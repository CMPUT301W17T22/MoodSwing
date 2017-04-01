package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-03-12.
 * Run from Login because a user needs to log in for app to work
 * Also tests deleting of a mood event.
 */

public class ViewMoodEventActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public ViewMoodEventActivityTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Try viewing more details of mood after created.
     * Begins from login (to navitage to the history after making an event),
     * and because accounts save their history.
     */
    public void testViewMoodEvent(){
        // creating a new mood event
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent101");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent101\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Happiness
        solo.pressSpinnerItem(0,5);
        // should press Party
        solo.pressSpinnerItem(1,3);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "fame fortune");
        solo.clickOnButton("Post");
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // navigate to mood history
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("fame fortune"));

        // view mood details
        solo.scrollToBottom();
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);
        assertTrue(solo.waitForText("fame fortune"));

        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");



    }

    /**
     * Test deleting a mood event
     */
    public void testDeleteMoodEvent(){
        // creating a new mood event
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent100");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent100\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Happiness
        solo.pressSpinnerItem(0,5);
        // should press Party
        solo.pressSpinnerItem(1,3);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "figgis");
        solo.clickOnButton("Post");
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // navigate to mood history
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("figgis"));

        // view mood details
        solo.scrollToBottom();
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);

        // test delete
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertFalse(solo.waitForText("figgis"));
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
