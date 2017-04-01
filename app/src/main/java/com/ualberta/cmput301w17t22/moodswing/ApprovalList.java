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
    private ArrayList<String> pending = new ArrayList<>();
    private ArrayList<String> approved = new ArrayList<>();

    public ApprovalList(){}

    /**
     * Method takes in a participant that the mainParticipant needs to add to the pending list.
     * If the participant is already in the pending or approved lists then an exception is thrown.
     * Otherwise it adds that participants username to the pending list.
     * @param otherParticipant
     */
    public void newPendingParticipant(Participant otherParticipant) throws InvalidParameterException{
        if(pending.contains(otherParticipant.getUsername()) || approved.contains(otherParticipant.getUsername())){
            throw new InvalidParameterException();
        } else {
            pending.add(otherParticipant.getUsername());
        }
    }

    /**
     * Checks to see if the participant is on the pending list of the ApprovalList and if they
     * are remove them from it and add them to the approved list.
     * @param otherParticipant
     */
    public void approvePending(Participant otherParticipant) throws InvalidParameterException {
        if (pending.contains(otherParticipant.getUsername())) {
            pending.remove(otherParticipant.getUsername());
            approved.add(otherParticipant.getUsername());

        } else {
            throw new InvalidParameterException();
        }
    }

    /**
     * Removes pending and approved Participants
     *
     * @param otherParticipant
     * @return true if removal successful
     */
    public boolean remove(Participant otherParticipant) {
        return (pending.remove(otherParticipant.getUsername()) |
                approved.remove(otherParticipant.getUsername()));
    }

    // --- getters and setters ---
    /**Returns the username of participants in the pending list. */
    public ArrayList<String> getPending() {
        return pending;
    }

    /**Returns the username of participants in the approved list. */
    public ArrayList<String> getApproved() {
        return approved;
    }

    // --- end getters and setters ---
}
