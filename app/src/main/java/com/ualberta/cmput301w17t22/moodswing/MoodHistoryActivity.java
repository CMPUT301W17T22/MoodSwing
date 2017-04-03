package com.ualberta.cmput301w17t22.moodswing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

/**
 * The MoodHistoryActivity displays the main participant's mood history in a readable format,
 * and allows the user to then select a mood event from their history to view.
 */
public class MoodHistoryActivity extends AppCompatActivity implements MSView<MoodSwing>,
        OnMapReadyCallback{

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

    GoogleMap historyMap;

    /**
     * Spinner that shows the user options for filtering the mood feed and map.
     */
    Spinner filterSpinner;

    // necessary?
    ElasticSearchController elasticSearchController;

    ArrayList<String> filter_strings = new ArrayList<>();

    // The trigger word that the user will search for in filter
    private String filterTrigger = "";
    // The chosen mood that the user will filter
    private String filterEmotion = "";

    /**
     * shows which filters are active and which aren't
     * if activeFilters = [a, b, c], a = recent week, b = emotion filter, c = trigger filter
     * activeFilters[x] = 1: this filter is active, 0: this filter is not applied
     */
    private int[] activeFilters = new int[3];   // should be initialized to 0


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // Initialize all widgets for this activity.
        initialize();

        setSupportActionBar(mainToolbar2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.history_map);

        mapFragment.getMapAsync(this);

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

        // handles when a different thing is selected in filter spinner
        // place in onStart deliberately to minimize calls to setOnItemSelectedListener
        // This will always call when the spinner is initialized.
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this, filter_strings.get(position)+"selected", Toast.LENGTH_SHORT);
                int selected = filterSpinner.getSelectedItemPosition();
                handleFilterSelection(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MoodHistoryActivity.this, "nothing selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        historyMap = googleMap;
        historyMap.setMinZoomPreference(10.0f);

        // Grab the mood swing controller for the last known location.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        LatLng lastKnownLocation =
                new LatLng(moodSwingController.getLastKnownLat(),
                        moodSwingController.getLastKnownLng());

        historyMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));
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
                finish();
                //intent = new Intent(MoodHistoryActivity.this, MainActivity.class);
                //startActivity(intent);
                return true;

            case R.id.followToolBarButton:
                // User chose the "Follower & Following" action, should navigate to the
                // follower/following activity
                intent = new Intent(MoodHistoryActivity.this, MainFollowActivity.class);
                finish();
                startActivity(intent);
                return true;

            case R.id.newMoodEventToolBarButton:
                // User chose the "New Mood Event" item, should navigate the NewMoodEventActivity.
                intent = new Intent(MoodHistoryActivity.this, NewMoodEventActivity.class);
                finish();
                startActivity(intent);
                return true;

            case R.id.blockUserToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MoodHistoryActivity.this, BlockUserActivity.class);
                finish();
                startActivity(intent);
                return true;

            case R.id.moodStatisticsToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MoodHistoryActivity.this, MoodStatistics.class);
                finish();
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Loads the map markers from the mood events in the mood history onto the map with their
     * special map markers.
     */
    public void loadMapMarkers() {
        for (MoodEvent moodEvent : moodHistory) {
            historyMap.addMarker(moodEvent.getMapMarker());
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

        //Log.d("msec", moodHistory.toString());
        //TODO: Put filter items here
        // filter by week here
        if (activeFilters[0] == 1){
            moodHistory = filterRecentWeek(moodHistory);
        }

        // filter by emotion
        if (activeFilters[1] == 1){
            moodHistory = filterByEmotion(moodHistory);
        }

        //Log.d("reout", String.valueOf(activeFilters[2]));
        // filter by trigger
        if (activeFilters[2] == 1){
            moodHistory = filterByTrigger(moodHistory);
        }

        // put moodHistory list into reverse chronological order
        Collections.reverse(moodHistory);

        // Initialize array adapter.
        moodHistoryAdapter = new MoodEventAdapter(this, moodHistory);
        moodHistoryListView.setEmptyView(emptyMoodHistory);
        moodHistoryListView.setAdapter(moodHistoryAdapter);

        // Populate map markers.
        loadMapMarkers();
    }

    /**
     * Initialize all widgets for this Activity, and add this view to the main model class.
     */
    public void initialize() {
        mainToolbar2 = (Toolbar) findViewById(R.id.mainToolBar2);
        mainToolbar2.setTitle("");
        moodHistoryListView  = (ListView) findViewById(R.id.moodHistory);
        emptyMoodHistory = (TextView) findViewById(R.id.emptyMoodHistory);
        filterSpinner = (Spinner) findViewById(R.id.filterSpinnerMoodHistory);

        // these populate the filter spinner
        filter_strings.add("No Filter");
        filter_strings.add("By Recent Week");
        filter_strings.add("By Emotion");
        filter_strings.add("By Trigger");
        //filter_strings.add("Test hidden");

        // for our custom spinner options and settings
        ArrayAdapter<String> filterAdapter =
                new ArrayAdapter<>(this, R.layout.filter_spinner, filter_strings);
        filterSpinner.setAdapter(filterAdapter);


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


    // from http://stackoverflow.com/questions/8924599/how-to-count-the-exact-number-of-words-in-a-string-that-has-empty-spaces-between
    // on 3/28
    // returns word count of a String s
    public static int wordCount(String s){
        if (s == null) {
            return 0;
        }
        if (s.equals("")){
            return 0;
        }
        return s.trim().split("\\s+").length;
    }


    /**
     * updateFilterUI: Changes the UI to reflect changes in activeFilters.
     * Called by updateFilterMenu
     */
    public void updateFilterUI() {

        if (activeFilters[0] == 1) {     // recent week filter active
            filter_strings.set(1, "By Recent Week: On");
        } else {                        // recent week filter off
            filter_strings.set(1, "By Recent Week");
        }

        if (activeFilters[1] == 1) { // emotion filter on
            filter_strings.set(2, "By Emotion: " + filterEmotion);
        } else {                    // emotion filter off
            filter_strings.set(2, "By Emotion");
        }

        if (activeFilters[2] == 1) {    // trigger filter on
            filter_strings.set(3, "By Trigger: " + filterTrigger);
        } else {                        // trigger filter off
            filter_strings.set(3, "By Trigger");
        }
    }

    /**
     * Changes and updates the filters for a given item.
     * Given an index of activeFilters, flip the value of that item.
     * If it's activated, deactivite it, and vice versa.
     * Then call updateFilter
     * i = 0 is recent week, i = 1 is emotion filter, i = 2 is trigger filter, -1 is no filters
     */
    public void updateFilterMenu(int i) {
        if(i < 0 | i > 2) { // by default, reset all filters, includes i = -1 case
            filterTrigger = "";
            filterEmotion = "";
            activeFilters[0] = 0;
            activeFilters[1] = 0;
            activeFilters[2] = 0;
        } else {            // flip the appropriate filter
            activeFilters[i] = activeFilters[i] ^= 1;
        }
        updateFilterUI();
    }

    /**
     * Given a list of MoodEvents, remove the MoodEvents that are more than a
     * week old. Logically all you need to do for filtering, should be more
     * efficient than doing this filter in elasticsearch.
     * @param moodList - a list of MoodEvents to filter
     * @return a moodlist without any MoodEvents older than a week.
     */
    public ArrayList<MoodEvent> filterRecentWeek(ArrayList<MoodEvent> moodList) {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DATE, -7);
        // date one week ago. Compare this to MoodEvents
        Date weekAgo = cal.getTime();

        //Toast.makeText(MoodHistoryActivity.this, weekAgo.toString(), Toast.LENGTH_SHORT).show();
        ArrayList<MoodEvent> returnMoodList = new ArrayList<>();
        for(int i = 0; i < moodList.size(); i++) {
            Date moodDate = moodList.get(i).getDate();
            if (moodDate.after(weekAgo)) {  // if date is within past week, keep it
                returnMoodList.add(moodList.get(i));
            }
        }
        return returnMoodList;
    }


    /**
     * Given a list of MoodEvents, remove the MoodEvents that are not equal
     * equal to the filtered emotions. Simply done with a for loop because all
     * MoodHistory items are preloaded.
     * @param moodList - a list of MoodEvents to filter
     * @return a moodlist with only MoodEvents equal to selected emotion (filterEmotion)
     */
    public ArrayList<MoodEvent> filterByEmotion(ArrayList<MoodEvent> moodList) {
        ArrayList<MoodEvent> returnMoodList = new ArrayList<>();
        for (int i = 0; i < moodList.size(); i++){
            if(moodList.get(i).getEmotionalState().getDescription().equals(filterEmotion)){
                returnMoodList.add(moodList.get(i));
            }
        }

        return returnMoodList;
    }


    /**
     * Given a list of MoodEvents, remove the MoodEvents that are not equal
     * equal to the filtered trigger word. Simply done with a for loop because all
     * MoodHistory items are preloaded.
     * @param moodList - a list of MoodEvents to filter
     * @return a moodlist with only MoodEvents equal to user entered word (filterTrigger)
     */
    public ArrayList<MoodEvent> filterByTrigger(ArrayList<MoodEvent> moodList) {
        ArrayList<MoodEvent> returnMoodList = new ArrayList<>();
        for (int i = 0; i < moodList.size(); i++) {
            Log.d("reout", "next word");
            if(searchForKeyword(moodList.get(i).getTrigger(), filterTrigger)) {
                returnMoodList.add(moodList.get(i));
            }
        }
        //return returnMoodList;
        return returnMoodList;    // for now until filterByEmotion is done
    }


    /**
     * searchForKeyword is a helper function for filterByTrigger.
     * It takes in a MoodEvent's trigger, and a keyword, and searches
     * the trigger for the keyword. Not case sensitive.
     * Ex: "Hat" matches with "hat".
     * Regular expressions in split used on 4/01/2017 from:
     * http://stackoverflow.com/questions/20933211/divide-sentence-into-words-and-punctuations
     * @param trigger A Mood Event's trigger, ie. a few words in string form
     * @param keyword The word to search for. Will never be longer than 1 word.
     * @return true if keyword is in trigger, false otherwise.
     */
    public boolean searchForKeyword(String trigger, String keyword) {
        // search is not case sensitive
        keyword = keyword.toLowerCase();
        trigger = trigger.toLowerCase();
        String[] triggerParts = trigger.split("(?<!^)\\b");

        for(int i = 0; i < triggerParts.length; i ++) {
            if(triggerParts[i].equals(keyword)){
                return true;
            }
        }
        return false;
    }




    /**
     * handleFilterSelection is called when a new filter spinner item is selected.
     * It acts depending on what item was chosen and applies the proper filters if necessary.
     * Case 0: No filters. Clear all filters if they exist then refresh Mood History
     * Case 1: Recent week filter. Add on recent week filter to existing and refresh Mood History.
     * Case 2: Emotion filter. Create dialog box and update depending on emotion chosen.
     * Case 3: Trigger filter. Create dialog box, check input, and update depending on emotion chosen.
     * @param selected - index of item chosen in spinner
     */
    public void handleFilterSelection(int selected){
        switch (selected) {
            case 0:       // no filter selected
                Toast.makeText(MoodHistoryActivity.this, "No filters.", Toast.LENGTH_SHORT).show();
                updateFilterMenu(-1);   // update filter list
                loadMoodSwing(); // refresh the feed
                break;


            case 1:     // sort by recent week
                Toast.makeText(MoodHistoryActivity.this, "Adding Recent Week Filter.", Toast.LENGTH_SHORT).show();
                updateFilterMenu(0); // update filter list
                loadMoodSwing(); // refresh the feed
                break;

            case 2:      // sort by emotion
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android on 3/27
                final Dialog dialog = new Dialog(MoodHistoryActivity.this);
                dialog.setContentView(R.layout.emotional_state_dialogue_box);
                dialog.setTitle("Select an emotion to filter.");
                dialog.setCancelable(true);

                // set up radio buttons and filter/cancel buttons
                final RadioGroup emotionsRadioGroup = (RadioGroup) dialog.findViewById(R.id.emotionsRadioGroup);
                RadioButton happinessRB = (RadioButton) dialog.findViewById(R.id.happinessRadioButton);
                RadioButton angerRB = (RadioButton) dialog.findViewById(R.id.angerRadioButton);
                RadioButton disgustRB = (RadioButton) dialog.findViewById(R.id.disgustRadioButton);
                RadioButton confusionRB = (RadioButton) dialog.findViewById(R.id.confusionRadioButton);
                RadioButton fearRB = (RadioButton) dialog.findViewById(R.id.fearRadioButton);
                RadioButton sadnessRB = (RadioButton) dialog.findViewById(R.id.sadnessRadioButton);
                RadioButton shameRB = (RadioButton) dialog.findViewById(R.id.shameRadioButton);
                RadioButton surpriseRB = (RadioButton) dialog.findViewById(R.id.surpriseRadioButton);
                Button validateEmotionFilter = (Button) dialog.findViewById(R.id.validateEmotionalFilterButton);
                Button cancelEmotionFilter = (Button) dialog.findViewById(R.id.cancelEmotionalFilterButton);

                // get value chosen if filter button is clicked
                validateEmotionFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int chosenEmotionIndex = emotionsRadioGroup.getCheckedRadioButtonId();
                        if(chosenEmotionIndex == -1){
                            Toast.makeText(MoodHistoryActivity.this, "No emotion selected.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            RadioButton chosenEmotionRadioButton = (RadioButton) dialog.findViewById(chosenEmotionIndex);
                            filterEmotion = chosenEmotionRadioButton.getText().toString();
                            Toast.makeText(MoodHistoryActivity.this, "Adding filter by " + filterEmotion, Toast.LENGTH_SHORT).show();
                            updateFilterMenu(1);
                            dialog.dismiss();
                            loadMoodSwing(); // refresh the feed
                        }

                    }
                });
                // dismiss dialog if cancel is clicked
                cancelEmotionFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                // now that the dialog is set up, it's time to show it
                dialog.show();
                break;


            default:                // sort by trigger
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android on 3/27
                AlertDialog.Builder builder = new AlertDialog.Builder(MoodHistoryActivity.this, R.style.DialogTheme);

                // Method for displaying an edittext nicely in an alertdialog adapted from
                // http://stackoverflow.com/questions/27774414/add-bigger-margin-to-edittext-in-android-alertdialog
                // on 3/25/2017.

                // Create edittext to take user input.
                final EditText triggerEditText = new EditText(MoodHistoryActivity.this);
                // Set custom edittext shape.
                triggerEditText.setBackgroundResource(R.drawable.dialog_edittext_shape);
                // Set some padding from the left.
                triggerEditText.setPadding(6, 0, 6, 0);

                // Create a container for the edittext.
                LinearLayout triggerEditTextContainer = new LinearLayout(MoodHistoryActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                // Set the margins for the edittext.
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

                // Set the parameters to the edittext and add it to the container.
                triggerEditText.setLayoutParams(params);
                triggerEditTextContainer.addView(triggerEditText);

                builder.setTitle("Enter Trigger Word:")
                        .setMessage("(max one word)")
                        .setCancelable(true)
                        .setView(triggerEditTextContainer);

                // Set up the buttons
                builder.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filterTrigger = triggerEditText.getText().toString();
                        // make sure filterTrigger is only one word long
                        if (wordCount(filterTrigger) != 1) {
                            Toast.makeText(MoodHistoryActivity.this, "Trigger search must be one word long.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            // filterTrigger is acceptable
                            Toast.makeText(MoodHistoryActivity.this, "Adding filter by trigger word: "+ filterTrigger, Toast.LENGTH_SHORT).show();
                            updateFilterMenu(2);
                            loadMoodSwing();// refresh the feed
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
        }

    }
}
