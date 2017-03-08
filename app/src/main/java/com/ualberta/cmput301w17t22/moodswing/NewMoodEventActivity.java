package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity that lets user add a new mood event to their mood history.
 * Layout TODO:
 * Increase spinner size
 */

public class NewMoodEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mood_event);
    }
}
