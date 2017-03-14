package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

/**
 * Factory class for construction EmotionalStates based on their description "Angry" "Sadness", etc.
 * Initializes the class to have an appropriate description and drawable id.
 * <p/>
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
                emotionalState = new EmotionalState("Anger", R.drawable.emoticon_anger);
                break;

            case "Confusion":
                emotionalState = new EmotionalState("Confusion", R.drawable.emoticon_confusion);
                break;

            case "Disgust":
                emotionalState = new EmotionalState("Disgust", R.drawable.emoticon_disgust);
                break;

            case "Fear":
                emotionalState = new EmotionalState("Fear", R.drawable.emoticon_fear);
                break;

            case "Happiness":
                emotionalState = new EmotionalState("Happiness", R.drawable.emoticon_happiness);
                break;

            case "Sadness":
                emotionalState = new EmotionalState("Sadness", R.drawable.emoticon_sadness);
                break;

            case "Shame":
                emotionalState = new EmotionalState("Shame", R.drawable.emoticon_shame);
                break;

            case "Surprise":
                emotionalState = new EmotionalState("Surprise", R.drawable.emoticon_surprise);
                break;

            default:
                Log.i("ERROR", "Invalid name for Emotional State: " + emotionalStateName);
                throw new IllegalArgumentException(
                        "Invalid name for Emotional State: " + emotionalStateName);

        }
        return emotionalState;
    }

}
