package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/** Unit Test class for MoodEvent
 * Created by Fred on 2017-02-18.
 * Errors occur with location changes. Commented out for now.
 */

public class MoodEventUnitTest {

    /** Testing basic MoodEvent creation. */
    @Test
    public void testBasicMoodEvent(){

        // Create the MoodEvent.
        String username = "username";
        String trigger = "trigger";
        Date date = new Date();
        EmotionalState emotionalState =
                new EmotionalStateFactory().createEmotionalStateByName("Happiness");
        SocialSituation socialSituation =
                new SocialSituationFactory().createSocialSituationByName("Alone");
        Location location = null;

        MoodEvent moodEvent = new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                socialSituation,
                location, null);

        // Test getters.
        assertEquals(moodEvent.getEmotionalState(), emotionalState);
        assertEquals(moodEvent.getOriginalPoster(), username);
        assertEquals(moodEvent.getSocialSituation(), socialSituation);
        assertEquals(moodEvent.getLocation(), location);
        assertEquals(moodEvent.getDate(), date);
        assertEquals(moodEvent.getTrigger(), trigger);

        // Test the more complicated attributes.
        assertEquals(moodEvent.getEmotionalState().getDescription(),
                emotionalState.getDescription());
        assertEquals(moodEvent.getSocialSituation().getDescription(),
                socialSituation.getDescription());
    }

    /** Test partially empty MoodEvents. */
    @Test
    public void testPartialMoodEvent() {
        String username = "username";
        String trigger = "trigger";
        Date date = new Date();
        EmotionalState emotionalState =
                new EmotionalStateFactory().createEmotionalStateByName("Happiness");
        SocialSituation socialSituation =
                new SocialSituationFactory().createSocialSituationByName("Alone");
        Location location = null;

        MoodEvent moodEvent1 = new MoodEvent(username,
                date,
                emotionalState,
                null,
                null,
                location,
                null);

        MoodEvent moodEvent2 = new MoodEvent(username,
                date,
                emotionalState,
                null,
                socialSituation,
                location,
                null);

        MoodEvent moodEvent3 = new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                null,
                location,
                null);

        MoodEvent moodEvent4 = new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                socialSituation,
                null,
                null);

        MoodEvent moodEvent5 = new MoodEvent(username,
                date,
                emotionalState,
                null,
                socialSituation,
                null,
                null);

        MoodEvent moodEvent6 = new MoodEvent(username,
                date,
                emotionalState,
                trigger,
                null,
                null,
                null);

        MoodEvent moodEvent7 = new MoodEvent(username,
                date,
                emotionalState,
                null,
                null,
                null,
                null);

        // Test empty getters and setters.
        assertEquals(moodEvent1.getEmotionalState(), emotionalState);
        assertEquals(moodEvent1.getOriginalPoster(), username);
        assertEquals(moodEvent1.getSocialSituation(), null);
        assertEquals(moodEvent1.getLocation(), location);
        assertEquals(moodEvent1.getDate(), date);
        assertEquals(moodEvent1.getTrigger(), null);

        assertEquals(moodEvent2.getEmotionalState(), emotionalState);
        assertEquals(moodEvent2.getOriginalPoster(), username);
        assertEquals(moodEvent2.getSocialSituation(), socialSituation);
        assertEquals(moodEvent2.getLocation(), location);
        assertEquals(moodEvent2.getDate(), date);
        assertEquals(moodEvent2.getTrigger(), null);

        assertEquals(moodEvent3.getEmotionalState(), emotionalState);
        assertEquals(moodEvent3.getOriginalPoster(), username);
        assertEquals(moodEvent3.getSocialSituation(), null);
        assertEquals(moodEvent3.getLocation(), location);
        assertEquals(moodEvent3.getDate(), date);
        assertEquals(moodEvent3.getTrigger(), trigger);

        assertEquals(moodEvent4.getEmotionalState(), emotionalState);
        assertEquals(moodEvent4.getOriginalPoster(), username);
        assertEquals(moodEvent4.getSocialSituation(), socialSituation);
        assertEquals(moodEvent4.getLocation(), null);
        assertEquals(moodEvent4.getDate(), date);
        assertEquals(moodEvent4.getTrigger(), trigger);

        assertEquals(moodEvent5.getEmotionalState(), emotionalState);
        assertEquals(moodEvent5.getOriginalPoster(), username);
        assertEquals(moodEvent5.getSocialSituation(), socialSituation);
        assertEquals(moodEvent5.getLocation(), null);
        assertEquals(moodEvent5.getDate(), date);
        assertEquals(moodEvent5.getTrigger(), null);

        assertEquals(moodEvent6.getEmotionalState(), emotionalState);
        assertEquals(moodEvent6.getOriginalPoster(), username);
        assertEquals(moodEvent6.getSocialSituation(), null);
        assertEquals(moodEvent6.getLocation(), null);
        assertEquals(moodEvent6.getDate(), date);
        assertEquals(moodEvent6.getTrigger(), trigger);

        assertEquals(moodEvent7.getEmotionalState(), emotionalState);
        assertEquals(moodEvent7.getOriginalPoster(), username);
        assertEquals(moodEvent7.getSocialSituation(), null);
        assertEquals(moodEvent7.getLocation(), null);
        assertEquals(moodEvent7.getDate(), date);
        assertEquals(moodEvent7.getTrigger(), null);
    }

    /** Test the editing methods of MoodEvent. */
    @Test
    public void testEditingMoodEvent(){

        String username = "username";
        String trigger1 = "trigger";
        String trigger2 = "trigger2";
        Date date = new Date();
        EmotionalState emotionalState1 =
                new EmotionalStateFactory().createEmotionalStateByName("Happiness");
        EmotionalState emotionalState2 =
                new EmotionalStateFactory().createEmotionalStateByName("Sadness");
        SocialSituation socialSituation1 =
                new SocialSituationFactory().createSocialSituationByName("Alone");
        SocialSituation socialSituation2 =
                new SocialSituationFactory().createSocialSituationByName("With A Crowd");
        Location location = null;

        // Create the mood events.
        MoodEvent moodEvent1 =
                new MoodEvent(username, date, emotionalState1, trigger1, socialSituation1, location, null);
        MoodEvent moodEvent2 =
                new MoodEvent(username, date, emotionalState2, trigger2, socialSituation2, location, null);

        // Check they are initially different.
        assertNotEquals(moodEvent1, moodEvent2);
        assertNotEquals(moodEvent1.getSocialSituation(), moodEvent2.getSocialSituation());
        assertNotEquals(moodEvent1.getTrigger(), moodEvent2.getTrigger());
        assertNotEquals(moodEvent1.getEmotionalState(), moodEvent2.getEmotionalState());

        // Edit the first mood event.
        moodEvent1.editMoodEvent(emotionalState2, trigger2, socialSituation2, null);

        // Now they should be equal.
        assertEquals(moodEvent1, moodEvent2);
        assertEquals(moodEvent1.getSocialSituation(), moodEvent2.getSocialSituation());
        assertEquals(moodEvent1.getTrigger(), moodEvent2.getTrigger());
        assertEquals(moodEvent1.getEmotionalState(), moodEvent2.getEmotionalState());

        // Edit the second mood event.
        moodEvent1.editMoodEvent(emotionalState1, null, null, null);

        // Check they are different now.
        assertNotEquals(moodEvent1, moodEvent2);
        assertNotEquals(moodEvent1.getSocialSituation(), moodEvent2.getSocialSituation());
        assertNotEquals(moodEvent1.getTrigger(), moodEvent2.getTrigger());
        assertNotEquals(moodEvent1.getEmotionalState(), moodEvent2.getEmotionalState());
    }
}
