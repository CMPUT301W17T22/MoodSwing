package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
        final Spinner socialsituationSpinner = (Spinner) findViewById(R.id.socialSituationSpinner);
        final Spinner emotionalStateSpinner = (Spinner) findViewById(R.id.emotionalStateSpinner);

        final Button newMoodEventPostButton = (Button) findViewById(R.id.newMoodEventPostButton);
        Button photoUploadButton = (Button) findViewById(R.id.photoUploadButton);

        // use current date/time for MoodEvent
        Date moodDate = new Date();

        // photo upload press. Need a real device to test this on
        // https://developer.android.com/training/camera/photobasics.html
        photoUploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // take photo, need to check on real device
                //dispatchTakePictureIntent();
            }
        });


        // Occurs when you press "Post" button. At the moment, just closes activity
        newMoodEventPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validtriggers(v);
                if (Objects.equals(String.valueOf(emotionalStateSpinner.getSelectedItem()),"")){
                    Toast.makeText(NewMoodEventActivity.this,
                            "Please enter an emotional state.",
                            Toast.LENGTH_SHORT).show();
                }
                else if (!validtriggers(v)){
                    Toast.makeText(NewMoodEventActivity.this,
                            "Trigger length too long.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(NewMoodEventActivity.this,
                            "Input Test: " +
                                    "\nEmotional State: "+ String.valueOf(emotionalStateSpinner.getSelectedItem()) +
                                    "\nSocial Situation: "+ String.valueOf(socialsituationSpinner.getSelectedItem()) +
                            String.valueOf(validtriggers(v)),
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

    // return true if addCurentLocationCheckBox is checked, false otherwise
    public boolean includeLocationCheck(View v){
        CheckBox addCurentLocationCheckBox = (CheckBox) findViewById(R.id.addCurentLocationCheckBox);
        //            Toast.makeText(NewMoodEventActivity.this,
//                    "Checked", Toast.LENGTH_LONG).show();
        return addCurentLocationCheckBox.isChecked();
    }

    // return true if entered triggers are 3 words or < 20 chars, false otherwise
    public boolean validtriggers(View v){
        EditText triggerEditText = (EditText)findViewById(R.id.triggerEditText);
        String triggers = triggerEditText.getText().toString();

        int trigger_charlen = triggers.length();
        String[] trigger_split = triggers.split(Pattern.quote(" "));
        int trigger_split_len = trigger_split.length;

        // test output
//        Toast.makeText(NewMoodEventActivity.this,
//                Integer.toString(trigger_split_len), Toast.LENGTH_SHORT).show();

        if (trigger_split_len <= 3 || trigger_charlen < 20){
            return true;
        } else {
            return false;
        }}





}
