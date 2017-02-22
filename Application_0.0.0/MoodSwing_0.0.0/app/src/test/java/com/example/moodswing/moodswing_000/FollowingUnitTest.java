package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 * This tests both FollowingList AND FollowerList   - Peter
 */

public class FollowingUnitTest {
    //TODO: finish.
    @Test   // test creating a follow request
    public void testCreateFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());

        // these will cause test to fail
        //assertFalse(participant1.getPendingFollowing().isEmpty());
        //assertFalse(participant1.getFollowing().isEmpty());

        participant1.followParticipant(participant2);
        assertEquals(participant1.getPendingFollowing().get(0), participant2);
        assertTrue(participant1.getFollowing().isEmpty());

    }

    @Test   // testing what happens if a follow request is approved
            // (not the act of approving, but the result)
    public void testConfirmFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);
        participant1.followRequestApproved(participant2);


        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2);
    }

    @Test   // testing what happens if a follow request is declined
    // (not the act of declining, but the result)
    public void testDeclineFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);
        assertFalse(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getPendingFollowing().get(0), participant2);

        participant2.declineFollowerRequest(participant1);
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
    }


    @Test   // test follower request act of approving
    public void testFollowRequestReceiver(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // before follow request
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // receiving follow request
        participant1.followParticipant(participant2);
        assertTrue(participant2.getPendingFollowers().get(0).equals(participant1));
        assertFalse(participant2.getPendingFollowers().get(0).equals(participant2));
        assertTrue(participant2.getFollowers().isEmpty());

        // approving follow request
        participant2.approveFollowerRequest(participant1);
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().get(0).equals(participant1));

        // check if act of approving causes correct result
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2);

    }

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
}
