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
        emotionalStates[0] = new EmotionalState("anger", "somewhere", Color.BLACK);
        emotionalStates[1] = new EmotionalState("confusion", "somewhere", Color.BLUE);
        emotionalStates[2] = new EmotionalState("disgust", "somewhere", Color.CYAN);
        emotionalStates[3] = new EmotionalState("fear", "somewhere", Color.GRAY);
        emotionalStates[4] = new EmotionalState("happiness", "emoticon_happy", Color.GREEN);
        emotionalStates[5] = new EmotionalState("sadness", "emoticon_sad", Color.MAGENTA);
        emotionalStates[6] = new EmotionalState("shame", "somewhere", Color.RED);
        emotionalStates[7] = new EmotionalState("surprise", "emoticon_surprised", Color.YELLOW);

        socialSituations[0] = new SocialSituation("alone", "social_situation_alone");
        socialSituations[1] = new SocialSituation("crowd", "social_situation_crowd");
        socialSituations[2] = new SocialSituation("party", "social_situation_party");
    }

    public EmotionalState getEmotionalState(int index){
        return emotionalStates[index];

    }

    public SocialSituation getSocialSituation(int index){
        return socialSituations[index];
    }
}
