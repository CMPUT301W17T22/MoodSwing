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
 * Intent testing the editing of a Mood Event.
 * All created Mood Events are deleted after testing.
 */

public class EditMoodEventActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public EditMoodEventActivityTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Try editing mood.
     * Begins from login (to navitage to the history after making an event),
     * and because accounts save their history.
     */
    public void testEditMoodEvent(){
        // creating a new mood event
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent109");
        solo.clickOnButton("login");
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent109\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // might be a way for this to work, but can't find out how yet
        //solo.clickOnActionBarItem(R.id.newMoodEventToolBarButton);
        // sloppier but works fine
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Happiness
        solo.pressSpinnerItem(0,5);
        // should press Party
        solo.pressSpinnerItem(1,3);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "fortune");
        solo.clickOnButton("Post");
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // navigate to mood history
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("fortune"));
        solo.scrollToBottom();
        // viewing mood details
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);
        assertTrue(solo.waitForText("fortune"));

        // editing mood
        solo.clickOnButton("Edit");
        solo.waitForActivity("EditMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", EditMoodEventActivity.class);
        // should press Anger
        solo.pressSpinnerItem(0,-4);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText_EditMoodEventActivity));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText_EditMoodEventActivity), "hangover");
        solo.clickOnButton("Edit");
        // view edited mood
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);
        assertTrue(solo.waitForText("hangover"));

        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");
    }


    /**
     * Try editing mood but no mood chosen. Check for error message.
     * Begins from login (to navitage to the history after making an event),
     * and because accounts save their history.
     */
    public void testEditMoodEventMoodless(){
        // creating a new mood event
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent108");
        solo.clickOnButton("login");
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent108\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // might be a way for this to work, but can't find out how yet
        //solo.clickOnActionBarItem(R.id.newMoodEventToolBarButton);
        // sloppier but works fine
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Happiness
        solo.pressSpinnerItem(0,5);
        // should press Party
        solo.pressSpinnerItem(1,3);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "fortune");
        solo.clickOnButton("Post");
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // navigate to mood history
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("fortune"));
        solo.scrollToBottom();
        // viewing mood details
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);
        assertTrue(solo.waitForText("fortune"));

        // editing mood
        solo.clickOnButton("Edit");
        solo.waitForActivity("EditMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", EditMoodEventActivity.class);
        // should press nothing
        solo.pressSpinnerItem(0,-5);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText_EditMoodEventActivity));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText_EditMoodEventActivity), "hangover");
        solo.clickOnButton("Edit");
        // error message should appear
        assertTrue(solo.waitForText("Entry is required!"));
        solo.assertCurrentActivity("Wrong Activity!", EditMoodEventActivity.class);

        // cleanup
        solo.goBack();
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");

    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
