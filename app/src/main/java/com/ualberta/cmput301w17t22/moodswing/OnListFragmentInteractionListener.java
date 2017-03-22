package com.ualberta.cmput301w17t22.moodswing;

import com.ualberta.cmput301w17t22.moodswing.dummy.DummyContent;

/**
 * Idea taken from
 * http://stackoverflow.com/questions/30762861/how-to-handle-multiple-fragment-interaction-listeners-in-one-activity-properly
 * on 3/12/2017.
 *
 * Makes an OnListFragmentInteractionListener for all our Following/Follower/Request Fragments to use.
 * Created by nyitrai on 3/12/2017.
 */

public interface OnListFragmentInteractionListener {
    void onListFragmentInteraction(Participant participant);
}
