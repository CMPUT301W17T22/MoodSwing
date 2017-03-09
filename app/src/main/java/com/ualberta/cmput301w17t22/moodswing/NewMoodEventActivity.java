package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

/**
 * Activity that lets user add a new mood event to their mood history.
 * Layout TODO:
 * Increase spinner height
 */

public class NewMoodEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mood_event);

        // going to just use pre-decided list of strings to represent moods and emotional states
        // for now, its a lot easier than a dynamic list (like in MoodOptions) for now
        // will have to change/update them if we end up changing the moods/states allowed
        Spinner socialsituationSpinner = (Spinner) findViewById(R.id.socialSituationSpinner);
        Spinner emotionalStateSpinner = (Spinner) findViewById(R.id.emotionalStateSpinner);
    }
}
