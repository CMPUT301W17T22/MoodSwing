package com.ualberta.cmput301w17t22.moodswing;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test of the SocialSituation class.
 *
 * Created by nyitrai on 3/13/2017.
 */

public class SocialSituationUnitTest {

    /** Test of social situation initialization and methods without factory class. */
    @Test
    public void testSocialSituation() {

        // Varying descriptions
        String alone = "Alone";
        String crowd = "Crowd";
        String alone_also = "Alone";

        // Create the social situation object,
        SocialSituation socialSituation = new SocialSituation(alone);

        // Test that the string is actually the description of the social situation.
        assertEquals(socialSituation.getDescription(), alone);
        assertEquals(socialSituation.getDescription(), alone_also);
        assertNotEquals(socialSituation.getDescription(), crowd);

        // Change the description.
        socialSituation.setDescription(crowd);

        // Test the correctness of the string again.
        assertEquals(socialSituation.getDescription(), crowd);
        assertNotEquals(socialSituation.getDescription(), alone);
        assertNotEquals(socialSituation.getDescription(), alone_also);

        // Test the toString conversion.
        assertEquals(socialSituation.toString(), crowd);

        // Change the description again.
        socialSituation.setDescription(alone_also);

        // Test toString when the strings are not the same.
        assertFalse(socialSituation.toString().equals(crowd));
    }

    /** Test of social situation initialization and methods with factory class. */
    @Test
    public void testSocialSituationFactory() {

        // Varying descriptions
        String alone = "Alone";
        String crowd = "Crowd";
        String alone_also = "Alone";

        // Initialize the social situation factory.
        SocialSituationFactory socialSituationFactory = new SocialSituationFactory();

        // Create the social situations.
        SocialSituation socialSituationAlone =
                socialSituationFactory.createSocialSituationByName(alone);
        SocialSituation socialSituationCrowd =
                socialSituationFactory.createSocialSituationByName(crowd);
        SocialSituation socialSituationAloneAlso =
                socialSituationFactory.createSocialSituationByName(alone_also);

        // Test of EmotionalState equals method.
        assertEquals(socialSituationAlone, socialSituationAloneAlso);
        assertNotEquals(socialSituationAlone, socialSituationCrowd);

        // Test descriptions equalling strings.
        assertEquals(socialSituationAlone.getDescription(), alone);
        assertEquals(socialSituationAloneAlso.getDescription(), alone_also);
        assertEquals(socialSituationCrowd.getDescription(), crowd);

        assertNotEquals(socialSituationCrowd.getDescription(), alone);
        assertNotEquals(socialSituationCrowd.getDescription(), alone_also);
    }
}
