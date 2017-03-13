package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.Color;

import org.junit.Test;

/**
 * Created by fred on 10/03/2017.
 */

public class ImagesTest {
    //TODO: finish

    @Test
    public void testMoodEventImages(){
        String trigger = "trigger";
        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);
        MoodEvent moodEvent = new MoodEvent("username1",emotionalState, trigger, null, null, null, null);
    }
}
