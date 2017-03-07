package com.example.moodswing.moodswing_000;

import android.graphics.Color;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-25.
 */

public class MapsTest {
    //TODO: finish

    /**
     * create new MoodEvent with location information
     * ensure that correct marker is returned
     */
    @Test
    public void testMoodEventMarker(){
        String trigger = "trigger";
        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);

        MoodEvent moodEvent = new MoodEvent("username1",emotionalState, trigger, null, null, null, null);
    }
}
