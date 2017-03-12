package com.ualberta.cmput301w17t22.moodswing;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static android.R.attr.data;

/**
 * Activity that lets user add a new mood event to their mood history.
 * Layout TODO:
 * Increase spinner height
 *
 * http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/
 * http://www.coderzheaven.com/2012/04/20/select-an-image-from-gallery-in-android-and-show-it-in-an-imageview/
 */

public class NewMoodEventActivity extends AppCompatActivity {
    // need to add this view to moodswing application?

    // for camera
    //static final int REQUEST_IMAGE_CAPTURE = 1;

    // for photo selection
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

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
        Button photoCaptureButton = (Button) findViewById(R.id.photoCaptureButton);

//        // use current date/time for MoodEvent
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

            }
        });



        // Occurs when you press "Post" button
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

                    // Get MoodSwingController.
                    MoodSwingController moodSwingController =
                            MoodSwingApplication.getMoodSwingController();

                    // Add the mood event to the main participant.
                    moodSwingController.addMoodEventToMainParticipant(moodDate,
                            emotionalState,
                            trigger,
                            socialSituation,
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

    // create and launch intent to view and select photo from gallery
    public void uploadGalleryImage(View v){
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(i, RESULT_LOAD_IMG);
    }

    // once user has selected a photo from gallery using Upload button
    // currently does not work on API 25
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                // this doesn't work
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // get image view
                ImageView imageView = (ImageView) findViewById(R.id.imageView_NewMoodEventActivity);
                // Set the Image in ImageView after decoding the String
                imageView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));


            } else {
                Toast.makeText(this, "You haven't picked an image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_LONG)
                    .show();
        }

    }


    // **Outdated for now
    // for taking a picture
    // https://github.com/DroidNinja/Android-FilePicker
    // https://developer.android.com/training/camera/photobasics.html
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }

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
        final Spinner socialSituationSpinner = (Spinner) findViewById(R.id.socialSituationSpinner);

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

}
