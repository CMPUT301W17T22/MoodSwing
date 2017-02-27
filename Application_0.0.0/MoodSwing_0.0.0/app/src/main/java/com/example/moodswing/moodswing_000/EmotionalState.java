package com.example.moodswing.moodswing_000;

/**
 * Contains the information associated with a pre-defined emotional state.
 *
 * @author Fred
 * @version 2017-02-18
 * @see Participant
 */

public class EmotionalState {
    private String description;
    private String emoticonName;    // what does emoticon name do?
    private String imageLocation;
    private int color;

    public EmotionalState(String description, String imageLocation, int color){
        this.description = description;
        //this.emoticonName = emoticonName;
        this.imageLocation = imageLocation;
        this.color = color;
    }

    public String getDescription(){
        return description;
    }

    public String getImageLocation() {return imageLocation;}

    public int getColor(){
        return color;
    }
}
