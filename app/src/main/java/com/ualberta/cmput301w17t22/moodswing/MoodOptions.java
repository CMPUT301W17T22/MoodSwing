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
    private EmotionalState emotionalStates[] = new EmotionalState[8];
    private SocialSituation socialSituations[] = new SocialSituation[3];

    public MoodOptions(){   // array of emotional states
        // location = emotional state (emotion, imagename, color)
        emotionalStates[0] = new EmotionalState("anger");
        emotionalStates[1] = new EmotionalState("confusion");
        emotionalStates[2] = new EmotionalState("disgust");
        emotionalStates[3] = new EmotionalState("fear");
        emotionalStates[4] = new EmotionalState("happiness");
        emotionalStates[5] = new EmotionalState("sadness");
        emotionalStates[6] = new EmotionalState("shame");
        emotionalStates[7] = new EmotionalState("surprise");

        socialSituations[0] = new SocialSituation("alone");
        socialSituations[1] = new SocialSituation("crowd");
        socialSituations[2] = new SocialSituation("party");
    }

    public EmotionalState getEmotionalState(int index){
        return emotionalStates[index];

    }

    public SocialSituation getSocialSituation(int index){
        return socialSituations[index];
    }
}
