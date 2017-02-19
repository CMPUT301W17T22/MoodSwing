package com.example.moodswing.moodswing_000;

import android.media.Image;

/**
 * Created by Fred on 2017-02-18.
 */

public class SocialSituation {
    private String description;
    private Image icon;

    public SocialSituation(String description, Image icon){
        this.description = description;
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public Image getIcon() {
        return icon;
    }
}
