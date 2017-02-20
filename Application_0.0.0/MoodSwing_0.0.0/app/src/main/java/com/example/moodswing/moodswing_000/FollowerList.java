package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-19.
 */

public class FollowerList {
    //TODO: remove references to Participant that redirect straight here.
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> followers = new ArrayList<>();

    public FollowerList(){}

    public ArrayList<Participant> getFollowers() {
        return followers;
    }

    public ArrayList<Participant> getPending() {
        return pending;
    }

    public void createRequest(Participant receivingParticipant, Participant requestingParticipant){
        if(pending.contains(receivingParticipant) || followers.contains(receivingParticipant)){
            throw new InvalidParameterException();
        }
        receivingParticipant.requestFollow(requestingParticipant);
        pending.add(receivingParticipant);
    }

    public void confirmRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.approveFollowRequest(receivingParticipant);
        if(pending.contains(receivingParticipant)){
            pending.remove(receivingParticipant);
            if(followers.contains(receivingParticipant)){
                throw new InvalidParameterException();
            }
            else{
                followers.add(receivingParticipant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    public void declineRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.cancelFollowRequest(receivingParticipant);
        receivingParticipant.removeFollowRequest(receivingParticipant);
    }

    public boolean removeFollowRequest(Participant receivingParticipant){
        return(pending.remove(receivingParticipant));
    }
}
