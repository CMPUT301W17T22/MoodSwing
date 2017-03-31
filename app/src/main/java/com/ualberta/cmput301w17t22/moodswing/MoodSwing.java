package com.ualberta.cmput301w17t22.moodswing;

import android.util.Log;

import java.security.InvalidParameterException;
import java.util.ArrayList;

/**
 * Main class for the Model portion of the MVC architecture.
 * <p/>
 * Handles backend information like the main participants information and saving/loading.
 * <p/>
 * Created by nyitrai on 2/26/2017.
 */
public class MoodSwing extends MSModel<MSView> {

    /**
     * This will be the list of MoodEvents that we are displaying to the user.
     * Either the mood history or the mood feed / mood map.
     * Will be filled with MoodEvents once the user chooses to view the history/feed/map.
     */
    private ArrayList<MoodEvent> moodFeed;

    /**
     * The main participant. The current user of the app from the Android device.
     */
    private Participant mainParticipant;

    /**
     * The position of the currently being viewed or edited mood event in the main participants
     * mood history.
     */
    private int moodHistoryPosition;

    /**
     * The position of the currently being viewed mood event in the mood list.
     */
    private int moodFeedPosition;

    /**
     * The last known location of the device/main participant.
     */
    private double lastKnownLat;
    private double lastKnownLng;

    /**
     * Adds the mainParticipant for the MoodSwing app to ElasticSearch. This is used when logging
     * in and the username entered is not found.
     * <p/>
     * This will initialize the participant with the entered username, and post it to
     * ElasticSearch.
     */
    public void newMainParticipantByUsername(String username) {

        // Get ElasticSearchController.
        ElasticSearchController elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Create a new participant and add it to ElasticSearch.
        this.setMainParticipant(elasticSearchController.newParticipantByUsername(username));

        // Notify Views so they update their information.
        notifyViews();
    }

    /**
     * Loads a Participant by username from ElasticSearch into the mainParticipant.
     * <p/>
     * This is used when first logging in as a participant.
     */
    public void loadMainParticipantByUsername(String username) {

        // Get ElasticSearchController.
        ElasticSearchController elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Get Participant from ElasticSearchController and load it into mainParticipant.
        this.setMainParticipant(elasticSearchController.getParticipantByUsername(username));

        // Then notify the Views that they need to update their information.
        notifyViews();
    }

    /**
     * Add a new mood event to the main participant's mood history.
     */
    public void addMoodEventToMainParticipant(MoodEvent moodEvent) {

        // Add the mood event to the main participant.
        getMainParticipant().addMoodEvent(moodEvent);

        // Post change to ElasticSearch.
        saveMainParticipant();

        // Notify all the views of the new information.
        notifyViews();
    }

    /**
     * Edit a specific mood event of the main participant.
     */
    public void editMoodEventToMainParticipantByPosition(int position, MoodEvent moodEvent) {

        // Get the main participant to edit its mood event.
        getMainParticipant().editMoodEventByPosition(position, moodEvent);

        // Post the updated mood event to ElasticSearch.
        saveMainParticipant();

        // Notify the views.
        notifyViews();
    }

    /**
     * Deletes the mood event at the given position from the MainParticipant and propagates
     * the changes through to ElasticSearch and the views.
     *
     * @param position The position of the mood event in the main participant's mood history.
     */
    public void deleteMoodEventFromMainParticipantByPosition(int position) {
        getMainParticipant().deleteMoodEventByPosition(position);

        // Post the updated participant to ElasticSearch.
        saveMainParticipant();

        // Notify all views of the change.
        notifyViews();
    }

    /**
     * Sends a follow request from the main participant to the given MoodSwing user
     * according to the provided username.
     *
     * @param username The username to send the request to.
     */
    public boolean sendFollowRequestFromMainParticipantToUsername(String username) {
        // Get the ElasticSearchController.
        ElasticSearchController elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Get the participant that we are sending the follower request to.
        Participant participantToFollow =
                elasticSearchController.getParticipantByUsername(username);

        // If the participant is found by the given username, send the follow request.
        if (participantToFollow != null) {
            // Send the follow request.
            mainParticipant.followParticipant(participantToFollow);

            // Update both participants.
            elasticSearchController.updateParticipantByParticipant(mainParticipant);
            elasticSearchController.updateParticipantByParticipant(participantToFollow);

            notifyViews();
            return true;
        } else {
            // Return that no user was found.
            notifyViews();
            return false;
        }

    }

    /**
     * Saves the main participant information (mood history, follower list, following list)
     * to ElasticSearch if online.
     * <p/>
     * This will be used when the main participant edits a mood event, creates a new mood event,
     * accepts/declines a follower request, sends out a new follow request, etc.
     */
    public void saveMainParticipant() {
        // Get ElasticSearchController.
        ElasticSearchController elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Update the main participant on ElasticSearch.
        elasticSearchController.updateParticipantByParticipant(mainParticipant);
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

    /**
     * Construct a mood feed (arraylist of mood events) appropriate to the given filters.
     * @param activeFilters
     * @return
     */
    public void buildMoodFeed(int[] activeFilters, String filterTrigger, String filterEmotion) {

        // Get ElasticSearchController.
        ElasticSearchController elasticSearchController =
                MoodSwingApplication.getElasticSearchController();

        // Use the elastic search controller to build the mood feed.
        setMoodFeed(elasticSearchController.buildMoodFeed(mainParticipant,
                activeFilters,
                filterTrigger,
                filterEmotion));

    }

    // --- START: Getters and Setters

    public ArrayList<MoodEvent> getMoodFeed() { return moodFeed; }

    public void setMoodFeed(ArrayList<MoodEvent> moodFeed) { this.moodFeed = moodFeed; }

    public Participant getMainParticipant() { return mainParticipant; }

    public void setMainParticipant(Participant mainParticipant) {
        this.mainParticipant = mainParticipant;
    }

    /** Getter method for the moodHistoryPosition variable with some extra handling. If the
     * mood event currently being viewed is deleted, the moodHistoryPosition is set to -1 so
     * as to prevent it from being viewed elsewhere somehow.
     */
    public int getMoodHistoryPosition() {
        if (moodHistoryPosition == -1) {
            Log.i("ERROR", "Position of MoodEvent in MoodHistory not found");
            throw new IllegalArgumentException();
        } else {
            return moodHistoryPosition;
        }
    }

    public void setMoodHistoryPosition(int moodHistoryPosition) {
        this.moodHistoryPosition = moodHistoryPosition;
    }

    public int getMoodFeedPosition() {
        return moodFeedPosition; }

    public void setMoodFeedPosition(int moodFeedPosition) {
        this.moodFeedPosition = moodFeedPosition;
    }

    public double getLastKnownLat() {
        return lastKnownLat;
    }

    public double getLastKnownLng() {
        return lastKnownLng;
    }

    public void setLastKnownLocation(double lastKnownLat, double lastKnownLng) {
        this.lastKnownLat = lastKnownLat;
        this.lastKnownLng = lastKnownLng;
    }

    // --- END: Getters and Setters

    MoodSwing() { super(); }
}
