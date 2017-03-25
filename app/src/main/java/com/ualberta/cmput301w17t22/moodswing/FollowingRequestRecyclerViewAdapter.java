package com.ualberta.cmput301w17t22.moodswing;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nyitrai on 3/21/2017.
 */

public class FollowingRequestRecyclerViewAdapter extends RecyclerView.Adapter<FollowingRequestRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FollowingRequestRecyclerViewAdapter(List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public FollowingRequestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_following_request, parent, false);
        return new FollowingRequestRecyclerViewAdapter.ViewHolder(view);
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

        holder.cancelRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCancelRequestDialog(holder, v);
            }
        });
    }

    /**
     * Dialog that handles cancelling a follow request. Gets user confirmation also. Need holder
     * for the username and the view to show the dialog box.
     */
    public void launchCancelRequestDialog(ViewHolder holder, View v) {

        // Get the username to stop from following you.
        final String usernameToCancel = holder.mContentView.getText().toString();

        // Create dialog box for unfollowing a user.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(v.getRootView().getContext(), R.style.DialogTheme);

        // Set the message and the title.
        adb.setMessage("Are you sure you want to cancel your follow request to " +
                usernameToCancel + "?")
                .setTitle("Cancel A Follow Request")
                .setCancelable(true);

        // Go through with the stopping of following.
        adb.setPositiveButton("Cancel Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the participantToStop from ElasticSearch given the username.
                Participant participantToCancel = elasticSearchController
                        .getParticipantByUsername(usernameToCancel);

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Decline the request using the following controller.
                followingController.cancelRequest(participantToCancel);

                // Notify the change of data.
                notifyDataSetChanged();
            }
        });

        // Create the negative button for the alert dialog.
        adb.setNegativeButton("Nevermind", new DialogInterface.OnClickListener() {
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
        public final ImageButton cancelRequestButton;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            cancelRequestButton = (ImageButton) view.findViewById(R.id.cancelRequestButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

}
