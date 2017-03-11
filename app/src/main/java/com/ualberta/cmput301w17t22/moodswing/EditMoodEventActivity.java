package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

public class EditMoodEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood_event);

        // receiving passed in moodEvent
        String moodEventJson = getIntent().getStringExtra("moodEvent");
        Gson gson = new Gson();
        final MoodEvent moodEvent = gson.fromJson(moodEventJson, MoodEvent.class);

        // Setting all fields to existing MoodEvent values
        Spinner emotionalStateSpinner =
                (Spinner) findViewById(R.id.emotionalStateSpinner_EditMoodEventActivity);

        // Grab the appropriate emotional state and set it to be selected in the spinner.
        switch (moodEvent.getEmotionalState().getDescription()) {
            case "Anger":
                emotionalStateSpinner.setSelection(1);
                break;

            case "Confusion":
                emotionalStateSpinner.setSelection(2);
                break;

            case "Disgust":
                emotionalStateSpinner.setSelection(3);
                break;

            case "Fear":
                emotionalStateSpinner.setSelection(4);
                break;

            case "Happiness":
                emotionalStateSpinner.setSelection(5);
                break;

            case "Sadness":
                emotionalStateSpinner.setSelection(6);
                break;

            case "Shame":
                emotionalStateSpinner.setSelection(7);
                break;

            case "Surprise":
                emotionalStateSpinner.setSelection(8);
                break;

            default:
                Log.i("ERROR", "Invalid Emotional State when setting image.");
                throw new IllegalArgumentException(
                        "Invalid Emotional State");
        }

        // Grab the social situation spinner, and then fill its value appropriately.
        Spinner socialSituationSpinner =
                (Spinner) findViewById(R.id.socialSituationSpinner_EditMoodEventActivity);

        switch (moodEvent.getSocialSituation().getDescription()) {
            case "Alone":
                socialSituationSpinner.setSelection(1);
                break;

            case "Crowd":
                socialSituationSpinner.setSelection(2);
                break;

            case "Party":
                socialSituationSpinner.setSelection(3);
                break;

            case "":
                socialSituationSpinner.setSelection(0);
                break;

            default:
                Log.i("ERROR", "Invalid Social Situation while displaying image.");
                throw new IllegalArgumentException(
                        "Invalid Social Situation while displaying image.");
        }


    }
}
