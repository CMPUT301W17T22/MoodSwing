package com.example.moodswing.moodswing_000;

/**
 * Main View class for MVC architecture.
 *
 * Created by nyitrai on 2/26/2017.
 */

public interface MSView<M> {

    /**
     * Each View has an update method that reloads all the information from the Model
     * that the View surfaces to the app's user.
     */
    public void update(M model);
}
