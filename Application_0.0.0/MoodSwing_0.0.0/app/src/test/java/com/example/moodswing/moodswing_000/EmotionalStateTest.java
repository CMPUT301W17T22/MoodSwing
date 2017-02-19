package com.example.moodswing.moodswing_000;

import android.graphics.Color;
import android.media.Image;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 */

public class EmotionalStateTest {
    @Test
    public void testEmotionalState(){
        String happy = "I am happy";
        String imageLocation = "somewhere";
        int color = Color.BLACK;

        EmotionalState emotionalState = new EmotionalState(happy, imageLocation, color);

        assertEquals(emotionalState.getDescription(), happy);
        assertEquals(emotionalState.getEmoticon(), imageLocation);
        assertEquals(emotionalState.getColor(), color);
    }
}
