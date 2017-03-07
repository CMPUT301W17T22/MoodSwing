package com.ualberta.cmput301w17t22.moodswing;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * This class contains two lists of Participants.
 * Participants are initially added as "pending".
 * After approval, Participants are promoted to "approved".
 *
 * This list is part-of Participant (implemented twice).
 *
 * @author Fred
 * @version 2017-03-03
 * @see Participant
 */

public class ApprovalList {
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> approved = new ArrayList<>();

    public ApprovalList(){}

    public void newPendingParticipant(Participant otherParticipant){
        if(pending.contains(otherParticipant) || approved.contains(otherParticipant)){
            throw new InvalidParameterException();
        }
        pending.add(otherParticipant);
    }

    public void approvePending(Participant otherParticipant){
        if(pending.contains(otherParticipant)){
            pending.remove(otherParticipant);

            if(approved.contains(otherParticipant)){
                throw new InvalidParameterException();
            }
            else{
                approved.add(otherParticipant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    /**
     * Removes pending and approved Participants
     *
     * @param otherParticipant
     * @return true if removal successful
     */
    public boolean remove(Participant otherParticipant){
        return(pending.remove(otherParticipant) | approved.remove(otherParticipant));
    }

    // --- getters and setters ---

    public ArrayList<Participant> getPending() {
        return pending;
    }

    public ArrayList<Participant> getApproved() {
        return approved;
    }

    // --- end getters and setters ---
}
