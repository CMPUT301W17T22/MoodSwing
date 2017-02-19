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
    private Picture photo;
    private Location location;

    //pass null for unused parameters
    //TODO: finish handling of null inputs
    public MoodEvent(EmotionalState emotionalState, Date date, String trigger, SocialSituation socialSituation, Picture photo, Location location) {
        this.emotionalState = emotionalState;
        this.date = date;
        if(trigger != null) {
            this.trigger = trigger;
        }
        else{
            this.trigger = "";
        }
        this.socialSituation = socialSituation;
        this.photo = photo;
        this.location = location;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    public Date getDate() {
        return date;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    public Picture getPhoto() {
        return photo;
    }

    public void setPhoto(Picture photo) {
        this.photo = photo;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
