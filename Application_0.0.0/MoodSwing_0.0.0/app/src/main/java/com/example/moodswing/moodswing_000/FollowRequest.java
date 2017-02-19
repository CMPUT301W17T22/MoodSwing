package com.example.moodswing.moodswing_000;

/**
 * Created by Fred on 2017-02-18.
 */

public class FollowRequest {
    private Participant requestingParticipant;
    private Participant recievingParticipant;

    public FollowRequest(Participant requestingParticipant, Participant recievingParticipant){
        this.requestingParticipant = requestingParticipant;
        this.recievingParticipant = recievingParticipant;
    }
}
