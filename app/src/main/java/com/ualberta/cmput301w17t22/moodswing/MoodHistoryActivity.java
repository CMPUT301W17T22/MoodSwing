package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * The MoodHistoryActivity displays the main participant's mood history in a readable format,
 * and allows the user to then select a mood event from their history to view.
 */
public class MoodHistoryActivity extends AppCompatActivity implements MSView<MoodSwing> {

    /** The ListView that will hold the main participant's mood history. */
    private ListView moodHistoryListView;

    /** The ArrayAdapter for the MoodHistoryListView. */
    private ArrayAdapter<MoodEvent> moodHistoryAdapter;

    /** The main participant, the current logged in user of the app. */
    private Participant mainParticipant;

    /** The main participant's mood history, an ArrayList of their MoodEvents */
    private ArrayList<MoodEvent> moodHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // Initialize all widgets for this activity.
        initialize();

        moodHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the Mood Event from the mood history list.
                MoodEvent moodEvent = moodHistory.get(position);

                Intent intent = new Intent(MoodHistoryActivity.this, ViewMoodEventActivity.class);

                // Serialize the mood event, convert to json, and put extra on the intent.
                intent.putExtra("moodEvent", (new Gson()).toJson(moodEvent));
                // Pass the position of the mood event through also.
                intent.putExtra("position", position);

                // Launch the ViewMoodEventActivity.
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();
        loadMainParticipant();

        // Initialize array adapter.
        moodHistoryAdapter = new ArrayAdapter<MoodEvent>(this, R.layout.mood_event, moodHistory);
        moodHistoryListView.setAdapter(moodHistoryAdapter);
    }

    public void loadMainParticipant() {
        // Get the main Model and get the main participant.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        mainParticipant = moodSwingController.getMainParticipant();
        moodHistory = mainParticipant.getMoodHistory();
    }

    /**
     * Initialize all widgets for this Activity.
     */
    public void initialize() {
        moodHistoryListView  = (ListView) findViewById(R.id.moodHistory);

        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }

    /**
     * Refreshes this view to have current information.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {
        loadMainParticipant();
    }
}
