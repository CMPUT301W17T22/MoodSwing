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
        receivingParticipant.createFollowerRequest(requestingParticipant);
        pending.add(receivingParticipant);
    }

    //called from FollowerList (via Participant)

    public void followRequestApproved(Participant receivingParticipant){
        if(pending.contains(receivingParticipant)){
            pending.remove(receivingParticipant);

            if(following.contains(receivingParticipant)){
                throw new InvalidParameterException();
            }
            else{
                following.add(receivingParticipant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    public boolean removeParticipant(Participant receivingParticipant){
        return(pending.remove(receivingParticipant) | following.remove(receivingParticipant));
    }


    // --- getters and setters ---

    public ArrayList<Participant> getPending() {
        return pending;
    }

    public ArrayList<Participant> getFollowing() {
        return following;
    }

    // --- end getters and setters ---
}
