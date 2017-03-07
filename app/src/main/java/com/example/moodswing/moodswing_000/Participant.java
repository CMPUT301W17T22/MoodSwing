package com.example.moodswing.moodswing_000;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

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

    // --- MoodEvent methods

    /**
     * Method to add a new mood event to the participant's mood history.
     * @param emotionalState
     * @param trigger
     * @param socialSituation
     * @param photoLocation
     * @param iconLocation
     * @param location
     */
    public void addMoodEvent(EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
                             String photoLocation, String iconLocation, LatLng location) {
        moodHistory.add(new MoodEvent(username, emotionalState, trigger,
                socialSituation, photoLocation, iconLocation, location));
    }

    // --- end MoodEvent methods


    // --- Following methods ---

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
    // --- end Following methods ---


    // --- Follower methods ---

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

    // --- end Follower methods ---


    // --- getters and setters

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

    public ArrayList<MoodEvent> getMoodEvents() {
        return moodHistory;
    }

    // --- end getters and setters
}
