package com.example.moodswing.moodswing_000;

import android.graphics.Color;
import android.location.Location;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Fred on 2017-02-18.
 */

public class MoodEventUnitTest {
    //TODO: finish
    @Test
    public void testBasicMoodEvent(){
        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);
        MoodEvent moodEvent = new MoodEvent(emotionalState, null, null, null, null);

        assertEquals(moodEvent.getEmotionalState(), emotionalState);
        //This might not work?
        assertEquals(moodEvent.getDate(), new Date());
        assertEquals(moodEvent.getTrigger(), null);
        assertEquals(moodEvent.getSocialSituation(), null);
        assertEquals(moodEvent.getPhotoLocation(), null);
        assertEquals(moodEvent.getLocation(), null);
    }

    @Test
    public void testAdvancedMoodEvent(){
        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);
        String trigger = "trigger";
        SocialSituation socialSituation = new SocialSituation("so popular", "somewhere");
        String photoLoaction = "somewhere else";
        //TODO: add location, pass as parameter, and test
        MoodEvent moodEvent = new MoodEvent(emotionalState, trigger, socialSituation, photoLoaction, null);

        assertEquals(moodEvent.getEmotionalState(), emotionalState);
        //TODO: test date
        assertEquals(moodEvent.getTrigger(), trigger);
        assertEquals(moodEvent.getSocialSituation(), socialSituation);
        assertEquals(moodEvent.getPhotoLocation(), photoLoaction);
        assertEquals(moodEvent.getLocation(), null);
    }
}
