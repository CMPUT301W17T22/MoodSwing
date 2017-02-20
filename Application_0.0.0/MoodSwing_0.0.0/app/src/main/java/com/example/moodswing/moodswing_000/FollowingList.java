package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-19.
 */

public class FollowingList<Participant> {
    private ArrayList<Participant> pending = new ArrayList<Participant>();
    private ArrayList<Participant> following = new ArrayList<Participant>();

    public FollowingList() {}

    public void followParticipant(Participant receivingParticipant, Participant requestingParticipant){
        if(pending.contains(receivingParticipant) || following.contains(receivingParticipant)){
            throw new InvalidParameterException();
        }
        receivingParticipant.requestFollow(requestingParticipant);
        pending.add(receivingParticipant);
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
}
