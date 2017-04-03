package com.ualberta.cmput301w17t22.moodswing;

import android.app.Application;
import android.content.Context;

/**
 * This Application class handles our singletons; the main model class, and the controllers.
 *
 * Created by nyitrai on 2/26/2017.
 */

public class MoodSwingApplication extends Application {
    private static MoodSwingApplication instance;

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

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
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
     * Get FollowingController. Returns the FollowingController to a View.
     */
    transient private static FollowingController followingController = null;
    static FollowingController getFollowingController(){
        if(followingController == null){
            followingController = new FollowingController(getMoodSwing());
        }
        return followingController;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
