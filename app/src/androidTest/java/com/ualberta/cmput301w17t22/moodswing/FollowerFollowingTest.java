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

    public void testFollower() {
        // Login and verify the activities at each step.
        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent001");
        solo.clickOnButton("login");
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Welcome user \"intent001\""));
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);

        //
    }
}
