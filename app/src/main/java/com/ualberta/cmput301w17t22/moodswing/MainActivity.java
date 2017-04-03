package com.ualberta.cmput301w17t22.moodswing;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import static java.lang.Double.NaN;

/**
 * The MainActivity of the MoodSwing app. This screen will display the map view of mood events,
 * the mood feed, and have a floating toolbar to navigate the user to the other functionalities of
 * the app.
 *
 * <p/>
 * TODO: For filtering: using activeFilters, as well as filterTrigger and filterEmotion
 * TODO: in loadMoodSwing() might be a good idea
 *
 * TODO: prevent filters from going off automatically onStart
 *
 * TODO: there is some repeated location code in here. likely need to extract method.
 */
public class MainActivity extends AppCompatActivity implements MSView<MoodSwing>,
        LocationListener,
        OnMapReadyCallback {

    /** The main toolbar of the app that lets users navigate to the other parts of the app. */
    Toolbar mainToolbar;

    /** The main participant, the current participant using the app. */
    private Participant mainParticipant;

    /**
     * The adapter for the list of mood events constructed from the main participant's
     * following list.
     */
    private MoodEventAdapter moodFeedAdapter;

    /** The listview that holds the mood feed. */
    private ListView moodFeedListView;

    /** The arraylist that contains the actual mood events in the mood feed. */
    ArrayList<MoodEvent> moodFeed = new ArrayList<>();

    /** Displays appropriate text when the main participant's mood feed is empty. */
    TextView emptyFeed;

    /**Spinner that shows the user options for filtering the mood feed and map. */
    Spinner filterSpinner;

    /** The controller for the main model class. */
    MoodSwingController moodSwingController;

    /** The controller that handles all elastic search getting/setting. */
    ElasticSearchController elasticSearchController;

    /** List of strings that describes the available filters. */
    ArrayList<String> filterStrings = new ArrayList<>();

    /** The trigger word that the user will search for in filter. */
    private String filterTrigger = "";

    /** The chosen mood that the user will filter */
    private String filterEmotion = "";

    /**
     * shows which filters are active and which aren't
     * if activeFilters = [a, b, c, d]
     * a = recent week, b = emotion filter, c = trigger filter, d = distance filter
     * activeFilters[x] = 1: this filter is active, 0: this filter is not applied
     */
    private int[] activeFilters = new int[4];   // should be initialized to 0

    // For whether or not to display filter messages
    boolean displayFilterToast = false;


    private GoogleMap mainMap;
    LocationManager locationManager;
    LatLng lastKnownLocation;
    String provider;

    /**
     * Called on opening of activity for the first time.
     */

    //command function that executes
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all the widgets of the app.
        initialize();

        setSupportActionBar(mainToolbar);

        // Get MoodSwingController.
        moodSwingController =
                MoodSwingApplication.getMoodSwingController();
        elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Load MainParticipant.
        mainParticipant = moodSwingController.getMainParticipant();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Get lastKnownLocation service
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Criteria object will select best service based on accuracy, power consumption,
        // response, bearing and monetary cost
        Criteria criteria = new Criteria();

        // Set false to use best service otherwise it will select the default Sim network
        //and give the lastKnownLocation based on sim network
        //now it will first check satellite than Internet than Sim network lastKnownLocation
        provider = locationManager.getBestProvider(criteria, false);

        getCurrentLocation();

        // On click for the mood feed list view.
        moodFeedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Get the MoodSwingController.
                MoodSwingController moodSwingController =
                        MoodSwingApplication.getMoodSwingController();

                // Report the position of the mood event being viewed to the main model.
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
        displayFilterToast = false;

        // Load information from main model class.
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
                Toast.makeText(MainActivity.this, "Nothing selected.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mainMap = googleMap;
        mainMap.setMinZoomPreference(10.0f);

        mainMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));

        loadMapMarkers();

        // On click of info window on map, launch the view mood event activity.
        mainMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                // Get MoodSwingController.
                MoodSwingController moodSwingController =
                        MoodSwingApplication.getMoodSwingController();

                // Set the mood history position to be the position of the event that the marker
                // represents.
                moodSwingController.setMoodHistoryPosition((int) marker.getTag());

                // Launch the view mood event activity.
                Intent intent = new Intent(MainActivity.this, ViewMoodEventActivity.class);
                intent.putExtra("MoodListType", "MoodFeed");
                startActivity(intent);
            }
        });
    }

    /**
     * Updates the last known location of the main model class.
     * @param lastKnownLocationLocation The last known location Location object.
     */
    @Override
    public void onLocationChanged(Location lastKnownLocationLocation)
    {
        lastKnownLocation = new LatLng(lastKnownLocationLocation.getLatitude(),
                        lastKnownLocationLocation.getLongitude());
        moodSwingController.setLastKnownLocation(lastKnownLocation.latitude,
                lastKnownLocation.longitude);

        mainMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));
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

            case R.id.moodStatisticsToolBarButton:
                // User chose the "Block User" item, should navigate to the
                // BlockUserActivity.
                intent = new Intent(MainActivity.this, MoodStatistics.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadMapMarkers() {
        for (MoodEvent moodEvent : moodFeed) {

            // If the mood has a location, get it's special map marker.

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

                Marker marker = mainMap.addMarker(markerOptions);

                // Set the tag of the marker to be the mood event's position in the mood history.
                marker.setTag(moodFeed.indexOf(moodEvent));
            }

        } // end for loop
    }

    /**
     * Loads information from the main model class MoodSwing. This includes things like the main
     * participant and their mood history.
     * Use
     */
    public void loadMoodSwing() {

        // Get the MoodSwingController, and use that to get the main participant.
        moodSwingController = MoodSwingApplication.getMoodSwingController();
        mainParticipant = moodSwingController.getMainParticipant();

        // Load the mood feed.
        Log.i("MoodSwing", moodFeed.toString());
        moodFeed = moodSwingController.getMoodFeed();

        // filter by week
        if (activeFilters[0] == 1){
            moodFeed = filterRecentWeek(moodFeed);
        }

        // The other filters are handled by elastic search.

        // filter by within 5km
        if (activeFilters[3] == 1){
            moodFeed = filterByDistance(moodFeed);
        }

        // Initialize array adapter.
        moodFeedAdapter = new MoodEventAdapter(this, moodFeed);
        moodFeedListView.setEmptyView(findViewById(R.id.emptyMoodFeed));
        moodFeedListView.setAdapter(moodFeedAdapter);
    }

    /**
     * Checks permissions and gets the current location. The current location is stored
     * as latitude and longitude double values in the main model class.
     */
    public void getCurrentLocation() {
        // Get the current location.
        // http://stackoverflow.com/questions/32491960/android-check-permission-for-locationmanager
        // Check if we have proper permissions to get the coarse lastKnownLocation.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.i("MoodSwing", "Requesting coarse permission.");
            // Request the permission.
            // Dummy request code 8 used.
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_COARSE_LOCATION}, 8);
        }

        // Check if we have proper permissions to get the fine lastKnownLocation.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            Log.i("debugMaps","Requesting fine permission");
            // Request the permission.
            // Dummy request code 8 used.
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 8);
        }

        // Get the lastKnownLocation once every 5 seconds, or every 10 meters.
        locationManager.requestLocationUpdates(provider, 5000, 10, this);

        Location lastKnownLocationLocation = locationManager.getLastKnownLocation(provider);

        // Get latitude and longitude of the lastKnownLocation
        if (lastKnownLocationLocation != null) {
            lastKnownLocation = new LatLng(lastKnownLocationLocation.getLatitude(),
                    lastKnownLocationLocation.getLongitude());
            moodSwingController.setLastKnownLocation(lastKnownLocation.latitude,
                    lastKnownLocation.longitude);
        } else {
            Log.i("MoodSwing","No location provider.");
        }
    }

    /**
     * Load mood events of the participant's that the main participant is following
     * into the mood feed according to the given filter information.
     */
    public void buildMoodFeed() {

        if (!mainParticipant.getFollowing().isEmpty()) {

            // Get Mood Swing Controller.
            MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();

            // Build the mood feed according to the current filters.
            moodSwingController.buildMoodFeed(
                    activeFilters,
                    filterTrigger,
                    filterEmotion);
        }
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

        if (activeFilters[3] == 1) {
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

        //Toast.makeText(MainActivity.this, weekAgo.toString(), Toast.LENGTH_SHORT).show();
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
                if (displayFilterToast) {
                    Toast.makeText(MainActivity.this, "No filters.", Toast.LENGTH_SHORT).show();
                }
                updateFilterMenu(-1);   // update filter list
                buildMoodFeed();
                update(MoodSwingApplication.getMoodSwing()); // refresh
                break;


            case 1:     // sort by recent week
                Toast.makeText(MainActivity.this,
                        "Adding Recent Week Filter.", Toast.LENGTH_SHORT).show();
                updateFilterMenu(0); // update filter list
                buildMoodFeed();
                update(MoodSwingApplication.getMoodSwing()); // refresh
                break;

            case 2:      // sort by emotion
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android on 3/27
                final Dialog dialog = new Dialog(MainActivity.this);
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

                        if(chosenEmotionIndex == -1) {
                            Toast.makeText(MainActivity.this,
                                    "No emotion selected.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            RadioButton chosenEmotionRadioButton =
                                    (RadioButton) dialog.findViewById(chosenEmotionIndex);
                            filterEmotion = chosenEmotionRadioButton.getText().toString();
                            Toast.makeText(MainActivity.this,
                                    "Adding filter by " + filterEmotion, Toast.LENGTH_SHORT).show();
                            updateFilterMenu(1);
                            dialog.dismiss();
                            buildMoodFeed();
                            update(MoodSwingApplication.getMoodSwing()); // refresh
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
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android on 3/27
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);

                // Method for displaying an edittext nicely in an alertdialog adapted from
                // http://stackoverflow.com/questions/27774414/add-bigger-margin-to-edittext-in-android-alertdialog
                // on 3/25/2017.

                // Create edittext to take user input.
                final EditText triggerEditText = new EditText(MainActivity.this);
                // Set custom edittext shape.
                triggerEditText.setBackgroundResource(R.drawable.dialog_edittext_shape);
                // Set some padding from the left.
                triggerEditText.setPadding(6, 0, 6, 0);

                // Create a container for the edittext.
                LinearLayout triggerEditTextContainer = new LinearLayout(MainActivity.this);
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

                            Toast.makeText(MainActivity.this,
                                    "Trigger search must be one word long.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();

                        } else {
                            // filterTrigger is acceptable
                            Toast.makeText(MainActivity.this,
                                    "Adding filter by trigger word: "+ filterTrigger,
                                    Toast.LENGTH_SHORT).show();
                            updateFilterMenu(2);
                            buildMoodFeed();
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
                Toast.makeText(MainActivity.this,
                        "Filtering Within 5km", Toast.LENGTH_SHORT).show();
                updateFilterMenu(3); // update filter list
                buildMoodFeed();
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
     * These methods must be implemented for our class
     * to implement the onMapReadyCallBack class.
     */
    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
    }

    /**
     * Initializes all the widgets for this activity.
     */
    public void initialize() {
        // Initialize all basic widgets.
        mainToolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mainToolbar.setTitle("");
        moodFeedListView  = (ListView) findViewById(R.id.MoodFeedListView);
        emptyFeed = (TextView) findViewById(R.id.emptyMoodFeed);
        filterSpinner = (Spinner) findViewById(R.id.filterSpinnerMoodFeed);

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
     * This is called when the Model tells this View to update because of some change in the Model.
     */
    public void update(MoodSwing moodSwing) {
        loadMoodSwing();
        mainMap.clear();
        loadMapMarkers();
    }

}
