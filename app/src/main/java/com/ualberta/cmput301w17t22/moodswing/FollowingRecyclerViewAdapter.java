package com.ualberta.cmput301w17t22.moodswing;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * TODO: Javadoc
 */
public class FollowingRecyclerViewAdapter extends RecyclerView.Adapter<FollowingRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FollowingRecyclerViewAdapter(List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_following, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.unfollowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Launch the dialog box that takes care of unfollowing the user.
                launchUnfollowDialog(holder, v);
            }

        });
    }

    /**
     * Launches the dialog box that handles unfollowing. Gets confirmation from the user
     * on wanting to unfollow, then actually does the unfollowing. We need the holder
     * to get the username, and the view to launch the dialog box.
     */
    public void launchUnfollowDialog(ViewHolder holder, View v) {

        // Get the username to unfollow.
        final String usernameToUnfollow = holder.mContentView.getText().toString();

        // Create dialog box for unfollowing a user.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(v.getRootView().getContext(), R.style.DialogTheme);

        // Set the message and the title.
        adb.setMessage("Are you sure you want to unfollow " + usernameToUnfollow + "?")
                .setTitle("Unfollow A User")
                .setCancelable(true);

        adb.setPositiveButton("Unfollow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the participantToUnfollow from ElasticSearch given the username.
                Participant participantToUnfollow = elasticSearchController
                        .getParticipantByUsername(usernameToUnfollow);

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Decline the request using the following controller.
                followingController.unfollowParticipant(participantToUnfollow);

                // Notify the change of data.
                notifyDataSetChanged();
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


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final ImageButton unfollowButton;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            unfollowButton = (ImageButton) view.findViewById(R.id.unfollowButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
