package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class ViewMoodEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mood_event);

        TextView usernameTextView =
                (TextView) findViewById(R.id.usernameTextView_ViewMoodEventActivity);
        TextView emotionalStateTextView =
                (TextView) findViewById(R.id.emotionalStateTextView_ViewMoodEventActivity);
        TextView socialSituationTextView =
                (TextView) findViewById(R.id.socialSituationTextView_ViewMoodEventActivity);
        TextView triggerTextView =
                (TextView) findViewById(R.id.triggerTextView_ViewMoodEventActivity);

        ImageView emotionalStateImageView =
                (ImageView) findViewById(R.id.emotionalStateImageView_ViewMoodEventActivity);
        ImageView socialSituationImageView =
                (ImageView) findViewById(R.id.socialSituationImageView_ViewMoodEventActivity);
        ImageView imageImageView =
                (ImageView) findViewById(R.id.imageImageView_ViewMoodEventActivity);


        // Get the MoodEvent that is passed through from the ViewMoodHistoryActivity.
        String moodEventJson = getIntent().getStringExtra("moodEvent");
        Gson gson = new Gson();
        MoodEvent moodEvent = gson.fromJson(moodEventJson, MoodEvent.class);

        // Load values from MoodEvent into the text fields.
        usernameTextView.setText(moodEvent.getOriginalPoster());
        emotionalStateTextView.setText(moodEvent.getEmotionalState().getDescription());
        socialSituationTextView.setText(moodEvent.getSocialSituation().getDescription());
        triggerTextView.setText(moodEvent.getTrigger());

        // Load valeus from MoodEvent into the images.
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




    }
}
