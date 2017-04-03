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
     * Constructor. Initializes the social situation's description, drawable id, and visibility.
     * @param description
     */
    public SocialSituation(String description, int drawableId, int visibility){
        this.description = description;
        this.drawableId = drawableId;
        this.visibility = visibility;
    }

    /**
     * The description of the SocialSituation, can be one of
     * Alone, Crowd, or Party.
     */
    private String description;

    /**
     * The drawable id of the emoticon for the social situation.
     */
    private int drawableId;

    /**
     * Whether or not the drawable for the social situation is drawable or not.
     */
    private int visibility;

    /** Android Studio generated equals function. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialSituation that = (SocialSituation) o;

        if (drawableId != that.drawableId) return false;
        if (visibility != that.visibility) return false;
        return description.equals(that.description);

    }
    /** Android Studio generated hashCode function. */
    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + drawableId;
        result = 31 * result + visibility;
        return result;
    }

    // --- START: Getters and Setters

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getDrawableId() { return drawableId; }


    public int getVisibility() { return visibility; }

    // -- END: Getters and Setters

    public String toString() { return this.getDescription(); }
}
