package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bbest on 15/03/17.
 * Tests pending follower and following lists with ApprovalList.
 */

public class ApprovalListTest {

    /**
     * Create a new pending follower request and ensure it works.
     */
    @Test
    public void newPendingParticipantTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());

        assertTrue(receivingParticipant.getPendingFollowers().contains(sendingParticipant.getUsername()));
        assertTrue(sendingParticipant.getPendingFollowing().contains(receivingParticipant.getUsername()));


    }

    /**
     * Approve a pending follower request and ensure it works.
     */
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

        assertTrue(receivingParticipant.getFollowers().contains(sendingParticipant.getUsername()));
        assertTrue(sendingParticipant.getFollowing().contains(receivingParticipant.getUsername()));

    }

    /**
     * Test declining a follower request as well as cancelling a request
     */
    @Test
    public void removeTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());

        assertTrue(receivingParticipant.getPendingFollowers().contains(sendingParticipant.getUsername()));
        assertTrue(sendingParticipant.getPendingFollowing().contains(receivingParticipant.getUsername()));

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

    /**
     * Test viewing the pending list of a user
     */
    @Test
    public void getPendingTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(sendingParticipant.getPendingFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);

        assertFalse(sendingParticipant.getPendingFollowing().isEmpty());
        assertTrue(sendingParticipant.getPendingFollowing().contains(receivingParticipant.getUsername()));

        assertFalse(receivingParticipant.getPendingFollowers().isEmpty());
        assertTrue(receivingParticipant.getPendingFollowers().contains(sendingParticipant.getUsername()));

    }

    /**
     * Test the approved list of a user
     */
    @Test
    public void getApprovedTest(){
        Participant receivingParticipant = new Participant("Jimmy");
        Participant sendingParticipant = new Participant("Johnny");

        assertTrue(receivingParticipant.getFollowers().isEmpty());
        assertTrue(sendingParticipant.getFollowing().isEmpty());

        sendingParticipant.followParticipant(receivingParticipant);
        receivingParticipant.approveFollowerRequest(sendingParticipant);

        assertFalse(sendingParticipant.getFollowing().isEmpty());
        assertTrue(sendingParticipant.getFollowing().contains(receivingParticipant.getUsername()));

        assertFalse(receivingParticipant.getFollowers().isEmpty());
        assertTrue(receivingParticipant.getFollowers().contains(sendingParticipant.getUsername()));

    }
}
