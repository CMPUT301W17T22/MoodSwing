package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.ualberta.cmput301w17t22.moodswing.R.id.image;

/**
 * Activity that lets user add a new mood event to their mood history.
 * <p/>
 * Layout TODO: Increase spinner height.
 * <p/>
 * Accessed from the MainActivity through the main toolbar. Returns to the MainActivity when done.
 * <p/>
 * The following pages and code were used and adapted in building this activity on 3/10-3/12:
 * http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
 * coderzheaven.com/2012/04/20/select-an-image-from-gallery-in-android-and-show-it-in-an-imageview/
 * developer.android.com/training/camera/photobasics.html
 *
 * More details of used pages/code are in appropriate sections below.
 */

public class NewMoodEventActivity extends AppCompatActivity implements MSView<MoodSwing> {
    // for camera
    //static final int REQUEST_IMAGE_CAPTURE = 1;

    /** Used in photo selection. */
    private static int RESULT_LOAD_IMG = 1;
    private static int REQUEST_IMAGE_CAPTURE = 2;

    /** Used in photo selection. */
    String imgDecodableString;

    /** The spinner that lets the participant select their social situation for the mood event.*/
    Spinner socialSituationSpinner;

    /** The spinner that lets the participant select their emotional state for the mood event.*/
    Spinner emotionalStateSpinner;

    /** The edit text where the user can enter their given reason for the mood event. */
    EditText triggerEditText;

    /** The checkbox that indicates if the user wants to attach their location to the mood event. */
    CheckBox addCurrentLocationCheckBox;

    /** Button that when pressed indicates the user is done creating their new mood event.*/
    Button newMoodEventPostButton;

    /** Button that triggers the app to allow the user to upload a photo. */
    Button photoUploadButton;

    /** Button that triggers the app to allow the user to capture a photo. */
    Button photoCaptureButton;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mood_event);

        // Initialize all widgets for this activity and add this View to the main Model class.
        initialize();

        // Use current date/time for MoodEvent
        final Date moodDate = new Date();

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

        // Occurs when you press "Post" button
        newMoodEventPostButton.setOnClickListener(new View.OnClickListener() {
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
//                    //Get image to attach to Mood Event,
//                    // if they didn't add an ImageView then it puts null
//                    ImageView imageView = (ImageView) findViewById(R.id.imageView_NewMoodEventActivity);
//                    if(imageView.getDrawable() == null) {
//                        imageView = null;
//                    }



                    // Get MoodSwingController.
                    MoodSwingController moodSwingController =
                            MoodSwingApplication.getMoodSwingController();

                    // Add the mood event to the main participant.
                    moodSwingController.addMoodEventToMainParticipant(new MoodEvent(
                            moodSwingController.getMainParticipant().getUsername(),
                            moodDate,
                            emotionalState,
                            trigger,
                            socialSituation,
                            location,
                            null));

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

    // invoke intent to capture a photo

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
     *
     * TODO:
     * Attach image to MoodEvent
     * Limit image size
     * Find out why gallery image selection doesn't work on API 25
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
                                findViewById(R.id.imageView_NewMoodEventActivity);
                        // Set the Image in ImageView after decoding the String
                        imageView.setImageBitmap(BitmapFactory
                                .decodeFile(imgDecodableString));


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
                        // get image view
                        ImageView imageView =
                                (ImageView) findViewById(R.id.imageView_NewMoodEventActivity);
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
     * Function that informs the user nicely that an Emotional State is required.
     */
    private void showEmotionalStateIsRequiredError() {
        // Show that the emotional state is required.
        TextView errorText = (TextView)emotionalStateSpinner.getSelectedView();
        errorText.setError("");
        errorText.setTextColor(Color.RED);
        errorText.setText(getResources().getString(R.string.entry_required));

        Toast.makeText(NewMoodEventActivity.this,
                "Please enter an Emotional State.",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Function that informs the user nicely that their trigger is invalid.
     */
    private void showTriggerInvalidError() {
        triggerEditText.setError(getResources().getString(R.string.trigger_invalid));

        Toast.makeText(NewMoodEventActivity.this,
                "Trigger length too long. \nMust be under 3 words and 20 chars.",
                Toast.LENGTH_SHORT).show();
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
        // TODO: I have no clue how to do this.

        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()){
            double lat = gps.getLatitude();
            double lon = gps.getLongitude();

            return new LatLng(lat, lon);
        }
        return null;
    }
    /** Gets the image from the image view*/
//    public ImageView getImage() {
//        ImageView imageView = (ImageView) findViewById(R.id.imageView_NewMoodEventActivity);
//
//        return imageView;
//    }

    /**
     * Initializes all the widgets for this activity and adds this View to the main Model class.
     */
    public void initialize() {
        // Initialize all widgets.
        socialSituationSpinner = (Spinner) findViewById(R.id.socialSituationSpinner);
        emotionalStateSpinner = (Spinner) findViewById(R.id.emotionalStateSpinner);
        newMoodEventPostButton = (Button) findViewById(R.id.newMoodEventPostButton);
        photoUploadButton = (Button) findViewById(R.id.photoUploadButton);
        photoCaptureButton = (Button) findViewById(R.id.photoCaptureButton);
        triggerEditText = (EditText)findViewById(R.id.triggerEditText);
        addCurrentLocationCheckBox = (CheckBox) findViewById(R.id.addCurentLocationCheckBox);
        //imageView = (ImageView) findViewById(R.id.imageView_NewMoodEventActivity);


        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }

    public void update(MoodSwing moodSwing) {

    }

}
