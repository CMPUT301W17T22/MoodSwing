package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;

import io.searchbox.annotations.JestId;

/**
 * A Participant is an end user of the app and is identified by their username.
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

    private ApprovalList followingList = new ApprovalList();
    private ApprovalList followerList = new ApprovalList();

    private ArrayList<MoodEvent> moodHistory = new ArrayList<>();

    public Participant(String username) {
        this.username = username;
    }

    // --- START: MoodEvent methods

    public void addMoodEvent(Date date,
                             EmotionalState emotionalState,
                             String trigger,
                             SocialSituation socialSituation,
                             LatLng location) {
        moodHistory.add(new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                socialSituation,
                location));

    }

    /**
     * Edit the mood event at the given position to be the new one passed in.
     * @param position
     * @param date
     * @param emotionalState
     * @param trigger
     * @param socialSituation
     * @param location
     */
    public void editMoodEventByPosition(int position,
                                        Date date,
                                        EmotionalState emotionalState,
                                        String trigger,
                                        SocialSituation socialSituation,
                                        LatLng location) {

        // Remove the mood event at the given position.
        moodHistory.remove(position);

        // Add the edited mood event at the index of the old one.
        moodHistory.add(position, new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                socialSituation,
                location));
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

    public void approveFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestApproved(this);
        followerList.approvePending(requestingParticipant);
    }

    public void declineFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestDeclined(this);
        followerList.remove(requestingParticipant);
    }

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
