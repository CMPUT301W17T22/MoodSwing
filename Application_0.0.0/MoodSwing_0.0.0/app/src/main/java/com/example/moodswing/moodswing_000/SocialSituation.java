package com.example.moodswing.moodswing_000;

import android.media.Image;

/**
 * Created by Fred on 2017-02-18.
 */

public class SocialSituation {
    private String description;
    private String iconLocation;

    public SocialSituation(String description, String icon){
        this.description = description;
        this.iconLocation = icon;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return iconLocation;
    }
}
