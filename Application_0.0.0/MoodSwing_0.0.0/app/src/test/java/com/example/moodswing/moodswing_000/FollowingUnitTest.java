package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 */

public class FollowingUnitTest {
    @Test
    public void testCreateFollowRequest(){
        //TODO: finish
        Participant participant1 = new Participant("participant1");
        Participant participant2 = new Participant("participant2");
        assertTrue(participant1.followingList.getPending().isEmpty());
        assertTrue(participant1.followingList.getFollowing().isEmpty());

        participant1.followingList.addParticipant(participant2);
        assertTrue(participant1.followingList.getPending().get(0) == participant2);
        assertTrue(participant1.followingList.getFollowing().isEmpty());
    }

    @Test
    public void testConfirmFollowRequest(){
        //TODO: finish
        assertTrue(false);
    }
}
