package com.example.moodswing.moodswing_000;

/**
 * Contains the information associated with a pre-defined social situation.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 */

public class SocialSituation {
    private String description;
    private String iconName;

    public SocialSituation(String description, String iconName){
        this.description = description;
        this.iconName = iconName;
    }

    public String getDescription() {

        return description;
    }

    public String getIconName() {

        return iconName;
    }
}
