package com.example.moodswing.moodswing_000;

/**
 * Created by Fred on 2017-02-18.
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
