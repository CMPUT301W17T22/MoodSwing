package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private LatLng location;

//    private String photoLocation;
//    private String iconLocation;
//    private BitmapDescriptor photo;
//    private BitmapDescriptor icon;

    //pass null for unused parameters
    //TODO: finish handling of null (or empty) inputs
    public MoodEvent(String posterUsername,
                     Date date,
                     EmotionalState emotionalState,
                     String trigger,
                     SocialSituation socialSituation,
                     // String photoLocation,
                     // String iconLocation,
                     LatLng location) {
        this.originalPoster = posterUsername;
        this.date = date;
        this.emotionalState = emotionalState;
        this.trigger = trigger;
        this.socialSituation = socialSituation;

        // Check for null location, set to 0 if its null.
        if (location == null) {
            this.location = new LatLng(0, 0);
        } else {
            this.location = location;
        }
    }

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
     * Edit MoodEvent method, uses setters to replace attributes.
     * @param emotionalState
     * @param trigger
     * @param socialSituation
     */
    public void editMoodEvent(EmotionalState emotionalState,
                              String trigger,
                              SocialSituation socialSituation) {
        this.setEmotionalState(emotionalState);
        this.setTrigger(trigger);
        this.setSocialSituation(socialSituation);

//        this.icon = BitmapDescriptorFactory.fromAsset(iconLocation);
//        this.photo = BitmapDescriptorFactory.fromFile(photoLocation);
    }

    /**
     * Creates Marker for map based on emoticon and position.
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
     * Used to generate a deep copy of the mood event.
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
     * To string method for printing to listviews.
     * @return
     */
    public String toString() {
        return "Original Poster: " + this.getOriginalPoster() +
                "\nEmotional State: " + this.getEmotionalState().toString() +
                "\nDate: " + this.getDate().toString() +
                "\nTrigger: " + this.getTrigger() +
                "\nSocial Situation: " + this.getSocialSituation().toString();
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

//    public String getPhotoLocation() { return photoLocation; }
//
//    public void setPhotoLocation(String photoLocation) { this.photoLocation = photoLocation; }
//
//    public String getIconLocation() { return iconLocation; }
//
//    public void setIconLocation(String iconLocation) { this.iconLocation = iconLocation; }

    public void setOriginalPoster(String originalPoster) { this.originalPoster = originalPoster; }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }
}
