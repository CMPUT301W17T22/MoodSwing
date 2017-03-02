package com.example.moodswing.moodswing_000;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        // Initialize button.
        Button loginButton = (Button) findViewById(R.id.loginButton);

        // Add this view to the MoodSwingApplication.
        MoodSwing ms = MoodSwingApplication.getMoodSwing();
        ms.addView(this);

        // Login Button button press. Fetch user data from ElasticSearch,
        // if nothing is found, create user data.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the user into the main Model class.
                boolean loadOK = loadUser();

                if (loadOK) {
                    // Launch the MainActivity.
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    /**
     * Triggers the Model to load the Participant information given the entered username.
     * Called when user presses the login button after entering their username.
     *
     * @return boolean value that informs if the user's information was loaded properly.
     */
    public boolean loadUser() {
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        String username = usernameEditText.getText().toString();

        // Username is required to be entered.
        if (!username.isEmpty() && !username.trim().equals("")) {

            // Fetch user information from the Model.
            // Get the main Model class.
            MoodSwing moodSwing = MoodSwingApplication.getMoodSwing();

            // Have the main Model class fetch the user's information.
            moodSwing.getParticipantByUsername(username);

            // Continue.
            return true;
        } else {
            // If no username is entered, refuse to continue.
            usernameEditText.setError("Entry is required!");

            // Wait for proper input.
            return false;
        }

    }

    /**
     * Should check whether the user is logged in or not. Called onStart.
     */
    public void checkLoggedIn() {

    }


    /**
     * Update function for the View LoginActivity. There is nothing in this View to update,
     * so the update function does nothing.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {

    }
}
