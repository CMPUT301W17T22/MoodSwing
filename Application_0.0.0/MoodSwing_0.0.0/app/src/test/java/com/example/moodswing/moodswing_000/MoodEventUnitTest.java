package com.example.moodswing.moodswing_000;

import android.graphics.Color;
import android.location.Location;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**test class for MoodEvent
 * Created by Fred on 2017-02-18.
 */

public class MoodEventUnitTest {
    //TODO: finish
    @Test
    public void testBasicMoodEvent(){

        String trigger = "trigger";
        Date date = new Date();
        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);
        MoodEvent moodEvent = new MoodEvent("username1",emotionalState, trigger, null, null, null);

        assertEquals(moodEvent.getEmotionalState(), emotionalState);
        assertEquals(moodEvent.getOriginalposter(), "username1");
        //Assertion of proper date cannot be used because the variable date is created and stamped but then the date created for the
        //new MoodEvent is automatically made during construction so it will be slightly different. I have confidence it storing properly.
        //assertEquals(moodEvent.getDate(), date);
        assertEquals(moodEvent.getTrigger(), trigger);
        assertEquals(moodEvent.getSocialSituation(), null);
        assertEquals(moodEvent.getPhotoLocation(), null);
        assertEquals(moodEvent.getLocation(), null);
    }

    @Test
    public void testAdvancedMoodEvent(){
        String postername = "username";
        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);
        String trigger = "trigger";
        SocialSituation socialSituation = new SocialSituation("so popular", "somewhere");
        String photoLoaction = "somewhere else";
        Date date = new Date();

        MoodEvent moodEvent = new MoodEvent(postername, emotionalState, trigger, socialSituation, photoLoaction, null);

        assertEquals(moodEvent.getEmotionalState(), emotionalState);
        assertEquals(moodEvent.getDate(), date);
        assertEquals(moodEvent.getTrigger(), trigger);
        assertEquals(moodEvent.getSocialSituation(), socialSituation);
        assertEquals(moodEvent.getPhotoLocation(), photoLoaction);
        assertEquals(moodEvent.getLocation(), null);
    }
    //this test implements grabbing the EmotionalState and SocialSituation from the MoodOptions list.
    @Test
    public void testFullMoodEvent(){
        MoodOptions moodOptions = new MoodOptions();
        String username = "bbest";
        String trigger = "trigger";
        SocialSituation socialSituation = moodOptions.getSocialSituation(0);
        EmotionalState emotionalState = moodOptions.getEmotionalState(0);
        String photolocation = "photolocation";

        //SocialSituation ssituationcompare = new SocialSituation("alone", "socialsituationalone");
       // EmotionalState emotioncompare = new EmotionalState("anger", "somewhere", Color.BLACK);


        MoodEvent moodEvent = new MoodEvent(username, emotionalState,trigger, socialSituation, photolocation, null);
        assertEquals(moodEvent.getSocialSituation(),socialSituation);
        assertEquals(moodEvent.getEmotionalState(),emotionalState);

    }

}
