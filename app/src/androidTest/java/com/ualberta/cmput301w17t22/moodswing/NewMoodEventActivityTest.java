package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import junit.framework.TestCase;
import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-03-12.
 * Run from Login because a user needs to log in for app to work.
 * Deletes created Mood Event when finished.
 */

public class NewMoodEventActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public NewMoodEventActivityTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Try creating new mood event.
     */
    public void testNewMoodEvent(){
        // getting to NewMoodEvent
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent103");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent103\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("New Mood Event");


        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);

        // should press Anger
        solo.pressSpinnerItem(0,1);

        // should press Alone
        solo.pressSpinnerItem(1,1);

        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "the finale sucked");

        solo.clickOnButton("Post");

        //assertTrue(solo.waitForText("Entry is required!"));

        // this command will come in handy
        solo.waitForActivity("MainActivity");

        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // cleanup
        // navigate to mood history
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("View Mood History");
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("the finale sucked"));
        // view mood details
        solo.scrollToBottom();
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        solo.assertCurrentActivity("Wrong Activity!", ViewMoodEventActivity.class);
        assertTrue(solo.waitForText("the finale sucked"));
        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");
    }


    /**
     * Try creating new mood event without mood event to catch error message.
     */
    public void testNewMoodEventMoodless(){
        // getting to NewMoodEvent
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent104");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent104\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("New Mood Event");


        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);

        // should press Alone
        solo.pressSpinnerItem(1,1);

        solo.clickOnButton("Post");

        assertTrue(solo.waitForText("Entry is required!"));

        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
    }


    /**
     * Try creating new mood event with incompatable triggers to catch error message.
     */
    public void testNewMoodEventTriggerLen(){
        // getting to NewMoodEvent
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent105");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent105\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("New Mood Event");


        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);

        // should press Anger
        solo.pressSpinnerItem(0,1);

        // should press Alone
        solo.pressSpinnerItem(1,1);

        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "123456789012345678901");

        solo.clickOnButton("Post");

        assertTrue(solo.waitForText("Trigger length too long."));

        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText),
                "1234567890 1234567890 123456890");

        assertTrue(solo.waitForText("Trigger length too long."));

        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
