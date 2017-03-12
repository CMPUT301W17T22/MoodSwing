package com.ualberta.cmput301w17t22.moodswing;

/**
 * Main View class for MVC architecture for use in MoodSwing app.
 *
 * Created by nyitrai on 2/26/2017.
 */

public interface MSView<M> {

    /**
     * Each View has an update method that reloads all the information from the Model
     * that the View surfaces to the app's user.
     */
    public void update(M model);

    /**
     * Each View, or android activity, should initialize all its widgets in this function, and also
     * add itself to the Views tracked by the main Model class.
     */
    public void initialize();
}
