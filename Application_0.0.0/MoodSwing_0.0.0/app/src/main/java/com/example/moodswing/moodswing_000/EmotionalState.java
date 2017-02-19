package com.example.moodswing.moodswing_000;

import android.graphics.Color;
import android.media.Image;

/**
 * Created by Fred on 2017-02-18.
 */

public class EmotionalState {
    private String description;
    private Image emoticon;
    private Color color;

    public EmotionalState(String description, Image emoticon, Color color){
        this.description = description;
        this. emoticon = emoticon;
        this.color = color;
    }

    public String getDescription(){
        return description;
    }

    public Image getEmoticon(){
        return emoticon;
    }

    public Color getColor(){
        return color;
    }
}
