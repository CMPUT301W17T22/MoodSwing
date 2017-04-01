package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;
import android.view.View;

/**
 * Factory class to create Social Situations based on their type "Alone", "Crowd", or "Party".
 *
 * Created by nyitrai on 3/10/2017.
 */



public class SocialSituationFactory {

    public SocialSituation createSocialSituationByName(String socialSituationName) {
        SocialSituation socialSituation;

        switch (socialSituationName) {
            case "Alone":
                socialSituation = new SocialSituation("Alone",
                        R.drawable.social_situation_alone,
                        View.VISIBLE);
                break;

            case "With One Other Person":
                //TODO: This icon is currently the same as the alone icon. We need a new icon.
                socialSituation = new SocialSituation("With One Other Person",
                        R.drawable.social_situation_with_one_other_person,
                        View.VISIBLE);
                break;

            case "With Two To Several People":
                socialSituation = new SocialSituation("With Two To Several People",
                        R.drawable.social_situation_with_two_to_several_people,
                        View.VISIBLE);
                break;

            case "With A Crowd":
                socialSituation = new SocialSituation("With A Crowd",
                        R.drawable.social_situation_with_a_crowd,
                        View.VISIBLE);
                break;

            case "":
                socialSituation = new SocialSituation("",
                        R.drawable.social_situation_alone,
                        View.INVISIBLE);
                break;

            default:
                Log.i("ERROR", "Invalid name for Social Situation: " + socialSituationName);
                throw new IllegalArgumentException(
                        "Invalid name for Social Situation: " + socialSituationName);
        }
        return socialSituation;
    }

}
