package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by PeterWeckend on 2017-04-02.
 * Test BlockUserActivity and everything related to blocking users.
 */

public class BlockingUnitTest {
    // test creating a follow request towards a user that blocks you
    @Test
    public void testFollowBlockingUser(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // make sure everything is empty before follow
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // participant1 tries to block participant2.
        participant1.blockParticipant("participant2");
        // mae sure blocklists work
        assertTrue(participant1.getBlockList().contains("participant2"));
        int i = 0;

        // participant1 requests to follow participant2.
        try {
            participant2.followParticipant(participant1);
            assertEquals(1,0); // should never get here
        }
        catch (Exception e) {
            // try and catch used here because
            // exception is thrown and caught if one tries to follow a blocked user
            i = 1; // make sure exception is thrown
        }

        // make sure exception is thrown
        assertEquals(i, 1);
        // make sure everything is empty after follow
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
        assertTrue(participant1.getFollowers().isEmpty());
        assertTrue(participant1.getPendingFollowers().isEmpty());
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());
        assertTrue(participant2.getPendingFollowing().isEmpty());
        assertTrue(participant2.getFollowing().isEmpty());
    }



    // test creating a follow request towards a user that blocked you
    @Test
    public void testFollowCancelledBlockingUser(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // make sure everything is empty before follow
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // participant1 tries to block participant2.
        participant1.blockParticipant("participant2");
        // mae sure blocklists work
        assertTrue(participant1.getBlockList().contains("participant2"));

        // make sure unblock function works
        participant1.unblockParticipant("participant2");

        participant2.followParticipant(participant1);

        // make sure following still works
        assertEquals(participant1.getPendingFollowers().get(0), participant2.getUsername());
        assertEquals(participant2.getPendingFollowing().get(0), participant1.getUsername());

    }

    // test checking if you can follow someone you are blocking
    // Passes if you can, fails if you cannot
    @Test
    public void testFollowBlockedUser(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        // make sure everything is empty before follow
        assertTrue(participant1.getPendingFollowing().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());
        assertTrue(participant2.getPendingFollowers().isEmpty());
        assertTrue(participant2.getFollowers().isEmpty());

        // participant1 tries to block participant2.
        participant1.blockParticipant("participant2");
        // mae sure blocklists work
        assertTrue(participant1.getBlockList().contains("participant2"));

        // follow the blocked user
        participant1.followParticipant(participant2);

        // make sure following  works
        assertEquals(participant1.getPendingFollowing().get(0), participant2.getUsername());
        assertEquals(participant2.getPendingFollowers().get(0), participant1.getUsername());

    }
}
