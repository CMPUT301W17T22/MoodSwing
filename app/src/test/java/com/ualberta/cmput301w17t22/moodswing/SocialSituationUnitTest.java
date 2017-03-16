package com.ualberta.cmput301w17t22.moodswing;

import android.view.View;

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
        String ss1 = "Alone";
        String ss2 = "With One Other Person";
        String ss3 = "With Two To Several People";
        String ss4 = "With A Crowd";
        String ss5 = "Alone";
        String ss6 = "";

        // Varying drawables.
        int id1 = R.drawable.social_situation_alone;
        int id2 = R.drawable.social_situation_with_a_crowd;
        int id3 = R.drawable.social_situation_with_one_other_person;
        int id4 = R.drawable.social_situation_with_two_to_several_people;

        int visibility = View.VISIBLE;

        // Create the social situation objects. Visiblity doesn't matter so they all have the same.
        SocialSituation socialSituation1 = new SocialSituation(ss1, id1, visibility);
        SocialSituation socialSituation2 = new SocialSituation(ss2, id2, visibility);
        SocialSituation socialSituation3 = new SocialSituation(ss3, id3, visibility);
        SocialSituation socialSituation4 = new SocialSituation(ss4, id4, visibility);
        SocialSituation socialSituation5 = new SocialSituation(ss5, id1, visibility);
        SocialSituation socialSituation6 = new SocialSituation(ss6, id1, visibility);


        // Test that the string is actually the description of the social situation.
        assertEquals(socialSituation1.getDescription(), ss1);
        assertEquals(socialSituation1.getDescription(), ss5);
        assertNotEquals(socialSituation1.getDescription(), ss3);

        // Test SocialSituation equality.
        assertEquals(socialSituation1, socialSituation5);
        assertNotEquals(socialSituation1, socialSituation3);

        // Change the description.
        socialSituation1.setDescription(ss3);

        // Test the correctness of the string again.
        assertEquals(socialSituation1.getDescription(), ss3);
        assertNotEquals(socialSituation1.getDescription(), ss1);
        assertNotEquals(socialSituation1.getDescription(), ss5);

        // Test the toString conversion.
        assertEquals(socialSituation1.toString(), ss3);

        // Change the description again.
        socialSituation1.setDescription(ss5);

        // Test toString when the strings are not the same.
        assertFalse(socialSituation1.toString().equals(ss3));

        // Empty string test.
        // Check it is not equal to any of the descriptions.
        assertNotEquals(socialSituation6.getDescription(), ss1);
        assertNotEquals(socialSituation6.getDescription(), ss2);
        assertNotEquals(socialSituation6.getDescription(), ss3);
        assertNotEquals(socialSituation6.getDescription(), ss4);
        assertNotEquals(socialSituation6.getDescription(), ss5);

        // Check the empty case is not equal to any other social situation.
        assertNotEquals(socialSituation6, socialSituation1);
        assertNotEquals(socialSituation6, socialSituation2);
        assertNotEquals(socialSituation6, socialSituation3);
        assertNotEquals(socialSituation6, socialSituation4);
        assertNotEquals(socialSituation6, socialSituation5);
    }

    /** Test of social situation initialization and methods with factory class. */
    @Test
    public void testSocialSituationFactory() {

        // Varying descriptions
        String ss1 = "Alone";
        String ss2 = "With One Other Person";
        String ss3 = "With Two To Several People";
        String ss4 = "With A Crowd";
        String ss5 = "Alone";
        String ss6 = "";

        // Initialize the social situation factory.
        SocialSituationFactory socialSituationFactory = new SocialSituationFactory();

        // Create the social situations.
        SocialSituation socialSituation1 =
                socialSituationFactory.createSocialSituationByName(ss1);
        SocialSituation socialSituation2 =
                socialSituationFactory.createSocialSituationByName(ss2);
        SocialSituation socialSituation3 =
                socialSituationFactory.createSocialSituationByName(ss3);
        SocialSituation socialSituation4 =
                socialSituationFactory.createSocialSituationByName(ss4);
        SocialSituation socialSituation5 =
                socialSituationFactory.createSocialSituationByName(ss5);
        SocialSituation socialSituation6 =
                socialSituationFactory.createSocialSituationByName(ss6);

        // Test of EmotionalState equals method.
        assertEquals(socialSituation1, socialSituation5);
        assertNotEquals(socialSituation1, socialSituation2);
        assertNotEquals(socialSituation1, socialSituation3);
        assertNotEquals(socialSituation1, socialSituation4);
        assertNotEquals(socialSituation1, socialSituation6);

        // Test descriptions equalling strings.
        assertEquals(socialSituation1.getDescription(), ss1);
        assertEquals(socialSituation5.getDescription(), ss5);
        assertEquals(socialSituation3.getDescription(), ss3);
        assertNotEquals(socialSituation3.getDescription(), ss1);
        assertNotEquals(socialSituation3.getDescription(), ss5);
    }
}
