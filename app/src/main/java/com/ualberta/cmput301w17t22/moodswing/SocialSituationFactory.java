package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/**
 * Factory class to create Social Situations based on their type "Alone", "Crowd", or "Party".
 *
 * Created by nyitrai on 3/10/2017.
 */

// TODO: Refactor SocialSituation and this to instead be a factory method and include
// image references.

public class SocialSituationFactory {

    public SocialSituation createSocialSituationByName(String socialSituationName) {
        SocialSituation socialSituation;

        switch (socialSituationName) {
            case "Alone":
                socialSituation = new SocialSituation("Alone");
                break;

            case "Crowd":
                socialSituation = new SocialSituation("Crowd");
                break;

            case "Party":
                socialSituation = new SocialSituation("Party");
                break;

            case "":
                socialSituation = new SocialSituation("");
                break;

            default:
                Log.i("ERROR", "Invalid name for Social Situation: " + socialSituationName);
                throw new IllegalArgumentException(
                        "Invalid name for Social Situation: " + socialSituationName);
        }
        return socialSituation;
    }

}
