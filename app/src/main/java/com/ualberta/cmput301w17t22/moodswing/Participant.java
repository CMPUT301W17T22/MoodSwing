package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;

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

    /** All of this participant's mood events in reverse chronological order. */
    private ArrayList<MoodEvent> moodHistory = new ArrayList<>();

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
    }

    /**
     * Remove the MoodEvent at the given position from this participant's mood history.
     * @param position The given position to remove the mood event from.
     */
    public void deleteMoodEventByPosition(int position) {
        moodHistory.remove(position);
    }

    /**
     * Returns the most recent mood event of this participant.
     * @return Most recent mood event of this participant.
     */
    public MoodEvent getMostRecentMoodEvent() {
        // Last element of moodHistory is the most recent.
        return moodHistory.get(moodHistory.size() - 1);
    }

    // --- END: MoodEvent methods


    // --- START: Following methods ---

    public void followParticipant(Participant receivingParticipant){
        followingList.newPendingParticipant(receivingParticipant);
        receivingParticipant.createFollowerRequest(this);
    }

    public void unfollowParticipant(Participant receivingParticipant){

        followingList.remove(receivingParticipant);
    }

    public void cancelFollowRequest(Participant receivingParticipant){
        followingList.remove(receivingParticipant);
        receivingParticipant.followerList.remove(this);
    }

    /**
     * DO NOT call explicitly. Should only be called by receivingParticipant
     *
     * @param receivingParticipant
     */
    public void followRequestApproved(Participant receivingParticipant){
        followingList.approvePending(receivingParticipant);
    }

    public void followRequestDeclined(Participant receivingParticipant){
        followingList.remove(receivingParticipant);
    }

    public void unfollowedEvent(Participant receivingParticipant){
        followerList.remove(receivingParticipant);
    }

    // --- END: Following methods ---


    // --- START: Follower methods ---

    /**
     * DO NOT call explicitly. Should only be called by requestingParticipant
     *
     * @param requestingParticipant
     */
    public void createFollowerRequest(Participant requestingParticipant){
        followerList.newPendingParticipant(requestingParticipant);
    }

    public void createUnfollowEvent(Participant requestingParticipant){
        requestingParticipant.unfollowedEvent(this);
    }

    public void approveFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestApproved(this);
        followerList.approvePending(requestingParticipant);
    }

    public void declineFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestDeclined(this);
        followerList.remove(requestingParticipant);
    }

    //Should make a blocked list that holds all the blocked Participants
    //When a follow request is made we check to see if the sending participant is in this list
    //If so it removes the request and triggers a notifying method
    // back that tells the sender they are blocked
    public void blockFollowerRequest(Participant requestingParticipant){
        followerList.remove(requestingParticipant);
    }

    // --- END: Follower methods ---


    // --- START: Getters and Setters

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public ArrayList<Participant> getPendingFollowing() {
        return followingList.getPending();
    }

    public ArrayList<Participant> getFollowing() {
        return followingList.getApproved();
    }

    public ArrayList<Participant> getPendingFollowers() {
        return followerList.getPending();
    }

    public ArrayList<Participant> getFollowers() {
        return followerList.getApproved();
    }

    public ArrayList<MoodEvent> getMoodHistory() {
        return moodHistory;
    }

    // --- END: Getters and Setters
}
