package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Peter on 2017-02-23.
 * This tests primarily FollowerList   - Peter
 */

public class FollowerUnitTest {




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
    // test follower request act of denying
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
