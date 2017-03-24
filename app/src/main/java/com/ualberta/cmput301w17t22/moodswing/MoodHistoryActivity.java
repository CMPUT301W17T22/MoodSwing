package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


    /** The main toolbar of the app that lets users navigate to the other parts of the app. */
    Toolbar mainToolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // Initialize all widgets for this activity.
        initialize();

        setSupportActionBar(mainToolbar2);

        moodHistoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the MoodSwingController.
                MoodSwingController moodSwingController =
                        MoodSwingApplication.getMoodSwingController();

                // Report the position of the mood event being viewed to the main model.
                moodSwingController.setMoodHistoryPosition(position);

                // Launch the ViewMoodEventActivity.
                Intent intent = new Intent(MoodHistoryActivity.this, ViewMoodEventActivity.class);
                // Tell the view mood event activity we are coming from the mood history.
                intent.putExtra("MoodListType", "MoodHistory");
                startActivity(intent);
            }
        });
    }

    protected void onStart() {
        super.onStart();

        // Load information on the main participant from MoodSwing.
        loadMoodSwing();

        // Initialize array adapter.
        //TODO: Change layout of items so we can display icons to indicate Social Siituation...
        //TODO:... if image is present and if location is present. This will be copied for mood feed...
        //TODO:...but instead the list content will be other peoples mood events.
        //http://stackoverflow.com/questions/8554443/custom-list-item-to-listview-android
        //accessed March 17th 2017
        moodHistoryAdapter = new ArrayAdapter<MoodEvent>(this, R.layout.mood_event, moodHistory);

        moodHistoryListView.setAdapter(moodHistoryAdapter);

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
     * Load the main participant from the main model class MoodSwing, and set the current moodList
     * to be the main participant's moodhistory.
     */
    public void loadMoodSwing() {
        // Get the main Model and get the main participant, and the main participant's mood history.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();

        mainParticipant = moodSwingController.getMainParticipant();
        moodHistory = mainParticipant.getMoodHistory();
    }

    /**
     * Initialize all widgets for this Activity, and add this view to the main model class.
     */
    public void initialize() {
        mainToolbar2 = (Toolbar) findViewById(R.id.mainToolBar2);
        mainToolbar2.setTitle("Mood History");
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
        loadMoodSwing();
    }
}
