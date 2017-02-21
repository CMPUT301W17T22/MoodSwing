package com.example.moodswing.moodswing_000;

import android.graphics.Color;

/**
 * Created by Fred on 2017-02-18.
 */

//This will likely end up being implemented in a sub-class or inside a controller.
//This is just a shell class. I wanted to write this code down
// but the code will end up implemented elsewhere.
public class MoodOptions {
    private EmotionalState emotionalStates[] = new EmotionalState[8];
    private SocialSituation socialSituations[] = new SocialSituation[3];

    public MoodOptions(){   // array of emotional states
        // location = emotional state (emotion, imagename, color)
        emotionalStates[0] = new EmotionalState("anger", "somewhere", Color.BLACK);
        emotionalStates[1] = new EmotionalState("confusion", "somewhere", Color.BLUE);
        emotionalStates[2] = new EmotionalState("disgust", "somewhere", Color.CYAN);
        emotionalStates[3] = new EmotionalState("fear", "somewhere", Color.GRAY);
        emotionalStates[4] = new EmotionalState("happiness", "emoticonhappy", Color.GREEN);
        emotionalStates[5] = new EmotionalState("sadness", "emoticonsad", Color.MAGENTA);
        emotionalStates[6] = new EmotionalState("shame", "somewhere", Color.RED);
        emotionalStates[7] = new EmotionalState("surprise", "emoticonsurprised", Color.YELLOW);

        socialSituations[0] = new SocialSituation("alone", "socialsituationalone");
        socialSituations[1] = new SocialSituation("crowd", "socialsituationcrowd");
        socialSituations[2] = new SocialSituation("party", "socialsituationparty");
    }
}
