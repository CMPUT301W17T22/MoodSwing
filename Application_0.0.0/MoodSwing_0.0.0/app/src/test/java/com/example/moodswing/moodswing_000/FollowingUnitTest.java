package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 * This tests both primarily FollowingList   - Peter
 */

public class FollowingUnitTest {
    //TODO: finish.
    @Test   // test creating a follow request
    public void testCreateFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");
        // make sure everything is empty before follow
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // these will cause test to fail
        //assertFalse(participant1.getPendingFollowing().isEmpty());
        //assertFalse(participant1.getFollowing().isEmpty());

        participant1.followParticipant(participant2);
        // make sure that after follow, everything works
        assertEquals(participant1.getPendingFollowing().get(0), participant2);
        assertTrue(participant1.getFollowing().isEmpty());
        assertEquals(participant2.getPendingFollowers().get(0), participant1);
        assertTrue(participant2.getFollowers().isEmpty());
    }

    @Test   // testing what happens if a follow request is approved
            // (not the act of approving, but the result)
    public void testConfirmFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);

        // following request goes right
        assertEquals(participant1.getPendingFollowing().get(0), participant2);
        assertTrue(participant1.getFollowing().isEmpty());

        // can test both participant1 approved, and participant2 approving
        //participant2.approveFollowerRequest(participant1);
        participant1.followRequestApproved(participant2);

        // participant2 is added to the following list
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2);
    }

    @Test   // testing what happens if a follow request is declined
    // (not the act of declining, but the result)
    public void testDeclineFollowRequestRequester(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);

        assertTrue(participant1.getFollowing().isEmpty());
        assertEquals(participant1.getPendingFollowing().get(0), participant2);

        // can test both participant 1 declined and participant 2 declining
        participant1.followRequestDeclined(participant2);
        //participant2.declineFollowerRequest(participant1);

        // participant2 is removed from pending following list
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
    }


}
