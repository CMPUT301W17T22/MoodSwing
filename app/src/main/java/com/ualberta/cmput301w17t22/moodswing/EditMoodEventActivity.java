package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * The Activity wherein the user edits a mood event via a very similar interface
 * to the NewMoodEventActivity. Much code is duplicated from NewMoodEventActivity.
 * <p/>
 * The user gets to this activity from the ViewMoodEventActivity and pressing the edit button there.
 * <p/>
 * The user is returned from this activity to the ViewMoodEventActivity after pressing the
 * edit button here.
 *
 */
public class EditMoodEventActivity extends AppCompatActivity implements MSView<MoodSwing> {

    /** The position of the mood event in its mood history array list. Identical to its position
     *  in chronological order in the mood history. */
    private int position;

    /** The spinner that lets the participant select their emotional state for the mood event.*/
    Spinner emotionalStateSpinner;

    /** The spinner that lets the participant select their social situation for the mood event.*/
    Spinner socialSituationSpinner;

    /** The edit text object that holds the trigger test for the mood event.*/
    EditText triggerEditText;

    /** The edit button object that confirms that the participant is done editing.*/
    Button editButton;

    /** Checkbox that indicates if the user wants to add their current location.*/
    CheckBox addCurrentLocationCheckBox;

    /**
     * Called when the EditMoodEventActivity is first created.
     * <p/>
     * Here we grab the mood event from the ViewMoodEventActivity and its position,
     * initialize all the widgets on the activity,
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood_event);

        // Receive the passed in mood event and convert it from Json.
        String moodEventJson = getIntent().getStringExtra("moodEvent");
        Gson gson = new Gson();
        final MoodEvent oldMoodEvent = gson.fromJson(moodEventJson, MoodEvent.class);


        // Receive the position of the mood event. -2 is the error code we use to detect
        // if this step went wrong. Position will be set to -2 if there is not position to get.
        position = getIntent().getIntExtra("position", -2);

        // Initialize all widgets.
        initialize();

        // Set the emotional state spinner to have the correct selection by getting the index
        // of the emotional state from the old mood event in the spinner.
        emotionalStateSpinner.setSelection(getSpinnerIndexOfString(emotionalStateSpinner,
                oldMoodEvent.getEmotionalState().getDescription()));

        // Set the social situation spinner to have the correct selection by getting the index
        // of the social situation from the old mood event in the spinner.
        socialSituationSpinner.setSelection(getSpinnerIndexOfString(socialSituationSpinner,
                oldMoodEvent.getSocialSituation().getDescription()));

        // Set the trigger appropriately.
        triggerEditText.setText(oldMoodEvent.getTrigger());


        // Edit button push. Confirms editing, proceeds to change mood event.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Emotional state is required, check that it is entered.
                // If it isn't, tell the user.
                if (Objects.equals(String.valueOf(emotionalStateSpinner.getSelectedItem()),"")){
                    showEmotionalStateIsRequiredError();
                }

                // Check if the trigger is valid. If it isn't, tell the user.
                else if (!validTrigger()){
                    showTriggerInvalidError();
                }

                // Post the Mood Event, and inform the user you are doing so.
                else {
                    // Get EmotionalState.
                    EmotionalState emotionalState = getEmotionalState();

                    // Get trigger.
                    String trigger = triggerEditText.getText().toString();

                    // Get social situation.
                    SocialSituation socialSituation = getSocialSituation();

                    // Get location if location box is checked, otherwise just use null.
                    LatLng location = null;
                    if (addCurrentLocationCheckBox.isChecked()) {
                        location = getLocation();
                    }

                    // Get MoodSwingController.
                    MoodSwingController moodSwingController =
                            MoodSwingApplication.getMoodSwingController();

                    // Edit the mood event to the main participant.
                    moodSwingController.editMoodEventToMainParticipant(position, new MoodEvent(
                            oldMoodEvent.getOriginalPoster(),
                            oldMoodEvent.getDate(),
                            emotionalState,
                            trigger,
                            socialSituation,
                            location));

                    // Toast to inform the user that the mood event was added.
                    Toast.makeText(EditMoodEventActivity.this,
                            "Mood Event added!\n" +
                                    "Input Test: " +
                                    "\nEmotional State: " + emotionalState.toString() +
                                    "\nSocial Situation: " + socialSituation.toString() +
                                    "\nTrigger: " + trigger,
                            Toast.LENGTH_SHORT).show();

                    //Convert moodEvent back to Json to send back to ViewMoodEventActivity.
                    MoodEvent editedMoodEvent = new MoodEvent(
                            oldMoodEvent.getOriginalPoster(),
                            oldMoodEvent.getDate(),
                            emotionalState,
                            trigger,
                            socialSituation,
                            location);

                    //sending moodEvent back to ViewMoodEventActivity
                    Intent intent = new Intent();
                    intent.putExtra("moodEvent",(new Gson()).toJson(editedMoodEvent));
                    setResult(RESULT_OK, intent);

                    finish();
                }
            }
        });
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
     * Function that informs the user nicely that an Emotional State is required.
     */
    private void showEmotionalStateIsRequiredError() {
        // Show that the emotional state is required.
        TextView errorText = (TextView)emotionalStateSpinner.getSelectedView();
        errorText.setError("");
        errorText.setTextColor(Color.RED);
        errorText.setText(getResources().getString(R.string.entry_required));

        Toast.makeText(EditMoodEventActivity.this,
                "Please enter an Emotional State.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Function that informs the user nicely that their trigger is invalid.
     */
    private void showTriggerInvalidError() {
        triggerEditText.setError(getResources().getString(R.string.trigger_invalid));

        Toast.makeText(EditMoodEventActivity.this,
                "Trigger length too long. \nMust be under 3 words and 20 chars.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Simple function to grab the index of a string in a spinner.
     * <p/>
     * Taken from http://www.mysamplecode.com/2012/11/android-spinner-set-selected-item-by-value.html
     * on 3/12/2017.
     * @param spinner The spinner with items to get the index of.
     * @param string The string to look for in the spinner.
     * @return The index of the string in the spinner.
     */
    private int getSpinnerIndexOfString(Spinner spinner, String string) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(string)) {
                index = i;
            }
        }
        return index;
    }


    /**
     * Returns true if entered trigger is 3 words or less than 20 chars. Returns false otherwise.
     * @return Boolean indicating if the trigger was entered properly.
     */
    public boolean validTrigger(){
        String trigger = triggerEditText.getText().toString();

        int triggerLength = trigger.length();
        String[] triggerSplit = trigger.split(Pattern.quote(" "));
        int triggerSplitLength = triggerSplit.length;

        return (triggerSplitLength <= 3 && triggerLength < 20);
    }

    /**
     * Gets the EmotionalState from the emotionalStateSpinner.
     * @return The appropriate emotional state.
     */
    public EmotionalState getEmotionalState() {
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
        // TODO: I have no clue if this works.

        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            double lat = gps.getLatitude();
            double lon = gps.getLongitude();

            return new LatLng(lat,lon);
        }
        return null;
    }

    public void initialize() {
        // Initialize all the widgets in the activity.
        emotionalStateSpinner =
                (Spinner) findViewById(R.id.emotionalStateSpinner_EditMoodEventActivity);
        socialSituationSpinner =
                (Spinner) findViewById(R.id.socialSituationSpinner_EditMoodEventActivity);
        triggerEditText =
                (EditText) findViewById(R.id.triggerEditText_EditMoodEventActivity);
        editButton =
                (Button) findViewById(R.id.newMoodEventPostButton_EditMoodEventActivity);
        addCurrentLocationCheckBox =
                (CheckBox) findViewById(R.id.addCurentLocationCheckBox_EditMoodEventActivity);

        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }

    /**
     * Update method from View superclass. Eventually want to have this pull the
     * MoodEvent to be edited straight from the main Model class MoodSwing. So we don't have
     * to deal with serializing and passing the mood events so much. For now, it sits empty.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {

    }

}
