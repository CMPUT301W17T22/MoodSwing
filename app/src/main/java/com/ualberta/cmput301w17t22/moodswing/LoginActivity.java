package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/** The LoginActivity is what is loaded when the app is opened for the first time
 * or after it has been closed and is being opened again. It serves the function of
 * checking if the user is logged in, if not, getting them to log in, fetching their information,
 * and continuing onto the MainActivity.
 * <p/>
 * This activity is one way only except for the case where the user chooses to log out of the app.
 * Created by nyitrai on 3/1/2017.
 */

public class LoginActivity extends AppCompatActivity implements MSView<MoodSwing> {

    /**
     * This file will be used for storing small information like the user's username that is
     * only relevant to logging in.
     */
    private static final String FILENAME = "MoodSwing.sav";

    /** Login button, when clicked, logs the user in with the username entered in the edittext. */
    Button loginButton;

    /** Sign up button, currently unused. Login button already creates a new user if none are found.*/
    Button signUpButton;

    /** The edit text where the user enters their username. */
    EditText usernameEditText;

    public ProgressBar loginProgress;

    /**
     * Triggered when the Activity first starts.
     * <p/>
     * In this we initialize buttons, and log the user in once the button is pushed.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
        loginProgress.setVisibility(View.INVISIBLE);

        /**
         * For now, data generation goes here. This will change to a different place.
         */
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.generate();

        // Initialize all widgets and add the view to the main model class.
        initialize();

        // Login Button button press. Fetch user data from ElasticSearch,
        // if nothing is found, create user data.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
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


    @Override
    protected void onResume(){
        super.onResume();
        loginProgress.setVisibility(View.GONE);
    }

    /**
     * Called when the Activity is finish()'d or otherwise closes. Removes this View from the main
     * Model's list of Views.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove this View from the main Model class' list of Views.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.removeView(this);
    }

    /**
     * Triggers the Model to load the Participant information given the entered username.
     * Called when user presses the activity_login button after entering their username.
     *
     * @return boolean value that informs if the user's information was loaded properly.
     */
    public boolean loadUser() {
        // Get the entered username from the EditText.
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        String username = usernameEditText.getText().toString();

        // Username is required to be entered.
        if (!username.isEmpty() && !username.trim().equals("")) {

            // Fetch the MoodSwingController and have it load the main participant
            // by username from ElasticSearch.
            MoodSwingController moodSwingController =
                    MoodSwingApplication.getMoodSwingController();

            // If the username for the desired participant is not in ElasticSearch,
            // a new participant will be added to ElasticSearch.
            moodSwingController.loadMainParticipantByUsername(username);

            // Initialize the mood feed.
            moodSwingController.buildMoodFeed(new int[]{0,0,0,0}, "", "");

            // Continue.
            return true;
        } else {
            // If no username is entered, refuse to continue.
            usernameEditText.setError(getResources().getString(R.string.entry_required));

            // Wait for proper input.
            return false;
        }

    }

    /**
     * Should check whether the user is logged in or not. Called onStart. Not yet implemented.
     */
    public void checkLoggedIn() {

    }

    /**
     * Initialize all widgets of this activity.
     */
    public void initialize() {
        // Initialize button.
        loginButton = (Button) findViewById(R.id.loginButton);
        // signUpButton does nothing for now.
        signUpButton = (Button) findViewById(R.id.signUpButton);
        // Initialize edit text.
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);

        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }

    /**
     * Update function for the View LoginActivity. There is nothing in this View to update,
     * so the update function does nothing.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {

    }
}
