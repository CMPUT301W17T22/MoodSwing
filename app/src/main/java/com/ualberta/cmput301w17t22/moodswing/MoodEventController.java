package com.ualberta.cmput301w17t22.moodswing;

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
     * Adds a new mood event to the Model when the user of the app chooses to create a new
     * mood event.
     */
    public void addMoodEvent(MoodEvent moodEvent) {
        ms.mainParticipant.addMoodEvent(moodEvent.getEmotionalState(),
                moodEvent.getTrigger(),
                moodEvent.getSocialSituation(),
                moodEvent.getPhotoLocation(),
                moodEvent.getIconLocation(),
                moodEvent.getLocation());
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
