package com.ualberta.cmput301w17t22.moodswing;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Participant} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FollowersRecyclerViewAdapter extends RecyclerView.Adapter<FollowersRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FollowersRecyclerViewAdapter(List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_followers, parent, false);
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

        holder.stopFollowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the dialog box that takes care of stopping the user from following you.
                launchStopFollowerDialog(holder, v);
            }
        });
    }

    public void launchStopFollowerDialog(ViewHolder holder, View v) {

        // Get the username to stop from following you.
        final String usernameToStop = holder.mContentView.getText().toString();

        // Create dialog box for unfollowing a user.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(v.getRootView().getContext(), R.style.DialogTheme);

        // Set the message and the title.
        adb.setMessage("Are you sure you want to stop " + usernameToStop + " from following you?")
                .setTitle("Stop A User From Following You")
                .setCancelable(true);

        // Go through with the stopping of following.
        adb.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the participantToStop from ElasticSearch given the username.
                Participant participantToStop = elasticSearchController
                        .getParticipantByUsername(usernameToStop);

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Decline the request using the following controller.
                followingController.stopParticipant(participantToStop);

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
        public final ImageButton stopFollowerButton;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            stopFollowerButton = (ImageButton) view.findViewById(R.id.stopFollowerButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
