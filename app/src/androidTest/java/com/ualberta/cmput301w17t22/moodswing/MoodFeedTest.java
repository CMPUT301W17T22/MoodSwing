package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-03-28.
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

//
//    public void testViewRecentPost(){
//        FollowUser();
//    }



    public void testFollowUser() {
        // login as the user to be followed - make sure account exists
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intentfollow");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000); // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.goBack();

        // login as follower user
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent305");
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
        solo.enterText(0, "intentfollow");
        solo.clickOnButton("Send Follow Request");
        assertTrue(solo.waitForText("Request successfully sent!"));
        solo.clickOnText("Your Requests");
        assertTrue(solo.waitForText("intentfollow"));



        // login as user to be followed again and make sure request shows up
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intentfollow");
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
        assertTrue(solo.waitForText("intent305"));
        // should work because intentfollow should only ever have one request at a time
        solo.clickOnImageButton(0); // click checkmark to approve
        solo.clickOnButton("Approve");
        solo.sleep(5000);
        assertFalse(solo.waitForText("intent305"));
        solo.sleep(5000);
        // Ensure follower shows up
        solo.clickOnText("Followers");
        solo.sleep(5000);
        // this is where the error occurs?
        assertTrue(solo.waitForText("intent305"));

        // login as user who sent request
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent305");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);



        // for future cleanup
        // navigate to followers/following
        solo.waitForText("Mood Event Feed");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("New Mood Event");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);


        // check follow request is gone
        solo.clickOnText("Your Requests");
        assertFalse(solo.waitForText("intentfollow"));
        // make sure you're following
        solo.clickOnText("Following");
        assertTrue(solo.waitForText("intentfollow"));
        // unfollow user
        solo.clickOnImageButton(0);
        solo.clickOnButton("Unfollow");
        assertFalse(solo.waitForText("intentfollow"));

    }



    // important
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

}
