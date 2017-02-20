package com.example.moodswing.moodswing_000;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-19.
 */

public class EqualsOverridesTest {
    @Test
    public void testUserEquals(){
        User user1 = new Participant("username1");
        User user2 = new Participant("username1");
        assertTrue(user1.equals(user2));
    }

    @Test
    public void testFollowRequestEquals(){
        Participant participant1 = new Participant("1");
        Participant participant2 = new Participant("2");
        FollowRequest followRequest1 = new FollowRequest(participant1, participant2);
        FollowRequest followRequest2 = new FollowRequest(participant1, participant2);
        assertTrue(followRequest1.equals(followRequest2));
    }
}
