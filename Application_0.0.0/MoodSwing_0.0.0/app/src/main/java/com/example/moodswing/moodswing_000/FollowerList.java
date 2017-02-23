package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-19.
 */

public class FollowerList {
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> followers = new ArrayList<>();


    public FollowerList(){}


    //called from FollowingList (via Participant)
    public void createRequest(Participant requestingParticipant){
        if(pending.contains(requestingParticipant) || followers.contains(requestingParticipant)){
            throw new InvalidParameterException();
        }
        pending.add(requestingParticipant);
    }

    // receiving participant accepts requesting participant's request
    public void approveRequest(Participant requestingParticipant, Participant receivingParticipant){
        if(pending.contains(requestingParticipant)){
            pending.remove(requestingParticipant);
            if(followers.contains(requestingParticipant)){
                throw new InvalidParameterException();
            }
            else{
                requestingParticipant.followRequestApproved(receivingParticipant);
                followers.add(requestingParticipant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    // receiving participant declines requesting participant's request
    public void declineRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.followRequestDeclined(receivingParticipant);
        removeFollowRequest(requestingParticipant);
    }

    //TODO: should this also remove from followers? (including rename)
    public boolean removeFollowRequest(Participant receivingParticipant){
        return(pending.remove(receivingParticipant));
    }


    // --- getters and setters ---

    public ArrayList<Participant> getFollowers() {
        return followers;
    }

    public ArrayList<Participant> getPending() {
        return pending;
    }

    // --- end getters and setters ---
}
