package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
    private MoodEventAdapter moodHistoryAdapter;

    /** The main participant, the current logged in user of the app. */
    private Participant mainParticipant;

    /** The main participant's mood history, an ArrayList of their MoodEvents */
    private ArrayList<MoodEvent> moodHistory;

    TextView emptyMoodHistory;
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

    /** onStart, load the information about the main participant from the Model. */
    @Override
    protected void onStart() {
        super.onStart();

        // Load information on the main participant from MoodSwing.
        loadMoodSwing();




        moodHistoryAdapter = new MoodEventAdapter(this, moodHistory);

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
     * Inflates the menu. Connects the menu_main_activity.xml to the
     * menu_main_activity in activity_main.xml.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_mood_history, menu);
        return true;
    }

    /**
     * This method handles clicks on menu items from the overflow menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.homeToolBarButton:
                // User chose the "Home" item, should navigate to MainActivity.
                intent = new Intent(MoodHistoryActivity.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.followToolBarButton:
                // User chose the "Follower & Following" action, should navigate to the
                // follower/following activity
                intent = new Intent(MoodHistoryActivity.this, MainFollowActivity.class);
                startActivity(intent);
                return true;

            case R.id.newMoodEventToolBarButton:
                // User chose the "New Mood Event" item, should navigate the NewMoodEventActivity.
                intent = new Intent(MoodHistoryActivity.this, NewMoodEventActivity.class);
                startActivity(intent);
                return true;

            case R.id.blockUserToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MoodHistoryActivity.this, BlockUserActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
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

        // Initialize array adapter.
        moodHistoryAdapter = new MoodEventAdapter(this, moodHistory);
        moodHistoryListView.setEmptyView(emptyMoodHistory);
        moodHistoryListView.setAdapter(moodHistoryAdapter);
    }

    /**
     * Initialize all widgets for this Activity, and add this view to the main model class.
     */
    public void initialize() {
        mainToolbar2 = (Toolbar) findViewById(R.id.mainToolBar2);
        mainToolbar2.setTitle("");
        moodHistoryListView  = (ListView) findViewById(R.id.moodHistory);
        emptyMoodHistory = (TextView) findViewById(R.id.emptyMoodHistory);
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
