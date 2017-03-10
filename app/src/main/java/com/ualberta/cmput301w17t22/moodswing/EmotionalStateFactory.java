package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/**
 * Factory class for construction EmotionalStates based on their description "Angry" "Sadness", etc.
 *
 * Created by nyitrai on 3/10/2017.
 */

public class EmotionalStateFactory {

    /**
     * Constructs an EmotionalState from the name of the state. i.e. "Anger", "Sadness", etc.
     * @param emotionalStateName The name of the emotional state.
     * @return The constructed emotional state.
     */
    public EmotionalState createEmotionalStateByName(String emotionalStateName) {
        EmotionalState emotionalState;
        int color;

        switch (emotionalStateName) {
            case "Anger":
                color = 1;
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            case "Confusion":
                color = 2;
                emotionalState = new EmotionalState("Confusion", "ConfusionImageLocation", color);
                break;

            case "Disgust":
                color = 3;
                emotionalState = new EmotionalState("Disgust", "AngerImageLocation", color);
                break;

            case "Fear":
                color = 4;
                emotionalState = new EmotionalState("Fear", "AngerImageLocation", color);
                break;

            case "Happiness":
                color = 5;
                emotionalState = new EmotionalState("Happiness", "AngerImageLocation", color);
                break;

            case "Sadness":
                color = 6;
                emotionalState = new EmotionalState("Sadness", "AngerImageLocation", color);
                break;

            case "Shame":
                color = 7;
                emotionalState = new EmotionalState("Shame", "AngerImageLocation", color);
                break;

            case "Surprise":
                color = 8;
                emotionalState = new EmotionalState("Surprise", "AngerImageLocation", color);
                break;

            default:
                Log.i("ERROR", "Invalid name for Emotional State: " + emotionalStateName);
                throw new IllegalArgumentException(
                        "Invalid name for Emotional State: " + emotionalStateName);

        }
        return emotionalState;
    }

}
