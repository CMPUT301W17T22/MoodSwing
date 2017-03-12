package com.ualberta.cmput301w17t22.moodswing;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Activity is launched when app user chooses to view a mood event.
 */
public class ViewMoodEventActivity extends AppCompatActivity implements MSView<MoodSwing> {

    // Declare every widget or value we need.
    private int position;
    TextView usernameTextView;
    TextView emotionalStateTextView;
    TextView socialSituationTextView;
    TextView triggerTextView;
    ImageView emotionalStateImageView;
    ImageView socialSituationImageView;
    ImageView imageImageView;
    Button editMoodEventButton;
    String moodEventJson;
    Button deleteMoodEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood_event);

        // Get the position of the MoodEvent in the MoodHistory from the ViewMoodHistoryActivity.
        position = getIntent().getIntExtra("position", -2);

        // Initialize the textviews.
        usernameTextView =
                (TextView) findViewById(R.id.usernameTextView_ViewMoodEventActivity);
        emotionalStateTextView =
                (TextView) findViewById(R.id.emotionalStateTextView_ViewMoodEventActivity);
        socialSituationTextView =
                (TextView) findViewById(R.id.socialSituationTextView_ViewMoodEventActivity);
        triggerTextView =
                (TextView) findViewById(R.id.triggerTextView_ViewMoodEventActivity);

        // Initialize the image views.
        emotionalStateImageView =
                (ImageView) findViewById(R.id.emotionalStateImageView_ViewMoodEventActivity);
        socialSituationImageView =
                (ImageView) findViewById(R.id.socialSituationImageView_ViewMoodEventActivity);
        imageImageView =
                (ImageView) findViewById(R.id.imageImageView_ViewMoodEventActivity);

        // Initialize the buttons.
        editMoodEventButton =
                (Button) findViewById(R.id.editMoodEventButton_ViewMoodEventActivity);
        deleteMoodEventButton =
                (Button) findViewById(R.id.deleteMoodEventButton_ViewMoodEventActivity);

        // Grab the mood event.
        moodEventJson = getIntent().getStringExtra("moodEvent");
    }

    /**
     * This runs when the activity is returned to. i.e. from the EditMoodEventActivity.
     */
    protected void onStart(){
        super.onStart();

        // Get the MoodEvent that is passed through from the ViewMoodHistoryActivity.
        Gson gson = new Gson();
        final MoodEvent moodEvent = gson.fromJson(moodEventJson, MoodEvent.class);

        // Hide the button if they do not own the MoodEvent.
        // Get the main participant.
        Participant mainParticipant =
                MoodSwingApplication.getMoodSwingController().getMainParticipant();
        if (moodEvent.getOriginalPoster().equals(mainParticipant.getUsername())) {
            editMoodEventButton.setVisibility(View.VISIBLE);
        } else {
            editMoodEventButton.setVisibility(View.GONE);
        }

        editMoodEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewMoodEventActivity.this, EditMoodEventActivity.class);
                // Serialize the mood event, convert to json, and put extra on the intent.
                intent.putExtra("moodEvent", (new Gson()).toJson(moodEvent));
                intent.putExtra("position", position);

                // Launch the ViewMoodEventActivity.
                startActivityForResult(intent,1);
            }
        });

        //Delete Button onClick Listener
        deleteMoodEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                // Create the alert dialog builder with the right theme.
                AlertDialog.Builder adb =
                        new AlertDialog.Builder(ViewMoodEventActivity.this, R.style.DialogTheme);

                // Set the message and the title.
                adb.setMessage("Are you sure you want to delete this Mood Event?")
                        .setTitle("Delete Mood Event")
                        .setCancelable(true);

                // Create the positive button for the alert dialog.
                adb.setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // Delete the mood event.

                        // Get the MoodSwingController to delete the MoodEvent.
                        MoodSwingController moodSwingController =
                                MoodSwingApplication.getMoodSwingController();

                        // Delete the mood event by passing in the position.
                        moodSwingController.deleteMoodEventFromMainParticipantByPosition(position);

                        // Inform the user that the mood event was deleted.
                        Toast.makeText(ViewMoodEventActivity.this,
                                "Mood Event deleted!",
                                Toast.LENGTH_SHORT).show();

                        finish();

                    }
                });

                // Create the negative button for the alert dialog.
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // Do nothing, cancel the dialog box.
                        dialog.cancel();
                    }
                });

                // Build and show the dialog box.
                AlertDialog dialog = adb.create();
                dialog.show();
            }
        });
        // Load values from MoodEvent into the text fields.
        usernameTextView.setText(moodEvent.getOriginalPoster());
        emotionalStateTextView.setText(moodEvent.getEmotionalState().getDescription());
        socialSituationTextView.setText(moodEvent.getSocialSituation().getDescription());
        triggerTextView.setText(moodEvent.getTrigger());

        // Load values from MoodEvent into the images.
        switch (moodEvent.getEmotionalState().getDescription()) {
            case "Anger":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_anger));
                break;

            case "Confusion":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_confusion));
                break;

            case "Disgust":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_disgust));
                break;

            case "Fear":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_fear));
                break;

            case "Happiness":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_happiness));
                break;

            case "Sadness":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_sadness));
                break;

            case "Shame":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_shame));
                break;

            case "Surprise":
                emotionalStateImageView.setImageDrawable(getDrawable(R.drawable.emoticon_surprise));
                break;

            default:
                Log.i("ERROR", "Invalid Emotional State when setting image.");
                throw new IllegalArgumentException(
                        "Invalid Emotional State");
        }

        // Set the image for the appropriate social situation.
        switch (moodEvent.getSocialSituation().getDescription()) {
            case "Alone":
                socialSituationTextView.setText("While alone");
                socialSituationImageView.setImageDrawable(
                        getDrawable(R.drawable.social_situation_alone));
                break;

            case "Crowd":
                socialSituationTextView.setText("While in a crowd");
                socialSituationImageView.setImageDrawable(
                        getDrawable(R.drawable.social_situation_crowd));
                socialSituationImageView.setVisibility(View.VISIBLE);
                break;

            case "Party":
                socialSituationTextView.setText("While in a party");
                socialSituationImageView.setImageDrawable(
                        getDrawable(R.drawable.social_situation_party));
                socialSituationImageView.setVisibility(View.VISIBLE);
                break;

            case "":
                socialSituationTextView.setText("");
                // No social situation was entered, so make the image invisible.
                socialSituationImageView.setVisibility(View.INVISIBLE);
                break;

            default:
                Log.i("ERROR", "Invalid Social Situation while displaying image.");
                throw new IllegalArgumentException(
                        "Invalid Social Situation while displaying image.");
        }

    }

    /**
     * When returning from the EditMoodEventActivity, get the changed information and properly
     * display it.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                // All we have to do is change the moodEventJson that we load from in the onStart.
                moodEventJson = data.getStringExtra("moodEvent");
            }
        }

    }

    public void initialize(){

        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }

    public void update(MoodSwing moodSwing){

    }
}
