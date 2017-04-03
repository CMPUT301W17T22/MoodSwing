package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test of the follower functionality of participants.
 * Tests on the FOLLOWER participant's side. (as defined in glossary)
 * Created by Peter on 2017-02-23.
 */

public class FollowerUnitTest {

    /**
     * test follower request act of approving
     */
    @Test
    public void testFollowRequestReceiver(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // before follow request
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // participant1 requests to follow participant2
        participant1.followParticipant(participant2);

        assertTrue(participant2.getPendingFollowers().get(0).equals(participant1.getUsername()));
        assertFalse(participant2.getPendingFollowers().get(0).equals(participant2.getUsername()));
        assertTrue(participant2.getFollowers().isEmpty());

        // approving follow request
        participant2.approveFollowerRequest(participant1);

        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().get(0).equals(participant1.getUsername()));

        // check if act of approving causes correct result
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2.getUsername());
    }

    /**
     * test follower request act of denying
     */
    @Test
    public void testDeclineFollowRequestReceiver(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // similar tests as testFollowRequestReceiver for follow
        participant1.followParticipant(participant2);
        assertFalse(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // make sure declineFollowerRequest removes from participant 2
        participant2.declineFollowerRequest(participant1);
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // make sure participant1 feels effect of 2's decline
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
    }

    /**
     * test follower request act of approving and checking effects of follow
     */
    @Test
    public void testApproveFollowRequestReceiver(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // similar tests as testFollowRequestReceiver for follow
        participant1.followParticipant(participant2);
        assertEquals(participant2.getPendingFollowers().get(0),participant1.getUsername());
        assertTrue(participant2.getFollowers().isEmpty());

        // make sure approveFollowerRequest adds participant1
        participant2.approveFollowerRequest(participant1);
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertEquals(participant2.getFollowers().get(0), participant1.getUsername());


        // make sure participant1 feels effect of 2's approve
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2.getUsername());
    }
}
