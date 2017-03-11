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

public class MoodHistoryActivity extends AppCompatActivity implements MSView<MoodSwing> {

    private ListView moodHistoryListView;
    private ArrayAdapter<MoodEvent> moodHistoryAdapter;

    private Participant mainParticipant;
    private ArrayList<MoodEvent> moodHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // Initialize ListView
        moodHistoryListView  = (ListView) findViewById(R.id.moodHistory);

        moodHistoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the Mood Event from the mood history list.
                MoodEvent moodEvent = moodHistory.get(position);

                Intent intent = new Intent(MoodHistoryActivity.this, ViewMoodEventActivity.class);

                // Serialize the mood event, convert to json, and put extra on the intent.
                intent.putExtra("moodEvent", (new Gson()).toJson(moodEvent));

                // Launch the ViewMoodEventActivity.
                startActivity(intent);
                return true;
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

    public void update(MoodSwing moodSwing) {
        loadMainParticipant();
    }
}
