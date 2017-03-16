package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.drawable.Drawable;

/**
 * An EmotionalState is the user entered emotional state of a MoodEvent.
 * <p/>
 * Contains the information associated with a pre-defined emotional state.
 * For now, this is only a description of the MoodEvent, we may expand this later.
 * This and EmotionalStateFactory should be refactored.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 * @see MoodEvent
 */

// TODO: Refactor this and EmotionalStateFactory to instead be a factory method and include
// image references.

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

    public String getDescription(){
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public int getDrawableId() { return drawableId; }
    public void setDrawableId(int drawableId) { this.drawableId = drawableId; }

    public String toString() { return this.getDescription(); }

    // --- END: Getters and Setters
}
