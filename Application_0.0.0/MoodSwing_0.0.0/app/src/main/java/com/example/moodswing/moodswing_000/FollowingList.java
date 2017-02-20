package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-19.
 */

public class FollowingList {
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> following = new ArrayList<>();

    public FollowingList() {}

    public void followParticipant(Participant receivingParticipant, Participant requestingParticipant){
        if(pending.contains(receivingParticipant) || following.contains(receivingParticipant)){
            throw new InvalidParameterException();
        }
        receivingParticipant.requestFollow(requestingParticipant);
        pending.add(receivingParticipant);
    }

    //TODO: rename this method.
    public boolean removeParticipant(Participant participant){
        return(pending.remove(participant) | following.remove(participant));
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
