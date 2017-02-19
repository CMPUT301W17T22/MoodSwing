package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 */

public class FollowingUnitTest {
    //TODO: modify due to refactor.
    @Test
    public void testCreateFollowRequest(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");
        assertTrue(participant1.getPending().isEmpty());
        assertTrue(participant1.getFollowing().isEmpty());

        participant1.followParticipant(participant2);
        assertEquals(participant1.getPending().get(0), participant2);
        assertTrue(participant1.getFollowing().isEmpty());
    }

    @Test
    public void testConfirmFollowRequest(){
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");

        participant1.followParticipant(participant2);
        participant1.approveFollowRequest(participant2);
        assertTrue(participant1.getPending().isEmpty());
        assertEquals(participant1.getFollowing().get(0), participant2);
    }
}
