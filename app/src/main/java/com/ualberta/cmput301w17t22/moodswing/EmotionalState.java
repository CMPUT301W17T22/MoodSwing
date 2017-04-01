package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.drawable.Drawable;

/**
 * An EmotionalState is the user entered emotional state of a MoodEvent.
 * <p/>
 * Contains the information associated with a pre-defined emotional state.
 * This includes its description which is the name of the emotion and
 * the drawable id for the attached emoticon.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 * @see MoodEvent
 */


public class EmotionalState {

    /**
     * Constructor. Initializes the Emotional State's description and drawable id.
     * @param description
     */
    public EmotionalState(String description, int drawableId){
        this.description = description;
        this.drawableId = drawableId;
    }

    /**
     * Description of the EmotionalState.
     * This can be one of Anger, Confusion, Disgust, Fear, Happiness, Sadness, Shame, or Surprise.
     */
    private String description;

    /**
     * Drawable id for emoticon relevant to the emotional state.
     */
    private int drawableId;

    /** Android Studio generated equals function. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmotionalState that = (EmotionalState) o;

        if (drawableId != that.drawableId) return false;
        return description.equals(that.description);

    }
    /** Android Studio generated hashCode function. */
    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + drawableId;
        return result;
    }

    // --- START: Getters and Setters

    /**Gets the description (name) of the emotional state.*/
    public String getDescription(){
        return description;
    }

    /**Sets the description to a new emotional state description
     * @param description */
    public void setDescription(String description) { this.description = description; }

    /**Returns the drawable id for the attached emoticon */
    public int getDrawableId() { return drawableId; }

    /**Sets the drawable id for the attached emoticon
     * @param drawableId */
    public void setDrawableId(int drawableId) { this.drawableId = drawableId; }


    // --- END: Getters and Setters
}
