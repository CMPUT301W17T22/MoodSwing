package com.ualberta.cmput301w17t22.moodswing;

/**
 * Contains the information associated with a pre-defined emotional state.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 */

public class EmotionalState {
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmotionalState that = (EmotionalState) o;

        if (!description.equals(that.description)) return false;
        else return true;
    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        return result;
    }

    public EmotionalState(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description) { this.description = description; }

    public String toString() { return this.getDescription(); }
}
