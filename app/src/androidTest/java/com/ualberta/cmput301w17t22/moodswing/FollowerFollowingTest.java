package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by nyitrai on 3/21/2017.
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
        solo.sleep(20000);
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Welcome user \"intent301\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
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

    // Verify that requesting to follow another user displays requests
    public void testFollowing() {
        // login as the user to be followed - make sure account exists
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intentfollow");
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(20000);
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.goBack();

        // login as follower user
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent301");
        // click on login button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(20000);
        solo.waitForActivity("MainActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        // navigate to followers/following
        solo.waitForText("Welcome user \"intent301\"");
        solo.clickOnActionBarItem(R.id.mainToolBar);
        solo.waitForText("Home");
        solo.clickOnMenuItem("Followers & Following");
        //solo.clickOnView(solo.getView(R.id.followToolBarButton));
        solo.waitForActivity("MainFollowActivity");
        solo.assertCurrentActivity("Wrong Activity!", MainFollowActivity.class);

        // request to follow another user
//        solo.clickOnView(solo.getView(R.id.fab));
//        solo.enterText(0, "intent999");
//        solo.clickOnButton("Send Follow Request");
//        assertTrue(solo.waitForText("No user with given username found."));
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
        super.tearDown();
    }
}
