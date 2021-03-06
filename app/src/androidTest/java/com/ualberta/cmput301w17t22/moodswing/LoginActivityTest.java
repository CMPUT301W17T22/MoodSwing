package com.ualberta.cmput301w17t22.moodswing;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import junit.framework.TestCase;
import com.robotium.solo.Solo;

/**
 * Created by PeterWeckend on 2017-03-12.
 * Does not follow assignment specifications for log in yet,
 * but works with our current app build.
 *
 * Have had some problems with intent tests not being able to log into
 * the main page (MainActivity) from the login screen (LoginActivity).
 * Re-running the test a second time almost always fixes the problem.
 * The tests themselves are fine, I suspect it’s something related to
 * network connectivity and elasticsearch.
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
     * Does not follow assignment specifications for log in yet,
     * but works with our current app build.
     */
    public void testLogIn(){

        solo.assertCurrentActivity("Wrong Activity!", LoginActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.usernameEditText));
        solo.enterText((EditText) solo.getView(R.id.usernameEditText), "intent106");

        // click on button
        solo.clickOnView(solo.getView(R.id.loginButton));
        solo.sleep(30000);
        solo.waitForActivity("MainActivity");
        assertTrue(solo.waitForText("Mood Event Feed"));

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
        // click on button
        solo.clickOnView(solo.getView(R.id.loginButton));

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
