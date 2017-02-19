package com.example.moodswing.moodswing_000;

import android.icu.text.MessagePattern;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class Participant extends User {
    //These are the participants that are requesting to follow this.participant
    private ArrayList<FollowRequest> followRequests = new ArrayList<FollowRequest>();
    private ArrayList<Participant> pending = new ArrayList<Participant>();
    private ArrayList<Participant> following = new ArrayList<Participant>();

    public Participant(String username){
        this.username = username;
    }

    public void requestFollow(Participant requestingParticipant){
        followRequests.add(new FollowRequest(requestingParticipant, this));
    }

    public void removeFollowRequest(Participant requestingParticipant){
        //TODO: make this actually work. Below statement might not work.
        followRequests.remove(new FollowRequest(requestingParticipant, this));
    }

    public void followParticipant(Participant participant){
        if(pending.contains(participant) || following.contains(participant)){
            throw new InvalidParameterException();
        }
        participant.requestFollow(this);
        pending.add(participant);
    }

    //TODO: rename the following 2 methods.
    public boolean removeParticipant(Participant participant){
        return(pending.remove(participant) | following.remove(participant));
    }
    public boolean declineFollowRequest(Participant participant){
        return removeParticipant(participant);
    }

    public void approveFollowRequest(Participant participant){
        if(pending.contains(participant)){
            pending.remove(participant);
            if(following.contains(participant)){
                throw new InvalidParameterException();
            }
            else{
                following.add(participant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    public ArrayList<Participant> getPending() {
        return pending;
    }

    public ArrayList<Participant> getFollowing() {
        return following;
    }
}
