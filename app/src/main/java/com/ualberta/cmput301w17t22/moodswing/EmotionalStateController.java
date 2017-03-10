package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/**
 * Controller for initializing and dealing with EmotionalStates.
 * This Controller currently does not interact with the Model, but just has a function
 * for creating EmotionalStates by name, which is useful to the View.
 *
 * Created by nyitrai on 3/10/2017.
 */

public class EmotionalStateController implements MSController {

    MoodSwing ms = null;
    public EmotionalStateController(MoodSwing ms) { this.ms = ms; }

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
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            case "Fear":
                color = 4;
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            case "Happiness":
                color = 5;
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            case "Sadness":
                color = 6;
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            case "Shame":
                color = 7;
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            case "Surprise":
                color = 8;
                emotionalState = new EmotionalState("Anger", "AngerImageLocation", color);
                break;

            default:
                Log.i("ERROR", "Invalid name for Emotional State: " + emotionalStateName);
                throw new IllegalArgumentException(
                        "Invalid name for Emotional State: " + emotionalStateName);

        }
        return emotionalState;
    }

}
