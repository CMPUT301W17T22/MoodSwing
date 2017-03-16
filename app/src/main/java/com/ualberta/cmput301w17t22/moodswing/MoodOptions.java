package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.Color;

/**
 * DO NOT USE
 *
 * This will likely end up being implemented in a sub-class or inside a controller.
 * This is just a shell class. I wanted to write this code down
 * but the code will end up implemented elsewhere.
 *
 * @author Fred
 * @version 2017-02-18
 * @deprecated
 */
public class MoodOptions {
    private EmotionalStateFactory emotionalStateFactory = new EmotionalStateFactory();
    private SocialSituationFactory socialSituationFactory = new SocialSituationFactory();
    private EmotionalState emotionalStates[] = new EmotionalState[8];
    private SocialSituation socialSituations[] = new SocialSituation[4];

    public MoodOptions(){   // array of emotional states
        emotionalStates[0] = emotionalStateFactory.createEmotionalStateByName("Anger");
        emotionalStates[1] = emotionalStateFactory.createEmotionalStateByName("Confusion");
        emotionalStates[2] = emotionalStateFactory.createEmotionalStateByName("Disgust");
        emotionalStates[3] = emotionalStateFactory.createEmotionalStateByName("Fear");
        emotionalStates[4] = emotionalStateFactory.createEmotionalStateByName("Happiness");
        emotionalStates[5] = emotionalStateFactory.createEmotionalStateByName("Sadness");
        emotionalStates[6] = emotionalStateFactory.createEmotionalStateByName("Shame");
        emotionalStates[7] = emotionalStateFactory.createEmotionalStateByName("Surprise");

        socialSituations[0] = socialSituationFactory.createSocialSituationByName("Alone");
        socialSituations[1] = socialSituationFactory.createSocialSituationByName("With One Other Person");
        socialSituations[2] = socialSituationFactory.createSocialSituationByName("With Two To Several People");
        socialSituations[3] = socialSituationFactory.createSocialSituationByName("With A Crowd");
    }

    public EmotionalState getEmotionalState(int index){
        return emotionalStates[index];

    }

    public SocialSituation getSocialSituation(int index){
        return socialSituations[index];
    }
}
