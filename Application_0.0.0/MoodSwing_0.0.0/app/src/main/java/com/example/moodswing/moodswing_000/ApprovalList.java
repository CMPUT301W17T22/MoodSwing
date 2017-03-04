package com.example.moodswing.moodswing_000;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Created by fred on 03/03/2017.
 */

public class ApprovalList {
    private ArrayList<Participant> pending = new ArrayList<>();
    private ArrayList<Participant> confirmed = new ArrayList<>();

    public ApprovalList(){}

    public void newPendingParticipant(Participant otherParticipant){
        if(pending.contains(otherParticipant) || confirmed.contains(otherParticipant)){
            throw new InvalidParameterException();
        }
        pending.add(otherParticipant);
    }

    public void confirmPending(Participant otherParticipant){
        if(pending.contains(otherParticipant)){
            pending.remove(otherParticipant);

            if(confirmed.contains(otherParticipant)){
                throw new InvalidParameterException();
            }
            else{
                confirmed.add(otherParticipant);
            }
        }
        else{
            throw new InvalidParameterException();
        }
    }

    public boolean removeParticipant(Participant otherParticipant){
        return(pending.remove(otherParticipant) | confirmed.remove(otherParticipant));
    }

    // --- getters and setters ---

    public ArrayList<Participant> getPending() {
        return pending;
    }

    public ArrayList<Participant> getConfirmed() {
        return confirmed;
    }

    // --- end getters and setters ---
}
