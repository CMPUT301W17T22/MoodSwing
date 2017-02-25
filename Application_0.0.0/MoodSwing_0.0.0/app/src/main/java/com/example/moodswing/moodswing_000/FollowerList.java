package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Is part of Participant participant.<br>
 * Manages the Participants that are following participant.
 *
 * @author Fred
 * @version 2017-02-19
 * @see Participant
 * @see FollowingList
 */

public class FollowerList {
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> followers = new ArrayList<>();


    public FollowerList(){}


    /**
     * DO NOT call explicitly.<br>
     * This method should only be called from requestingParticipant's FollowingList.
     *
     * @param requestingParticipant
     */
    public void createRequest(Participant requestingParticipant){
        if(pending.contains(requestingParticipant) || followers.contains(requestingParticipant)){
            throw new InvalidParameterException();
        }
        pending.add(requestingParticipant);
    }

    // receiving participant accepts requesting participant's request
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

    // receiving participant declines requesting participant's request
    public void declineRequest(Participant requestingParticipant, Participant receivingParticipant){
        requestingParticipant.followRequestDeclined(receivingParticipant);
        removeFollowRequest(requestingParticipant);
    }

    //TODO: should this also remove from followers? (including rename)
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
