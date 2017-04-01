package com.ualberta.cmput301w17t22.moodswing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Very basics of MoodStatistics page. Contains menu for navigation and nothing else.
 * Blank canvas for us to add our main wow factor stuff.
 * Will contain statsitics about various mood events.
 */
public class MoodStatistics extends AppCompatActivity {

    ElasticSearchController elasticSearchController;

    /** The main toolbar of the app that lets users navigate to the other parts of the app. */
    Toolbar mainToolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_statistics);


        // Initialize all the widgets of the app.
        initialize();

        setSupportActionBar(mainToolbar);


        // Get MoodSwingController.
        MoodSwingController moodSwingController =
                MoodSwingApplication.getMoodSwingController();
        elasticSearchController =
                MoodSwingApplication.getElasticSearchController();
    }




    /**
     * Inflates the menu. Connects the menu_main_activity.xml to the
     * menu_main_activity in activity_main.xml.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_mood_statistics, menu);
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
                intent = new Intent(MoodStatistics.this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.followToolBarButton:
                // User chose the "Follower & Following" action, should navigate to the
                // follower/following activity
                intent = new Intent(MoodStatistics.this, MainFollowActivity.class);
                startActivity(intent);
                return true;

            case R.id.newMoodEventToolBarButton:
                // User chose the "New Mood Event" item, should navigate the NewMoodEventActivity.
                intent = new Intent(MoodStatistics.this, NewMoodEventActivity.class);
                startActivity(intent);
                return true;

            case R.id.moodHistoryToolBarButton:
                // User chose the "View Mood History" item, should navigate to the
                // MoodHistoryActivity.
                intent = new Intent(MoodStatistics.this, MoodHistoryActivity.class);
                startActivity(intent);
                return true;

            case R.id.blockUserToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MoodStatistics.this, BlockUserActivity.class);
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
        // Initialize all basic widgets.
        mainToolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mainToolbar.setTitle("");
    }



}
