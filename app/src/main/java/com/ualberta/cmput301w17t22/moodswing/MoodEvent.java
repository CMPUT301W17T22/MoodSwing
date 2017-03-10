package com.ualberta.cmput301w17t22.moodswing;

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
    private String photoLocation;



    private String iconLocation;
    private BitmapDescriptor photo;
    private BitmapDescriptor icon;

    //pass null for unused parameters
    //TODO: finish handling of null (or empty) inputs
    public MoodEvent(String posterUsername, EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
                     String photoLocation, String iconLocation, LatLng location) {
        this.originalPoster = posterUsername;
        // I changed this here because I wanted to test if ElasticSearch was working, and the map
        // stuff wasn't working. The 5 lines below this should be deleted and just the
        // editMoodEvent() line below should be uncommented when the map stuff works properly.
        this.setDate(new Date());
        this.location = location;
        this.emotionalState = emotionalState;
        this.trigger = trigger;
        this.socialSituation = socialSituation;
        //editMoodEvent(emotionalState, trigger, socialSituation, iconLocation, photoLocation);
    }

    //Edit MoodEvent Method, uses setters to replace attributes
    public void editMoodEvent(EmotionalState emotionalState, String trigger, SocialSituation socialSituation,
            String iconLocation, String photoLocation) {
        //automatically selects current date
        this.setDate(new Date());
        //emotionalState and socialSituation will be picked from a scroller list
        //depending on which one is picked it will return an int which corresponds to the correct option in the list in the MoodOptions lists.
        this.emotionalState = emotionalState;
        this.trigger = trigger;
        this.socialSituation = socialSituation;
        this.icon = BitmapDescriptorFactory.fromAsset(iconLocation);
        this.photo = BitmapDescriptorFactory.fromFile(photoLocation);
    }


    /**
     * Creates Marker for map based on emoticon and position.
     *
     * @return Marker corresponding to this MoodEvent
     * @return null if no position
     */
    public Marker getMapMarker(GoogleMap googleMap){
        if(location == null){
            return null;
        }

        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(location)
                .title(emotionalState.getDescription())
                .icon(icon));

        return marker;
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

    public String getPhotoLocation() { return photoLocation; }

    public void setPhotoLocation(String photoLocation) { this.photoLocation = photoLocation; }

    public String getIconLocation() { return iconLocation; }

    public void setIconLocation(String iconLocation) { this.iconLocation = iconLocation; }

    public void setOriginalPoster(String originalPoster) { this.originalPoster = originalPoster; }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public LatLng getLocation() {
        return location;
    }
}
