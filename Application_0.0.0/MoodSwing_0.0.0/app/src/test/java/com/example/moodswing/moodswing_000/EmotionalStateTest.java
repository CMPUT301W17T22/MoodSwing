package com.example.moodswing.moodswing_000;

import android.graphics.Color;
import android.media.Image;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test of The EmotionalState Class that is set by a user to a Mood Event to dictate the emotion to be tagged
 * with that Mood Event.
 * Created by Fred on 2017-02-18.
 */

public class EmotionalStateTest {
    @Test
    public void testEmotionalState(){
        String happy = "was happy";
        String imageLocation = "somewhere";
        int color = Color.BLACK;

        EmotionalState emotionalState = new EmotionalState(happy, imageLocation, color);

        assertEquals(emotionalState.getDescription(), happy);
        //assertEquals(emotionalState.getEmoticon(), imageLocation);
        assertEquals(emotionalState.getColor(), color);
    }
}
