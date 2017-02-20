package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class Participant extends User {
    //TODO: rename methods to be more clear (requester vs receiver)
    private FollowingList followingList = new FollowingList();
    private FollowerList followerList = new FollowerList();

    private ArrayList<MoodEvent> moodEvents = new ArrayList<>();

    public Participant(String username){
        this.username = username;
    }

    public void followParticipant(Participant receivingParticipant){
        followingList.followParticipant(receivingParticipant, this);
    }

    public void approveFollowRequest(Participant receivingParticipant){
        followingList.approveFollowRequest(receivingParticipant);
    }

    public void cancelFollowRequest(Participant receivingParticipant){
        followingList.removeParticipant(receivingParticipant);
    }

    public void createFollowerRequest(Participant requestingParticipant){
        followerList.createRequest(requestingParticipant, this);
    }

    public void confirmFollowerRequest(Participant requestingParticipant){
        followerList.confirmRequest(requestingParticipant, this);
    }

    public void declineFollowRequest(Participant requestingParticipant){
        followerList.declineRequest(requestingParticipant, this);
    }

    public void requestFollow(Participant requestingParticipant){
        followerList.createRequest(requestingParticipant, this);
    }

    public void removeFollowRequest(Participant requestingParticipant){
        followerList.removeFollowRequest(requestingParticipant);
    }

    public ArrayList<Participant> getPendingFollowing() {
        return followingList.getPending();
    }

    public ArrayList<Participant> getFollowing() {
        return followingList.getFollowing();
    }

    public ArrayList<Participant> getFollowerRequests() {
        return followerList.getPending();
    }

    public ArrayList<Participant> getFollowers() {
        return followerList.getFollowers();
    }


}
