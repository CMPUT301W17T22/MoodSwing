package com.example.moodswing.moodswing_000;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**A MoodEvent is an object that is posted to certain feeds. It contains the posting participant's username,
 * emotional state, date, trigger(s), social situation, photolocation, location.
 *
 * @author Fred
 * @author bbest
 * @version 2017-02-19
 * @see EmotionalState
 * @see SocialSituation
 */

public class MoodEvent {
    //TODO: most of these setters won't be used. Depending on implementation, a single getter may replace multiple getters.
    //TODO: probably make setters private or revert to original form (approx. date 20-02-2017) - original form prevents duplication of code
    private String originalPoster;
    private EmotionalState emotionalState;
    private Date date;
    private String trigger;
    private SocialSituation socialSituation;
    private String photoLocation;
    private LatLng location;

    //pass null for unused parameters
    //TODO: finish handling of null (or empty) inputs
    public MoodEvent(String posterUsername, EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
                     String photoLocation, LatLng location) {
        //initializes attributes form the arguments in the constructor.
        this.originalPoster = posterUsername;
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
    String photoLocation, LatLng location) {
        this.setDate(new Date());
        //emotionalState and socialSituation will be picked from a scroller list
        //depending on which one is picked it will return an int which corresponds to the correct option in the list in the MoodOptions lists.
        this.setEmotionalState(emotionalState);
        this.setTrigger(trigger);
        this.setSocialSituation(socialSituation);
        this.setPhotoLocation(photoLocation);
        this.setLocation(location);
    }


    //Getter and Setter methods for attributes.

    public String getOriginalPoster(){
        return originalPoster;
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

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }
}
