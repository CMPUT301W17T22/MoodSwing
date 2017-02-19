package com.example.moodswing.moodswing_000;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by Fred on 2017-02-18.
 */

//This will likely end up being implemented in a sub-class or inside a controller.
public class MoodOptions {
    private EmotionalState emotionalStates[] = new EmotionalState[5];
    private SocialSituation socialSituations[] = new SocialSituation[3];

    public MoodOptions(){
        emotionalStates[0] = new EmotionalState("happy", "somewhere", Color.BLACK);
        emotionalStates[2] = new EmotionalState("sad", "somewhere", Color.BLACK);
        emotionalStates[3] = new EmotionalState("happy", "somewhere", Color.BLACK);
        emotionalStates[4] = new EmotionalState("happy", "somewhere", Color.BLACK);
        emotionalStates[5] = new EmotionalState("happy", "somewhere", Color.BLACK);

        socialSituations[0] = new SocialSituation("alone", "somewhere");
        socialSituations[1] = new SocialSituation("crowd", "somewhere");
        socialSituations[2] = new SocialSituation("party", "somewhere");
    }
}
