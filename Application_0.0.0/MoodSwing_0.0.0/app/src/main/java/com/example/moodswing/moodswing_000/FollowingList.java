package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Is part of Participant <code>participant</code>.<br>
 * Manages the Participants that <code>participant</code> is following.
 *
 * @author Fred
 * @version 2017-02-19
 * @see Participant
 * @see FollowerList
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

    /**
     * DO NOT call explicitly.<br>
     * Should only be called from receivingParticipant's FollowerList.
     *
     * @param receivingParticipant
     */
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
