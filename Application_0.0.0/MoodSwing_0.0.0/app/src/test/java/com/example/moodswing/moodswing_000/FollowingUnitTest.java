package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 */

public class FollowingUnitTest {
    //TODO: finish.
    @Test
    public void testCreateFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());

        participant1.followParticipant(participant2);
        assertEquals(participant1.getPendingFollowing().get(0), participant2);
        assertTrue(participant1.getFollowing().isEmpty());
    }

    @Test
    public void testConfirmFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);
        participant1.followRequestApproved(participant2);
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2);
    }

    @Test
    public void testDeclineFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);
        assertFalse(participant1.getPendingFollowing().isEmpty());

        participant2.declineFollowerRequest(participant1);
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
    }


    @Test
    public void testFollowRequestReceiver(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        participant1.followParticipant(participant2);
        assertTrue(participant2.getPendingFollowers().get(0).equals(participant1));

        participant2.approveFollowerRequest(participant1);
        assertTrue(participant2.getPendingFollowers().isEmpty());
    }

    @Test
    public void testDeclineFollowRequestReceiver(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);
        assertFalse(participant2.getPendingFollowers().isEmpty());
        
        participant2.declineFollowerRequest(participant1);
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());
    }
}
