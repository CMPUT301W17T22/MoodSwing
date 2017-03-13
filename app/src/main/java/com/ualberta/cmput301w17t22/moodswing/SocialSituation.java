package com.ualberta.cmput301w17t22.moodswing;

/**
 * A SocialSituation is a user selected descriptor of the social situation they were in
 * when a mood event happened.
 * <p/>
 * Contains the information associated with a pre-defined social situation.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 */

public class SocialSituation {
    /**
     * The description of the SocialSituation, can be one of
     * Alone, Crowd, or Party.
     */
    private String description;

    public SocialSituation(String description){
        this.description = description;
    }

    public String getDescription() { return description; }

    public String toString() { return this.getDescription(); }

    /** Android Studio generated equals function */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialSituation that = (SocialSituation) o;

        return description.equals(that.description);

    }

    /** Android Studio generated hashCode function */
    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
