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
        requestingParticipant.approveFollowRequest(receivingParticipant);
        receivingParticipant.removeFollowRequest(receivingParticipant);
    }

    public void declineRequest(){
        requestingParticipant.declineFollowRequest(receivingParticipant);
        receivingParticipant.removeFollowRequest(receivingParticipant);
    }

    public void blockRequest(){
        //do nothing with requestingParticipant. Therefore requestingParticipant cannot re-request.
        receivingParticipant.removeFollowRequest(receivingParticipant);
    }

    public Participant getRequestingParticipant() {
        return requestingParticipant;
    }

    public Participant getReceivingParticipant() {
        return receivingParticipant;
    }

    @Override
    public boolean equals(Object otherObject){
        if(this == otherObject){
            return true;
        }
        if(!(otherObject instanceof FollowRequest)){
            return false;
        }
        FollowRequest otherFollowRequest = (FollowRequest) otherObject;
        return(this.getReceivingParticipant().getUsername() == otherFollowRequest.getReceivingParticipant().getUsername()
                && this.getRequestingParticipant().getUsername() == otherFollowRequest.getRequestingParticipant().getUsername());
    }
}
