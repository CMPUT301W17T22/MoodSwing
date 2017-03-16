package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import java.util.ArrayList;

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

        assertTrue(receivingParticipant.getPendingFollowers().contains(sendingParticipant));
        assertTrue(sendingParticipant.getPendingFollowing().contains(receivingParticipant));


    }

    @Test
    public void approvePendingTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());

        receivingParticipant.approveFollowerRequest(sendingParticipant);

        assertFalse(receivingParticipant.getFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());

        assertTrue(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(sendingParticipant.getFollowing().isEmpty());

        assertTrue(receivingParticipant.getFollowers().contains(sendingParticipant));
        assertTrue(sendingParticipant.getFollowing().contains(receivingParticipant));

    }

    @Test
    public void removeTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());

        assertTrue(receivingParticipant.getPendingFollowers().contains(sendingParticipant));
        assertTrue(sendingParticipant.getPendingFollowing().contains(receivingParticipant));

        receivingParticipant.declineFollowerRequest(sendingParticipant);

        assertTrue(sendingParticipant.getPendingFollowing().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());

        //removal from the sendingParticipant side now

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());

        sendingParticipant.cancelFollowRequest(receivingParticipant);

        assertTrue(sendingParticipant.getPendingFollowing().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
    }

    @Test
    public void getPendingTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(sendingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertTrue(sendingParticipant.getPendingFollowing().contains(receivingParticipant));

        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowers().contains(sendingParticipant));

    }

    @Test
    public void getApprovedTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getFollowers().isEmpty());
        assertTrue(sendingParticipant.getFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);
        receivingParticipant.approveFollowerRequest(sendingParticipant);

        assertFalse(sendingParticipant.getFollowing().isEmpty());
        assertTrue(sendingParticipant.getFollowing().contains(receivingParticipant));

        assertFalse(receivingParticipant.getFollowers().isEmpty());
        assertTrue(receivingParticipant.getFollowers().contains(sendingParticipant));

    }
}
