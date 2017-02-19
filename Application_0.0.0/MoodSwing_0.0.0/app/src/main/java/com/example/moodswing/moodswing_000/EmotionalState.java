package com.example.moodswing.moodswing_000;

import android.graphics.Color;
import android.media.Image;

/**
 * Created by Fred on 2017-02-18.
 */

public class EmotionalState {
    private String description;
    private String emoticonLocation;
    private int color;

    public EmotionalState(String description, String emoticon, int color){
        this.description = description;
        this.emoticonLocation = emoticon;
        this.color = color;
    }

    public String getDescription(){
        return description;
    }

    public String getEmoticon(){
        return emoticonLocation;
    }

    public int getColor(){
        return color;
    }
}
