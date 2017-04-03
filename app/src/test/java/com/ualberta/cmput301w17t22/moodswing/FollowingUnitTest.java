package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test of the following list functionality of participants.
 * Created by Fred on 2017-02-18.
 */
public class FollowingUnitTest {
    // TODO: Finish testing when implementation is finalized.

    // test creating a follow request
    @Test
    public void testCreateFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // make sure everything is empty before follow
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());

        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // participant1 requests to follow participant2.
        participant1.followParticipant(participant2);

        // make sure that after follow, they are in eachother's appropriate pending lists.
        assertEquals(participant1.getPendingFollowing().get(0), participant2.getUsername());
        assertTrue(participant1.getFollowing().isEmpty());

        assertEquals(participant2.getPendingFollowers().get(0), participant1.getUsername());
        assertTrue(participant2.getFollowers().isEmpty());
    }

    // testing what happens if a follow request is approved.
    // (not the act of approving, but the result)
    @Test
    public void testConfirmFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // participant1 requests to follow participant2
        participant1.followParticipant(participant2);

        // following request goes right
        assertEquals(participant1.getPendingFollowing().get(0), participant2.getUsername());
        assertTrue(participant1.getFollowing().isEmpty());

        // test both participant1 approved, and participant2 approving
        //participant2.approveFollowerRequest(participant1);
        participant1.followRequestApproved(participant2);

        // participant2 is added to the following list
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2.getUsername());
    }

    // testing what happens if a follow request is declined
    // (not the act of declining, but the result)
    @Test
    public void testDeclineFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);

        assertTrue(participant1.getFollowing().isEmpty());
        assertEquals(participant1.getPendingFollowing().get(0), participant2.getUsername());

        // can test both participant 1 declined and participant 2 declining
        participant1.followRequestDeclined(participant2);
        //participant2.declineFollowerRequest(participant1);

        // participant2 is removed from pending following list
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
    }
}
