package com.ualberta.cmput301w17t22.moodswing;

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

/** TODO: Javadocs.
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class FollowersRequestRecyclerViewAdapter extends RecyclerView.Adapter<FollowersRequestRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FollowersRequestRecyclerViewAdapter(List<String> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_followers_request, parent, false);
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

        holder.denyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // Launch the dialog box that handles denying the follow request.
                launchDenyDialog(holder, v);
            }
        });

        holder.approveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // Launch the dialog that handles approving the follow request.
                launchApproveDialog(holder, v);
            }
        });
    }

    /**
     * Launches the dialog that handles the approving of a follow request. This includes
     * getting user confirmation of approval, and also doing the acutal approving. We need
     * the holder to get the username to approve, and the view to show the dialog box.
     */
    public void launchApproveDialog(ViewHolder holder, final View v) {
        final String usernameToApprove = holder.mContentView.getText().toString();

        // Create dialog box for denying a user's follow request.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(v.getRootView().getContext(), R.style.DialogTheme);

        // Set the message and the title.
        adb.setMessage("Are you sure you want to approve " +
                usernameToApprove + "'s follow request?")
                .setTitle("Approve A Follow Request")
                .setCancelable(true);

        // Going forward with the approval of the follow request.
        adb.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the receivingParticipant from ElasticSearch given the username.
                Participant receivingParticipant = elasticSearchController
                        .getParticipantByUsername(usernameToApprove);

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Use the following controller to approve the request.
                followingController.approveRequest(receivingParticipant);

                // Notify the change of data.
                notifyDataSetChanged();

                // Tell the user.
                Toast.makeText(v.getRootView().getContext(),
                        "Follower request approved!", Toast.LENGTH_SHORT).show();
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
     * Launches a dialog box that handles getting user approval for the denial of a follow request
     * and also actual does the denying. We need the holder for the username and the view
     * to push the dialog to.
     */
    public void launchDenyDialog(ViewHolder holder, final View v) {
        final String usernameToDeny = holder.mContentView.getText().toString();

        // Create dialog box for denying a user's follow request.
        AlertDialog.Builder adb =
                new AlertDialog.Builder(v.getRootView().getContext(), R.style.DialogTheme);

        // Set the message and the title.
        adb.setMessage("Are you sure you want to deny " + usernameToDeny + "'s follow request?")
                .setTitle("Deny A Follow Request")
                .setCancelable(true);

        // Going forward with the denying of the follow request.
        adb.setPositiveButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the receivingParticipant from ElasticSearch given the username.
                Participant receivingParticipant = elasticSearchController
                        .getParticipantByUsername(usernameToDeny);

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Decline the request using the following controller.
                followingController.declineRequest(receivingParticipant);

                // Notify the change of data.
                notifyDataSetChanged();

                // Tell the user.
                Toast.makeText(v.getRootView().getContext(),
                        "Follower request denied.", Toast.LENGTH_SHORT).show();
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
        public final ImageButton approveButton;
        public final ImageButton denyButton;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            approveButton = (ImageButton) view.findViewById(R.id.ApproveFollowerButton);
            denyButton = (ImageButton) view.findViewById(R.id.DenyFollowerButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
