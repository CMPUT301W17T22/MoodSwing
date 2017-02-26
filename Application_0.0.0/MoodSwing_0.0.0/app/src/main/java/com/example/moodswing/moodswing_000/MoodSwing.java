package com.example.moodswing.moodswing_000;

import java.util.ArrayList;

/**
 * Main class for the Model portion of the MVC architecture.
 *
 * Handles backend information like the main participants information and saving/loading.
 *
 * Created by nyitrai on 2/26/2017.
 */

public class MoodSwing extends MSModel<MSView> {

    /**
     * This will be the list of MoodEvents that we are displaying to the user.
     * Either the mood history or the mood feed / mood map.
     * Will be filled with MoodEvents once the user chooses to view the history/feed/map.
     */
    public ArrayList<MoodEvent> moodList;

    /**
     * The main participant. The current user of the app from the Android device.
     */
    public Participant mainParticipant;

    /**
     * Creates a new participant for the MoodSwing app. This is used when logging in and the
     * username entered is correct but not found.
     *
     * This will initialize the participant with the entered username, and post it to
     * ElasticSearch.
     */
    public void newParticipant() {

    }

    /**
     * Loads information about the main participant (mood history, follower list
     * following list) into the mainParticipant, the followingfrom ElasticSearch if online.
     *
     * This will be used when first logging in as a participant.
     */
    public void loadInfo() {
        // Load information from online.

        // Then update the Views.
        notifyViews();
    }

    /**
     * Saves the main participant information (mood history, follower list, following list)
     * to ElasticSearch if online.
     *
     * This will be used when the main participant edits a mood event, creates a new mood event,
     * accepts/declines a follower request, sends out a new follow request, etc.
     */
    public void saveOnline() {

    }

    /**
     * An alternative to saveOnline that is used if the device is not online. This will store
     * the same information onto a temporary file on the device.
     *
     * This will be used if offline when the main participant edits a mood event, creates
     * a new mood event, accepts/declines a follower request, sends out a new follow request, etc.
     */
    public void saveLocal() {

    }

    MoodSwing() { super(); }
}
