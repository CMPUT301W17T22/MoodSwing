package com.ualberta.cmput301w17t22.moodswing;

/**
 * Contains the information associated with a pre-defined social situation.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 */

public class SocialSituation {
    private String description;

    public SocialSituation(String description){
        this.description = description;
    }

    public String getDescription() { return description; }

    public String toString() { return this.getDescription(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialSituation that = (SocialSituation) o;

        return description.equals(that.description);

    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
