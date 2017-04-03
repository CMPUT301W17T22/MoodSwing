package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
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

    /** Used in photo selection. */
    private static int RESULT_LOAD_IMG = 1;
    /** Used in photo selection. */
    private static int REQUEST_IMAGE_CAPTURE = 2;
    /** Used in photo selection. */
    private static int COMPRESSION_AMOUNT = 16;

    /** Used in photo selection. */
    String imgDecodableString;

    /** The toolbar, shows the title. */
    Toolbar editMoodToolbar;


    /** The position of the mood event in its mood history array list. Identical to its position
     *  in chronological order in the mood history. */
    private int position;

    /** The mood event being edited as it first arrived to this Activity.*/
    MoodEvent oldMoodEvent;

    /** The spinner that lets the participant select their emotional state for the mood event.*/
    Spinner emotionalStateSpinner;

    /** The spinner that lets the participant select their social situation for the mood event.*/
    Spinner socialSituationSpinner;

    /** The edit text object that holds the trigger test for the mood event.*/
    EditText triggerEditText;

    /** The edit button object that confirms that the participant is done editing.*/
    Button editButton;


    /**Button used to trigger the gallery option to upload a photo. */
    Button photoUploadButton;

    /**Button used to trigger the camera on the phone to take a picture and attach it to the Mood Event. */
    Button photoCaptureButton;

    /**Image View used to display the chosen image. */
    ImageView imageView;

    /**A byte array we write the image to in order to compress it. */
    ByteArrayOutputStream image = null;

    /**If the image is edited, then change this to true.*/
    boolean imageEdited = false;

    /**Initialize all the Views or Widgets in the Activity  */
    public void initialize() {

        //initialize widgets to variable names
        emotionalStateSpinner =
                (Spinner) findViewById(R.id.emotionalStateSpinner_EditMoodEventActivity);
        socialSituationSpinner =
                (Spinner) findViewById(R.id.socialSituationSpinner_EditMoodEventActivity);

        triggerEditText = (EditText) findViewById(R.id.triggerEditText_EditMoodEventActivity);
        editButton = (Button) findViewById(R.id.newMoodEventPostButton_EditMoodEventActivity);
        photoUploadButton = (Button) findViewById(R.id.photoUploadButton_EditMoodEventActivity);
        photoCaptureButton = (Button) findViewById(R.id.photoCaptureButton_EditMoodEventActivity);

        imageView = (ImageView) findViewById(R.id.imageView_EditMoodEventActivity);

        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);

        //setting the toolbar
        editMoodToolbar = (Toolbar) findViewById(R.id.editMoodToolbar);
        editMoodToolbar.setTitle("Edit Mood Event");
    }

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

        // Initialize all widgets.
        initialize();

        // Load the mood event information including position in mood history from mood swing.
        loadFromMoodSwing();

        // Load the mood event information into the widgets of the app.
        loadFromOldMoodEvent();

        // photo upload press. Need a real device to test this on
        // https://developer.android.com/training/camera/photobasics.html
        photoUploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // **Outdated for now
                // take photo, need to check on real device
                //dispatchTakePictureIntent();
                uploadGalleryImage(v);

            }
        });

        // on photo capture press. Will open the camera to take a picture
        photoCaptureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        // Edit button push. Confirms editing, proceeds to change mood event.
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Emotional state is required, check that it is entered.
                // If it isn't, tell the user.
                if (Objects.equals(String.valueOf(emotionalStateSpinner.getSelectedItem()), "")){
                    showEmotionalStateIsRequiredError();
                }

                // Check if the trigger is valid. If it isn't, tell the user.
                else if (!validTrigger()){
                    showTriggerInvalidError();
                }

                // Post the updated Mood Event, and inform the user you are doing so.
                else {
                    // Get EmotionalState.
                    EmotionalState emotionalState = getSpinnerEmotionalState();

                    // Get trigger.
                    String trigger = triggerEditText.getText().toString();

                    // Get social situation.
                    SocialSituation socialSituation = getSpinnerSocialSituation();

                    // if image is not edited:
                    if (!imageEdited) {
                        Bitmap imageBitmap = oldMoodEvent.getImage();
                        // If there was a previous image.
                        if (imageBitmap != null) {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();

                            imageBitmap.compress(Bitmap.CompressFormat.JPEG,
                                    COMPRESSION_AMOUNT, out);

                            image = out;
                        }
                    }

                    // Get lastKnownLocation if lastKnownLocation box is checked,
                    // otherwise just use the old value.
                    double lat = oldMoodEvent.getLat();
                    double lng = oldMoodEvent.getLng();


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
                            lat,
                            lng,
                            image));

                    // Toast to inform the user that the mood event was edited.
                    Toast.makeText(EditMoodEventActivity.this,
                            "Mood Event edited!",
                            Toast.LENGTH_SHORT).show();
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

    /** Load the mood event and the position of the mood event in the main participant's mood
     * history from MoodSwing.
     */
    private void loadFromMoodSwing() {
        // Get the moodSwingController from the application class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();

        position = moodSwingController.getMoodHistoryPosition();
        oldMoodEvent = moodSwingController.getMainParticipant().getMoodHistory().get(position);
    }

    /**
     * Load the values from the loaded mood event into the widgets of the activity.
     */
    private void loadFromOldMoodEvent() {
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

        //Set the Image from the old mood event.
        //image = oldMoodEvent.getImage();
        if(oldMoodEvent.getImage() != null) {
            imageView.setImageBitmap(oldMoodEvent.getImage());
        }
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
    public EmotionalState getSpinnerEmotionalState() {
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
    public SocialSituation getSpinnerSocialSituation() {
        // Get Social Situation Factory to create a social situation object.
        SocialSituationFactory socialSituationFactory = new SocialSituationFactory();

        // Create social situation by the string entered and return it.
        return socialSituationFactory.createSocialSituationByName(
                String.valueOf(socialSituationSpinner.getSelectedItem()));
    }


    /**
     * Create and launch intent to view and select photo from gallery.
     *
     * Uses code sourced from below sites on 3/11-3/12:
     * http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
     * coderzheaven.com/2012/04/20/
     *      select-an-image-from-gallery-in-android-and-show-it-in-an-imageview/
     *
     * @param v the view we use
     */
    public void uploadGalleryImage(View v){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(i, RESULT_LOAD_IMG);
    }

    /**
     * Invoke intent to capture a photo.
     * Uses code from below site on 03/12:
     * developer.android.com/training/camera/photobasics.html
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * After user selects an image from gallery or takes a picture,
     * grab image and display thumbnail.
     *
     * Uses code adapted from the following sites on 03/11.
     * http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
     * coderzheaven.com/2012/04/20/
     *      select-an-image-from-gallery-in-android-and-show-it-in-an-imageview/
     * developer.android.com/training/camera/photobasics.html
     * @param requestCode onActivityResult uses this to know which intent finished
     * @param resultCode = RESULT_OK if everything worked
     * @param data is returned by the intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch(requestCode) {
                case (1):   // user picks image from gallery
                {
                    // When an Image is picked
                    if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                            && null != data) {
                        // Get the Image from data

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);

                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgDecodableString = cursor.getString(columnIndex);
                        cursor.close();

                        // get image view
                        ImageView imageView = (ImageView)
                                findViewById(R.id.imageView_EditMoodEventActivity);
                        // Set the Image in ImageView after decoding the String
                        imageView.setImageBitmap(BitmapFactory
                                .decodeFile(imgDecodableString));

                        //image = BitmapFactory.decodeFile(imgDecodableString);
                        Bitmap imageBitmap = BitmapFactory.decodeFile(imgDecodableString);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, out);
                        image = out;
                        imageEdited = true;


                    } else {
                        Toast.makeText(this, "You haven't picked an image",
                                Toast.LENGTH_LONG).show();
                    }
                }

                case (2):   // user takes picture with camera
                {
                    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_AMOUNT, out);
                        image = out;
                        imageEdited = true;



                        // get image view
                        ImageView imageView =
                                (ImageView) findViewById(R.id.imageView_EditMoodEventActivity);
                        imageView.setImageBitmap(imageBitmap);  // display thumbnail
                    }
                }
                break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG)
                    .show();
        }

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
