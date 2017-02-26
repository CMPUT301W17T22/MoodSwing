package com.example.moodswing.moodswing_000;

import android.app.Application;

/**
 * Created by nyitrai on 2/26/2017.
 */

public class MoodSwingApplication extends Application {

    transient private static MoodSwing moodSwing = null;
    static MoodSwing getMoodSwing() {
        if (moodSwing == null) {
            moodSwing = new MoodSwing();
        }
        return moodSwing;
    }

}
