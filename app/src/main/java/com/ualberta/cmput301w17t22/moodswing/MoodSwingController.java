package com.ualberta.cmput301w17t22.moodswing;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

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

    public void addView(MSView view) {
        ms.addView(view);
    }

    public void removeView(MSView view) { ms.removeView(view);}

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
     * Adds a new mood event to the mainParticipant.
     */
    public void addMoodEventToMainParticipant(MoodEvent moodEvent) {

        ms.addMoodEventToMainParticipant(moodEvent);

        ms.notifyViews();
    }

    /**
     * Edits a mood event of the mainParticipant's given the position of the mood event
     * and the updated mood event.
     */
    public void editMoodEventToMainParticipant(int position, MoodEvent moodEvent) {

        ms.editMoodEventToMainParticipantByPosition(position, moodEvent);

        ms.notifyViews();
    }

    /**
     * Delete's the MoodEvent at the given position in the main participants mood history.
     * @param position The position of the MoodEvent in the main participants mood history.
     */
    public void deleteMoodEventFromMainParticipantByPosition(int position) {
        ms.deleteMoodEventFromMainParticipantByPosition(position);

        ms.notifyViews();
    }

    /**
     * Sends a follow request from the main participant to the given username.
     * @param username The username of the MoodSwing user that the main participant wants to follow.
     * @return Returns true if a user by that username was found, false otherwise.
     */
    public Boolean sendFollowRequestFromMainParticipantToUsername(String username) {
        Boolean requestStatus = ms.sendFollowRequestFromMainParticipantToUsername(username);

        ms.notifyViews();

        return requestStatus;
    }

    /**
     * Gets the main participant from the main Model class
     * @return The main participant.
     */
    public Participant getMainParticipant() { return ms.getMainParticipant(); }

    public void setMoodFeed(ArrayList<MoodEvent> moodFeed) { ms.setMoodFeed(moodFeed); }

    public ArrayList<MoodEvent> getMoodFeed() { return ms.getMoodFeed(); }

    /** Get the position of the currently being viewed or edited mood event in the main
     * participant's mood history.
     * @return The position of the current mood event in the mood history list.
     */
    public int getMoodHistoryPosition() { return ms.getMoodHistoryPosition(); }

    /**
     * Report the position of the currently being viewed or edited mood event in the main
     * participant's mood history to the main model class.
     * @param moodHistoryPosition The position of the current mood event in the mood history list.
     */
    public void setMoodHistoryPosition(int moodHistoryPosition) {
        ms.setMoodHistoryPosition(moodHistoryPosition);
    }

    /**
     * Get the position of the currently being viewed or edited mood event in the main
     * model class's mood feed.
     * @return The position of the current mood event in the mood feed.
     */
    public int getMoodFeedPosition() { return ms.getMoodFeedPosition(); }

    /**
     * Report the position of the currently being viewed or edited mood event in the main model
     * class's mood feed to the main model class.
     * @param moodFeedPosition The position of the current mood event in the mood feed.
     */
    public void setMoodFeedPosition(int moodFeedPosition) {
        ms.setMoodFeedPosition(moodFeedPosition);
    }

    public Location getLastKnownLocation() { return ms.getLastKnownLocation(); }

    public void setLastKnownLocation(Location location) { ms.setLastKnownLocation(location); }
}
