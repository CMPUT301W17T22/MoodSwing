package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-19.
 */

public class FollowerList {
    //TODO: remove any references to Participant that redirect straight here.
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> followers = new ArrayList<>();


    public FollowerList(){}


    public void createRequest(Participant requestingParticipant){
        if(pending.contains(requestingParticipant) || followers.contains(requestingParticipant)){
            throw new InvalidParameterException();
        }
        pending.add(requestingParticipant);
    }

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

    public void declineRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.followRequestDeclined(receivingParticipant);
        removeFollowRequest(requestingParticipant);
    }

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
