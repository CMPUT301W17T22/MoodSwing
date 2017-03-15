package com.ualberta.cmput301w17t22.moodswing;

import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by PeterWeckend on 2017-03-13.
 * Tests out Participant class.
 * Other Participant methods tested by Follower/Following Unit Tests.
 */

public class ParticipantTest {

    /**
     * Testing out adding a mood event.
     */
    @Test
    public void testAddMoodEvent() {
        // initializing
        Participant participant1 = new Participant("participant1");
        Date date = new Date();
        EmotionalState emotionalState = new EmotionalState("my emo state", R.drawable.emoticon_happiness);
        String trigger = "my trigger";
        SocialSituation socialSituation = new SocialSituation("my soc state", R.drawable.social_situation_alone, View.VISIBLE);
        LatLng location = new LatLng(0,0);
        MoodEvent moodEvent = new MoodEvent(participant1.getUsername(),date,emotionalState,trigger,socialSituation,location);

        // moodhistory empty
        assertTrue(participant1.getMoodHistory().isEmpty());
        // add mood event
        participant1.addMoodEvent(moodEvent);
        // mood event added successfully
        assertTrue(participant1.getMoodHistory().get(0).getTrigger().equals(trigger));
        assertTrue(participant1.getMoodHistory().get(0).getOriginalPoster().equals("participant1"));
        assertTrue(participant1.getMoodHistory().get(0).getDate().equals(date));
        assertTrue(participant1.getMoodHistory().get(0).getEmotionalState().equals(emotionalState));
        assertTrue(participant1.getMoodHistory().get(0).getLocation().equals(location));

        // test negatives
        assertFalse(participant1.getMoodHistory().get(0).getOriginalPoster().equals("participant2"));

    }


    /**
     * Testing out editing a mood event at a certain position.
     */
    @Test
    public void testEditMoodEventByPosition() {
        // initializing
        Participant participant1 = new Participant("participant1");
        Date date = new Date();
        EmotionalState emotionalState = new EmotionalState("my emo state", R.drawable.emoticon_happiness);
        String trigger = "my trigger";
        SocialSituation socialSituation = new SocialSituation("my soc state", R.drawable.social_situation_alone, View.VISIBLE);
        LatLng location = new LatLng(0,0);

        Date date2 = new Date();
        EmotionalState emotionalState2 = new EmotionalState("my emo state2", R.drawable.emoticon_anger);
        String trigger2 = "my trigger2";
        SocialSituation socialSituation2 = new SocialSituation("my soc state2", R.drawable.social_situation_with_a_crowd, View.VISIBLE);
        LatLng location2 = new LatLng(0,1);

        Date date3 = new Date();
        EmotionalState emotionalState3 = new EmotionalState("my emo state3", R.drawable.emoticon_confusion);
        String trigger3 = "my trigger3";
        SocialSituation socialSituation3 = new SocialSituation("my soc state3", R.drawable.social_situation_with_one_other_person, View.VISIBLE);
        LatLng location3 = new LatLng(0,2);

        MoodEvent moodEvent1 = new MoodEvent(participant1.getUsername(), date,emotionalState, trigger, socialSituation, location);
        MoodEvent moodEvent2 = new MoodEvent(participant1.getUsername(),date2,emotionalState2,trigger2,socialSituation2,location2);
        MoodEvent moodEvent3 = new MoodEvent(participant1.getUsername(),date3,emotionalState3,trigger3,socialSituation3,location3);

        assertTrue(participant1.getMoodHistory().isEmpty());    // moodhistory empty
        // add mood event
        participant1.addMoodEvent(moodEvent1);
        participant1.addMoodEvent(moodEvent2);

        // edit first mood event
        participant1.editMoodEventByPosition(0,moodEvent1);


        // check first event is edited
        assertTrue(participant1.getMoodHistory().get(0).getTrigger().equals(trigger3));
        assertTrue(participant1.getMoodHistory().get(0).getOriginalPoster().equals("participant1"));
        assertTrue(participant1.getMoodHistory().get(0).getDate().equals(date3));
        assertTrue(participant1.getMoodHistory().get(0).getEmotionalState().equals(emotionalState3));
        assertTrue(participant1.getMoodHistory().get(0).getLocation().equals(location3));


        // check second event still there and hasn't changed
        assertTrue(participant1.getMoodHistory().get(1).getTrigger().equals(trigger2));
        assertTrue(participant1.getMoodHistory().get(1).getOriginalPoster().equals("participant1"));
        assertTrue(participant1.getMoodHistory().get(1).getDate().equals(date2));
        assertTrue(participant1.getMoodHistory().get(1).getEmotionalState().equals(emotionalState2));

    }

    /**
     * Testing out deleting a mood event at a certain position.
     */
    @Test
    public void testDeleteMoodEventByPosition() {
        // initializing
        Participant participant1 = new Participant("participant1");
        Date date = new Date();
        EmotionalState emotionalState = new EmotionalState("my emo state");
        String trigger = "my trigger";
        SocialSituation socialSituation = new SocialSituation("my soc state");
        LatLng location = new LatLng(0,0);

        Date date2 = new Date();
        EmotionalState emotionalState2 = new EmotionalState("my emo state2");
        String trigger2 = "my trigger2";
        SocialSituation socialSituation2 = new SocialSituation("my soc state2");
        LatLng location2 = new LatLng(0,1);

        assertTrue(participant1.getMoodHistory().isEmpty());    // moodhistory empty
        // add mood events
        participant1.addMoodEvent(date, emotionalState, trigger, socialSituation, location);
        participant1.addMoodEvent(date2, emotionalState2, trigger2, socialSituation2, location2);

        // delete first mood event
        participant1.deleteMoodEventByPosition(0);
        // check second event still there in first event's position
        assertTrue(participant1.getMoodHistory().get(0).getTrigger().equals(trigger2));
        assertTrue(participant1.getMoodHistory().get(0).getOriginalPoster().equals("participant1"));
        assertTrue(participant1.getMoodHistory().get(0).getDate().equals(date2));
        assertTrue(participant1.getMoodHistory().get(0).getEmotionalState().equals(emotionalState2));
        assertTrue(participant1.getMoodHistory().get(0).getLocation().equals(location2));

        // delete second mood event
        participant1.deleteMoodEventByPosition(0);
        assertTrue(participant1.getMoodHistory().isEmpty());    // moodhistory empty
    }


    /**
     * Testing out getting the most recent mood event.
     */
    @Test
    public void testGetMostRecentMoodEvent() {
        // initializing
        Participant participant1 = new Participant("participant1");
        Date date = new Date();
        EmotionalState emotionalState = new EmotionalState("my emo state");
        String trigger = "my trigger";
        SocialSituation socialSituation = new SocialSituation("my soc state");
        LatLng location = new LatLng(0,0);

        Date date2 = new Date();
        EmotionalState emotionalState2 = new EmotionalState("my emo state2");
        String trigger2 = "my trigger2";
        SocialSituation socialSituation2 = new SocialSituation("my soc state2");
        LatLng location2 = new LatLng(0,1);

        assertTrue(participant1.getMoodHistory().isEmpty());    // moodhistory empty
        // add mood event
        participant1.addMoodEvent(date, emotionalState, trigger, socialSituation, location);
        participant1.addMoodEvent(date2, emotionalState2, trigger2, socialSituation2, location2);

        // getmostrecentmoodevent grabs mood event #2
        assertTrue(participant1.getMostRecentMoodEvent().getTrigger().equals(trigger2));
        assertTrue(participant1.getMostRecentMoodEvent().getOriginalPoster().equals("participant1"));
        assertTrue(participant1.getMostRecentMoodEvent().getDate().equals(date2));
        assertTrue(participant1.getMostRecentMoodEvent().getEmotionalState().equals(emotionalState2));
        assertTrue(participant1.getMostRecentMoodEvent().getLocation().equals(location2));

        // check it doesn't grab #1
        assertFalse(participant1.getMostRecentMoodEvent().getTrigger().equals(trigger));
        assertFalse(participant1.getMostRecentMoodEvent().getEmotionalState().equals(emotionalState));
        assertFalse(participant1.getMostRecentMoodEvent().getLocation().equals(location));

    }


}
