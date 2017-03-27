package com.ualberta.cmput301w17t22.moodswing;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * An activity where the user of the app may view who they have blocked, block a new user,
 * or unblock a user that they have blocked.
 */
public class BlockUserActivity extends AppCompatActivity implements MSView<MoodSwing> {

    /** The main participant, the current logged in user of the app. */
    private Participant mainParticipant;

    /** The ListView that will hold the main participant's block list. */
    private ListView blockListListView;

    /** The ArrayAdapter for the blockListListView. */
    private ArrayAdapter<String> blockListAdapter;

    /** The main participant's mood history, an ArrayList of their MoodEvents */
    private ArrayList<String> blockList;

    /** The floating action button the user may press to block a new user. */
    private FloatingActionButton blockUserFab;

    /** The toolbar for this activity. */
    private Toolbar toolbar;

    /** Text that informs the user if they are not blocking any users. */
    private TextView notBlockingTextView;

    /**
     * blockStatus is true when the user has been successfully found and blocked.
     * False when a username that is not on MoodSwing is entered to be blocked.
     */
    private Boolean blockStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_user);

        // Set up all the widgets for this activity.
        initialize();

        /**
         * onClick for the users in the blockList. Un-block functionality goes here. The user
         * will click on a user, and a dialog box will appear and give them the choice to unblock
         * the user.
         */
        blockListListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch dialog box that handles unblocking the user at the given position.
                launchUnblockUserDialog(position);
            }
        });

        /**
         * On click for floating action button.
         * This button will let participant's block a new user.
         */
        blockUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the dialog box that handles blocking the user.
                launchBlockUserDialog();
            }
        });
    }

    /** onStart, load the information about the main participant from the Model. */
    @Override
    protected void onStart() {
        super.onStart();

        // Load information on the main participant from MoodSwing.
        loadMoodSwing();
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
     * Launches a dialog box that handles blocking a new user. This includes getting user input,
     * and using the FollowingController to actually block the user.
     */
    public void launchBlockUserDialog() {
        // Create dialog box for blocking a user.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(BlockUserActivity.this, R.style.DialogTheme);

        // Method for displaying an edittext nicely in an alertdialog adapted from
        // http://stackoverflow.com/questions/27774414/add-bigger-margin-to-edittext-in-android-alertdialog
        // on 3/25/2017.

        // Create edittext to take user input.
        final EditText usernameToBlockEditText = new EditText(BlockUserActivity.this);

        // Set custom edittext shape.
        usernameToBlockEditText.setBackgroundResource(R.drawable.dialog_edittext_shape);

        // Set some padding from the left.
        usernameToBlockEditText.setPadding(16, 0, 0, 0);

        // Create a container for the edittext.
        LinearLayout usernameToBlockEditTextContainer = new LinearLayout(BlockUserActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set the margins for the edittext.
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

        // Set the parameters to the edittext and add it to the container.
        usernameToBlockEditText.setLayoutParams(params);
        usernameToBlockEditTextContainer.addView(usernameToBlockEditText);

        // Set the message, the title, and the edittext container with the edittext.
        adb.setMessage("Enter the username of the user you would like to block: ")
                .setTitle("Block A User")
                .setCancelable(true)
                .setView(usernameToBlockEditTextContainer);

        // Create the positive button for the alert dialog.
        adb.setPositiveButton("Block", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get edittext value.
                String usernameToBlock = usernameToBlockEditText.getText().toString();

                // Check that the user is not trying to block themselves.
                if (!usernameToBlock.equals(mainParticipant.getUsername())) {

                    // Check that the user is not trying to block someone they already block.
                    if (!blockList.contains(usernameToBlock)) {

                        // Get FollowingController.
                        FollowingController followingController =
                                MoodSwingApplication.getFollowingController();

                        // Send the block through. blockStatus is true if the block went
                        // through, and false if no user with the given username was found.
                        blockStatus = followingController.blockParticipant(usernameToBlock);

                        if (blockStatus) {
                            Toast.makeText(BlockUserActivity.this,
                                    "User blocked successfully!",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(BlockUserActivity.this,
                                    "No user with given username found.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // Set the not blocking anyone text appropriately.
                        setNotBlockingText();

                        // Notify the adapter to update.
                        blockListAdapter.notifyDataSetChanged();
                        dialog.cancel();

                    } else {
                        // Tell the user they already are blocking that user.
                        Toast.makeText(BlockUserActivity.this,
                                "You are already blocking that user.",
                                Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }

                } else {
                    // Tell the user that they can not block themselves.
                    Toast.makeText(BlockUserActivity.this,
                            "You can't block yourself.",
                            Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            }
        });

        // Create the negative button for the alert dialog.
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, cancel the dialog box.
                dialog.cancel();
            }
        });

        // Build and show the dialog box.
        AlertDialog dialog = adb.create();
        dialog.show();
    }

    /**
     * This dialog box gives the user the choice to unblock the selected currently blocked user.
     */
    public void launchUnblockUserDialog(int position) {
        // Get the username of the user we want to maybe unblock.
        final String usernameToUnblock = blockList.get(position);

        // Create dialog box for unblocking a user.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(BlockUserActivity.this, R.style.DialogTheme);

        // Set the message, the title, and the edittext container with the edittext.
        adb.setMessage("Would you like to unblock " + usernameToUnblock + "?")
                .setTitle("Unblock A User")
                .setCancelable(true);

        // Create the positive button for the alert dialog.
        adb.setPositiveButton("Unblock", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Get FollowingController.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Send the unblock through the following controller.
                followingController.unblockParticipant(usernameToUnblock);

                // Tell the user they unblocked the user they chose.
                Toast.makeText(BlockUserActivity.this,
                        "User " + usernameToUnblock + " successfully unblocked!",
                        Toast.LENGTH_SHORT).show();

                // Set the not blocking anyone text appropriately.
                setNotBlockingText();

                // Notify the adapter to update.
                blockListAdapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        // Create the negative button for the alert dialog.
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, cancel the dialog box.
                dialog.cancel();
            }
        });

        // Build and show the dialog box.
        AlertDialog dialog = adb.create();
        dialog.show();
    }

    /**
     * Checks if the block list is empty. If it is empty, set the notBlockingTextView to be visible,
     * otherwise, set it to be invisible.
     */
    public void setNotBlockingText() {
        if (blockList.isEmpty() || blockList == null) {
            notBlockingTextView.setVisibility(View.VISIBLE);
        } else {
            notBlockingTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Load the main participant from the main model class MoodSwing, load the user's block list,
     * and setup the adapter.
     */
    public void loadMoodSwing() {
        // Get the main Model and get the main participant, and the main participant's mood history.
        MoodSwingController moodSwingController = MoodSwingApplication.getMoodSwingController();
        mainParticipant = moodSwingController.getMainParticipant();
        blockList = mainParticipant.getBlockList();

        // Set the not blocking text appropriately.
        setNotBlockingText();

        // Initialize array adapter.
        blockListAdapter = new ArrayAdapter<String>(this, R.layout.blocked_user, blockList);
        blockListListView.setAdapter(blockListAdapter);
    }

    /**
     * Initialize all widgets for this Activity, and add this view to the main model class.
     */
    public void initialize() {
        // Setup the widgets.
        blockListListView  = (ListView) findViewById(R.id.blockListListView);
        blockUserFab = (FloatingActionButton) findViewById(R.id.blockUserFab);
        toolbar = (Toolbar) findViewById(R.id.blockUserToolbar);
        notBlockingTextView = (TextView) findViewById(R.id.blockUserNotBlockingText);

        // Set the toolbar.
        setSupportActionBar(toolbar);

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
