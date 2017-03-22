package com.ualberta.cmput301w17t22.moodswing;

import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

/**
 * Includes auto generated code from Android Studio when created a tabbed layout.
 */
public class MainFollowActivity extends AppCompatActivity implements MSView<MoodSwing>,
        OnListFragmentInteractionListener {

    /**
     * requestStatus is true if a follow request went through, and false if no user with
     * the given username was found.
     */
    Boolean requestStatus;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_follow);

        Toolbar toolbar = (Toolbar) findViewById(R.id.followToolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // TODO: We can use this as our add a new follower button.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create dialog box for following a new user.
                AlertDialog.Builder adb =
                        new AlertDialog.Builder(MainFollowActivity.this, R.style.DialogTheme);

                // Create edittext to take user input.
                final EditText usernameToFollowEditText = new EditText(MainFollowActivity.this);

                // Set the message, the title, and the edittext.
                adb.setMessage("Enter the username of the user you would like to follow: ")
                        .setTitle("Request To Follow A New User")
                        .setCancelable(true)
                        .setView(usernameToFollowEditText);

                // Create the positive button for the alert dialog.
                adb.setPositiveButton("Send Follow Request", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // Get edittext value.
                        String usernameToFollow = usernameToFollowEditText.getText().toString();

                        // Get MoodSwingController.
                        MoodSwingController moodSwingController =
                                MoodSwingApplication.getMoodSwingController();

                        // Send the follow request. requestStatus is true if the request went
                        // through, and false if no user with the given username was found.
                        requestStatus = moodSwingController
                                .sendFollowRequestFromMainParticipantToUsername(usernameToFollow);

                        if (requestStatus) {
                            Toast.makeText(MainFollowActivity.this,
                                    "Request successfully sent!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainFollowActivity.this,
                                    "No user with given username found.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        dialog.cancel();
                    }
                });

                // Create the negative button for the alert dialog.
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        // Do nothing, cancel the dialog box.
                        dialog.cancel();
                    }
                });

                // Build and show the dialog box.
                AlertDialog dialog = adb.create();
                dialog.show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_follow, menu);
        return true;
    }

    /**
     * We could add menu items here, but currently we have no menu items.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            // getItem is called to instantiate the fragment for the given page.
            // Switch the fragment appropriately.

            switch (position) {
                case 0:
                    fragment = FollowersFragment.newInstance(1);
                break;

                case 1:
                    fragment = FollowingFragment.newInstance(1);
                break;

                case 2:
                    fragment = FollowersRequestFragment.newInstance(1);
                break;

                case 3:
                    fragment = FollowingRequestFragment.newInstance(1);
                break;

                default:
                    throw new IllegalArgumentException("No fragment found.");
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Followers";
                case 1:
                    return "Following";
                case 2:
                    return "Follow Requests";
                case 3:
                    return "Your Requests";
            }
            return null;
        }
    }

    /**
     * Taken from http://stackoverflow.com/questions/30762861/how-to-handle-multiple-fragment-interaction-listeners-in-one-activity-properly
     * on 3.12.2017.
     */
    @Override
    public void onListFragmentInteraction(Participant participant) {
        //do some stuff with the data
    }

    public void initialize() {
        // Add this View to the main Model class.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        moodSwingController.addView(this);

    }

    public void update(MoodSwing moodSwing) {

    }

}
