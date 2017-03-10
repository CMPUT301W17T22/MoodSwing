package com.ualberta.cmput301w17t22.moodswing;

import com.google.android.gms.maps.model.LatLng;

/**
 * This Controller will be used mostly from the View end to add information to the Model.
 * For instance, when the user of the app chooses to create a new mood event.
 *
 * Created by nyitrai on 2/26/2017.
 */

public class MoodEventController implements MSController {

    MoodSwing ms = null;
    public MoodEventController(MoodSwing ms) { this.ms = ms; }

    /**
     * Add's a new mood event to the mainParticipant.
     * @param emotionalState Required. The emotional state of the mood event.
     * @param trigger Less than 3 words or 20 chars. The trigger for the mood event.
     * @param socialSituation Optional. The social situation of the mood event.
     * @param photoLocation Optional. A string for the location of the photo.
     * @param iconLocation Optional.
     * @param location Automatically set to the location the mood event was created.
     */
    public void addMoodEvent(EmotionalState emotionalState,
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
     * Deletes a mood event from the Model.
     */
    public void deleteMoodEvent() {
        // TODO: do we need this?
    }

    /**
     * Edits a mood event in the model.
     */
    public void editMoodEvent() {
        // TODO: need to have a way to find the mood event that is being edited in the participants mood history.
    }

    /**
     * Gets mood events from the Model based on the given filter from the View.
     * @param filter
     */
    public void getMoodEvents(String filter) {

    }

}
