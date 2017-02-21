package com.example.moodswing.moodswing_000;

/**
 * Created by Fred on 2017-02-18.
 */

public class EmotionalState {
    private String description;
    private String emoticonName;
    private int color;

    public EmotionalState(String description, String emoticonName, int color){
        this.description = description;
        this.emoticonName = emoticonName;
        this.color = color;
    }

    public String getDescription(){
        return description;
    }

    public String getEmoticonName(){
        return emoticonName;
    }

    public int getColor(){
        return color;
    }
}
