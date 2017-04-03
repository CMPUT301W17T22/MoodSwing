package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-03-28.
 *
 * Have had some problems with intent tests not being able to log into
 * the main page (MainActivity) from the login screen (LoginActivity).
 * Re-running the test a second time almost always fixes the problem.
 * The tests themselves are fine, I suspect it’s something related to
 * network connectivity and elasticsearch.
 */

public class MoodFeedTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public MoodFeedTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Tests making sure intent307 can see intentfollow1's most recent
     * MoodEvent if intent307 is following intentfollow1.
     */
    public void testFollowUser() {
        // login as the user to be followed - make sure account exists
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intentfollow1");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000); // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // Post a mood event
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
        solo.enterText((EditText) solo.getView(R.id.triggerEditText), "walking dog");
        solo.clickOnButton("Post");
        solo.waitForActivity("MainActivity");
        solo.goBack();

        // login as follower user
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent307");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Mood Event Feed");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // request to follow another user
        solo.clickOnView(solo.getView(R.id.fab));
        solo.enterText(0, "intentfollow1");
        solo.clickOnButton("Send Follow Request");
        solo.sleep(10000);  // ensure request is sent

        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // login as user to be followed again and make sure request shows up
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intentfollow1");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Mood Event Feed");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // check follow requests and approve the request
        solo.clickOnText("Follow Requests");
        assertTrue(solo.waitForText("intent307"));
        // should work because intentfollow1 should only ever have one request at a time
        solo.clickOnImageButton(0); // click checkmark to approve
        solo.clickOnButton("Approve");
        solo.sleep(5000);
        assertFalse(solo.waitForText("intent307"));
        solo.sleep(5000);

        // login as user who sent request
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent307");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        // make sure MoodFeed event is there
        assertTrue(solo.waitForText("walking dog"));



        // for cleanup
        // navigate to followers/following
        solo.waitForText("Mood Event Feed");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // unfollow the intentfollow1 participant
        solo.clickOnText("Following");
        solo.sleep(10000); // wait for text to show up
        solo.waitForText("intentfollow1");
        // unfollow user
        solo.clickOnImageButton(0);
        solo.clickOnButton("Unfollow");

    }



    // important
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

}
