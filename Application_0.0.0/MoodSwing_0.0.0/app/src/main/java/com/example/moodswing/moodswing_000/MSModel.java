package com.example.moodswing.moodswing_000;

import java.util.ArrayList;

/**
 * Base Model class for MVC architecture for use in MoodSwing app.
 *
 * Created by nyitrai on 2/26/2017.
 */

public class MSModel<V extends MSView> {
    private ArrayList<V> views;

    /**
     * The Model has an array list of Views (android activities).
     */
    public MSModel() { views = new ArrayList<V>(); }

    /**
     * notifyViews is used when some change has been made to the Model. (New mood event created,
     * new filter applied, follow request approved/declined.) It lets the Views know
     * that there is new information and they need to update.
     */
    public void notifyViews() {
        for (V view: views) {
            view.update(this);
        }
    }

    public void addView(V view) {
        if (!views.contains(view)) {
            views.add(view);
        }
    }

    public void deleteView(V view) {views.remove(view); }
}
