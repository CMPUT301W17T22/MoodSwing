package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
