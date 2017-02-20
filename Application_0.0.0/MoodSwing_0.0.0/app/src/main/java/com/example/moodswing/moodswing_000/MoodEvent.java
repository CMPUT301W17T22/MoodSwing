package com.example.moodswing.moodswing_000;

import android.graphics.Picture;
import android.location.Location;

import java.util.Date;

/**
 * Created by Fred on 2017-02-18.
 */

public class MoodEvent {
    private EmotionalState emotionalState;
    private Date date;
    private String trigger;
    private SocialSituation socialSituation;
    private String photoLocation;
    private Location location;

    //pass null for unused parameters
    //TODO: finish handling of null (or empty) inputs
    public MoodEvent(EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
                     String photoLocation, boolean saveLocation) {
        this.editMoodEvent(emotionalState, trigger, socialSituation, photoLocation, saveLocation);
        //automatically selects current date
        this.date = new Date();
    }

    public void editMoodEvent(EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
    String photoLocation, boolean saveLocation) {
        this.emotionalState = emotionalState;
        if(trigger != null) {
            this.trigger = trigger;
        }
        else{
            this.trigger = "";
        }
        this.socialSituation = socialSituation;
        this.photoLocation = photoLocation;
        if(saveLocation){
            //TODO: save current location
            this.location = null;
        }
        else{
            this.location = null;
        }
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public Date getDate() {
        return date;
    }

    public String getTrigger() {
        return trigger;
    }

    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    public String getPhotoLocation() {
        return photoLocation;
    }

    public Location getLocation() {
        return location;
    }
}
