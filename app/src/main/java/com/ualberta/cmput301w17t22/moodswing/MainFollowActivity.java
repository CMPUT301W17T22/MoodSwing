package com.ualberta.cmput301w17t22.moodswing;

import android.content.DialogInterface;
import android.content.Intent;
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

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.InvalidParameterException;

/**
 * Main activity for the Follower & Following Fragment tabs.
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
     * blockStatus is true
     */
    Boolean blockStatus;


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // FAB OnClick.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFollowNewUserDialog();
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
     * Launches the dialog box that handles the following of a new user.
     */
    public void launchFollowNewUserDialog() {
        // Create dialog box for following a new user.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(MainFollowActivity.this, R.style.DialogTheme);

        // Method for displaying an edittext nicely in an alertdialog adapted from
        // http://stackoverflow.com/questions/27774414/add-bigger-margin-to-edittext-in-android-alertdialog
        // on 3/25/2017.

        // Create edittext to take user input.
        final EditText usernameToFollowEditText = new EditText(MainFollowActivity.this);
        // Set custom edittext shape.
        usernameToFollowEditText.setBackgroundResource(R.drawable.dialog_edittext_shape);
        // Set some padding from the left.
        usernameToFollowEditText.setPadding(16, 0, 0, 0);

        // Create a container for the edittext.
        LinearLayout usernameToFollowEditTextContainer = new LinearLayout(MainFollowActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // Set the margins for the edittext.
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        // Set the parameters to the edittext and add it to the container.
        usernameToFollowEditText.setLayoutParams(params);
        usernameToFollowEditTextContainer.addView(usernameToFollowEditText);

        // Set the message, the title, and the edittext container.
        adb.setMessage("Enter the username of the user you would like to follow: ")
                .setTitle("Request To Follow A New User")
                .setCancelable(true)
                .setView(usernameToFollowEditTextContainer);

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
                try {
                    requestStatus = moodSwingController
                            .sendFollowRequestFromMainParticipantToUsername(usernameToFollow);

                    // TODO:
                    // requestStatus can be 1, but the request might not send
                    // ex: because we are already following them
                    // So "Request successfully sent!" displays when it might not be
                    // supposed to. Need to fix this somehow.

                    if (requestStatus) {
                        Toast.makeText(MainFollowActivity.this,
                                "Request successfully sent!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainFollowActivity.this,
                                "No user with given username found.",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (InvalidParameterException E){
                    Toast.makeText(MainFollowActivity.this,
                            "Request could not send!",
                            Toast.LENGTH_SHORT).show();
                }

                mSectionsPagerAdapter.notifyDataSetChanged();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_follow, menu);
        return true;
    }

    /**onClick method for menu options from the Follower & Following page.
     *  @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        // Handle action bar item clicks.
        if (item.getItemId() == R.id.blockUserButton) {
            // User chose the "Block User" item, should navigate to the
            // BlockUserActivity.
            intent = new Intent(MainFollowActivity.this, BlockUserActivity.class);
            startActivity(intent);
        }
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
            // Show 4 total pages.
            return 4;
        }
        /**Returns the name of the current page by its position.
         * @return CharSequence */
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
    public void onListFragmentInteraction(String username) {
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
