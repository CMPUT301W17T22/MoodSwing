package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * A MoodEvent is an object that is posted to the MoodSwing app and displayed on certain feeds
 * within it.
 * <p/>
 * A MoodEvent contains the posting participant's username, emotional state, the date the mood
 * event was created, a short explanation trigger for the mood event, a social situation that
 * contributed to the mood event, and the location of the mood event.
 *
 * @author Fred
 * @author bbest
 * @version 2017-02-19
 * @see EmotionalState
 * @see SocialSituation
 */

public class MoodEvent {
    /** The original poster of the MoodEvent's username. */
    private String originalPoster;

    /** The emotional state of the mood event.
     * @see EmotionalState */
    private EmotionalState emotionalState;

    /** The date that the mood event was created. CAN NOT BE EDITED AFTER INITIALIZATION.*/
    private Date date;

    /** A short explanation of what triggered the mood event. */
    private String trigger;

    /** The social situation that contributed to the mood event.
     * @see SocialSituation */
    private SocialSituation socialSituation;

    /** The location that the mood event originally was entered at.
     * CAN NOT BE EDITED AFTER INITIALIZATION.*/
    private LatLng location;

    /**
     * Mood event constructor. Sets the attributes of this MoodEvent.
     * @param posterUsername
     * @param date
     * @param emotionalState
     * @param trigger
     * @param socialSituation
     * @param location
     */
    public MoodEvent(String posterUsername,
                     Date date,
                     EmotionalState emotionalState,
                     String trigger,
                     SocialSituation socialSituation,
                     LatLng location) {
        this.originalPoster = posterUsername;
        this.date = date;
        this.emotionalState = emotionalState;
        this.trigger = trigger;
        this.socialSituation = socialSituation;

        // Check for null location, set to 0 if it is null.
        if (location == null) {
            this.location = new LatLng(0, 0);
        } else {
            this.location = location;
        }
    }

    /**
     * Android Studio generated equals method.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoodEvent moodEvent = (MoodEvent) o;

        Log.i("MoodSwing", "Got here.");

        if (!originalPoster.equals(moodEvent.originalPoster)) return false;
        if (!emotionalState.equals(moodEvent.emotionalState)) return false;
        if (!date.equals(moodEvent.date)) return false;
        if (trigger != null ? !trigger.equals(moodEvent.trigger) : moodEvent.trigger != null)
            return false;
        if (socialSituation != null ? !socialSituation.equals(moodEvent.socialSituation) : moodEvent.socialSituation != null)
            return false;
        return location != null ? location.equals(moodEvent.location) : moodEvent.location == null;
    }

    /**
     * Android Studio generated hashCode method.
     * @return
     */
    @Override
    public int hashCode() {
        int result = originalPoster.hashCode();
        result = 31 * result + emotionalState.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        result = 31 * result + (socialSituation != null ? socialSituation.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    /**
     * Edit MoodEvent method, uses setters to replace the attributes that are editable.
     * @param emotionalState The new emotional state of the mood event.
     * @param trigger The new trigger of the mood event.
     * @param socialSituation The new social situation of the mood event.
     */
    public void editMoodEvent(EmotionalState emotionalState,
                              String trigger,
                              SocialSituation socialSituation) {
        this.setEmotionalState(emotionalState);
        this.setTrigger(trigger);
        this.setSocialSituation(socialSituation);
    }

    /**
     * Creates Marker for map based on emoticon and position. Things have changed, so this
     * method will have to change a lot to work with how things work now.
     *
     * @return Marker corresponding to this MoodEvent
     * @return null if no position
     */
//    public Marker getMapMarker(GoogleMap googleMap){
//        if(location == null){
//            return null;
//        }
//
//        Marker marker = googleMap.addMarker(new MarkerOptions()
//                .position(location)
//                .title(emotionalState.getDescription())
//                .icon(icon));
//
//        return marker;
//    }

    /**
     * Generates and returns a deep copy of this mood event.
     * @return A deep copy of this mood event.
     */
    public MoodEvent getDeepCopy() {
        return new MoodEvent(this.getOriginalPoster(),
                this.getDate(),
                this.getEmotionalState(),
                this.getTrigger(),
                this.getSocialSituation(),
                this.getLocation());
    }

    /**
     * To string method for printing.
     * @return The nicely printed version of this mood event.
     */
    public String toString() {
        return "Original Poster: " + this.getOriginalPoster() +
                "\nEmotional State: " + this.getEmotionalState().toString() +
                "\nDate: " + this.getDate().toString() +
                "\nTrigger: " + this.getTrigger() +
                "\nSocial Situation: " + this.getSocialSituation().toString() +
                "\nLocation: " + this.getLocation().toString();
    }

    // --- START: Getters and Setters

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

    public void setOriginalPoster(String originalPoster) { this.originalPoster = originalPoster; }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }

    // --- END: Getters and Setters
}
