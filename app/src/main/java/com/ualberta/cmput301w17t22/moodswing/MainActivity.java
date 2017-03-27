package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * The MainActivity of the MoodSwing app. This screen will display the map view of mood events,
 * the mood feed, and have a floating toolbar to navigate the user to the other functionalities of
 * the app.
 * <p/>
 * Hexadecimal color codes:
 * dark magenta: #66023C
 * background: e0b0ff
 */
public class MainActivity extends AppCompatActivity implements MSView<MoodSwing>  {

    /** The main toolbar of the app that lets users navigate to the other parts of the app. */
    Toolbar mainToolbar;

    /**
     * The welcome text for the app. Currently displays the username and the Jest id. Used mainly
     * for testing right now.
     */
    TextView welcomeText;

    private Participant mainParticipant;
    /**
     *
     */
    private MoodEventAdapter moodFeedAdapter;

    // listview that holds the mood feed
    private ListView moodFeedListView;

    ArrayList<MoodEvent> moodFeedEvents = new ArrayList<MoodEvent>();

    ElasticSearchController elasticSearchController;
    /**
     * Called on opening of activity for the first time.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all the widgets of the app.
        initialize();

        setSupportActionBar(mainToolbar);

        // Get MoodSwingController.
        MoodSwingController moodSwingController =
                MoodSwingApplication.getMoodSwingController();
        elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Load MainParticipant.
        Participant mainParticipant = moodSwingController.getMainParticipant();

        // Set the welcome text appropriately.
        welcomeText.setText("Welcome user \"" + mainParticipant.getUsername() +
                "\" with ID \"" + mainParticipant.getId() + "\"");

        moodFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the MoodSwingController.
                MoodSwingController moodSwingController =
                        MoodSwingApplication.getMoodSwingController();

                // Report the position of the mood event being viewed to the main model.
                //Log.d("help", String.valueOf(position));
                moodSwingController.setMoodFeedPosition(position);

                // Launch the ViewMoodEventActivity.
                Intent intent = new Intent(MainActivity.this, ViewMoodEventActivity.class);
                // Tell the view mood event activity we are coming from the mood history.
                intent.putExtra("MoodListType", "MoodFeed");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        loadMoodSwing();

        moodFeedAdapter = new MoodEventAdapter(this, moodFeedEvents);

        moodFeedListView.setAdapter(moodFeedAdapter);
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
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
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

                return true;

            case R.id.followToolBarButton:
                // User chose the "Follower & Following" action, should navigate to the
                // follower/following activity
                intent = new Intent(MainActivity.this, MainFollowActivity.class);
                startActivity(intent);
                return true;

            case R.id.newMoodEventToolBarButton:
                // User chose the "New Mood Event" item, should navigate the NewMoodEventActivity.
                intent = new Intent(MainActivity.this, NewMoodEventActivity.class);
                startActivity(intent);
                return true;

            case R.id.moodHistoryToolBarButton:
                // User chose the "View Mood History" item, should navigate to the
                // MoodHistoryActivity.
                intent = new Intent(MainActivity.this, MoodHistoryActivity.class);
                startActivity(intent);
                return true;

            case R.id.blockUserToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MainActivity.this, BlockUserActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Initializes all the widgets for this activity.
     */
    public void initialize() {
        mainToolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mainToolbar.setTitle("");
        welcomeText = (TextView)findViewById(R.id.mainWelcomeText);
        moodFeedListView  = (ListView) findViewById(R.id.MoodFeedListView);


        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }
    public void loadMoodSwing() {
        // Get the main Model and get the main participant, and the main participant's mood history.
        moodFeedEvents.clear();
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();

        mainParticipant = moodSwingController.getMainParticipant();
        ArrayList<String> mainParticipantFollowingList = mainParticipant.getFollowing();

        int i;
        int size =0 ;
        if(mainParticipantFollowingList.isEmpty() == false){
            size = mainParticipantFollowingList.size();
        }
        // add most recent mood event of all those we're following
         for(i =0; i < size ; i++){
             Participant followingParticipant = elasticSearchController.getParticipantByUsername(mainParticipantFollowingList.get(i));
             if(followingParticipant.getMostRecentMoodEvent() != null){
                 // make sure the followed person as at least one mood event
                 moodFeedEvents.add(followingParticipant.getMostRecentMoodEvent());
             }
         }
         // add our own most recent mood event
        //Log.d("help", mainParticipant.getMostRecentMoodEvent().getEmotionalState().getDescription());
        if(mainParticipant.getMostRecentMoodEvent() != null){
            moodFeedEvents.add(mainParticipant.getMostRecentMoodEvent());
            //Log.d("help", mainParticipant.getMostRecentMoodEvent().getEmotionalState().getDescription());
        }

        moodSwingController.setMoodFeed(moodFeedEvents);
        // Initialize array adapter.
        moodFeedAdapter = new MoodEventAdapter(this, moodFeedEvents);
        moodFeedListView.setAdapter(moodFeedAdapter);
    }

    /**
     * This is called when the Model tells this View to update because of some change in the Model.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {
        loadMoodSwing();
    }
}
