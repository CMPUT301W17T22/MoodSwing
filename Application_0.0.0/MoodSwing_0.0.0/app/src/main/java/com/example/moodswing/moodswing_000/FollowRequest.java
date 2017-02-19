package com.example.moodswing.moodswing_000;

/**
 * Created by Fred on 2017-02-18.
 */

public class FollowRequest {
    private Participant requestingParticipant;
    private Participant receivingParticipant;

    public FollowRequest(Participant requestingParticipant, Participant receivingParticipant){
        this.requestingParticipant = requestingParticipant;
        this.receivingParticipant = receivingParticipant;
    }

    public void confirmRequest(){
        requestingParticipant.confirmFollowRequest(receivingParticipant);
    }
}
