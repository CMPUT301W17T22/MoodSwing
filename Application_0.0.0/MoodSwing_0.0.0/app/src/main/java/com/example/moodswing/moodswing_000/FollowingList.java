package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

public class FollowingList {
    private ArrayList<Participant> pending = new ArrayList<Participant>();
    private ArrayList<Participant> following = new ArrayList<Participant>();

    public FollowingList(){

    }

    public void addParticipant(Participant participant){
        if(pending.contains(participant) || following.contains(participant)){
            throw new InvalidParameterException();
        }
        //TODO: create new follow request?
        pending.add(participant);
    }

    public boolean removeParticipant(Participant participant){
        return(pending.remove(participant) | following.remove(participant));
    }

    public void approveParticipant(Participant participant){
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
