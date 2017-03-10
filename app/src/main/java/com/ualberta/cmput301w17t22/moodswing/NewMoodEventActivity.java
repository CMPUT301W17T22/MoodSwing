package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Activity that lets user add a new mood event to their mood history.
 * Layout TODO:
 * Increase spinner height
 */

public class NewMoodEventActivity extends AppCompatActivity {
    // need to add this view to moodswing application?

    // for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mood_event);

        // going to just use pre-decided list of strings to represent moods and emotional states
        // for now, its a lot easier than a dynamic list (like in MoodOptions) for now
        // will have to change/update them if we end up changing the moods/states allowed
        final Spinner socialSituationSpinner = (Spinner) findViewById(R.id.socialSituationSpinner);
        final Spinner emotionalStateSpinner = (Spinner) findViewById(R.id.emotionalStateSpinner);

        final Button newMoodEventPostButton = (Button) findViewById(R.id.newMoodEventPostButton);
        Button photoUploadButton = (Button) findViewById(R.id.photoUploadButton);

        // use current date/time for MoodEvent
        final Date moodDate = new Date();

        // photo upload press. Need a real device to test this on
        // https://developer.android.com/training/camera/photobasics.html
        photoUploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // take photo, need to check on real device
                //dispatchTakePictureIntent();
            }
        });

        // Occurs when you press "Post" button. At the moment, just closes activity.
        newMoodEventPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validTrigger(v);

                // Emotional state is required.
                if (Objects.equals(String.valueOf(emotionalStateSpinner.getSelectedItem()),"")){
                    Toast.makeText(NewMoodEventActivity.this,
                            "Please enter an Emotional State.",
                            Toast.LENGTH_SHORT).show();
                }
                // Check if the trigger is valid.
                else if (!validTrigger()){
                    Toast.makeText(NewMoodEventActivity.this,
                            "Trigger length too long. \nMust be under 3 words and 20 chars.",
                            Toast.LENGTH_SHORT).show();
                }

                // Post the Mood Event, and inform the user you are doing so.
                else {
                    // Get MoodEvent Controller.
                    MoodEventController moodEventController =
                            MoodSwingApplication.getMoodEventController();

                    // Get EmotionalState.
                    EmotionalState emotionalState = getEmotionalState();

                    // Get trigger.
                    EditText triggerEditText = (EditText)findViewById(R.id.triggerEditText);
                    String trigger = triggerEditText.getText().toString();

                    // Get social situation.
                    SocialSituation socialSituation = getSocialSituation();

                    // Get location if location box is checked, otherwise just use null.
                    LatLng location = null;
                    if (includeLocationCheck()) {
                        location = getLocation();
                    }

                    // Add the mood event to the main participant.
                    moodEventController.addMoodEvent(
                            emotionalState,
                            trigger,
                            socialSituation,
                            // Place holder values, not sure how this is going to work.
                            "photoLocation",
                            "iconLocation",
                            location);

                    // Toast to inform the user that the mood event was added.
                    Toast.makeText(NewMoodEventActivity.this,
                            "Mood Event added!\n" +
                                    "Input Test: " +
                                    "\nEmotional State: " + emotionalState.toString() +
                                    "\nSocial Situation: " + socialSituation.toString() +
                                    "\nTrigger: " + trigger,
                            Toast.LENGTH_SHORT).show();

                    finish();
                }

            }
        });
    }



    // for taking a picture
    // https://github.com/DroidNinja/Android-FilePicker
    // https://developer.android.com/training/camera/photobasics.html
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Returns true if addCurrentLocationCheckBox is checked, and false otherwise.
     * @return The state of the addCurrentLocationCheckBox.
     */
    public boolean includeLocationCheck(){
        CheckBox addCurentLocationCheckBox = (CheckBox) findViewById(R.id.addCurentLocationCheckBox);
        //            Toast.makeText(NewMoodEventActivity.this,
//                    "Checked", Toast.LENGTH_LONG).show();
        return addCurentLocationCheckBox.isChecked();
    }


    /**
     * Returns true if entered trigger is 3 words or less than 20 chars. Returns false otherwise.
     * @return Boolean indicating if the trigger was entered properly.
     */
    public boolean validTrigger(){
        EditText triggerEditText = (EditText)findViewById(R.id.triggerEditText);
        String trigger = triggerEditText.getText().toString();

        int triggerLength = trigger.length();
        String[] triggerSplit = trigger.split(Pattern.quote(" "));
        int triggerSplitLength = triggerSplit.length;

        // test output
//        Toast.makeText(NewMoodEventActivity.this,
//                Integer.toString(triggerSplitLength), Toast.LENGTH_SHORT).show();

        return (triggerSplitLength <= 3 || triggerLength < 20);
    }

    /**
     * Gets the EmotionalState from the emotionalStateSpinner.
     * @return The appropriate emotional state.
     */
    public EmotionalState getEmotionalState() {
        final Spinner emotionalStateSpinner = (Spinner) findViewById(R.id.emotionalStateSpinner);

        // Get EmotionalState Controller to create an emotional state object.
        EmotionalStateController emotionalStateController =
                MoodSwingApplication.getEmotionalStateController();

        // Create the EmotionalState by the string selected and return it.
        return emotionalStateController.createEmotionalStateByName(
                String.valueOf(emotionalStateSpinner.getSelectedItem()));
    }

    /**
     * Gets the SocialSituation from the socialSituationSpinner.
     * @return The appropriate social situation.
     */
    public SocialSituation getSocialSituation() {
        final Spinner socialSituationSpinner = (Spinner) findViewById(R.id.socialSituationSpinner);

        // Get Social Situation Controller to create a social situation object.
        SocialSituationController socialSituationController =
                MoodSwingApplication.getSocialSituationController();

        // Create social situation by the string entered and return it.
        return socialSituationController.createSocialSituationByName(
                String.valueOf(socialSituationSpinner.getSelectedItem()));
    }

    /**
     * Gets the LatLng location from the android device.
     * @return The current / last known location as a LatLng
     */
    public LatLng getLocation() {
        // TODO: I have no clue how to do this.
        return null;
    }





}
