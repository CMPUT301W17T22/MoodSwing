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

    public ArrayList<Participant> getFollowers() {
        return followers;
    }

    public ArrayList<Participant> getPending() {
        return pending;
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
}
