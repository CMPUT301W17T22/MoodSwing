package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

public class EditMoodEventActivity extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood_event);

        // receiving passed in moodEvent
        String moodEventJson = getIntent().getStringExtra("moodEvent");
        Gson gson = new Gson();
        final MoodEvent moodEvent = gson.fromJson(moodEventJson, MoodEvent.class);

        // Recieve the position of the mood event.
        Log.i("MoodSwing", String.valueOf(position));
        position = getIntent().getIntExtra("position", -2);

        // Grab a snapshot of the MoodEvent so we know which one we are editing.
        final MoodEvent oldMoodEvent = moodEvent.getDeepCopy();

        // Initialize all the widgets in the activity.
        final Spinner emotionalStateSpinner =
                (Spinner) findViewById(R.id.emotionalStateSpinner_EditMoodEventActivity);
        final EditText triggerEditText =
                (EditText) findViewById(R.id.triggerEditText_EditMoodEventActivity);
        final Spinner socialSituationSpinner =
                (Spinner) findViewById(R.id.socialSituationSpinner_EditMoodEventActivity);
        final Button editButton =
                (Button) findViewById(R.id.newMoodEventPostButton_EditMoodEventActivity);

        // Grab a snapshot of the Mood

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

        // Fill the social situation value appropriately.
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

        // Set the trigger appropriately.
        triggerEditText.setText(moodEvent.getTrigger());

        // Edit button push, post edited mood event to elasticsearch.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Emotional state is required.
                if (Objects.equals(String.valueOf(emotionalStateSpinner.getSelectedItem()),"")){
                    Toast.makeText(EditMoodEventActivity.this,
                            "Please enter an Emotional State.",
                            Toast.LENGTH_SHORT).show();
                }
                // Check if the trigger is valid.
                else if (!validTrigger()){
                    Toast.makeText(EditMoodEventActivity.this,
                            "Trigger length too long. \nMust be under 3 words and 20 chars.",
                            Toast.LENGTH_SHORT).show();
                }

                // Post the Mood Event, and inform the user you are doing so.
                else {
                    // Get EmotionalState.
                    EmotionalState emotionalState = getEmotionalState();

                    // Get trigger.
                    String trigger = triggerEditText.getText().toString();

                    // Get social situation.
                    SocialSituation socialSituation = getSocialSituation();

                    // Get the old date.
                    Date date = oldMoodEvent.getDate();

                    // Get location if location box is checked, otherwise just use null.
                    LatLng location = null;
                    if (includeLocationCheck()) {
                        location = getLocation();
                    }

                    // Get MoodSwingController.
                    MoodSwingController moodSwingController =
                            MoodSwingApplication.getMoodSwingController();

                    // Add the mood event to the main participant.
                    moodSwingController.editMoodEventToMainParticipant(position,
                            date,
                            emotionalState,
                            trigger,
                            socialSituation,
                            location);

                    // Toast to inform the user that the mood event was added.
                    Toast.makeText(EditMoodEventActivity.this,
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

    /**
     * Returns true if addCurrentLocationCheckBox is checked, and false otherwise.
     * @return The state of the addCurrentLocationCheckBox.
     */
    public boolean includeLocationCheck(){
        CheckBox addCurentLocationCheckBox =
                (CheckBox) findViewById(R.id.addCurentLocationCheckBox_EditMoodEventActivity);
        //            Toast.makeText(NewMoodEventActivity.this,
//                    "Checked", Toast.LENGTH_LONG).show();
        return addCurentLocationCheckBox.isChecked();
    }


    /**
     * Returns true if entered trigger is 3 words or less than 20 chars. Returns false otherwise.
     * @return Boolean indicating if the trigger was entered properly.
     */
    public boolean validTrigger(){
        EditText triggerEditText =
                (EditText)findViewById(R.id.triggerEditText_EditMoodEventActivity);
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
        final Spinner emotionalStateSpinner =
                (Spinner) findViewById(R.id.emotionalStateSpinner_EditMoodEventActivity);

        // Get EmotionalStateFactory to create an emotional state object.
        EmotionalStateFactory emotionalStateFactory = new EmotionalStateFactory();

        // Create the EmotionalState by the string selected on the spinner and return it.
        return emotionalStateFactory.createEmotionalStateByName(
                String.valueOf(emotionalStateSpinner.getSelectedItem()));
    }

    /**
     * Gets the SocialSituation from the socialSituationSpinner.
     * @return The appropriate social situation.
     */
    public SocialSituation getSocialSituation() {
        final Spinner socialSituationSpinner =
                (Spinner) findViewById(R.id.socialSituationSpinner_EditMoodEventActivity);

        // Get Social Situation Factory to create a social situation object.
        SocialSituationFactory socialSituationFactory = new SocialSituationFactory();

        // Create social situation by the string entered and return it.
        return socialSituationFactory.createSocialSituationByName(
                String.valueOf(socialSituationSpinner.getSelectedItem()));
    }

    /**
     * Gets the LatLng location from the android device.

     * @return The current / last known location as a LatLng
     */
    public LatLng getLocation() {
        // TODO: I have no clue how to do this.

        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            double lat = gps.getLatitude();
            double lon = gps.getLongitude();

            return new LatLng(lat,lon);
        }
        return null;
    }

}
