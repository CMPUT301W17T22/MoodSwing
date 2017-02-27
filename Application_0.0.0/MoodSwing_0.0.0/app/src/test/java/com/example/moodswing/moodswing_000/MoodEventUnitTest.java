package com.example.moodswing.moodswing_000;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

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
        assertEquals(moodEvent.getOriginalPoster(), "username1");
        ///TODO: test date by value (less than and greater than)
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
        //TODO: use latlng
        assertEquals(moodEvent.getLocation(), null);
    }

    @Test
    public void testEditingMoodEvent(){
        //TODO: finish
        String trigger = "trigger";
        Date date = new Date();

        EmotionalState emotionalState = new EmotionalState("happy","somewhere", Color.BLACK);
        MoodEvent moodEvent = new MoodEvent("username1",emotionalState, trigger, null, null, null);

        EmotionalState emotionalState2 = new EmotionalState("happy","somewhere", Color.BLACK);
        String trigger2 = "trigger2";
        SocialSituation socialSituation2 = new SocialSituation("so popular", "somewhere");
        String photoLoaction2 = "somewhere else";

        assertNotEquals(moodEvent.getEmotionalState(), emotionalState2);
        assertNotEquals(moodEvent.getTrigger(), trigger2);
        assertNotEquals(moodEvent.getSocialSituation(), socialSituation2);
        assertNotEquals(moodEvent.getPhotoLocation(), photoLoaction2);
       // assertNotEquals(moodEvent.getLocation(), null);

        moodEvent.editMoodEvent(emotionalState2, trigger2, socialSituation2, photoLoaction2);

        assertEquals(moodEvent.getEmotionalState(), emotionalState2);
        assertEquals(moodEvent.getTrigger(), trigger2);
        assertEquals(moodEvent.getSocialSituation(), socialSituation2);
        assertEquals(moodEvent.getPhotoLocation(), photoLoaction2);
        assertEquals(moodEvent.getLocation(), null);
    }

    //TODO: redundant?
    //this test implements grabbing the EmotionalState and SocialSituation from the MoodOptions list.
    @Test
    public void testFullMoodEvent(){
        MoodOptions moodOptions = new MoodOptions();
        String username = "bbest";
        String trigger = "trigger";
        SocialSituation socialSituation = moodOptions.getSocialSituation(0);
        EmotionalState emotionalState = moodOptions.getEmotionalState(0);
        String photolocation = "photolocation";

        //SocialSituation ssituationcompare = new SocialSituation("alone", "social_situation_alone");
       // EmotionalState emotioncompare = new EmotionalState("anger", "somewhere", Color.BLACK);


        MoodEvent moodEvent = new MoodEvent(username, emotionalState,trigger, socialSituation, photolocation, null);
        assertEquals(moodEvent.getSocialSituation(),socialSituation);
        assertEquals(moodEvent.getEmotionalState(),emotionalState);

    }

}
