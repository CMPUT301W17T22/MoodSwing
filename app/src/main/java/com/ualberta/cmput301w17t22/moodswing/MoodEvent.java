package com.ualberta.cmput301w17t22.moodswing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import static java.lang.Double.NaN;

/**
 * A MoodEvent is an object that is posted to the MoodSwing app and displayed on certain feeds
 * within it.
 * <p/>
 * A MoodEvent contains the posting participant's username, emotional state, the date the mood
 * event was created, a short explanation trigger for the mood event, a social situation that
 * contributed to the mood event, and the lastKnownLocation of the mood event.
 *
 * @author Fred
 * @author bbest
 * @version 2017-02-19
 * @see EmotionalState
 * @see SocialSituation
 */

public class MoodEvent {
    private static final double NOT_SET = 911.911;

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

    /** The lastKnownLat that the mood event originally was entered at.
     * CAN NOT BE EDITED AFTER INITIALIZATION.*/
    private double lat = NOT_SET;
    /** The lastKnownLng that the mood event originally was entered at.
     * CAN NOT BE EDITED AFTER INITIALIZATION.*/
    private double lng = NOT_SET;

    /**The image that can be attached to the mood event. This can be added by taking a picture
     * or selecting one from the phones gallery */
    private ByteArrayOutputStream image;

    /**
     * Mood event constructor. Sets the attributes of this MoodEvent.
     * @param posterUsername
     * @param date
     * @param emotionalState
     * @param trigger
     * @param socialSituation
     * @param lat
     * @param lng
     */
    //TODO: this needs to be implemented in the same way as the editor!!!!!!
    public MoodEvent(String posterUsername,
                     Date date,
                     EmotionalState emotionalState,
                     String trigger,
                     SocialSituation socialSituation,
                     double lat,
                     double lng,
                     ByteArrayOutputStream importImage) {
        this.originalPoster = posterUsername;
        this.date = date;
        this.emotionalState = emotionalState;
        this.trigger = trigger;
        this.socialSituation = socialSituation;
        this.image = importImage;
        if(!Double.isNaN(lat) && !Double.isNaN(lng)) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    //TODO: Regenerate equals method to include image and location.
    /**
     * Android Studio generated equals method.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoodEvent moodEvent = (MoodEvent) o;

        if (!originalPoster.equals(moodEvent.originalPoster)) return false;
        if (!emotionalState.equals(moodEvent.emotionalState)) return false;
        if (!date.equals(moodEvent.date)) return false;
        if (trigger != null ? !trigger.equals(moodEvent.trigger) : moodEvent.trigger != null)
            return false;
        if (socialSituation != null ? !socialSituation.equals(moodEvent.socialSituation) : moodEvent.socialSituation != null)
            return false;
       // if(image != null ? !image.equals(moodEvent.image) : moodEvent.image != null) return false;
        if(Double.isNaN(lat) || Double.isNaN(moodEvent.lat)){
            if(!Double.isNaN(lat) || !Double.isNaN(moodEvent.lat)) return false;
        }
        else if(lat != moodEvent.lat) return false;
        if(Double.isNaN(lng) || Double.isNaN(moodEvent.lng)) {
            if (!Double.isNaN(lng) || !Double.isNaN(moodEvent.lng)) return false;
        }
        else if (lng != moodEvent.lng) return false;
        return true;
    }

    /**
     * Android Studio generated hashCode method.
     * double hashing from Tomasz Nurkiewicz grab date 29/03/2017
     * http://stackoverflow.com/questions/9650798/hash-a-double-in-java
     * TODO: regenerate with Android Studio
     * @return
     */
    @Override
    public int hashCode() {
        int result = originalPoster.hashCode();
        result = 31 * result + emotionalState.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
        result = 31 * result + (socialSituation != null ? socialSituation.hashCode() : 0);
        //result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + Double.valueOf(lat).hashCode();
        result = 31 * result + Double.valueOf(lng).hashCode();
        return result;
    }

    /**
     * Edit MoodEvent method, uses setters to replace the attributes that are editable.
     * Date, original poster, and lastKnownLocation are not editable.
     * @param emotionalState The new emotional state of the mood event.
     * @param trigger The new trigger of the mood event.
     * @param socialSituation The new social situation of the mood event.
     */
    //TODO: this needs to be implemented in the same way as the constructor!!!!!!
    public void editMoodEvent(EmotionalState emotionalState,
                              String trigger,
                              SocialSituation socialSituation,
                              ByteArrayOutputStream image) {
        this.setEmotionalState(emotionalState);
        this.setTrigger(trigger);
        this.setSocialSituation(socialSituation);
        this.setImage(image);
    }

    /**
     * Creates Marker for map based on emoticon and position.
     *
     * @return MarkerOptions object corresponding to this MoodEvent
     * @return null if no position
     */
    public MarkerOptions getMapMarker(){
        if(Double.isNaN(lat) || Double.isNaN(lng)){
            return null;
        }

        // Get bitmap descriptor for the drawable of the emotional state emoticon.
        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(emotionalState.getDrawableId());

         return new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(emotionalState.getDescription())
                .icon(icon);
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
                "\nLatitude: " + this.getLat() +
                "\nLongitude: " + this.getLng();
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

    public void setLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat == NOT_SET ? NaN : lat;
    }

    public double getLng() {
        return lng == NOT_SET ? NaN : lng;
    }

    /**Compresses the ByteArrayOutputStream into a byte size restricted Bitmap and returns the
     * image Bitmap*/
    public Bitmap getImage() {
        if (image == null) {
            return null;
        }
        Bitmap newimage = BitmapFactory.decodeByteArray(image.toByteArray(), 0, image.size());
        return newimage; }

    public void setImage(ByteArrayOutputStream image) {
        this.image = image; }

    // --- END: Getters and Setters
}
