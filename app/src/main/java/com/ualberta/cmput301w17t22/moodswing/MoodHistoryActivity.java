package com.ualberta.cmput301w17t22.moodswing;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    /** Textview that displays the "No Mood Events' text when appropriate. */
    TextView emptyMoodHistory;

    /** The main toolbar of the app that lets users navigate to the other parts of the app. */
    Toolbar mainToolbar2;

    /** The map fragment for he mood events to be displayed on. */
    GoogleMap historyMap;

    SupportMapFragment mapFragment;

    /** The last known location of the user. */
    LatLng lastKnownLocation;

    /**
     * Spinner that shows the user options for filtering the mood feed and map.
     */
    Spinner filterSpinner;

    // necessary?
    ElasticSearchController elasticSearchController;

    ArrayList<String> filterStrings = new ArrayList<>();

    // The trigger word that the user will search for in filter
    private String filterTrigger = "";
    // The chosen mood that the user will filter
    private String filterEmotion = "";

    /**
     * shows which filters are active and which aren't
     * if activeFilters = [a, b, c, d]
     * a = recent week, b = emotion filter, c = trigger filter, d = 5km filter
     * activeFilters[x] = 1: this filter is active, 0: this filter is not applied
     */
    private int[] activeFilters = new int[4];   // should be initialized to 0

    // For whether or not to display filter messages
    boolean displayFilterToast = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        // Initialize all widgets for this activity.
        initialize();

        setSupportActionBar(mainToolbar2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
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
        displayFilterToast = false;

        // Load information on the main participant from MoodSwing.
        loadMoodSwing();

        // handles when a different thing is selected in filter spinner
        // place in onStart deliberately to minimize calls to setOnItemSelectedListener
        // This will always call when the spinner is initialized.
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int selected = filterSpinner.getSelectedItemPosition();
                handleFilterSelection(selected);
                displayFilterToast = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MoodHistoryActivity.this,
                        "Nothing selected.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        historyMap = googleMap;
        historyMap.setMinZoomPreference(10.0f);

        // Grab the mood swing controller for the last known location.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        lastKnownLocation = new LatLng(moodSwingController.getLastKnownLat(),
                moodSwingController.getLastKnownLng());

        historyMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));

        // Populate the map with custom markers for the mood events.
        loadMapMarkers();

        historyMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                // Get MoodSwingController.
                MoodSwingController moodSwingController =
                        MoodSwingApplication.getMoodSwingController();

                // Set the mood history position to be the position of the event that the marker
                // represents.
                moodSwingController.setMoodHistoryPosition((int) marker.getTag());

                // Launch the view mood event activity.
                Intent intent = new Intent(MoodHistoryActivity.this, ViewMoodEventActivity.class);
                intent.putExtra("MoodListType", "MoodHistory");
                startActivity(intent);
            }
        });
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
                // User chose the "Block User" item, should navigate to the MoodStatisticsActivity.
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

            // If the mood event has a location, make a map marker for it.
            if (!Double.isNaN(moodEvent.getLat()) && !Double.isNaN(moodEvent.getLng())) {

                EmotionalState emotionalState = moodEvent.getEmotionalState();

                // Method to resize bitmap taken from
                // http://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2
                // on 04/02/2017.
                Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                        emotionalState.getDrawableId());
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 120, 120, false);

                // Create the icon from the resized emoticon
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resizedBitmap);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(moodEvent.getLat(), moodEvent.getLng()))
                        .title(emotionalState.getDescription())
                        .snippet(moodEvent.getOriginalPoster())
                        .icon(icon);

                Marker marker = historyMap.addMarker(markerOptions);

                // Set the tag of the marker to be the mood event's position in the mood history.
                marker.setTag(moodHistory.indexOf(moodEvent));
            }

        } // end for loop
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

        // filter by week
        if (activeFilters[0] == 1){
            moodHistory = filterRecentWeek(moodHistory);
        }

        // filter by emotion
        if (activeFilters[1] == 1){
            moodHistory = filterByEmotion(moodHistory);
        }

        // filter by trigger
        if (activeFilters[2] == 1){
            moodHistory = filterByTrigger(moodHistory);
        }

        // filter by within 5km
        if (activeFilters[3] == 1){
            moodHistory = filterByDistance(moodHistory);
        }

        // Initialize array adapter.
        moodHistoryAdapter = new MoodEventAdapter(this, moodHistory);
        moodHistoryListView.setEmptyView(emptyMoodHistory);
        moodHistoryListView.setAdapter(moodHistoryAdapter);
    }

    /**
     * updateFilterUI: Changes the UI to reflect changes in activeFilters.
     * Called by updateFilterMenu
     */
    public void updateFilterUI() {

        if (activeFilters[0] == 1) {     // recent week filter active
            filterStrings.set(1, "By Recent Week: On");
        } else {                        // recent week filter off
            filterStrings.set(1, "By Recent Week");
        }

        if (activeFilters[1] == 1) { // emotion filter on
            filterStrings.set(2, "By Emotion: " + filterEmotion);
        } else {                    // emotion filter off
            filterStrings.set(2, "By Emotion");
        }

        if (activeFilters[2] == 1) {    // trigger filter on
            filterStrings.set(3, "By Trigger: " + filterTrigger);
        } else {                        // trigger filter off
            filterStrings.set(3, "By Trigger");
        }

        if (activeFilters[3] == 1) { // 5km filter on
            filterStrings.set(4, "Within 5km: On");
        } else {
            filterStrings.set(4, "Within 5km");
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
        if(i < 0 | i > 3) { // by default, reset all filters, includes i = -1 case
            filterTrigger = "";
            filterEmotion = "";
            activeFilters[0] = 0;
            activeFilters[1] = 0;
            activeFilters[2] = 0;
            activeFilters[3] = 0;
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
     * Given a list of MoodEvents, remove the MoodEvents that are not within 5km to the
     * current location of the android device. Done with a for loop because all the MoodHistory
     * items are already loaded.
     * @param moodList The list of mood events to filter.
     * @return The list of mood events within 5km of the current location.
     */
    public ArrayList<MoodEvent> filterByDistance(ArrayList<MoodEvent> moodList) {
        ArrayList<MoodEvent> returnMoodList = new ArrayList<>();

        Location lastKnownLocationLocation = new Location("");
        lastKnownLocationLocation.setLatitude(lastKnownLocation.latitude);
        lastKnownLocationLocation.setLongitude(lastKnownLocation.longitude);

        for (MoodEvent moodEvent : moodList) {

            Location moodEventLocation = new Location("");
            moodEventLocation.setLatitude(moodEvent.getLat());
            moodEventLocation.setLongitude(moodEvent.getLng());

            // Check the distance between the last known location and the mood event location
            // using the Location object's distanceTo function.
            if (lastKnownLocationLocation.distanceTo(moodEventLocation) < 5000.0) {
                returnMoodList.add(moodEvent);
            }
        }

        return returnMoodList;
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
     * Case 4: Distance filter. Add within 5km filter to existing and refresh mood history.
     * @param selected - index of item chosen in spinner
     */
    public void handleFilterSelection(int selected){
        switch (selected) {
            case 0:       // no filter selected
                if(displayFilterToast){
                    Toast.makeText(MoodHistoryActivity.this,
                            "No filters.", Toast.LENGTH_SHORT).show();
                }
                updateFilterMenu(-1);   // update filter list
                update(MoodSwingApplication.getMoodSwing()); // refresh
                break;


            case 1:     // sort by recent week
                Toast.makeText(MoodHistoryActivity.this,
                        "Filtering by Recent Week", Toast.LENGTH_SHORT).show();
                updateFilterMenu(0); // update filter list
                update(MoodSwingApplication.getMoodSwing()); // refresh
                break;

            case 2:      // sort by emotion
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android
                // on 3/27
                final Dialog dialog = new Dialog(MoodHistoryActivity.this);
                dialog.setContentView(R.layout.emotional_state_dialogue_box);
                dialog.setTitle("Select an emotion to filter.");
                dialog.setCancelable(true);

                // set up radio buttons and filter/cancel buttons
                final RadioGroup emotionsRadioGroup =
                        (RadioGroup) dialog.findViewById(R.id.emotionsRadioGroup);
                RadioButton happinessRB =
                        (RadioButton) dialog.findViewById(R.id.happinessRadioButton);
                RadioButton angerRB =
                        (RadioButton) dialog.findViewById(R.id.angerRadioButton);
                RadioButton disgustRB =
                        (RadioButton) dialog.findViewById(R.id.disgustRadioButton);
                RadioButton confusionRB =
                        (RadioButton) dialog.findViewById(R.id.confusionRadioButton);
                RadioButton fearRB =
                        (RadioButton) dialog.findViewById(R.id.fearRadioButton);
                RadioButton sadnessRB =
                        (RadioButton) dialog.findViewById(R.id.sadnessRadioButton);
                RadioButton shameRB =
                        (RadioButton) dialog.findViewById(R.id.shameRadioButton);
                RadioButton surpriseRB =
                        (RadioButton) dialog.findViewById(R.id.surpriseRadioButton);
                Button validateEmotionFilter =
                        (Button) dialog.findViewById(R.id.validateEmotionalFilterButton);
                Button cancelEmotionFilter =
                        (Button) dialog.findViewById(R.id.cancelEmotionalFilterButton);

                // get value chosen if filter button is clicked
                validateEmotionFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int chosenEmotionIndex = emotionsRadioGroup.getCheckedRadioButtonId();

                        if(chosenEmotionIndex == -1){
                            Toast.makeText(MoodHistoryActivity.this,
                                    "No emotion selected.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        } else {
                            RadioButton chosenEmotionRadioButton =
                                    (RadioButton) dialog.findViewById(chosenEmotionIndex);
                            filterEmotion = chosenEmotionRadioButton.getText().toString();
                            Toast.makeText(MoodHistoryActivity.this,
                                    "Adding filter by " + filterEmotion, Toast.LENGTH_SHORT).show();
                            updateFilterMenu(1);
                            dialog.dismiss();
                            update(MoodSwingApplication.getMoodSwing()); // refresh the feed
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


            case 3:                // sort by trigger
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android
                // on 3/27
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MoodHistoryActivity.this, R.style.DialogTheme);

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
                            Toast.makeText(MoodHistoryActivity.this,
                                    "Trigger search must be one word long.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            // filterTrigger is acceptable
                            Toast.makeText(MoodHistoryActivity.this,
                                    "Adding filter by trigger word: "+ filterTrigger,
                                    Toast.LENGTH_SHORT).show();
                            updateFilterMenu(2);
                            update(MoodSwingApplication.getMoodSwing()); // refresh
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

            case 4: // sort for within 5km
                Toast.makeText(MoodHistoryActivity.this,
                        "Filtering Within 5km", Toast.LENGTH_SHORT).show();
                updateFilterMenu(3); // update filter list
                update(MoodSwingApplication.getMoodSwing()); // refresh
                break;
        }
    }

    /**
     * Gets the word count of a string.
     * from http://stackoverflow.com/questions/8924599/how-to-count-the-exact-number-of-words-in-a-string-that-has-empty-spaces-between
     * on 3/28
     * @param s
     * @return
     */
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
     * Initialize all widgets for this Activity, and add this view to the main model class.
     */
    public void initialize() {
        mainToolbar2 = (Toolbar) findViewById(R.id.mainToolBar2);
        mainToolbar2.setTitle("");
        moodHistoryListView  = (ListView) findViewById(R.id.moodHistory);
        emptyMoodHistory = (TextView) findViewById(R.id.emptyMoodHistory);
        filterSpinner = (Spinner) findViewById(R.id.filterSpinnerMoodHistory);

        // these populate the filter spinner
        filterStrings.add("No Filter");
        filterStrings.add("By Recent Week");
        filterStrings.add("By Emotion");
        filterStrings.add("By Trigger");
        filterStrings.add("Within 5km");

        // for our custom spinner options and settings
        ArrayAdapter<String> filterAdapter =
                new ArrayAdapter<>(this, R.layout.filter_spinner, filterStrings);
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
        historyMap.clear();
        loadMapMarkers();
    }

}
