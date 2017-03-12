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

        switch (emotionalStateName) {
            case "Anger":
                emotionalState = new EmotionalState("Anger");
                break;

            case "Confusion":
                emotionalState = new EmotionalState("Confusion");
                break;

            case "Disgust":
                emotionalState = new EmotionalState("Disgust");
                break;

            case "Fear":
                emotionalState = new EmotionalState("Fear");
                break;

            case "Happiness":
                emotionalState = new EmotionalState("Happiness");
                break;

            case "Sadness":
                emotionalState = new EmotionalState("Sadness");
                break;

            case "Shame":
                emotionalState = new EmotionalState("Shame");
                break;

            case "Surprise":
                emotionalState = new EmotionalState("Surprise");
                break;

            default:
                Log.i("ERROR", "Invalid name for Emotional State: " + emotionalStateName);
                throw new IllegalArgumentException(
                        "Invalid name for Emotional State: " + emotionalStateName);

        }
        return emotionalState;
    }

}
