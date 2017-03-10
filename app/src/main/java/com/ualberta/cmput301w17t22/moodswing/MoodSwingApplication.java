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
     * Get MoodSwingController. Returns the MoodSwingController to a View.
     */
    transient  private  static MoodSwingController moodSwingController = null;
    static MoodSwingController getMoodSwingController() {
        if (moodSwingController == null) {
            moodSwingController = new MoodSwingController(getMoodSwing());
        }
        return moodSwingController;
    }

    /**
     * Get EmotionalStateController. Returns the EmotionalStateController to a View.
     */
    transient  private  static EmotionalStateController emotionalStateController = null;
    static EmotionalStateController getEmotionalStateController() {
        if (emotionalStateController == null) {
            emotionalStateController = new EmotionalStateController(getMoodSwing());
        }
        return emotionalStateController;
    }

    /**
     * Get SocialSituationController. Returns the SocialSituationController to a View.
     */
    transient private  static  SocialSituationController socialSituationController = null;
    static SocialSituationController getSocialSituationController() {
        if (socialSituationController == null) {
            socialSituationController = new SocialSituationController(getMoodSwing());
        }
        return socialSituationController;
    }
}
