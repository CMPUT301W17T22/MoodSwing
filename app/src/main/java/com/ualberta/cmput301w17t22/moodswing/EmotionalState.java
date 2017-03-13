package com.ualberta.cmput301w17t22.moodswing;

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
     * Constructor. Initializes the Emotional State's description.
     * @param description
     */
    public EmotionalState(String description){
        this.description = description;
    }

    /**
     * Description of the EmotionalState.
     * This can be one of Anger, Confusion, Disgust, Fear, Happiness, Sadness, Shame, or Surprise.
     */
    private String description;

    /**
     * Android studio generated equals function.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmotionalState that = (EmotionalState) o;

        if (!description.equals(that.description)) return false;
        else return true;
    }

    /**
     * Android studio generated hashCode function.
     * @return
     */
    @Override
    public int hashCode() {
        return description.hashCode();
    }

    // --- START: Getters and Setters

    public String getDescription(){
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public String toString() { return this.getDescription(); }

    // --- END: Getters and Setters
}
