package com.example.moodswing.moodswing_000;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Hexadecimal color codes:
 * dark magenta: #66023C
 * background: e0b0ff
 */
public class MainActivity extends AppCompatActivity implements MSView<MoodSwing>  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.MainToolBar);

        setSupportActionBar(myToolbar);
       // getSupportActionBar().setTitle("MoodSwing");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //inflates menu if it is present, I think this might be the connecting method from the
        // toolbar.xml to the activity_main.xml
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    //This is the method to handle clicks on menu items from the overflow menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeToolBarButton:
                // User chose the "Home" item, should navigate to main activity
                return true;

            case R.id.FollowToolBarButton:
                // User chose the "Follower & Following" action, should navigate to the follower/following activity

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This is called when the Model tells this View to update because of some change in the Model.
     * @param moodSwing
     */
    public void update(MoodSwing moodSwing) {

    }
}
