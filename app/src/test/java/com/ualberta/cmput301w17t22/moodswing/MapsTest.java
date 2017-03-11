package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

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
        double latitude = -34;
        double longitude = 151;
        LatLng location = new LatLng(latitude, longitude); //Sydney
        MoodEvent moodEvent = new MoodEvent("username1",emotionalState, trigger, null, null, null, location);

        LatLng moodEventLocation = moodEvent.getLocation();
        assertEquals(moodEventLocation.latitude, latitude);
        assertEquals(moodEventLocation.longitude, longitude);
    }
}
