package com.ualberta.cmput301w17t22.moodswing;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ualberta.cmput301w17t22.moodswing.dummy.DummyContent.DummyItem;

import java.util.List;

/** TODO: Javadocs.
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
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

                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the receivingParticipant from ElasticSearch given the username.
                Participant receivingParticipant = elasticSearchController
                                .getParticipantByUsername(holder.mContentView.getText().toString());

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Decline the request using the following controller.
                followingController.declineRequest(receivingParticipant);

                // Notify the change of data.
                notifyDataSetChanged();
            }
        });

        holder.approveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                // Get the elastic search controller.
                ElasticSearchController elasticSearchController =
                        MoodSwingApplication.getElasticSearchController();

                // Retrieve the receivingParticipant from ElasticSearch given the username.
                Participant receivingParticipant = elasticSearchController
                        .getParticipantByUsername(holder.mContentView.getText().toString());

                // Get the following controller.
                FollowingController followingController =
                        MoodSwingApplication.getFollowingController();

                // Use the following controller to approve the request.
                followingController.approveRequest(receivingParticipant);

                // Notify the change of data.
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final Button approveButton;
        public final Button denyButton;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
            approveButton = (Button) view.findViewById(R.id.ApproveFollowerButton);
            denyButton = (Button) view.findViewById(R.id.DenyFollowerButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
