package com.ualberta.cmput301w17t22.moodswing;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * A Participant is an end user of the app and is uniquely identified by their username.
 * <p/>
 * The Participant holds all the information about a user; all of their mood events, their
 * followers, who they are following, etc.
 *
 * @author Fred
 * @version 2017-03-03
 * @see User
 * @see MoodEvent
 * @see ApprovalList
 */

public class Participant extends User {
    /**
     * This id is used in ElasticSearch and Jest to identify a Participants document.
     */
    @JestId
    private String id;

    /** The list of other participants that this participant is following. */
    private ApprovalList followingList = new ApprovalList();

    /** The list of other participants that are following this participant. */
    private ApprovalList followerList = new ApprovalList();

    /** The list of participants being blocked from sending further follow requests. */
    private ArrayList<String> blockList = new ArrayList<>();

    /** All of this participant's mood events in reverse chronological order. */
    private ArrayList<MoodEvent> moodHistory = new ArrayList<>();

    /** These attributes keep track of the most recent mood event for this participant's
     * emotional state, trigger, and date. These are used only in filtering. */
    private MoodEvent mostRecentMoodEvent;
    private String mostRecentEmotionalStateDescription;
    private String mostRecentTrigger;
    private Date mostRecentDate;

    public Participant(String username) {
        this.username = username;
    }

    // --- START: MoodEvent methods

    /**
     * Adds a mood event defined into the Participant's mood history.
     */
    public void addMoodEvent(MoodEvent moodEvent) {
        if (!moodEvent.getOriginalPoster().equals(this.getUsername())) {
            Log.i("ERROR","Attempted to add MoodEvent to a different Participant than the " +
                    "Participant who created the MoodEvent.");
            throw new IllegalArgumentException();
        } else {
            moodHistory.add(moodEvent);
            sortMoodHistory();
            updateMostRecentMoodEvent();
        }
    }

    /**
     * Edit the mood event at the given position to be the new one passed in.
     * @param position
     */
    public void editMoodEventByPosition(int position, MoodEvent moodEvent) {

        // Remove the mood event at the given position.
        moodHistory.remove(position);

        // Add the edited mood event at the index of the old one.
        moodHistory.add(position, moodEvent);
        updateMostRecentMoodEvent();
    }

    /**
     * Remove the MoodEvent at the given position from this participant's mood history.
     * @param position The given position to remove the mood event from.
     */
    public void deleteMoodEventByPosition(int position) {
        moodHistory.remove(position);
        updateMostRecentMoodEvent();
    }

    /**
     * Sorts the participant's mood history to be in reverse chronological order,
     * so the first item in the mood history is always the most recent.
     */
    private void sortMoodHistory() {
        // Sort the participant's mood history to be in chronological order.
        if (!moodHistory.isEmpty()) {
            Collections.sort(moodHistory, new Comparator<MoodEvent>() {
                @Override
                public int compare(MoodEvent o1, MoodEvent o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
            // Reverse it.
            Collections.reverse(moodHistory);
        }
    }


    /**
     * Internal method to update the most recent mood event of this participant. Sets the
     * mostRecentMoodEvent to be the last one in the mood history if the mood history is not empty.
     */
    private void updateMostRecentMoodEvent() {
        if (!moodHistory.isEmpty()) {
            // The first element of moodHistory is the most recent.
            mostRecentMoodEvent = moodHistory.get(0);

            mostRecentEmotionalStateDescription =
                    mostRecentMoodEvent.getEmotionalState().getDescription();
            mostRecentTrigger = mostRecentMoodEvent.getTrigger();
            mostRecentDate = mostRecentMoodEvent.getDate();

        } else {
            // If the participant has no mood events, initialize the most recent information to
            // be empty, so it will not show up in filters.
            mostRecentMoodEvent = null;
            mostRecentEmotionalStateDescription = "";
            mostRecentTrigger = "";
            mostRecentDate = new Date(0);
        }
    }

    // --- END: MoodEvent methods


    // --- START: Following methods ---

        /**Follow a participant by:
         * Checking if main participant is in their block list.
         * Checks to see if the sending participant has already sent a follow request to that participant.
         * Checks to see if the sending participant is already following the participant.
         * Successfully follows a participant by:
         * Adding the participant to the sending participants pending on the followingList.
         * Creates a follower request that is sent to the receiving participant.
         * @param receivingParticipant */
    public void followParticipant(Participant receivingParticipant) throws InvalidParameterException {
        Boolean sendFollowRequest = false;

        // Make sure we aren't in their block list
         if (receivingParticipant.blockList.isEmpty()
                 || !(receivingParticipant.blockList.contains(this.getUsername()))) {
             sendFollowRequest = true;
         }

        if (sendFollowRequest) {
            followingList.newPendingParticipant(receivingParticipant);
            receivingParticipant.createFollowerRequest(this);
        } else {
            throw new InvalidParameterException();
        }
    }

    /**Removes the participant from the main participants from our followingList, and sends a
     * unfollowedEvent to the receiving participant to remove the main participant
     * from their followerList.
     * @param receivingParticipant */
    public void unfollowParticipant(Participant receivingParticipant){

        followingList.remove(receivingParticipant);

    }
    /**Remove a pending following request from the main participant and corresponding
     * follower request from the receiving participant.
     * @param receivingParticipant */
    public void cancelFollowRequest(Participant receivingParticipant){
        followingList.remove(receivingParticipant);
        receivingParticipant.followerList.remove(this);
    }
    /**Adds the participant username to the main participants block list, unless they are already in it.
     * @param participantUsername  */
    public void blockParticipant(String participantUsername){
        if (this.blockList.contains(participantUsername)) {
        } else {
            blockList.add(participantUsername);
        }
    }
    /**Removes the participant username from the main Participants block list. */
    public void unblockParticipant(String participantUsername){
        this.blockList.remove(participantUsername);
    }


     //DO NOT call explicitly. Should only be called by receivingParticipant
    /**Event that is received indicating the main participants follow request was approved so
     * the pending following request is moved to the approved list in followingList.
     * @param receivingParticipant
     */
    public void followRequestApproved(Participant receivingParticipant) {
        followingList.approvePending(receivingParticipant);
    }
    /**Event that is received indicating the main participants follow request was declined so
     * the pending following request is removed from followingList.
     * @param receivingParticipant
     */
    public void followRequestDeclined(Participant receivingParticipant){
        followingList.remove(receivingParticipant);
    }
    /**Event received indicating that the main participant has been unfollowed. The corresponding
     * follower is removed from the main participants follower list. */
    public void unfollowedEvent(Participant receivingParticipant){
        followerList.remove(receivingParticipant);
    }

    // --- END: Following methods ---


    // --- START: Follower methods ---


     // DO NOT call explicitly. Should only be called by requestingParticipant
     /**Creates a request object received from a participant
      * that adds them to the main participants pending followers.
     * @param requestingParticipant
     */
    public void createFollowerRequest(Participant requestingParticipant) throws InvalidParameterException{

            followerList.newPendingParticipant(requestingParticipant);

    }
    /**Create an unfollow event to inform the requesting participant that they are being unfollowed.
     * @param requestingParticipant  */
    public void createUnfollowEvent(Participant requestingParticipant){
        requestingParticipant.unfollowedEvent(this);
    }
    /**Approves a pending follower request and sends a followRequestApproved(this) event
     * to the participant in order to let them know to update their list.
     * @param requestingParticipant */
    public void approveFollowerRequest(Participant requestingParticipant) throws InvalidParameterException{
        requestingParticipant.followRequestApproved(this);
        followerList.approvePending(requestingParticipant);
    }
    /**Declines a follower request by removing it from the followerList and sends a
     * followRequestDeclined(this) to the participant to indicate their request has been denied.
     * @param requestingParticipant */
    public void declineFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestDeclined(this);
        followerList.remove(requestingParticipant);
    }



    // --- END: Follower methods ---


    // --- START: Getters and Setters

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public ArrayList<String> getBlockList() { return blockList; }

    public ArrayList<String> getPendingFollowing() {
        return followingList.getPending();
    }

    public ArrayList<String> getFollowing() {
        return followingList.getApproved();
    }

    public ArrayList<String> getPendingFollowers() {
        return followerList.getPending();
    }

    public ArrayList<String> getFollowers() {
        return followerList.getApproved();
    }

    public ArrayList<MoodEvent> getMoodHistory() {
        return moodHistory;
    }

    // added for unit testing
    public MoodEvent getMostRecentMoodEvent() { return mostRecentMoodEvent;}

    public String getMostRecentEmotionalStateDescription() {
        return mostRecentEmotionalStateDescription;
    }

    public String getMostRecentTrigger() {
        return mostRecentTrigger;
    }

    public Date getMostRecentDate() {
        return mostRecentDate;
    }

    // --- END: Getters and Setters
}
