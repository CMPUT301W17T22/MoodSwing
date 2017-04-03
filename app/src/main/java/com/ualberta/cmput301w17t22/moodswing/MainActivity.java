package com.ualberta.cmput301w17t22.moodswing;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
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
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Double.NaN;

/**
 * The MainActivity of the MoodSwing app. This screen will display the map view of mood events,
 * the mood feed, and have a floating toolbar to navigate the user to the other functionalities of
 * the app.
 *
 * MORE JAVADOC TESTING 123123123
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

    /**
     * The welcome text for the app. Currently displays the username and the Jest id. Used mainly
     * for testing right now.
     */
    TextView welcomeText;

    /**
     * The main participant, the current participant using the app.
     */
    private Participant mainParticipant;

    /**
     * The adapter for the list of mood events constructed from the main participant's
     * following list.
     */
    private MoodEventAdapter moodFeedAdapter;

    /**
     * The listview that holds the mood feed.
     */
    private ListView moodFeedListView;

    /**
     * The arraylist that contains the actual mood events in the mood feed.
     */
    ArrayList<MoodEvent> moodFeedEvents = new ArrayList<>();

    /**
     * Displays appropriate text when the main participant's mood feed is empty.
     */
    TextView emptyFeed;

    /**
     * Spinner that shows the user options for filtering the mood feed and map.
     */
    Spinner filterSpinner;

    ElasticSearchController elasticSearchController;

    ArrayList<String> filter_strings = new ArrayList<>();

    // The trigger word that the user will search for in filter
    private String filterTrigger = "";
    // The chosen mood that the user will filter
    private String filterEmotion = "";

    // shows which filters are active and which aren't
    // if activeFilters = [a, b, c], a = recent week, b = emotion filter, c = trigger filter
    // activeFilters[x] = 1: this filter is active, 0: this filter is not applied
    private int[] activeFilters = new int[3];   // should be initialized to 0




    private GoogleMap mMap;
    LocationManager locationManager;
    //Location lastKnownLocation;
    double lastKnownLat = NaN;
    double lastKnownLng = NaN;
    String provider;

    /**
     * Called on opening of activity for the first time.
     * @param savedInstanceState
     */

    //command function that executes
    NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all the widgets of the app.
        initialize();

        //listen for network changes
        Log.d("NetworkStateReceiver", "Registering receiver");
        registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

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

        // On click for the mood feed list view.
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

        // Get Location start
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

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/

        // Get the lastKnownLocation once every 5 seconds, or every 10 meters.
        locationManager.requestLocationUpdates(provider, 5000, 10, this);

        Location lastKnownLocation = locationManager.getLastKnownLocation(provider);

        // Get latitude and longitude of the lastKnownLocation
        if (lastKnownLocation != null) {

            lastKnownLat = lastKnownLocation.getLatitude();
            lastKnownLng = lastKnownLocation.getLongitude();
            moodSwingController.setLastKnownLocation(lastKnownLat, lastKnownLng);
        } else {
            Log.i("MoodSwing","No location provider.");
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Load information from main model class.
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
                Toast.makeText(MainActivity.this, "nothing selected", Toast.LENGTH_SHORT).show();
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
        unregisterReceiver(networkStateReceiver);
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
        // Initialize all basic widgets.
        mainToolbar = (Toolbar) findViewById(R.id.mainToolBar);
        mainToolbar.setTitle("");
        welcomeText = (TextView)findViewById(R.id.mainWelcomeText);
        moodFeedListView  = (ListView) findViewById(R.id.MoodFeedListView);
        emptyFeed = (TextView) findViewById(R.id.emptyMoodFeed);
        filterSpinner = (Spinner) findViewById(R.id.filterSpinnerMoodFeed);

        // these populate the filter spinner
        filter_strings.add("No Filter");
        filter_strings.add("Recent Week");
        filter_strings.add("By Emotion");
        filter_strings.add("By Trigger");
        filter_strings.add("Test hidden");

        // for our custom spinner options and settings
        ArrayAdapter<String> filterAdapter =
                new ArrayAdapter<>(this, R.layout.filter_spinner, filter_strings);
        filterSpinner.setAdapter(filterAdapter);

        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);
    }

    /**
     * Loads information from the main model class MoodSwing. This includes things like the main
     * participant and their mood history.
     * Use
     */
    public void loadMoodSwing() {

        //Toast.makeText(MainActivity.this, filter_strings.get(0), Toast.LENGTH_SHORT).show();

        //Toast.makeText(MainActivity.this, "Filtering..", Toast.LENGTH_SHORT).show();

        // Get the MoodSwingController, and use that to get the main participant.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        mainParticipant = moodSwingController.getMainParticipant();

        // Load the mood feed.
        Log.i("MoodSwing",moodFeedEvents.toString());
        moodFeedEvents = moodSwingController.getMoodFeed();

        // Initialize array adapter.
        moodFeedAdapter = new MoodEventAdapter(this, moodFeedEvents);
        moodFeedListView.setEmptyView(findViewById(R.id.emptyMoodFeed));
        moodFeedListView.setAdapter(moodFeedAdapter);
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
     * This is called when the Model tells this View to update because of some change in the Model.
     */
    public void update(MoodSwing moodSwing) {
        loadMoodSwing();
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
        mMap = googleMap;
        mMap.setMinZoomPreference(10.0f); //minimum zoom set to
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLat, lastKnownLng)));
    }

    //If you want lastKnownLocation on changing place also than use below method
    //otherwise remove all below methods and don't implement lastKnownLocation listener
    @Override
    public void onLocationChanged(Location lastKnownLocation)
    {
        // Get MoodSwingController.
        MoodSwingController moodSwingController =
                MoodSwingApplication.getMoodSwingController();

        lastKnownLat = lastKnownLocation.getLatitude();
        lastKnownLng = lastKnownLocation.getLongitude();
        moodSwingController.setLastKnownLocation(lastKnownLat, lastKnownLng);

        //marker used for testing
        //mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("new marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastKnownLat, lastKnownLng)));
    }


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
            filter_strings.set(1, "Recent Week*");
        } else {                        // recent week filter off
            filter_strings.set(1, "Recent Week");
        }

        if (activeFilters[1] == 1) { // emotion filter on
            filter_strings.set(2, "By Emotion*");
        } else {                    // emotion filter off
            filter_strings.set(2, "By Emotion");
        }

        if (activeFilters[2] == 1) {    // trigger filter on
            filter_strings.set(3, "By Trigger*");
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

    public void handleFilterSelection(int selected){
        switch (selected) {
            case 0:       // no filter selected
                Toast.makeText(MainActivity.this, "Filters removed.", Toast.LENGTH_SHORT).show();
                updateFilterMenu(-1);   // update filter list
                buildMoodFeed();
                loadMoodSwing(); // refresh the feed
                break;


            case 1:     // sort by recent week
                Toast.makeText(MainActivity.this, "Filtering by week.", Toast.LENGTH_SHORT).show();
                updateFilterMenu(0); // update filter list
                buildMoodFeed();
                loadMoodSwing(); // refresh the feed
                break;

            case 2:      // sort by emotion
                // from http://stackoverflow.com/questions/10903754/input-text-dialog-android on 3/27
                final Dialog dialog = new Dialog(MainActivity.this);
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
                        RadioButton chosenEmotionRadioButton = (RadioButton) dialog.findViewById(chosenEmotionIndex);
                        filterEmotion = chosenEmotionRadioButton.getText().toString();
                        Toast.makeText(MainActivity.this, "Filtering by " + filterEmotion, Toast.LENGTH_SHORT).show();
                        updateFilterMenu(1);
                        dialog.dismiss();
                        buildMoodFeed();
                        loadMoodSwing(); // refresh the feed
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);

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
                            Toast.makeText(MainActivity.this, "Trigger search must be one word long.",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            // filterTrigger is acceptable
                            Toast.makeText(MainActivity.this, "Filtering by trigger word: "+ filterTrigger, Toast.LENGTH_SHORT).show();
                            updateFilterMenu(2);
                            buildMoodFeed();
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
