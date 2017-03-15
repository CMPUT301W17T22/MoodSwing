package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bbest on 15/03/17.
 */

public class ApprovalListTest {

    @Test
    public void newPendingParticipantTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());


    }

    @Test
    public void approvePendingTest(){

    }

    @Test
    public void removeTest(){

    }

    @Test
    public void getPendingTest(){

    }

    @Test
    public void getApprovedTest(){

    }
}
