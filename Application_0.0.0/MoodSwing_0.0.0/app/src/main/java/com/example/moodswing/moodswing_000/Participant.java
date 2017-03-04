package com.example.moodswing.moodswing_000;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * A Participant is an end user of the app and is identified by their username.
 *
 * @author Fred
 * @version 2017-02-18
 * @see User
 * @see MoodEvent
 * @see FollowingList
 * @see FollowerList
 */

public class Participant extends User {
    private ApprovalList followingList = new ApprovalList();
    private ApprovalList followerList = new ApprovalList();

    private ArrayList<MoodEvent> moodHistory = new ArrayList<>();


    public Participant(String username){

        this.username = username;
    }


    // --- MoodEvent methods

    public void addMoodEvent(EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
                             String photoLocation, LatLng location) {
        moodHistory.add(new MoodEvent(username ,emotionalState, trigger, socialSituation, photoLocation, location));
    }

    // --- end MoodEvent methods


    // --- Following methods ---

    public void followParticipant(Participant receivingParticipant){
        followingList.newPendingParticipant(receivingParticipant);
        receivingParticipant.createFollowerRequest(this);
    }

    //called from other.followerList
    public void followRequestApproved(Participant receivingParticipant){
        followingList.approvePending(receivingParticipant);
    }

    public void followRequestDenied(Participant receivingParticipant){
        followingList.removeParticipant(receivingParticipant);
    }

    // --- end Following methods ---


    // --- Follower methods ---

    //called from other.followingList
    public void createFollowerRequest(Participant requestingParticipant){
        followerList.newPendingParticipant(requestingParticipant);
    }

    public void approveFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestApproved(this);
        followerList.approvePending(requestingParticipant);
    }

    public void declineFollowerRequest(Participant requestingParticipant){
        requestingParticipant.followRequestDenied(this);
        followerList.removeParticipant(requestingParticipant);
    }

    public void blockFollowerRequest(Participant requestingParticipant){
        followerList.removeParticipant(requestingParticipant);
    }

    // --- end Follower methods ---


    // --- getters and setters

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
