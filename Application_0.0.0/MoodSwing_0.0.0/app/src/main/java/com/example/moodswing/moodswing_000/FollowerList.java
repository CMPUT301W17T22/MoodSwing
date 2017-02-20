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

    public ArrayList<Participant> getFollowers() {
        return followers;
    }

    public ArrayList<Participant> getPending() {
        return pending;
    }

    public void createRequest(Participant requestingParticipant, Participant receivingParticipant){
        if(pending.contains(requestingParticipant) || followers.contains(requestingParticipant)){
            throw new InvalidParameterException();
        }
        pending.add(requestingParticipant);
    }

    public void confirmRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.followRequestApproved(receivingParticipant);
        if(pending.contains(requestingParticipant)){
            pending.remove(requestingParticipant);
            if(followers.contains(requestingParticipant)){
                throw new InvalidParameterException();
            }
            else{
                followers.add(requestingParticipant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    public void declineRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.cancelFollowRequest(receivingParticipant);
        receivingParticipant.removeFollowerRequest(requestingParticipant);
    }

    public boolean removeFollowRequest(Participant receivingParticipant){
        return(pending.remove(receivingParticipant));
    }
}
