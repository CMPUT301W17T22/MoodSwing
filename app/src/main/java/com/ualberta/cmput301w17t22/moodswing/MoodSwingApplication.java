package com.ualberta.cmput301w17t22.moodswing;

import android.app.Application;

/**
 * Created by nyitrai on 2/26/2017.
 */

public class MoodSwingApplication extends Application {

    /**
     * Get MoodSwing. Returns the MoodSwing object to the Controller that needs it.
     */
    transient private static MoodSwing moodSwing = null;
    static MoodSwing getMoodSwing() {
        if (moodSwing == null) {
            moodSwing = new MoodSwing();
        }
        return moodSwing;
    }

    /**
     * Get MoodEventController. Returns the MoodEventController to the Model.
     */
    transient private static MoodEventController moodEventController = null;
    static MoodEventController getMoodEventController() {
        if (moodEventController == null) {
            moodEventController = new MoodEventController(getMoodSwing());
        }
        return moodEventController;
    }

    /**
     * Get ElasticSearchController. Returns the ElasticSearchController to the Model.
     */
    transient private static ElasticSearchController elasticSearchController = null;
    static ElasticSearchController getElasticSearchController() {
        if (elasticSearchController == null) {
            elasticSearchController = new ElasticSearchController(getMoodSwing());
        }
        return elasticSearchController;
    }

    /**
     * Get ParticipantController. Returns the ParticipantController to a View.
     */
    transient  private  static  ParticipantController participantController = null;
    static ParticipantController getParticipantController() {
        if (participantController == null) {
            participantController = new ParticipantController(getMoodSwing());
        }
        return participantController;
    }

}