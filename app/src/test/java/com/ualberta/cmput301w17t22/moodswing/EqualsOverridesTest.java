package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static org.junit.Assert.*;

/** Testing of the equals override for participants.
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
