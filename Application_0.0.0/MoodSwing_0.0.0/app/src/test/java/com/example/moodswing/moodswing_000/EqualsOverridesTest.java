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
}
