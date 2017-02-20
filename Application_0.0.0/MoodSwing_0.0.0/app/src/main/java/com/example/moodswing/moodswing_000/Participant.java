package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class Participant extends User {
    private FollowingList followingList = new FollowingList();
    //These are the participants that are requesting to follow this.participant
    private ArrayList<FollowRequest> followRequests = new ArrayList<>();

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

    public void declineFollowRequest(Participant requestingParticipant){
        for(FollowRequest followRequest : followRequests){
            if(followRequest.getRequestingParticipant().equals(requestingParticipant)){
                followRequest.declineRequest();
            }
        }
    }

    public void requestFollow(Participant requestingParticipant){
        followRequests.add(new FollowRequest(requestingParticipant, this));
    }

    public void removeFollowRequest(Participant requestingParticipant){
        //TODO: This doesn't work.
        //TODO: Using a hash may be a cleaner solution?
        for(FollowRequest followRequest : followRequests){
            if(followRequest.getRequestingParticipant().equals(requestingParticipant)){
                followRequests.remove(followRequest);
            }
        }
    }

    public ArrayList<Participant> getPendingFollowing() {
        return followingList.getPending();
    }

    public ArrayList<Participant> getFollowing() {
        return followingList.getFollowing();
    }

    public ArrayList<FollowRequest> getFollowRequests() {
        return followRequests;
    }


}
