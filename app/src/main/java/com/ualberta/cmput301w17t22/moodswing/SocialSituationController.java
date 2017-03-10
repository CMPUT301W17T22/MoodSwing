package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/**
 * Controller for initializing and dealing with SocialSituations.
 * This Controller currently does not interact with the Model, but just has a function
 * for creating SocialSituations by name, which is useful to the View.
 *
 * Created by nyitrai on 3/10/2017.
 */

public class SocialSituationController implements MSController {

    MoodSwing ms = null;
    public SocialSituationController(MoodSwing ms) { this.ms = ms; }

    public SocialSituation createSocialSituationByName(String socialSituationName) {
        SocialSituation socialSituation;

        switch (socialSituationName) {
            case "Alone":
                socialSituation = new SocialSituation("Alone", "AloneIconName");
                break;

            case "Crowd":
                socialSituation = new SocialSituation("Alone", "AloneIconName");
                break;

            case "Party":
                socialSituation = new SocialSituation("Alone", "AloneIconName");
                break;

            default:
                Log.i("ERROR", "Invalid name for Social Situation: " + socialSituationName);
                throw new IllegalArgumentException(
                        "Invalid name for Social Situation: " + socialSituationName);
        }
        return socialSituation;
    }

}
