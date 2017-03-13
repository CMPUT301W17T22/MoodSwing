package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import junit.framework.TestCase;
import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-03-12.
 *
 */

public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }


    public void setUp() throws Exception{
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }


    /**
     * Try logging in.
     * Does not follow assignment specifications for log in yet.
     */
    public void testLogIn(){

        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "TedMosby");
        solo.clickOnButton("login");

        assertTrue(solo.waitForText("Welcome user \"tedmosby\""));

        // this command will come in handy
        //solo.waitForActivity("MainActivity");

        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
    }


    /**
     * Try logging in without entering username.
     * Does not follow assignment specifications for log in yet.
     */
    public void testLogInNoInput(){

        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.clickOnButton("login");

        assertTrue(solo.waitForText("Entry is required!"));

        // this command will come in handy
        //solo.waitForActivity("MainActivity");

        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();  // important
    }

}
