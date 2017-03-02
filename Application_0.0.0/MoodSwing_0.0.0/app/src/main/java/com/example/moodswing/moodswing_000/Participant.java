package com.example.moodswing.moodswing_000;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

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

    /**
     * This id is used in ElasticSearch and Jest to identify a Participants document.
     */
    @JestId
    private String id;

    private FollowingList followingList = new FollowingList();
    private FollowerList followerList = new FollowerList();

    private ArrayList<MoodEvent> moodHistory = new ArrayList<>();

    public Participant(String username) {
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
        followingList.followParticipant(receivingParticipant, this);
    }

    //called from other.followerList
    public void followRequestApproved(Participant receivingParticipant){
        followingList.followRequestApproved(receivingParticipant);
    }

    public void followRequestDeclined(Participant receivingParticipant){
        followingList.followRequestDenied(receivingParticipant);
    }
    // --- end Following methods ---


    // --- Follower methods ---

    //called from other.followingList
    public void createFollowerRequest(Participant requestingParticipant){
        followerList.createRequest(requestingParticipant);
    }

    public void approveFollowerRequest(Participant requestingParticipant){
        followerList.approveRequest(requestingParticipant, this);
    }

    public void declineFollowerRequest(Participant requestingParticipant){
        followerList.declineRequest(requestingParticipant, this);
    }

    // --- end Follower methods ---


    // --- getters and setters

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public ArrayList<Participant> getPendingFollowing() {
        return followingList.getPending();
    }

    public ArrayList<Participant> getFollowing() {
        return followingList.getFollowing();
    }

    public ArrayList<Participant> getPendingFollowers() {
        return followerList.getPending();
    }

    public ArrayList<Participant> getFollowers() {
        return followerList.getFollowers();
    }

    public ArrayList<MoodEvent> getMoodEvents() {
        return moodHistory;
    }

    // --- end getters and setters
}
