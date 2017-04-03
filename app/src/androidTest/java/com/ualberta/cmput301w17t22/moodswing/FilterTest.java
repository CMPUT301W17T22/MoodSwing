package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-04-02.
 * Intent tests check filtering of MoodEvents. Uses MoodHistory for
 * faster testing speeds (and less robotium errors. Robotium
 * seems to be having occasional trouble with elastic search refreshing
 * and fetching. This speeds up testing and alleviates the errors).
 *
 * How to test recent week? Move clock to test a week ahead?
 */

public class FilterTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public FilterTest() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }


    /**
     * Tests filters and ensures they are working and work to filter
     * out the appropriate MoodEvents.
     */
    public void testFilters(){
        createTestData();

        // should filter out the first MoodEvent
        clickOnFilterItem("No Filter", "By Trigger"); // should click on By Recent Week
//        solo.clearEditText(R.id.triggerEditText);
//        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "rocked");
        solo.typeText(0, "rocked");
        solo.clickOnButton("Filter");
        solo.waitForActivity(MoodHistoryActivity.class);
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("the show rocked"));
        assertTrue(solo.waitForText("the game rocked"));
        assertFalse(solo.waitForText("the finale sucked"));

        // should only leave one mood event
        clickOnFilterItem("By Trigger", "By Emotion");
        solo.clickOnRadioButton(1); // should filter by anger
        solo.clickOnButton("Select");
        solo.waitForActivity(MoodHistoryActivity.class);
        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        assertTrue(solo.waitForText("the show rocked"));
        assertFalse(solo.waitForText("the game rocked"));
        assertFalse(solo.waitForText("the finale sucked"));

        clickOnFilterItem("By Emotion", "No Filter");
        assertTrue(solo.waitForText("the show rocked"));
        assertTrue(solo.waitForText("the game rocked"));
        assertTrue(solo.waitForText("the finale sucked"));

        // clear all filters
        cleanUpData();
    }



    /**
     * Creates multiple MoodHistory events for testing, then remains in MoodHistory
     */
    public void createTestData(){
        // getting to NewMoodEvent
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent303");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Mood Event Feed"));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // create mood event 1
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
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
        // this command will come in handy
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);


        // create mood event 2
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Anger
        solo.pressSpinnerItem(0,1);
        // should press Alone
        solo.pressSpinnerItem(1,1);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "the show rocked");
        solo.clickOnButton("Post");
        // this command will come in handy
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);


        // create mood event 3
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("New Mood Event");
        // Once inside NewMoodEvent:
        solo.assertCurrentActivity("Wrong Activity!", NewMoodEventActivity.class);
        // should press Confusion
        solo.pressSpinnerItem(0,2);
        // should press Alone
        solo.pressSpinnerItem(1,1);
        solo.clearEditText((EditText) solo.getView(R.id.triggerEditText));
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "the game rocked");
        solo.clickOnButton("Post");
        // this command will come in handy
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to MoodHistory
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("View Mood History");
    }

    /**
     * Starts in MoodHistory, and deletes all created MoodEvents.
     */
    public void cleanUpData() {

        solo.assertCurrentActivity("Wrong Activity!", MoodHistoryActivity.class);
        solo.sleep(5000);
        // view mood details of bottom mood event
        ListView listView = (ListView)solo.getView(R.id.moodHistory);
        View moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");

        // delete next mood event
        solo.sleep(5000);
        // view mood details of bottom mood event
        listView = (ListView)solo.getView(R.id.moodHistory);
        moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");

        // delete last mood event
        solo.sleep(5000);
        // view mood details of bottom mood event
        listView = (ListView)solo.getView(R.id.moodHistory);
        moodView = listView.getChildAt(listView.getAdapter().getCount()-1);
        solo.clickLongOnView(moodView);
        solo.waitForActivity("ViewMoodEventActivity");
        // cleanup
        solo.clickOnButton("Delete");
        solo.clickOnButton("Confirm");
        solo.waitForActivity("MoodHistoryActivity");
    }

    /**
     * Clicks on a filter item using robotium.
     * Must be in MoodHistory, will remain in MoodHistory afterwards.
     * @param currentFilter the string value of the filter being displayed in menu
     * @param desiredFilter the string value of the filter item to click on
     */
    public void clickOnFilterItem(String currentFilter, String desiredFilter){
        //solo.clickOnActionBarItem(R.id.filterToolbar_MoodHistory);
        solo.clickOnText(currentFilter);
        solo.clickOnMenuItem(desiredFilter);
        solo.sleep(2000);
    }



    // important
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
