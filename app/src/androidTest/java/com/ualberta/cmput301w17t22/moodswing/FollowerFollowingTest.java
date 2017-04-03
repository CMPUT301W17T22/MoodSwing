package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by nyitrai on 3/21/2017.
 * Some code commented to save time in test testing.
 * Does NOT test viewing MoodFeed events of followers/following.
 * That is handled in MoodFeed intent testing.
 */

public class FollowerFollowingTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public FollowerFollowingTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    // Verify that requesting to follow non-existant user fails
    public void testFollowingInvalidUser() {
        // login and navigate to following/followers
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent301");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);
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
        solo.enterText(0, "intent999");
        solo.clickOnButton("Send Follow Request");
        assertTrue(solo.waitForText("No user with given username found."));
    }

//    /**
//     * Verify that following another user and having that user decline the request
//     * works in UI.
//     */
    public void testFollowingDeclineRequest() {
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
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent302");
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

        // check follow requests and deny the request
        solo.clickOnText("Follow Requests");
        assertTrue(solo.waitForText("intent302"));
        // should work because intentfollow should only ever have one request at a time
        solo.clickOnImageButton(1); // click 'deny'
        solo.clickOnButton("Deny");
        solo.sleep(5000);
        assertFalse(solo.waitForText("intent302"));



        // login as user who sent request and ensure request is gone
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent302");
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

        solo.clickOnText("Following");
        assertFalse(solo.waitForText("intentfollow"));
        // check follow request is gone
        solo.clickOnText("Your Requests");
        assertFalse(solo.waitForText("intentfollow"));

    }


    /**
     * Verify that following another user and having the user cancel the request
     * works.
     */
    public void testFollowingCancelRequest() {
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
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent303");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Welcome user \"intent303\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
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
        solo.clickOnImageButton(0);  // cancel request
        solo.clickOnText("Cancel Request");
        assertFalse(solo.waitForText("intentfollow"));


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
        solo.waitForText("Welcome user \"intentfollow\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // check follow requests and make sure cancelled one is gone
        solo.clickOnText("Follow Requests");
        assertFalse(solo.waitForText("intent303"));
    }



    /**
     * Verify that following another user works, and then test
     * unfollowing.
     * TODO: Retry this test when the follower/following bugs are fixed.
     */
    public void testFollowingApproveRequest() {
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
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent304");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Welcome user \"intent304\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
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
        solo.waitForText("Welcome user \"intentfollow\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // check follow requests and approve the request
        solo.clickOnText("Follow Requests");
        assertTrue(solo.waitForText("intent304"));
        // should work because intentfollow should only ever have one request at a time
        solo.clickOnImageButton(0); // click checkmark to approve
        solo.clickOnButton("Approve");
        solo.sleep(5000);
        assertFalse(solo.waitForText("intent304"));
        // Ensure follower shows up
        solo.clickOnText("Followers");
        solo.sleep(5000);
        // this is where the error occurs?
        assertTrue(solo.waitForText("intent304"));

        // login as user who sent request and ensure request approved
        solo.goBack();
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent304");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);  // make sure MainActivity has loaded
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Welcome user \"intent304\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
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

        // Make sure follower is gone in intentfollow
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
        solo.waitForText("Welcome user \"intentfollow\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // check follow requests and approve the request
        solo.clickOnText("Followers");
        assertFalse(solo.waitForText("intent304"));
    }


    // important
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
