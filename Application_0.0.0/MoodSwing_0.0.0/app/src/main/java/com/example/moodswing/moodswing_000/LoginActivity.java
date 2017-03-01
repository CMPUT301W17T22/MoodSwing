package com.example.moodswing.moodswing_000;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

/** The LoginActivity is what is loaded when the app is opened for the first time
 * or after it has been closed and is being opened again. It serves the function of
 * checking if the user is logged in, if not, getting them to log in, fetching their information,
 * and continuing onto the MainActivity.
 *
 * This activity is one way only except for the case where the user chooses to log out of the app.
 * Created by nyitrai on 3/1/2017.
 */

public class LoginActivity extends AppCompatActivity implements MSView<MoodSwing> {

    /**
     * This file is used for storing small information like the user's username that is
     * only relevant to logging in.
     */
    private static final String FILENAME = "MoodSwing.sav";

    /**
     * Triggered when the Activity first starts.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button loginButton = (Button) findViewById(R.id.loginButton);
        EditText username = (EditText) findViewById(R.id.username);

        // Add this view to the MoodSwingApplication.
        MoodSwing ms = MoodSwingApplication.getMoodSwing();
        ms.addView(this);
    }

    /**
     * Grabs the Participant information from ElasticSearch.
     */
    public void fetchUser() {

    }


    /**
     * Should check whether the user is logged in or not.
     */
    public void checkLoggedIn() {

    }


    /**
     * Update function for the LoginActivity. There is nothing in this View to update,
     * so the update function does nothing.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {

    }
}
