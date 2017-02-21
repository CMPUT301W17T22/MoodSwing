package com.example.moodswing.moodswing_000;

import android.graphics.Picture;
import android.location.Location;

import java.util.Date;

/**A MoodEvent is an object that is posted to certain feeds. It contains the posting participant's username,
 * emotional state, date, trigger(s), social situation, photolocation, location.
 * Created by Fred on 2017-02-18.
 */

public class MoodEvent {
    private String originalposter;
    private EmotionalState emotionalState;
    private Date date;
    private String trigger;
    private SocialSituation socialSituation;
    private String photoLocation;
    private Location location;

    //pass null for unused parameters
    //TODO: finish handling of null (or empty) inputs
    public MoodEvent(String postername, EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
                     String photoLocation, Location location) {
        //initializes attributes form the arguments in the consctructor.
        this.originalposter = postername;
        this.emotionalState = emotionalState;
        this.trigger = trigger;
        this.socialSituation = socialSituation;
        this.photoLocation = photoLocation;
        this.location = location;
        //automatically selects current date
        this.date = new Date();
    }

    //Edit MoodEvent Method, uses setters to replace attributes
    public void editMoodEvent(EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
    String photoLocation, Location location) {
        this.setDate(new Date());
        //emotionalstate and socialsituation will be picked from a scroller list
        //depending on which one is picked it will return an int which corresponds to the correct option in the list in the MoodOptions lists.
        this.setEmotionalState(emotionalState);
        this.setTrigger(trigger);
        this.setSocialSituation(socialSituation);
        this.setPhotoLocation(photoLocation);
        this.setLocation(location);
    }


    //Getter and Setter methods for attributes.

    public String getOriginalposter(){
        return originalposter;
    }
    public void setEmotionalState(EmotionalState emotionalState){
        this.emotionalState = emotionalState;
    }

    public EmotionalState getEmotionalState() {

        return emotionalState;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTrigger() {

        return trigger;
    }

    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }


    public SocialSituation getSocialSituation() {

        return socialSituation;
    }
    public void setPhotoLocation(String photoLocation) {
        this.photoLocation = photoLocation;
    }

    public String getPhotoLocation() {

        return photoLocation;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {

        return location;
    }
}
