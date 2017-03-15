package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test of The EmotionalState Class that is set by a user to a Mood Event to dictate the emotion
 * to be tagged with that Mood Event. Also tests the EmotionalStateFactory class.
 * Created by Fred on 2017-02-18.
 */

public class EmotionalStateUnitTest {
    /** Test of emotional state initialization and methods without factory class. */
    @Test
    public void testEmotionalState(){

        // Varying descriptions
        String happiness = "Happiness";
        String sadness = "Sadness";
        String happiness_also = "Happiness";

        // Create the emotionalState object.
        EmotionalState emotionalState = new EmotionalState(happiness, R.drawable.emoticon_happiness);

        // Test that the string is actually the description of the emotional state.
        assertEquals(emotionalState.getDescription(), happiness);
        assertEquals(emotionalState.getDescription(), happiness_also);
        assertNotEquals(emotionalState.getDescription(), sadness);

        // Change the description.
        emotionalState.setDescription(sadness);

        // Test the correctness of the string again.
        assertEquals(emotionalState.getDescription(), sadness);
        assertNotEquals(emotionalState.getDescription(), happiness);
        assertNotEquals(emotionalState.getDescription(), happiness_also);

        // Test the toString conversion.
        assertEquals(emotionalState.toString(), sadness);

        // Change the description again.
        emotionalState.setDescription(happiness);

        // Test toString when the strings are not the same.
        assertFalse(emotionalState.toString().equals(sadness));
    }

    /** Test of emotional state initialization and methods with factory class. */
    @Test
    public void testEmotionalStateFactory() {

        // Varying descriptions.
        String happiness = "Happiness";
        String sadness = "Sadness";
        String happiness_also = "Happiness";

        // Initialize the emotional state factory.
        EmotionalStateFactory emotionalStateFactory = new EmotionalStateFactory();

        // Create the emotional states.
        EmotionalState emotionalStateHappiness =
                emotionalStateFactory.createEmotionalStateByName(happiness);
        EmotionalState emotionalStateSadness =
                emotionalStateFactory.createEmotionalStateByName(sadness);
        EmotionalState emotionalStateHappinessAlso =
                emotionalStateFactory.createEmotionalStateByName(happiness_also);

        // Test of EmotionalState equals method.
        assertEquals(emotionalStateHappiness, emotionalStateHappinessAlso);
        assertNotEquals(emotionalStateHappiness, emotionalStateSadness);

        // Test descriptions equalling strings.
        assertEquals(emotionalStateHappiness.getDescription(), happiness);
        assertEquals(emotionalStateHappinessAlso.getDescription(), happiness_also);
        assertEquals(emotionalStateSadness.getDescription(), sadness);

        assertNotEquals(emotionalStateSadness.getDescription(), happiness_also);
        assertNotEquals(emotionalStateSadness.getDescription(), happiness);
    }
}
