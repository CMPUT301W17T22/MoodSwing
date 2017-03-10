package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Mood Swing Controller.
 *
 * Handles all getting and setting for the main Model class MoodSwing.
 *
 * Created by nyitrai on 3/7/2017.
 */

public class MoodSwingController implements MSController {

    // Grab the main model class singleton.
    MoodSwing ms = null;
    public MoodSwingController(MoodSwing ms) { this.ms = ms; }

    /**
     * Loads a participant into the Model's main participant given a username.
     * @param username
     * @return
     */
    public void loadMainParticipantByUsername(String username) {

        // Have the main Model class fetch the user's information.
        ms.loadMainParticipantByUsername(username);

        // If the main Model class could not fetch the user's information,
        // the mainParticipant will be null. Basically, if the user can not be gotten from
        // Jest/ElasticSearch, create a new user.
        if (ms.getMainParticipant() == null) {
            ms.newMainParticipantByUsername(username);

            // If the participant is still null, something is broken.
            if (ms.getMainParticipant() == null) {
                Log.i("ERROR", "Unable to create new participant by username.");
            }
        }

    }

    /**
     * Add's a new mood event to the mainParticipant.
     * @param emotionalState Required. The emotional state of the mood event.
     * @param trigger Less than 3 words or 20 chars. The trigger for the mood event.
     * @param socialSituation Optional. The social situation of the mood event.
     * @param photoLocation Optional. A string for the location of the photo.
     * @param iconLocation Optional.
     * @param location Automatically set to the location the mood event was created.
     */
    public void addMoodEventToMainParticipant(EmotionalState emotionalState,
                             String trigger,
                             SocialSituation socialSituation,
                             String photoLocation,
                             String iconLocation,
                             LatLng location) {

        ms.addMoodEventToMainParticipant(
                emotionalState,
                trigger,
                socialSituation,
                photoLocation,
                iconLocation,
                location);

        ms.notifyViews();
    }

    /**
     * Gets the main participant from the main Model class
     * @return The main participant.
     */
    public Participant getMainParticipant() { return ms.getMainParticipant(); }
}
